/*
 * This file is part of JDATools, licensed under the MIT License (MIT).
 *
 * Copyright (c) ROMVoid95
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.readonly.command.included;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import io.github.readonly.common.waiter.EventWaiter;
import io.github.readonly.menu.Menu;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import net.dv8tion.jda.internal.utils.Checks;

public class EmbedMessageMenu extends Menu
{

	private final BiFunction<Integer, Integer, String>	text;
	private final Consumer<Message>						finalAction;
	private Map<MessageEmbed, Guild>					guildMap;
	private final JDA									botInstance;
	private final boolean								wrapPageEnds	= true;

	private Guild			currentGuild;
	private MessageEmbed	currentEmbed;

	protected EmbedMessageMenu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit, BiFunction<Integer, Integer, String> text, Consumer<Message> finalAction, Map<MessageEmbed, Guild> guildMap, JDA botInstance)
	{
		super(waiter, users, roles, timeout, unit);
		this.text = text;
		this.finalAction = finalAction;
		this.guildMap = guildMap;
		this.botInstance = botInstance;
	}

	@Override
	public void display(Message message)
	{
		this.display(message.getChannel());
	}

	/**
	 * Begins pagination on page 1 as a new {@link Message} in the provided {@link MessageChannel
	 * MessageChannel}.
	 *
	 * @param channel
	 *            The MessageChannel to send the new Message to
	 */
	@Override
	public void display(MessageChannel channel)
	{
		paginate(channel, 1);
	}

	@Override
	public void display(InteractionHook hook)
	{
		paginate(hook, 1);
	}

	/**
	 * Begins pagination as a new {@link Message} in the provided {@link MessageChannel},
	 * starting on whatever page number is provided.
	 *
	 * @param channel
	 *            The MessageChannel to send the new Message to
	 * @param pageNum
	 *            The page number to begin on
	 */
	public void paginate(MessageChannel channel, int pageNum)
	{
		if (pageNum < 1)
		{
			pageNum = 1;
		} else if (pageNum > this.guildMap.size())
		{
			pageNum = this.guildMap.size();
		}
		MessageEditData msg = renderPage(pageNum);
		initialize(channel.sendMessage(MessageCreateData.fromEditData(msg)), pageNum);
	}

	public void paginate(InteractionHook hook, int pageNum) {
		if(pageNum<1)
		{
			pageNum = 1;
		} else if (pageNum > guildMap.size())
		{
			pageNum = guildMap.size();
		}
		MessageEditData msg = renderPage(pageNum);
		initialize(hook.editOriginal(msg), pageNum);
	}

	private void initialize(RestAction<Message> action, int pageNum)
	{
		action.queue(m ->
		{
			if (guildMap.size() > 1)
			{
				m.addReaction(Emoji.fromFormatted(CommandButton.LEFT.get())).queue();
				m.addReaction(Emoji.fromFormatted(CommandButton.STOP.get())).queue();
				m.addReaction(Emoji.fromFormatted(CommandButton.RIGHT.get())).queue();
				m.addReaction(Emoji.fromFormatted(CommandButton.LEAVE.get())).queue(v -> pagination(m, pageNum), t -> pagination(m, pageNum));
			} else
			{
				finalAction.accept(m);
			}
		});
	}

	private void pagination(Message message, int pageNum)
	{
		paginationWithoutTextInput(message, pageNum);
	}

	private void paginationWithoutTextInput(Message message, int pageNum)
	{
		waiter.waitForEvent(MessageReactionAddEvent.class, event -> checkReaction(event, message.getIdLong()), event -> handleMessageReactionAddAction(event, message, pageNum), timeout, unit, () -> finalAction.accept(message));
	}

	private boolean checkReaction(MessageReactionAddEvent event, long messageId)
	{
		if (event.getMessageIdLong() != messageId)
		{
			return false;
		}

		CommandButton button = CommandButton.get(event.getReaction().getEmoji().getName());

		switch (button)
		{
		case LEFT:
		case STOP:
		case RIGHT:
		case LEAVE:
			return isValidUser(event.getUser(), event.isFromGuild() ? event.getGuild() : null);
		default:
			return false;
		}
	}

	private void handleMessageReactionAddAction(MessageReactionAddEvent event, Message message, int pageNum)
	{
		int	newPageNum	= pageNum;
		int	pages		= guildMap.size();

		CommandButton button = CommandButton.get(event.getReaction().getEmoji().getName());

		switch (button)
		{
		case LEFT:
			if ((newPageNum == 1) && wrapPageEnds)
			{
				newPageNum = pages + 1;
			}
			if (newPageNum > 1)
			{
				newPageNum--;
			}
			break;
		case RIGHT:
			if ((newPageNum == pages) && wrapPageEnds)
			{
				newPageNum = 0;
			}
			if (newPageNum < pages)
			{
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
		try
		{
			event.getReaction().removeReaction(event.getUser()).queue();
		} catch (PermissionException ignored)
		{
		}
		int n = newPageNum;
		message.editMessage(renderPage(newPageNum)).queue(m -> pagination(m, n));
	}

	private MessageEditData renderPage(int pageNum)
	{
		MessageEditBuilder	mbuilder	= new MessageEditBuilder();
		int					size		= this.guildMap.keySet().size();
		List<MessageEmbed>	embedList	= new ArrayList<>(size);
		for (MessageEmbed em : this.guildMap.keySet())
		{
			embedList.add(em);
		}
		MessageEmbed membed = embedList.get(pageNum - 1);
		this.currentGuild = null;
		this.currentGuild = this.guildMap.get(membed);
		this.currentEmbed = null;
		this.currentEmbed = membed;
		mbuilder.setEmbeds(new MessageEmbed[]
			{ membed });
		if (this.text != null)
		{
			mbuilder.setContent(this.text.apply(Integer.valueOf(pageNum), Integer.valueOf(embedList.size())));
		}
		return mbuilder.build();
	}

	public static class Builder extends Menu.Builder<Builder, EmbedMessageMenu>
	{

		private BiFunction<Integer, Integer, String>	text		= (page, pages) -> null;
		private Consumer<Message>						finalAction	= m -> m.delete().queue();
		private JDA										botInstance	= null;

		private Map<MessageEmbed, Guild> guildMap = new LinkedHashMap<>();

		@Override
		public EmbedMessageMenu build()
		{
			Checks.check(waiter != null, "Must set an EventWaiter");
			Checks.check(botInstance != null, "Must set the botInstance");
			Checks.check(!guildMap.isEmpty(), "Must include at least one item to paginate");

			return new EmbedMessageMenu(waiter, users, roles, timeout, unit, text, finalAction, guildMap, botInstance);
		}

		public Builder setJda(JDA botInstance)
		{
			this.botInstance = botInstance;
			return this;
		}

		/**
		 * Sets the text of the {@link Message} to be displayed when the
		 * {@link io.github.readonly.menu.EmbedPaginator EmbedPaginator} is built.
		 *
		 * @param text
		 *            The Message content to be displayed above the embed when the EmbedPaginator is built.
		 *
		 * @return This builder
		 */
		public Builder setText(String text)
		{
			this.text = (i0, i1) -> text;
			return this;
		}

		/**
		 * Sets the text of the {@link Message} to be displayed relative to the total page number and the
		 * current page as determined by the provided {@link java.util.function.BiFunction BiFunction}. <br>
		 * As the page changes, the BiFunction will re-process the current page number and the total page number,
		 * allowing for the displayed text of the Message to change depending on the page number.
		 *
		 * @param textBiFunction
		 *            The BiFunction that uses both current and total page numbers, to get text for the Message
		 *
		 * @return This builder
		 */
		public Builder setText(BiFunction<Integer, Integer, String> textBiFunction)
		{
			this.text = textBiFunction;
			return this;
		}

		/**
		 * Sets the {@link java.util.function.Consumer Consumer} to perform if the
		 * {@link io.github.readonly.menu.EmbedPaginator EmbedPaginator} times out.
		 *
		 * @param finalAction
		 *            The Consumer action to perform if the EmbedPaginator times out
		 *
		 * @return This builder
		 */
		public Builder setFinalAction(Consumer<Message> finalAction)
		{
			this.finalAction = finalAction;
			return this;
		}

		/**
		 * Clears all previously set items.
		 *
		 * @return This builder
		 */
		public Builder clearItems()
		{
			this.guildMap.clear();
			return this;
		}

		/**
		 * Adds the collection of provided {@link MessageEmbed MessageEmbeds} to the list of items to paginate.
		 *
		 * @param guildMap
		 *            The Map of &lt;MessageEmbed, Guild&gt; to add
		 *
		 * @return This builder
		 */
		public Builder setItems(Map<MessageEmbed, Guild> guildMap)
		{
			this.guildMap = guildMap;
			return this;
		}
	}
}
