package com.readonlydev.cmd.included;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.readonlydev.cmd.Command;
import com.readonlydev.cmd.CommandEvent;
import com.readonlydev.common.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class CommandGuildlist extends Command {

	private final EmbedMessageMenu.Builder menuBuilder;
	private final JDA jdaInstance;

	public CommandGuildlist(EventWaiter waiter, JDA jdaInstance) {
		this.jdaInstance = jdaInstance;
		this.category = new Command.Category("Bot-Owner");
		this.help = "shows the list of guilds the bot is on";
		this.ownerCommand = true;
		this.guildOnly = false;
		this.menuBuilder = new EmbedMessageMenu.Builder()
				.setText("Servers That I Am In")
				.setEventWaiter(waiter)
				.setJda(jdaInstance)
				.setFinalAction(m -> {
					try {
						m.clearReactions().queue();
					} catch(PermissionException ex) {
						m.delete().queue();
					}
				})
				.setTimeout(3, TimeUnit.MINUTES);
	}

	@Override
	public void execute(CommandEvent event) {

		menuBuilder.clearItems();

		Stream<Guild> guilds = Stream.of(this.jdaInstance.getGuilds().toArray(new Guild[0]));
		menuBuilder.setItems(guilds.collect(Collectors.toMap(g -> JoinedServerEmbed.func().apply(g), g -> g)));

		EmbedMessageMenu menu = menuBuilder.build();
		event.getMessage().delete().queue();
		menu.display(event.getChannel());
	}

	@FunctionalInterface
	interface FunctionalEmbed extends Function<Guild, MessageEmbed> {

		@Override
		MessageEmbed apply(Guild t);

	}

	final static class JoinedServerEmbed implements FunctionalEmbed {

		private EmbedBuilder embed = new EmbedBuilder();

		static JoinedServerEmbed func() {
			return new JoinedServerEmbed();
		}

		@Override
		public MessageEmbed apply(Guild g) {
			embed.setTitle(g.getName(), g.getIconUrl());
			embed.setDescription(format("Owner", g.getOwner().getUser().getAsTag()));
			embed.appendDescription(format("Members", g.getMemberCount()));
			embed.appendDescription(format("Created", time(g.getTimeCreated())));
			embed.setThumbnail(g.getIconUrl()).build();
			return embed.build();
		}

		private String time(OffsetDateTime offset) {
			return offset.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
		}

		private String format(String title, String rest) {
			return "**" + title + "**: \n" + rest + "\n\n";
		}

		private String format(String title, int rest) {
			return "**" + title + "**: \n" + rest + "\n\n";
		}
	}
}
