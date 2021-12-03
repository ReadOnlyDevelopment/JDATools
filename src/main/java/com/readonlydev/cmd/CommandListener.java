package com.readonlydev.cmd;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * An implementable "Listener" that can be added to a
 * {@link com.readonlydev.cmd.CommandClient CommandClient} and used to
 * handle events relating to {@link com.readonlydev.cmd.Command
 * Command}s.
 */
public interface CommandListener {
	/**
	 * Called when a {@link com.readonlydev.cmd.Command Command} is
	 * triggered by a {@link com.readonlydev.cmd.CommandEvent CommandEvent}.
	 * 
	 * @param event The CommandEvent that triggered the Command
	 * @param command The Command that was triggered
	 */
	default void onCommand(CommandEvent event, Command command) {}

	/**
	 * Called when a {@link com.readonlydev.cmd.SlashCommand SlashCommand}
	 * is triggered by a
	 * {@link net.dv8tion.jda.api.events.interaction.SlashCommandEvent
	 * SlashCommandEvent}.
	 *
	 * @param event The SlashCommandEvent that triggered the Command
	 * @param command The SlashCommand that was triggered
	 */
	default void onSlashCommand(SlashCommandEvent event, SlashCommand command) {}

	/**
	 * Called when a {@link com.readonlydev.cmd.Command Command} is
	 * triggered by a {@link com.readonlydev.cmd.CommandEvent CommandEvent}
	 * after it's completed successfully.
	 * <p>
	 * Note that a <i>successfully</i> completed command is one that has not
	 * encountered an error or exception. Calls that do face errors should be
	 * handled by
	 * {@link CommandListener#onCommandException(CommandEvent, Command, Throwable)
	 * CommandListener#onCommandException}
	 * 
	 * @param event The CommandEvent that triggered the Command
	 * @param command The Command that was triggered
	 */
	default void onCompletedCommand(CommandEvent event, Command command) {}

	/**
	 * Called when a {@link com.readonlydev.cmd.SlashCommand SlashCommand}
	 * is triggered by a
	 * {@link net.dv8tion.jda.api.events.interaction.SlashCommandEvent
	 * SlashCommandEvent} after it's completed successfully.
	 * <p>
	 * Note that a <i>successfully</i> completed slash command is one that has not
	 * encountered an error or exception. Calls that do face errors should be
	 * handled by
	 * {@link CommandListener#onSlashCommandException(SlashCommandEvent, SlashCommand, Throwable)
	 * CommandListener#onSlashCommandException}
	 *
	 * @param event The SlashCommandEvent that triggered the Command
	 * @param command The SlashCommand that was triggered
	 */
	default void onCompletedSlashCommand(SlashCommandEvent event, SlashCommand command) {}

	/**
	 * Called when a {@link com.readonlydev.cmd.Command Command} is
	 * triggered by a {@link com.readonlydev.cmd.CommandEvent CommandEvent}
	 * but is terminated before completion.
	 * 
	 * @param event The CommandEvent that triggered the Command
	 * @param command The Command that was triggered
	 */
	default void onTerminatedCommand(CommandEvent event, Command command) {}

	/**
	 * Called when a {@link com.readonlydev.cmd.SlashCommand Command} is
	 * triggered by a
	 * {@link net.dv8tion.jda.api.events.interaction.SlashCommandEvent
	 * SlashCommandEvent} but is terminated before completion.
	 *
	 * @param event The SlashCommandEvent that triggered the Command
	 * @param command The SlashCommand that was triggered
	 */
	default void onTerminatedSlashCommand(SlashCommandEvent event, SlashCommand command) {}

	/**
	 * Called when a {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent
	 * MessageReceivedEvent} is caught by the Client Listener's but doesn't
	 * correspond to a {@link com.readonlydev.cmd.Command Command}.
	 * <p>
	 * In other words, this catches all <b>non-command</b> MessageReceivedEvents
	 * allowing you to handle them without implementation of another listener.
	 * 
	 * @param event A MessageReceivedEvent that wasn't used to call a Command
	 */
	default void onNonCommandMessage(MessageReceivedEvent event) {}

	/**
	 * Called when a {@link com.readonlydev.cmd.Command Command} catches a
	 * {@link java.lang.Throwable Throwable} <b>during execution</b>.
	 * <p>
	 * This doesn't account for exceptions thrown during other pre-checks, and
	 * should not be treated as such!
	 * <p>
	 * An example of this misconception is via a
	 * {@link com.readonlydev.cmd.Command.Category Category} test:
	 *
	 * <pre>
	 * <code> public class BadCommand extends Command {
	 *
	 *      public BadCommand() {
	 *          this.name = "bad";
	 *          this.category = new Category("bad category", event {@literal ->} {
	 *              // This will throw a NullPointerException if it's not from a Guild!
	 *              return event.getGuild().getIdLong() == 12345678910111213;
	 *          });
	 *      }
	 *
	 *      {@literal @Override}
	 *      protected void execute(CommandEvent) {
	 *          event.reply("This is a bad command!");
	 *      }
	 *
	 * }</code>
	 * </pre>
	 *
	 * The {@link java.lang.NullPointerException NullPointerException} thrown will
	 * not be caught by this method!
	 *
	 * @param event The CommandEvent that triggered the Command
	 * @param command The Command that was triggered
	 * @param throwable The Throwable thrown during Command execution
	 */
	default void onCommandException(CommandEvent event, Command command, Throwable throwable) {
		// Default rethrow as a runtime exception.
		throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
	}

	/**
	 * Called when a {@link com.readonlydev.cmd.SlashCommand SlashCommand}
	 * catches a {@link java.lang.Throwable Throwable} <b>during execution</b>.
	 * <p>
	 * This doesn't account for exceptions thrown during other pre-checks, and
	 * should not be treated as such!
	 * The {@link java.lang.NullPointerException NullPointerException} thrown will
	 * not be caught by this method!
	 *
	 * @param event The CommandEvent that triggered the Command
	 * @param command The Command that was triggered
	 * @param throwable The Throwable thrown during Command execution
	 */
	default void onSlashCommandException(SlashCommandEvent event, SlashCommand command, Throwable throwable) {
		// Default rethrow as a runtime exception.
		throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
	}
}
