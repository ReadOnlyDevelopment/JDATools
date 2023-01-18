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
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */
package com.readonlydev.command.event;

import java.io.File;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.readonlydev.command.Client;
import com.readonlydev.settings.GuildSettingsManager;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.context.UserContextInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class UserContextMenuEvent extends UserContextInteractionEvent
{
	private final Client client;

	public UserContextMenuEvent(@NotNull JDA api, long responseNumber, @NotNull UserContextInteraction interaction, Client client)
	{
		super(api, responseNumber, interaction);
		this.client = client;
	}

	/**
	 * Returns the {@link Client} that triggered this event.
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
	 * {@link ReplyCallbackAction#queue() }.
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
	 * Responds with a {@link Message}.
	 *
	 * <p>
	 * The {@link ReplyCallbackAction} returned by sending the response as a {@link Message} automatically does
	 * {@link ReplyCallbackAction#queue() }.
	 *
	 * @param message
	 *            The MessageCreateData to reply with
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
