package com.readonlydev.command.included;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.readonlydev.common.waiter.EventWaiter;
import com.readonlydev.menu.Menu;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Checks;

class EmbedMessageMenu extends Menu {

	private final BiFunction<Integer, Integer, String> text;
	private final Consumer<Message> finalAction;
	private Map<MessageEmbed, Guild> guildMap;
	private final JDA botInstance;
	private final boolean wrapPageEnds = true;

	public static final String LEFT = "\u25C0";
	public static final String STOP = "\u23F9";
	public static final String RIGHT = "\u25B6";
	public static final String LEAVE = "\uD83D\uDEAA";

	private Guild currentGuild;
	private MessageEmbed currentEmbed;

	protected EmbedMessageMenu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit,
			BiFunction<Integer, Integer, String> text, Consumer<Message> finalAction, Map<MessageEmbed, Guild> guildMap,
			JDA botInstance) {
		super(waiter, users, roles, timeout, unit);
		this.text = text;
		this.finalAction = finalAction;
		this.guildMap = guildMap;
		this.botInstance = botInstance;
	}

	@Override
	public void display(Message message) {
		this.display(message.getChannel());
	}

	/**
	 * Begins pagination on page 1 as a new
	 * {@link net.dv8tion.jda.api.entities.Message Message} in the provided
	 * {@link net.dv8tion.jda.api.entities.MessageChannel MessageChannel}.
	 *
	 * @param channel The MessageChannel to send the new Message to
	 */
	@Override
	public void display(MessageChannel channel) {
		paginate(channel, 1);
	}

	/**
	 * Begins pagination as a new {@link net.dv8tion.jda.api.entities.Message
	 * Message} in the provided {@link net.dv8tion.jda.api.entities.MessageChannel
	 * MessageChannel}, starting on whatever page number is provided.
	 *
	 * @param channel The MessageChannel to send the new Message to
	 * @param pageNum The page number to begin on
	 */
	public void paginate(MessageChannel channel, int pageNum) {
		if (pageNum < 1) {
			pageNum = 1;
		} else if (pageNum > guildMap.size()) {
			pageNum = guildMap.size();
		}
		Message msg = renderPage(pageNum);
		initialize(channel.sendMessage(msg), pageNum);
	}

	private void initialize(RestAction<Message> action, int pageNum) {
		action.queue(m -> {
			if (guildMap.size() > 1) {
				m.addReaction(LEFT).queue();
				m.addReaction(STOP).queue();
				m.addReaction(RIGHT).queue();
				m.addReaction(LEAVE).queue(v -> pagination(m, pageNum), t -> pagination(m, pageNum));
			} else {
				finalAction.accept(m);
			}
		});
	}

	private void pagination(Message message, int pageNum) {
		paginationWithoutTextInput(message, pageNum);
	}

	private void paginationWithoutTextInput(Message message, int pageNum) {
		waiter.waitForEvent(MessageReactionAddEvent.class, event -> checkReaction(event, message.getIdLong()),
				event -> handleMessageReactionAddAction(event, message, pageNum), timeout, unit,
				() -> finalAction.accept(message));
	}

	private boolean checkReaction(MessageReactionAddEvent event, long messageId) {
		if (event.getMessageIdLong() != messageId) {
			return false;
		}
		switch (event.getReactionEmote().getName()) {
		case LEFT:
		case STOP:
		case RIGHT:
		case LEAVE:
			return isValidUser(event.getUser(), event.isFromGuild() ? event.getGuild() : null);
		default:
			return false;
		}
	}

	private void handleMessageReactionAddAction(MessageReactionAddEvent event, Message message, int pageNum) {
		int newPageNum = pageNum;
		int pages = guildMap.size();
		switch (event.getReaction().getReactionEmote().getName()) {
		case LEFT:
			if ((newPageNum == 1) && wrapPageEnds) {
				newPageNum = pages + 1;
			}
			if (newPageNum > 1) {
				newPageNum--;
			}
			break;
		case RIGHT:
			if ((newPageNum == pages) && wrapPageEnds) {
				newPageNum = 0;
			}
			if (newPageNum < pages) {
				newPageNum++;
			}
			break;
		case LEAVE:
			botInstance.getGuildById(currentGuild.getId()).leave().queue();
			guildMap.remove(currentEmbed, currentGuild);
			break;
		case STOP:
			finalAction.accept(message);
			return;
		}
		try {
			event.getReaction().removeReaction(event.getUser()).queue();
		} catch (PermissionException ignored) {
		}
		int n = newPageNum;
		message.editMessage(renderPage(newPageNum)).queue(m -> pagination(m, n));
	}

	private Message renderPage(int pageNum) {
		MessageBuilder mbuilder = new MessageBuilder();
		int size = guildMap.keySet().size();
		List<MessageEmbed> embedList = new ArrayList<>(size);
		for (MessageEmbed em : guildMap.keySet()) {
			embedList.add(em);
		}
		MessageEmbed membed = embedList.get(pageNum - 1);

		currentGuild = null;
		currentGuild = guildMap.get(membed);

		currentEmbed = null;
		currentEmbed = membed;

		mbuilder.setEmbeds(membed);
		if (text != null) {
			mbuilder.append(text.apply(pageNum, embedList.size()));
		}
		return mbuilder.build();
	}

	public static class Builder extends Menu.Builder<Builder, EmbedMessageMenu> {

		private BiFunction<Integer, Integer, String> text = (page, pages) -> null;
		private Consumer<Message> finalAction = m -> m.delete().queue();
		private JDA botInstance = null;

		private Map<MessageEmbed, Guild> guildMap = new LinkedHashMap<>();

		@Override
		public EmbedMessageMenu build() {
			Checks.check(waiter != null, "Must set an EventWaiter");
			Checks.check(botInstance != null, "Must set the botInstance");
			Checks.check(!guildMap.isEmpty(), "Must include at least one item to paginate");

			return new EmbedMessageMenu(waiter, users, roles, timeout, unit, text, finalAction, guildMap, botInstance);
		}

		public Builder setJda(JDA botInstance) {
			this.botInstance = botInstance;
			return this;
		}

		/**
		 * Sets the text of the {@link net.dv8tion.jda.api.entities.Message Message} to
		 * be displayed when the {@link com.readonlydev.menu.EmbedPaginator
		 * EmbedPaginator} is built.
		 *
		 * @param text The Message content to be displayed above the embed when the
		 * EmbedPaginator is built.
		 *
		 * @return This builder
		 */
		public Builder setText(String text) {
			this.text = (i0, i1) -> text;
			return this;
		}

		/**
		 * Sets the text of the {@link net.dv8tion.jda.api.entities.Message Message} to
		 * be displayed relative to the total page number and the current page as
		 * determined by the provided {@link java.util.function.BiFunction BiFunction}.
		 * <br>
		 * As the page changes, the BiFunction will re-process the current page number
		 * and the total page number, allowing for the displayed text of the Message to
		 * change depending on the page number.
		 *
		 * @param textBiFunction The BiFunction that uses both current and total page
		 * numbers, to get text for the Message
		 *
		 * @return This builder
		 */
		public Builder setText(BiFunction<Integer, Integer, String> textBiFunction) {
			this.text = textBiFunction;
			return this;
		}

		/**
		 * Sets the {@link java.util.function.Consumer Consumer} to perform if the
		 * {@link com.readonlydev.menu.EmbedPaginator EmbedPaginator} times
		 * out.
		 *
		 * @param finalAction The Consumer action to perform if the EmbedPaginator times
		 * out
		 *
		 * @return This builder
		 */
		public Builder setFinalAction(Consumer<Message> finalAction) {
			this.finalAction = finalAction;
			return this;
		}

		/**
		 * Clears all previously set items.
		 *
		 * @return This builder
		 */
		public Builder clearItems() {
			this.guildMap.clear();
			return this;
		}

		/**
		 * Adds the collection of provided
		 * {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbeds} to the list
		 * of items to paginate.
		 *
		 * @param embeds The collection of MessageEmbeds to add
		 *
		 * @return This builder
		 */
		public Builder setItems(Map<MessageEmbed, Guild> guildMap) {
			this.guildMap = guildMap;
			return this;
		}
	}
}
