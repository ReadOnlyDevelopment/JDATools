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

/**
 * Copyright (c) 2022 ReadOnlyDevelopment
 * 
 * Licensed under the MIT license.
 * See LICENSE file in the project root for details.
 */

package com.github.readonlydevelopment.command;

import com.github.readonlydevelopment.command.ctx.ContextMenu;
import com.github.readonlydevelopment.command.ctx.MessageContextMenu;
import com.github.readonlydevelopment.command.ctx.UserContextMenu;
import com.github.readonlydevelopment.command.event.CommandEvent;
import com.github.readonlydevelopment.command.event.MessageContextMenuEvent;
import com.github.readonlydevelopment.command.event.SlashCommandEvent;
import com.github.readonlydevelopment.command.event.UserContextMenuEvent;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * An implementable "Listener" that can be added to a {@link Client} and used to
 * handle events relating to {@link Command}s.
 */
public interface CommandListener
{

	/**
	 * Called when a {@link Command} is triggered by a {@link CommandEvent}.
	 *
	 * @param event
	 *            The CommandEvent that triggered the Command
	 * @param command
	 *            The Command that was triggered
	 */
	default void onCommand(CommandEvent event, Command command)
	{
	}

	/**
	 * Called when a {@link SlashCommand} is triggered by a
	 * {@link SlashCommandEvent SlashCommandEvent}.
	 *
	 * @param event
	 *            The SlashCommandEvent that triggered the Command
	 * @param command
	 *            The SlashCommand that was triggered
	 */
	default void onSlashCommand(SlashCommandEvent event, SlashCommand command)
	{
	}

	/**
	 * Called when a {@link MessageContextMenu} is triggered by a
	 * {@link MessageContextMenuEvent}.
	 *
	 * @param event
	 *            The MessageContextMenuEvent that triggered the
	 *            MessageContextMenu
	 * @param menu
	 *            The MessageContextMenu that was triggered
	 */
	default void onMessageContextMenu(MessageContextMenuEvent event, MessageContextMenu menu)
	{
	}

	/**
	 * Called when a {@link UserContextMenu} is triggered by a
	 * {@link UserContextMenuEvent}.
	 *
	 * @param event
	 *            The UserContextMenuEvent that triggered the UserContextMenu
	 * @param menu
	 *            The UserContextMenu that was triggered
	 */
	default void onUserContextMenu(UserContextMenuEvent event, UserContextMenu menu)
	{
	}

	/**
	 * Called when a {@link Command} is triggered by a {@link CommandEvent}
	 * after it's completed successfully.
	 * <p>
	 * Note that a <i>successfully</i> completed command is one that has not
	 * encountered an error or exception. Calls that do face errors should be
	 * handled by
	 * {@link CommandListener#onCommandException(CommandEvent, Command, Throwable)
	 * CommandListener#onCommandException}
	 *
	 * @param event
	 *            The CommandEvent that triggered the Command
	 * @param command
	 *            The Command that was triggered
	 */
	default void onCompletedCommand(CommandEvent event, Command command)
	{
	}

	/**
	 * Called when a {@link SlashCommand} is triggered by a
	 * {@link SlashCommandEvent} after it's completed successfully.
	 * <p>
	 * Note that a <i>successfully</i> completed slash command is one that has
	 * not encountered an error or exception. Calls that do face errors should
	 * be handled by
	 * {@link CommandListener#onSlashCommandException(SlashCommandEvent, SlashCommand, Throwable)
	 * CommandListener#onSlashCommandException}
	 *
	 * @param event
	 *            The SlashCommandEvent that triggered the Command
	 * @param command
	 *            The SlashCommand that was triggered
	 */
	default void onCompletedSlashCommand(SlashCommandEvent event, SlashCommand command)
	{
	}

	/**
	 * Called when a {@link MessageContextMenu} is triggered by a
	 * {@link MessageContextMenuEvent} after it's completed successfully.
	 * <p>
	 * Note that a <i>successfully</i> completed context menu interaction is one
	 * that has not encountered an error or exception. Calls that do face errors
	 * should be
	 * handled by
	 * {@link CommandListener#onTerminatedMessageContextMenu(MessageContextMenuEvent, MessageContextMenu)}
	 *
	 * @param event
	 *            The MessageContextMenuEvent that triggered the Menu
	 * @param menu
	 *            The MessageContextMenu that was triggered
	 */
	default void onCompletedMessageContextMenu(MessageContextMenuEvent event, MessageContextMenu menu)
	{
	}

	/**
	 * Called when a {@link UserContextMenu} is triggered by a
	 * {@link UserContextMenuEvent} after it's completed successfully.
	 * <p>
	 * Note that a <i>successfully</i> completed context menu interaction is one
	 * that has not encountered an error or exception. Calls that do face errors
	 * should be handled by
	 * {@link CommandListener#onTerminatedUserContextMenu(UserContextMenuEvent, UserContextMenu)}
	 *
	 * @param event
	 *            The MessageContextMenuEvent that triggered the Menu
	 * @param menu
	 *            The MessageContextMenu that was triggered
	 */
	default void onCompletedUserContextMenu(UserContextMenuEvent event, UserContextMenu menu)
	{
	}

	/**
	 * Called when a {@link Command} is triggered by a {@link CommandEvent} but
	 * is terminated before completion.
	 *
	 * @param event
	 *            The CommandEvent that triggered the Command
	 * @param command
	 *            The Command that was triggered
	 */
	default void onTerminatedCommand(CommandEvent event, Command command)
	{
	}

	/**
	 * Called when a {@link SlashCommand} is triggered by a
	 * {@link SlashCommandEvent} but is terminated before completion.
	 *
	 * @param event
	 *            The SlashCommandEvent that triggered the Command
	 * @param command
	 *            The SlashCommand that was triggered
	 */
	default void onTerminatedSlashCommand(SlashCommandEvent event, SlashCommand command)
	{
	}

	/**
	 * Called when a {@link MessageContextMenu} is triggered by a
	 * {@link MessageContextMenuEvent} but is terminated before completion.
	 *
	 * @param event
	 *            The ContextMenuEvent that triggered the Context Menu
	 * @param menu
	 *            The ContextMenu that was triggered
	 */
	default void onTerminatedMessageContextMenu(MessageContextMenuEvent event, MessageContextMenu menu)
	{
	}

	/**
	 * Called when a {@link UserContextMenu} is triggered by a
	 * {@link UserContextMenuEvent} but is terminated before completion.
	 *
	 * @param event
	 *            The ContextMenuEvent that triggered the Context Menu
	 * @param menu
	 *            The ContextMenu that was triggered
	 */
	default void onTerminatedUserContextMenu(UserContextMenuEvent event, UserContextMenu menu)
	{
	}

	/**
	 * Called when a {@link MessageReceivedEvent} is caught by the Client
	 * Listener's but doesn't correspond to a {@link Command}.
	 * <p>
	 * In other words, this catches all <b>non-command</b> MessageReceivedEvents
	 * allowing you to handle them without implementation of another listener.
	 *
	 * @param event
	 *            A MessageReceivedEvent that wasn't used to call a Command
	 */
	default void onNonCommandMessage(MessageReceivedEvent event)
	{
	}

	/**
	 * Called when a {@link Command} catches a {@link Throwable} <b>during
	 * execution</b>.
	 * <p>
	 * This doesn't account for exceptions thrown during other pre-checks, and
	 * should not be treated as such!
	 * <p>
	 * An example of this misconception is via a {@link Command.Category} test:
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
	 * <p>
	 * The {@link NullPointerException} thrown will not be caught by this
	 * method!
	 *
	 * @param event
	 *            The CommandEvent that triggered the Command
	 * @param command
	 *            The Command that was triggered
	 * @param throwable
	 *            The Throwable thrown during Command execution
	 */
	default void onCommandException(CommandEvent event, Command command, Throwable throwable)
	{
		// Default rethrow as a runtime exception.
		throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
	}

	/**
	 * Called when a {@link com.github.readonlydevelopment.command.SlashCommand
	 * SlashCommand} catches a {@link java.lang.Throwable Throwable} <b>during
	 * execution</b>.
	 * <p>
	 * This doesn't account for exceptions thrown during other pre-checks, and
	 * should not be treated as such!
	 * <p>
	 * The
	 * {@link java.lang.NullPointerException NullPointerException} thrown will
	 * not be caught by this method!
	 *
	 * @param event
	 *            The CommandEvent that triggered the Command
	 * @param command
	 *            The Command that was triggered
	 * @param throwable
	 *            The Throwable thrown during Command execution
	 */
	default void onSlashCommandException(SlashCommandEvent event, SlashCommand command, Throwable throwable)
	{
		// Default rethrow as a runtime exception.
		throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
	}

	/**
	 * Called when a {@link ContextMenu} catches a {@link java.lang.Throwable
	 * Throwable} <b>during execution</b>.
	 * <p>
	 * This doesn't account for exceptions thrown during other pre-checks, and
	 * should not be treated as such!
	 * <p>
	 * The {@link NullPointerException} thrown will not be caught by this
	 * method!
	 *
	 * @param event
	 *            The Context Menu Event that triggered the ContextMenu
	 * @param menu
	 *            The Context Menu that was triggered
	 * @param throwable
	 *            The Throwable thrown during Command execution
	 */
	default void onMessageContextMenuException(MessageContextMenuEvent event, MessageContextMenu menu, Throwable throwable)
	{
		// Default rethrow as a runtime exception.
		throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
	}

	/**
	 * Called when a {@link ContextMenu} catches a {@link java.lang.Throwable
	 * Throwable} <b>during execution</b>.
	 * <p>
	 * This doesn't account for exceptions thrown during other pre-checks, and
	 * should not be treated as such!
	 * <p>
	 * The {@link NullPointerException} thrown will not be caught by this
	 * method!
	 *
	 * @param event
	 *            The Context Menu Event that triggered the ContextMenu
	 * @param menu
	 *            The Context Menu that was triggered
	 * @param throwable
	 *            The Throwable thrown during Command execution
	 */
	default void onUserContextMenuException(UserContextMenuEvent event, UserContextMenu menu, Throwable throwable)
	{
		// Default rethrow as a runtime exception.
		throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
	}
}
