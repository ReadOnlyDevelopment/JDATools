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

package com.readonlydev.settings;

import javax.annotation.Nullable;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

/**
 * An implementable frame for classes that handle Guild-Specific settings.
 * <p>
 * Standard implementations should be able to simply provide a type of {@link java.lang.Object Object} provided a
 * non-null {@link net.dv8tion.jda.api.entities.Guild Guild}. Further customization of the implementation is allowed on
 * the developer end.
 *
 * @param <T>
 *            The specific type of the settings object.
 *
 * @implNote Unless in the event of a major breaking change to JDA, there is no chance of implementations of this
 *           interface being required to implement additional methods. <br>
 *           If in the future it is decided to add a method to this interface, the method will have a default
 *           implementation that doesn't require developer additions.
 */
public interface GuildSettingsManager<T>
{

	/**
	 * Gets settings for a specified {@link net.dv8tion.jda.api.entities.Guild Guild} as an object of the specified type
	 * {@code T}, or {@code null} if the guild has no settings.
	 *
	 * @param guild
	 *            The guild to get settings for.
	 *
	 * @return The settings object for the guild, or {@code null} if the guild has no settings.
	 */
	@Nullable
	T getSettings(Guild guild);

	/**
	 * Called when JDA has fired a {@link net.dv8tion.jda.api.events.session.ReadyEvent ReadyEvent}.
	 * <p>
	 * Developers should implement this method to create or initialize resources when starting their bot.
	 */
	default void init(JDA jda)
	{
	};

	/**
	 * Called when JDA has fired a {@link net.dv8tion.jda.api.events.session.ReadyEvent ReadyEvent}.
	 * <p>
	 * Developers should implement this method to create or initialize resources when starting their bot.
	 */
	default void init()
	{
	}

	/**
	 * Called when JDA has fired a {@link net.dv8tion.jda.api.events.session.ShutdownEvent ShutdownEvent}.
	 * <p>
	 * Developers should implement this method to free up or close resources when shutting their bot.
	 */
	default void shutdown()
	{
	}
}
