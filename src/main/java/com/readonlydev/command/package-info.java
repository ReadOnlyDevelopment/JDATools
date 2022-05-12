/*
 * Copyright 2016-2018 John Grosh (jagrosh) & Kaidan Gustave (TheMonitorLizard)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Items in this package pertain to the
 * {@link com.readonlydev.api.ClientInterface Client} and
 * {@link com.readonlydev.command.Command Commands}. <p> All of the contents are
 * used heavily in the {@link com.readonlydev.command.client.Client ClientImpl},
 * and are summarized as follows: <ul>
 * <li>{@link com.readonlydev.command.CommandBuilder CommandBuilder} <br> An
 * chain builder for Commands.</li> <li>{@link com.readonlydev.command.Command
 * Command} <br> An abstract class that can be inherited by classes to create
 * Commands compatible with the {@code ClientImpl}.</li>
 * <li>{@link com.readonlydev.api.ClientInterface Client} <br> An interface used
 * for getting info set when building a {@code ClientImpl}.</li>
 * <li>{@link com.readonlydev.command.client.ClientBuilder ClientBuilder} <br> A
 * builder system used to create a {@code ClientImpl} across several optional
 * chained methods.</li> <li>{@link com.readonlydev.command.event.CommandEvent
 * CommandEvent} <br> A wrapper for a
 * {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent
 * MessageReceivedEvent}, {@code Client}, and String arguments. The main basis
 * for carrying information to be used in Commands.</li>
 * <li>{@link com.readonlydev.command.CommandListener CommandListener} <br> An
 * interface to be provided to a {@code ClientImpl} that can provide Command
 * operations depending on the outcome of the call.</li>
 * <li>{@link com.readonlydev.settings.GuildSettingsManager
 * GuildSettingsManager} <br> An abstract object used to store and handle
 * {@code GuildSettingsProvider} implementations.</li>
 * <li>{@link com.readonlydev.settings.GuildSettingsProvider
 * GuildSettingsProvider} <br> An implementable interface used to supply default
 * methods for handling guild specific settings via a
 * {@code GuildSettingsManager}.</li> </ul>
 */

package com.readonlydev.command;
