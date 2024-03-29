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
 * Items in this package pertain to the {@link io.github.readonly.api.ClientInterface Client} and
 * {@link io.github.readonly.command.Command Commands}.
 * <p>
 * All of the contents are used heavily in the {@link io.github.readonly.command.Client ClientImpl}, and are
 * summarized as follows:
 * <ul>
 * <li>{@link io.github.readonly.command.CommandBuilder CommandBuilder} <br>
 * An chain builder for Commands.</li>
 * <li>{@link io.github.readonly.command.Command Command} <br>
 * An abstract class that can be inherited by classes to create Commands compatible with the {@code ClientImpl}.</li>
 * <li>{@link io.github.readonly.api.ClientInterface Client} <br>
 * An interface used for getting info set when building a {@code ClientImpl}.</li>
 * <li>{@link io.github.readonly.command.ClientBuilder ClientBuilder} <br>
 * A builder system used to create a {@code ClientImpl} across several optional chained methods.</li>
 * <li>{@link io.github.readonly.command.event.CommandEvent CommandEvent} <br>
 * A wrapper for a {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent MessageReceivedEvent}, {@code Client}, and String
 * arguments. The main basis for carrying information to be used in Commands.</li>
 * <li>{@link io.github.readonly.command.CommandListener CommandListener} <br>
 * An interface to be provided to a {@code ClientImpl} that can provide Command operations depending on the outcome of
 * the call.</li>
 * <li>{@link io.github.readonly.settings.GuildSettingsManager GuildSettingsManager} <br>
 * An abstract object used to store and handle {@code GuildSettingsProvider} implementations.</li>
 * <li>{@link io.github.readonly.settings.GuildSettingsProvider GuildSettingsProvider} <br>
 * An implementable interface used to supply default methods for handling guild specific settings via a
 * {@code GuildSettingsManager}.</li>
 * </ul>
 */

package io.github.readonly.command;
