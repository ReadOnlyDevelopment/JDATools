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

package io.github.readonly.api;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

import io.github.readonly.command.Command;
import io.github.readonly.command.CommandListener;
import io.github.readonly.command.ServerCommands;
import io.github.readonly.command.SlashCommand;
import io.github.readonly.command.Command.Category;
import io.github.readonly.command.ctx.ContextMenu;
import io.github.readonly.settings.GuildSettingsManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A Bot Client interface implemented on objects used to hold bot data.
 * <p>
 * This is implemented in {@link io.github.readonly.command.Client ClientImpl} alongside implementation of
 * {@link net.dv8tion.jda.api.hooks.EventListener EventListener} to create a compounded "Client Listener" which catches
 * specific kinds of events thrown by JDA and processes them automatically to handle and execute
 * {@link io.github.readonly.command.Command Command}s.
 * <p>
 * Implementations also serve as a useful platforms, carrying reference info such as the bot's {@linkplain #getOwnerId()
 * Owner ID}, {@linkplain #getPrefix() prefix}, and a {@linkplain #getServerInvite() support server invite}.
 * <p>
 * For the ClientImpl, once initialized, only the following can be modified:
 * <ul>
 * <li>{@link io.github.readonly.command.Command Command}s may be added or removed.</li>
 * <li>The {@link io.github.readonly.command.CommandListener CommandListener} may be set.</li>
 * </ul>
 *
 * @implNote While typically safe, there are a few ways to misuse the standard implementation of this interface: the
 *           ClientImpl. <br>
 *           Because of this the following should <b>ALWAYS</b> be followed to avoid such errors:
 *           <p>
 *           <b>1)</b> Do not build and add more than one Client to an instance JDA, <b>EVER</b>.
 *           <p>
 *           <b>2)</b> Always create and add the ClientImpl to JDA <b>BEFORE</b> you build it, or there is a chance some
 *           minor errors will occur, <b>especially</b> if JDA has already fired a
 *           {@link net.dv8tion.jda.api.events.session.ReadyEvent ReadyEvent}.
 *           <p>
 *           <b>3)</b> Do not provide anything other than a String representing a long (and furthermore a User ID) as an
 *           Owner ID or a CoOwner ID. This will generate errors, but not stop the creation of the ClientImpl which will
 *           cause several errors to occur very quickly after startup (except if you provide {@code null} for the Owner
 *           ID, that'll just flat out throw an {@link java.lang.IllegalArgumentException IllegalArgumentException}).
 *           <p>
 *           <b>4)</b> Do not provide strings when using
 *           {@link io.github.readonly.command.ClientBuilder#setEmojis(String, String, String)
 *           ClientBuilder#setEmojis(String, String, String)} that are not unicode emojis or that do not match the
 *           custom emote format specified in {@link net.dv8tion.jda.api.entities.emoji.Emoji#getFormatted()
 *           Emote#getAsMention()} (IE: {@code <:EmoteName:EmoteID>}).
 *           <p>
 *           <b>5)</b> Avoid using
 *           {@link io.github.readonly.command.Client#linkIds(long, net.dv8tion.jda.api.entities.Message)}. This
 *           will create errors and has no real purpose outside of it's current usage.
 */
public interface ClientInterface
{

	/**
	 * Gets the Client's prefix.
	 *
	 * @return A possibly-null prefix
	 */
	String getPrefix();

	/**
	 * Gets the array of prefixes
	 *
	 * @return A possibly-null list of prefixes
	 */
	String[] getPrefixes();

	/**
	 * Gets the prefix BiConsumer
	 *
	 * @return A possibly-null prefix BiConsumer
	 */
	Function<MessageReceivedEvent, String> getPrefixFunction();

	/**
	 * Returns the visual representation of the bot's prefix.
	 * <p>
	 * This is the same as {@link io.github.readonly.api.ClientInterface#getPrefix() } unless the prefix is the default, in
	 * which case it appears as {@literal @Botname}.
	 *
	 * @return A never-null prefix
	 */
	String getTextualPrefix();

	/**
	 * Adds a single {@link io.github.readonly.command.Command Command} to this Client's registered Commands.
	 * <p>
	 * For Client's containing 20 commands or less, command calls by users will have the bot iterate through the entire
	 * {@link java.util.ArrayList ArrayList} to find the command called. As expected, this can get fairly hefty if a bot
	 * has a lot of Commands registered to it.
	 * <p>
	 * To prevent delay a Client that has more that 20 Commands registered to it will begin to use <b>indexed calls</b>.
	 * <br>
	 * Indexed calls use a {@link java.util.HashMap HashMap} which links their
	 * {@link io.github.readonly.command.Command#name name} and their {@link io.github.readonly.command.Command#aliases
	 * aliases} to the index that which they are located at in the ArrayList they are stored.
	 * <p>
	 * This means that all insertion and removal of Commands must reorganize the index maintained by the HashMap. <br>
	 * For this particular insertion, the Command provided is inserted at the end of the index, meaning it will become
	 * the "rightmost" Command in the ArrayList.
	 *
	 * @param command
	 *            The Command to add
	 *
	 * @throws java.lang.IllegalArgumentException
	 *             If the Command provided has a name or alias that has already been registered
	 */
	void addCommand(Command command);

	/**
	 * Adds a single {@link io.github.readonly.command.Command Command} to this Client's registered Commands at the
	 * specified index.
	 * <p>
	 * For Client's containing 20 commands or less, command calls by users will have the bot iterate through the entire
	 * {@link java.util.ArrayList ArrayList} to find the command called. As expected, this can get fairly hefty if a bot
	 * has a lot of Commands registered to it.
	 * <p>
	 * To prevent delay a Client that has more that 20 Commands registered to it will begin to use <b>indexed calls</b>.
	 * <br>
	 * Indexed calls use a {@link java.util.HashMap HashMap} which links their
	 * {@link io.github.readonly.command.Command#name name} and their {@link io.github.readonly.command.Command#aliases
	 * aliases} to the index that which they are located at in the ArrayList they are stored.
	 * <p>
	 * This means that all insertion and removal of Commands must reorganize the index maintained by the HashMap. <br>
	 * For this particular insertion, the Command provided is inserted at the index specified, meaning it will become
	 * the Command located at that index in the ArrayList. This will shift the Command previously located at that index
	 * as well as any located at greater indices, right one index ({@code size()+1}).
	 *
	 * @param command
	 *            The Command to add
	 * @param index
	 *            The index to add the Command at (must follow the specifications {@code 0<=index<=size()})
	 *
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             If {@code index < 0} or {@code index > size()}
	 * @throws java.lang.IllegalArgumentException
	 *             If the Command provided has a name or alias that has already been registered to an index
	 */
	void addCommand(Command command, int index);

	default void addCommands(Command... commands)
	{
		for (Command cmd : commands)
		{
			addCommand(cmd);
		}
	}

	/**
	 * Adds a single {@link io.github.readonly.command.SlashCommand SlashCommand} to this Client's registered
	 * SlashCommand.
	 * <p>
	 * For Client's containing 20 commands or less, command calls by users will have the bot iterate through the entire
	 * {@link java.util.ArrayList ArrayList} to find the command called. As expected, this can get fairly hefty if a bot
	 * has a lot of Commands registered to it.
	 * <p>
	 * To prevent delay a Client that has more that 20 Commands registered to it will begin to use <b>indexed calls</b>.
	 * <br>
	 * Indexed calls use a {@link java.util.HashMap HashMap} which links their
	 * {@link io.github.readonly.command.SlashCommand#name name} to the index that which they are located at in the
	 * ArrayList they are stored.
	 * <p>
	 * This means that all insertion and removal of SlashCommands must reorganize the index maintained by the HashMap.
	 * <br>
	 * For this particular insertion, the SlashCommand provided is inserted at the end of the index, meaning it will
	 * become the "rightmost" Command in the ArrayList.
	 *
	 * @param command
	 *            The Command to add
	 *
	 * @throws java.lang.IllegalArgumentException
	 *             If the SlashCommand provided has a name or alias that has already been registered
	 */
	void indexSlashCommand(SlashCommand command);

	/**
	 * Adds a single {@link io.github.readonly.command.SlashCommand SlashCommand} to this Client's registered
	 * Commands at the specified index.
	 * <p>
	 * For Client's containing 20 commands or less, command calls by users will have the bot iterate through the entire
	 * {@link java.util.ArrayList ArrayList} to find the command called. As expected, this can get fairly hefty if a bot
	 * has a lot of Commands registered to it.
	 * <p>
	 * To prevent delay a Client that has more that 20 Commands registered to it will begin to use <b>indexed calls</b>.
	 * <br>
	 * Indexed calls use a {@link java.util.HashMap HashMap} which links their
	 * {@link io.github.readonly.command.Command#name name} to the index that which they are located at in the ArrayList
	 * they are stored.
	 * <p>
	 * This means that all insertion and removal of Commands must reorganize the index maintained by the HashMap. <br>
	 * For this particular insertion, the Command provided is inserted at the index specified, meaning it will become
	 * the Command located at that index in the ArrayList. This will shift the Command previously located at that index
	 * as well as any located at greater indices, right one index ({@code size()+1}).
	 *
	 * @param command
	 *            The Command to add
	 * @param index
	 *            The index to add the Command at (must follow the specifications {@code 0<=index<=size()})
	 *
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             If {@code index < 0} or {@code index > size()}
	 * @throws java.lang.IllegalArgumentException
	 *             If the Command provided has a name or alias that has already been registered to an index
	 */
	void indexSlashCommand(SlashCommand command, int index);

	/**
	 * Adds a single {@link ContextMenu} to this CommandClient's registered Context Menus.
	 *
	 * @param menu
	 *            The menu to add
	 *
	 * @throws java.lang.IllegalArgumentException
	 *             If the Context Menu provided has a name that has already been registered
	 */
	void indexContextMenu(ContextMenu menu);

	/**
	 * Adds a single {@link ContextMenu} to this CommandClient's registered Context Menus.
	 *
	 * @param menu
	 *            The menu to add
	 * @param index
	 *            The index to add the Context Menu at (must follow the specifications {@code 0<=index<=size()})
	 *
	 * @throws java.lang.IllegalArgumentException
	 *             If the Context Menu provided has a name that has already been registered
	 */
	void indexContextMenu(ContextMenu menu, int index);

	/**
	 * Removes a single {@link io.github.readonly.command.Command Command} from this Client's registered Commands at the
	 * index linked to the provided name/alias.
	 * <p>
	 * For Client's containing 20 commands or less, command calls by users will have the bot iterate through the entire
	 * {@link java.util.ArrayList ArrayList} to find the command called. As expected, this can get fairly hefty if a bot
	 * has a lot of Commands registered to it.
	 * <p>
	 * To prevent delay a Client that has more that 20 Commands registered to it will begin to use <b>indexed calls</b>.
	 * <br>
	 * Indexed calls use a {@link java.util.HashMap HashMap} which links their
	 * {@link io.github.readonly.command.Command#name name} and their {@link io.github.readonly.command.Command#aliases
	 * aliases} to the index that which they are located at in the ArrayList they are stored.
	 * <p>
	 * This means that all insertion and removal of Commands must reorganize the index maintained by the HashMap. <br>
	 * For this particular removal, the Command removed is that of the corresponding index retrieved by the name
	 * provided. This will shift any Commands located at greater indices, left one index ({@code size()-1}).
	 *
	 * @param name
	 *            The name or an alias of the Command to remove
	 *
	 * @throws java.lang.IllegalArgumentException
	 *             If the name provided was not registered to an index
	 */
	void removeCommand(String name);

	/**
	 * Sets the {@link io.github.readonly.command.CommandListener CommandListener} to catch command-related events thrown
	 * by this {@link io.github.readonly.api.ClientInterface Client}.
	 *
	 * @param listener
	 *            The CommandListener
	 */
	void setListener(CommandListener listener);

	/**
	 * Returns the current {@link io.github.readonly.command.CommandListener CommandListener}.
	 *
	 * @return A possibly-null CommandListener
	 */
	CommandListener getListener();

	/**
	 * Returns the list of registered {@link io.github.readonly.command.Command Command}s during this session.
	 *
	 * @return A never-null List of Commands registered during this session
	 */
	List<Command> getCommands();

	List<ServerCommands> getServerCommands();

	/**
	 * Returns the list of registered {@link io.github.readonly.command.SlashCommand SlashCommand}s during this
	 * session.
	 *
	 * @return A never-null List of Slash Commands registered during this session
	 */
	List<SlashCommand> getGlobalSlashCommands();

	/**
	 * Returns the list of registered {@link ContextMenu}s during this session.
	 *
	 * @return A never-null List of Context Menus registered during this session
	 */
	List<ContextMenu> getContextMenus();

	/**
	 * Returns the list of registered command {@link Category}'s during this session.
	 *
	 * @return A never-null List of command Category's registered during this session
	 */
	HashMap<Category, List<Command>> getCommandCategoryToCommandListMap();

	/**
	 * Gets the time this {@link io.github.readonly.api.ClientInterface Client} implementation was created.
	 *
	 * @return The start time of this Client implementation
	 */
	OffsetDateTime getStartTime();

	/**
	 * Gets the {@link java.time.OffsetDateTime OffsetDateTime} that the specified cooldown expires.
	 *
	 * @param name
	 *            The cooldown name
	 *
	 * @return The expiration time, or null if the cooldown does not exist
	 */
	OffsetDateTime getCooldown(String name);

	/**
	 * Gets the remaining number of seconds on the specified cooldown.
	 *
	 * @param name
	 *            The cooldown name
	 *
	 * @return The number of seconds remaining
	 */
	int getRemainingCooldown(String name);

	/**
	 * Whether or not to embed all replies from the bot
	 * <p>
	 *
	 * @return {@code true} if the setAllRepliesAsEmbed() marker was called in the ClientBuilder , {@code false}
	 *         otherwise.
	 */
	boolean makeAllRepliesEmbeded();

	/**
	 * Applies the specified cooldown with the provided name.
	 *
	 * @param name
	 *            The cooldown name
	 * @param seconds
	 *            The time to make the cooldown last
	 */
	void applyCooldown(String name, int seconds);

	/**
	 * Cleans up expired cooldowns to reduce memory.
	 */
	void cleanCooldowns();

	/**
	 * Gets the number of uses for the provide {@link io.github.readonly.command.Command Command} during this session, or
	 * {@code 0} if the command is not registered to this Client.
	 *
	 * @param command
	 *            The Command
	 *
	 * @return The number of uses for the Command
	 */
	int getCommandUses(Command command);

	/**
	 * Gets the number of uses for a {@link io.github.readonly.command.Command Command} during this session matching the
	 * provided String name, or {@code 0} if there is no Command with the name.
	 * <p>
	 * <b>NOTE:</b> this method <b>WILL NOT</b> get uses for a command if an
	 * {@link io.github.readonly.command.Command#aliases alias} is provided! Also note that
	 * {@link io.github.readonly.command.Command#children child commands} <b>ARE NOT</b> tracked and providing names or
	 * effective names of child commands will return {@code 0}.
	 *
	 * @param name
	 *            The name of the Command
	 *
	 * @return The number of uses for the Command, or {@code 0} if the name does not match with a Command
	 */
	int getCommandUses(String name);

	/**
	 * Gets the ID of the owner of this bot as a String.
	 *
	 * @return The String ID of the owner of the bot
	 */
	String getOwnerId();

	/**
	 * Gets the ID of the owner of this bot as a {@code long}.
	 *
	 * @return The {@code long} ID of the owner of the bot
	 */
	long getOwnerIdLong();

	/**
	 * Gets the ID(s) of any CoOwners of this bot as a String Array.
	 *
	 * @return The String ID(s) of any CoOwners of this bot
	 */
	String[] getCoOwnerIds();

	/**
	 * Gets the ID(s) of any CoOwners of this bot as a {@code long} Array.
	 *
	 * @return The {@code long} ID(s) of any CoOwners of this bot
	 */
	long[] getCoOwnerIdsLong();

	/**
	 * Gets the success emoji.
	 *
	 * @return The success emoji
	 */
	String getSuccess();

	/**
	 * Gets the warning emoji.
	 *
	 * @return The warning emoji
	 */
	String getWarning();

	/**
	 * Gets the error emoji.
	 *
	 * @return The error emoji
	 */
	String getError();

	/**
	 * Gets the {@link java.util.concurrent.ScheduledExecutorService ScheduledExecutorService} held by this client.
	 * <p>
	 * This is used for methods such as {@link io.github.readonly.command.event.CommandEvent#async(Runnable)
	 * CommandEvent#async(Runnable)} run code asynchronously.
	 *
	 * @return The ScheduledExecutorService held by this client.
	 */
	ScheduledExecutorService getScheduleExecutor();

	/**
	 * Gets the invite to the bot's support server.
	 *
	 * @return A possibly-null server invite
	 */
	String getServerInvite();

	/**
	 * Gets an a recently updated count of all the {@link net.dv8tion.jda.api.entities.Guild Guild}s the bot is
	 * connected to on all shards.
	 * <p>
	 * <b>NOTE:</b> This may not always or should not be assumed accurate! Any time a shard joins or leaves a guild it
	 * will update the number retrieved by this method but will not update when other shards join or leave guilds. This
	 * means that shards will not always retrieve the same value. For instance:
	 * <ul>
	 * <li>1) Shard A joins 10 Guilds</li>
	 * <li>2) Shard B invokes this method</li>
	 * <li>3) Shard A invokes this method</li>
	 * </ul>
	 * The number retrieved by Shard B will be that of the number retrieved by Shard A, minus 10 guilds because Shard B
	 * hasn't updated and accounted for those 10 guilds on Shard A.
	 * <p>
	 * <b>This feature requires a Discord Bots API Key to be set!</b> <br>
	 * To set your Discord Bots API Key, you'll have to retrieve it from the <a href="http://bots.discord.pw/">Discord
	 * Bots</a> website.
	 *
	 * @return A recently updated count of all the Guilds the bot is connected to on all shards.
	 */
	int getTotalGuilds();

	/**
	 * Gets the word used to invoke a help DM.
	 *
	 * @return The help word
	 */
	String getHelpWord();

	/**
	 * Gets whether this Client uses linked deletion.
	 * <p>
	 * Linking calls is the basic principle of pairing bot responses with their calling
	 * {@link net.dv8tion.jda.api.entities.Message Message}s. <br>
	 * Using this with a basic function such as deletion, this causes bots to delete their Messages as a response to the
	 * calling Message being deleted.
	 *
	 * @return {@code true} if the bot uses linked deletion, {@code false} otherwise.
	 *
	 * @see io.github.readonly.command.ClientBuilder#setLinkedCacheSize(int) ClientBuilder#setLinkedCacheSize(int)
	 */
	boolean usesLinkedDeletion();

	/**
	 * Returns an Object of the type parameter that should contain settings relating to the specified
	 * {@link net.dv8tion.jda.api.entities.Guild Guild}.
	 * <p>
	 * The returning object for this is specified via provision of a
	 * {@link io.github.readonly.settings.GuildSettingsManager GuildSettingsManager} to
	 * {@link io.github.readonly.command.ClientBuilder#setGuildSettingsManager(io.github.readonly.settings.GuildSettingsManager)
	 * ClientBuilder#setGuildSettingsManager(GuildSettingsManager)}, more specifically
	 * {@link GuildSettingsManager#getSettings(net.dv8tion.jda.api.entities.Guild)
	 * GuildSettingsManager#getSettings(Guild)}.
	 *
	 * @param <S>
	 *            The type of settings the GuildSettingsManager provides
	 * @param guild
	 *            The Guild to get Settings for
	 *
	 * @return The settings object for the Guild, specified in
	 *         {@link io.github.readonly.settings.GuildSettingsManager#getSettings(Guild)
	 *         GuildSettingsManager#getSettings(Guild)}, can be {@code null} if the implementation allows it.
	 */
	<S> S getSettingsFor(Guild guild);

	/**
	 * Returns the type of {@link io.github.readonly.settings.GuildSettingsManager GuildSettingsManager}, the same type of
	 * one provided when building this Client, or {@code null} if one was not provided there.
	 * <p>
	 * This is good if you want to use non-abstract methods specific to your implementation.
	 *
	 * @param <M>
	 *            The type of the GuildSettingsManager
	 *
	 * @return The GuildSettingsManager, or {@code null} if one was not provided when building this Client.
	 */
	<M extends GuildSettingsManager<?>> M getSettingsManager();

	/**
	 * Shuts down internals of the Command Client, such as the threadpool and guild settings manager
	 */
	void shutdown();

	/**
	 * Upserts all interactions globally. <b>This may take up to an hour.</b><br>
	 * This runs after the {@link net.dv8tion.jda.api.events.session.ReadyEvent ReadyEvent} has been fired
	 *
	 * @param jda
	 *            The JDA instance to use
	 */
	default void upsertInteractions(JDA jda)
	{
	};

	/**
	 * Upserts all interactions to the provided server.<br>
	 * This runs after the {@net.dv8tion.jda.api.events.session.ReadyEvent ReadyEvent} has been fired <br>
	 *
	 * @param jda
	 *            The JDA instance to use
	 * @param serverIdList
	 *            The list of server ID's to upsert interactions for
	 */
	default void upsertInteractions(JDA jda, List<String> serverIdList)
	{
	};
}
