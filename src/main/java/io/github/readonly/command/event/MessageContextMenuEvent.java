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

package io.github.readonly.command.event;

import java.io.File;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.readonly.command.Client;
import io.github.readonly.command.ctx.MessageContextMenu;
import io.github.readonly.settings.GuildSettingsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.context.MessageContextInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * <h2><b>Message Context Menus In JDATools</b></h2>
 *
 * <p>
 * The internal inheritance for Message Context Menus used in JDATools is that of the object.
 *
 * <p>
 * Classes created inheriting this class gain the unique traits of commands operated using the menu Extension. <br>
 * Using several fields, a command can define properties that make it unique and complex while maintaining a low level
 * of development. <br>
 * All classes extending this class can define any number of these fields in a object constructor and then create the
 * menu action/response in the abstract {@link MessageContextMenu#execute(MessageContextMenuEvent)} body:
 *
 * <pre><code> public class ExampleCmd extends MessageContextMenu {
 *
 *      public ExampleCmd() {
 *          this.name = "Example";
 *      }
 *
 *      {@literal @Override}
 *      protected void execute(MessageContextMenuEvent event) {
 *          event.reply("Hey look! This would be the bot's reply if this was a command!");
 *      }
 *
 * }</code></pre>
 *
 * Execution is with the provision of a MessageContextInteractionEvent-Client wrapper called a
 * {@link MessageContextMenu} and is performed in two steps:
 * <ul>
 * <li>{@link MessageContextMenu#run(MessageContextMenuEvent) run} - The menu runs through a series of conditionals,
 * automatically terminating the command instance if one is not met, and possibly providing an error response.</li>
 *
 * <li>{@link MessageContextMenu#execute(MessageContextMenuEvent) execute} - The menu, now being cleared to run,
 * executes and performs whatever lies in the abstract body method.</li>
 * </ul>
 *
 * @author Olivia (Chew)
 */
public class MessageContextMenuEvent extends MessageContextInteractionEvent
{
	private final Client client;

	public MessageContextMenuEvent(@NotNull JDA api, long responseNumber, @NotNull MessageContextInteraction interaction, Client client)
	{
		super(api, responseNumber, interaction);
		this.client = client;
	}

	/**
	 * Returns the {@link Client} that initiated this event.
	 *
	 * @return The initiating Client
	 */
	public Client getClient()
	{
		return client;
	}

	/**
	 * Responds with a String message.
	 *
	 * <p>
	 * The {@link ReplyCallbackAction} returned by sending the response as a {@link Message} automatically does
	 * {@link ReplyCallbackAction#queue()}.
	 *
	 * @param message
	 *            A String message to reply with
	 */
	public void respond(String message)
	{
		reply(message).queue();
	}

	/**
	 * Responds with a {@link MessageEmbed}.
	 *
	 * <p>
	 * The {@link ReplyCallbackAction} returned by sending the response as a {@link Message} automatically does
	 * {@link ReplyCallbackAction#queue() }.
	 *
	 * @param embed
	 *            The MessageEmbed to reply with
	 */
	public void respond(MessageEmbed embed)
	{
		replyEmbeds(embed).queue();
	}

	/**
	 * Replies with a {@link Message}.
	 *
	 * <p>
	 * The {@link ReplyCallbackAction} returned by sending the response as a {@link Message} automatically does
	 * {@link ReplyCallbackAction#queue() }.
	 *
	 * @param message
	 *            The Message to reply with
	 */
	public void respond(MessageCreateData message)
	{
		reply(message).queue();
	}

	/**
	 * Responds with a {@link File} with the provided name, or a default name if left null.
	 *
	 * <p>
	 * The {@link ReplyCallbackAction} returned by sending the response as a {@link Message} automatically does
	 * {@link ReplyCallbackAction#queue() RestAction#queue()}.
	 *
	 * <p>
	 * This method uses {@link GenericCommandInteractionEvent#replyFiles(FileUpload...)} to send the File. For more
	 * information on what a bot may send using this, you may find the info in that method.
	 *
	 * @param file
	 *            The File to reply with
	 * @param filename
	 *            The filename that Discord should display (null for default).
	 * @param description
	 *            The description to set (null for no description).
	 * @param spoiler
	 *            whether the file should be marked as spoiler.
	 */
	public void respond(File file, String filename, String description, boolean spoiler)
	{
		FileUpload fileUpload = FileUpload.fromData(file, filename);
		if ((description != null) && !description.isEmpty())
		{
			fileUpload.setDescription(description);
		}

		if (spoiler)
		{
			fileUpload.asSpoiler();
		}

		replyFiles(fileUpload).queue();
	}

	/**
	 * Tests whether the {@link User} who triggered this event is an owner of the bot.
	 *
	 * @return {@code true} if the User is the Owner, else {@code false}
	 */
	public boolean isOwner()
	{
		if (getUser().getId().equals(this.getClient().getOwnerId()))
		{
			return true;
		}
		if (this.getClient().getCoOwnerIds() == null)
		{
			return false;
		}
		for (String id : this.getClient().getCoOwnerIds())
		{
			if (id.equals(getUser().getId()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the settings of the guild in which this context menu was used.
	 *
	 * @param <S> the type of the settings
	 * @return the settings, or {@code null} if either of the following conditions are met:
	 * <ul>
	 *     <li>this interaction didn't happen in a guild</li>
	 *     <li>the client's {@link GuildSettingsManager} is null</li>
	 *     <li>the {@link GuildSettingsManager} returned null settings for the guild</li>
	 * </ul>
	 */
	@Nullable
	public <S> S getGuildSettings()
	{
		if (!isFromGuild()) {
			return null;
		}
		final GuildSettingsManager<S> manager = getClient().getSettingsManager();
		if (manager == null)
		{
			return null;
		}
		return manager.getSettings(getGuild());
	}

	/**
	 * Gets the settings of the guild in which this context menu was used.
	 *
	 * @param settingsClazz the class of the settings
	 * @param <S> the type of the settings
	 * @return the settings, or {@code null} if either of the following conditions are met:
	 * <ul>
	 *     <li>this interaction didn't happen in a guild</li>
	 *      <li>the client's {@link GuildSettingsManager} is null</li>
	 *      <li>the {@link GuildSettingsManager} returned null settings for the guild</li>
	 *      <li>the {@link GuildSettingsManager} returned settings that are not assignable to the {@code settingsClazz}</li>
	 * </ul>
	 */
	@Nullable
	@SuppressWarnings("rawtypes")
	public <S> S getGuildSettings(Class<? extends S> settingsClazz)
	{
		if (!isFromGuild()) {
			return null;
		}
		final GuildSettingsManager manager = getClient().getSettingsManager();
		if (manager == null)
		{
			return null;
		}
		final Object settings = manager.getSettings(getGuild());
		if (!settingsClazz.isInstance(settings))
		{
			return null;
		}
		return settingsClazz.cast(settings);
	}
}
