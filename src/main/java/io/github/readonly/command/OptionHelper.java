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

package io.github.readonly.command;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * Utility class containing useful methods for getting values of Slash command arguments. <h2>Example</h2>
 *
 * <pre>
 * <code>
 * public class MyCommand extends SlashCommand {
 *     public MyCommand() {
 *         this.name = "example";
 *         this.help = "Example command";
 *
 *         this.options = Arrays.asList(
 *             new OptionData(OptionType.STRING, "string", "A String option").setRequired(true),
 *             new OptionData(OptionType.USER, "user", "A optional User")
 *         );
 *     }
 *
 *    {@literal @Override}
 *     protected void execute(SlashCommandInteractionEvent event) {
 *         // get "string" option as String. Defaults to null if not found
 *         String arg1 = OptionHelper.optString(event, "string");
 *         // Get the provided user, or use the executor if they did not provide one
 *         User optionalUser = OptionHelper.optUser(event, "user", event.getUser());
 *     }
 * }
 * </code>
 * </pre>
 */
@UtilityClass
public class OptionHelper
{

	/**
	 * Gets the provided Option Key as a String value, or returns {@code null} if the option cannot be found.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or null if the option is not present
	 */
	@Nullable
	public static String optString(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optString(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a String value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static String optString(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable String defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsString();
	}

	public static int optInteger(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optInteger(event, key, -1);
	}

	public static int optInteger(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable int defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsInt();
	}

	/**
	 * Gets the provided Option Key as a boolean value, or returns {@code false} if the option cannot be found.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or false if the option is not present
	 */
	public static boolean optBoolean(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optBoolean(event, key, false);
	}

	/**
	 * Gets the provided Option Key as a boolean value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     The fallback option in case of the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	public static boolean optBoolean(@NotNull SlashCommandInteractionEvent event, @NotNull String key, boolean defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsBoolean();
	}

	/**
	 * Gets the provided Option Key as a long value, or returns {@code 0} if the option cannot be found.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or 0 if the option is not present
	 */
	public static long optLong(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optLong(event, key, 0);
	}

	/**
	 * Gets the provided Option Key as a long value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     The fallback option in case of the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	public static long optLong(@NotNull SlashCommandInteractionEvent event, @NotNull String key, long defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsLong();
	}

	/**
	 * Gets the provided Option Key as a double value, or returns {@code 0.0} if the option cannot be found.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or 0.0 if the option is not present
	 */
	public static double optDouble(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optDouble(event, key, 0.0);
	}

	/**
	 * Gets the provided Option Key as a double value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     The fallback option in case of the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	public static double optDouble(@NotNull SlashCommandInteractionEvent event, @NotNull String key, double defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsDouble();
	}

	/**
	 * Gets the provided Option Key as a GuildChannel value, or returns {@code null} if the option cannot be found. <br>
	 * This will <b>always</b> return null when the SlashCommandInteractionEvent was not executed in a Guild.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or null if the option is not present
	 */
	@Nullable
	public static GuildChannel optGuildChannel(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optGuildChannel(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a GuildChannel value, or returns the default one if the option cannot be found.
	 * <br> This will <b>always</b> return the default value when the SlashCommandInteractionEvent was not executed in a
	 * Guild.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static GuildChannel optGuildChannel(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable GuildChannel defaultValue)
	{
		if (!event.isFromGuild())
		{
			return defaultValue;
		}

		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsChannel();
	}

	/**
	 * Gets the provided Option Key as a Member value, or returns {@code null} if the option cannot be found. <br> This
	 * will <b>always</b> return null when the SlashCommandInteractionEvent was not executed in a Guild.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or null if the option is not present
	 */
	@Nullable
	public static Member optMember(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optMember(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a Member value, or returns the default one if the option cannot be found. <br>
	 * This will <b>always</b> return the default value when the SlashCommandInteractionEvent was not executed in a
	 * Guild.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static Member optMember(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable Member defaultValue)
	{
		if (!event.isFromGuild())
		{
			return defaultValue; // Non-guild commands do not have a member.
		}

		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsMember();
	}

	/**
	 * Gets the provided Option Key as a IMentionable value, or returns {@code null} if the option cannot be found.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or null if the option is not present
	 */
	@Nullable
	public static IMentionable optMentionable(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optMentionable(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a IMentionable value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static IMentionable optMentionable(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable IMentionable defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsMentionable();
	}

	/**
	 * Gets the provided Option Key as a Role value, or returns {@code null} if the option cannot be found. <br> This
	 * will <b>always</b> return null when the SlashCommandInteractionEvent was not executed in a Guild.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or null if the option is not present
	 */
	@Nullable
	public static Role optRole(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optRole(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a Role value, or returns the default one if the option cannot be found. <br> This
	 * will <b>always</b> return the default value when the SlashCommandInteractionEvent was not executed in a Guild.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static Role optRole(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable Role defaultValue)
	{
		if (!event.isFromGuild())
		{
			return defaultValue;
		}

		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsRole();
	}

	/**
	 * Gets the provided Option Key as a User value, or returns {@code null} if the option cannot be found.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or null if the option is not present
	 */
	@Nullable
	public static User optUser(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optUser(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a User value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static User optUser(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable User defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsUser();
	}

	/**
	 * Gets the provided Option Key as a MessageChannel value, or returns {@code null} if the option cannot be found.
	 *
	 * @param event
	 *              The slash command event to get options from
	 * @param key
	 *              The option we want
	 *
	 * @return The provided option, or null if the option is not present
	 */
	@Nullable
	public static MessageChannel optMessageChannel(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optMessageChannel(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a MessageChannel value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static MessageChannel optMessageChannel(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable MessageChannel defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsChannel().asGuildMessageChannel();
	}

	/**
	 * Gets the provided Option Key as a MessageChannel value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static GuildMessageChannel optGuildMessageChannel(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return optGuildMessageChannel(event, key, null);
	}

	/**
	 * Gets the provided Option Key as a MessageChannel value, or returns the default one if the option cannot be found.
	 *
	 * @param event
	 *                     The slash command event to get options from
	 * @param key
	 *                     The option we want
	 * @param defaultValue
	 *                     Nullable default value used in the absence of the option value
	 *
	 * @return The provided option, or the default value if the option is not present
	 */
	@Nullable
	public static GuildMessageChannel optGuildMessageChannel(@NotNull SlashCommandInteractionEvent event, @NotNull String key, @Nullable GuildMessageChannel defaultValue)
	{
		OptionMapping option = event.getOption(key);

		return option == null ? defaultValue : option.getAsChannel().asGuildMessageChannel();
	}

	/**
	 * Will return if the provided key resolves into a provided Option for the SlashCommand.
	 *
	 * @param event
	 *              the slash command event to get options from
	 * @param key
	 *              the option we want
	 *
	 * @return true if the option exists, false otherwise
	 */
	public static boolean hasOption(@NotNull SlashCommandInteractionEvent event, @NotNull String key)
	{
		return event.getOption(key) != null;
	}
}
