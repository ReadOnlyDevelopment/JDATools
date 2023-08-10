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

package io.github.readonly.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.readonly.command.Command.Category;
import io.github.readonly.command.ctx.ContextMenu;
import io.github.readonly.command.event.CommandEvent;
import io.github.readonly.settings.GuildSettingsManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A simple builder used to create a {@link io.github.readonly.command.Client ClientImpl}. <p> Once built, add the
 * {@link io.github.readonly.api.ClientInterface Client} as an EventListener to {@link net.dv8tion.jda.api.JDA JDA} and it
 * will automatically handle commands with ease!
 */
public class ClientBuilder
{

	private Activity											activity					= Activity.playing("default");
	private OnlineStatus										status						= OnlineStatus.ONLINE;
	private String												ownerId;
	private String[]											coOwnerIds;
	private String												prefix;
	private String[]											prefixes;
	private Function<MessageReceivedEvent, String>				prefixFunction;
	private Function<MessageReceivedEvent, Boolean>				commandPreProcessFunction;
	private BiFunction<MessageReceivedEvent, Command, Boolean>	commandPreProcessBiFunction;
	private String												serverInvite;
	private String												success;
	private String												warning;
	private String												error;
	private final LinkedList<Command>							commands					= new LinkedList<>();
	private final LinkedList<ServerCommands>					serverCommands				= new LinkedList<>();
	private final LinkedList<SlashCommand>						globalSlashCommands			= new LinkedList<>();
	private final LinkedList<ContextMenu>						globalUserInteractions		= new LinkedList<>();
	final HashMap<Category, List<Command>>						categoryToCommandListMap	= new LinkedHashMap<Command.Category, List<Command>>();
	private boolean												embedAllReplies				= false;
	private CommandListener										listener;
	private boolean												useHelp						= true;
	private boolean												shutdownAutomatically		= true;
	private Consumer<CommandEvent>								helpConsumer;
	private String												helpWord;
	private ScheduledExecutorService							executor;
	private int													linkedCacheSize				= 0;
	private GuildSettingsManager<?>								manager						= null;

	/**
	 * Builds a {@link io.github.readonly.command.Client ClientImpl} with the provided settings. <br> Once built,
	 * only the {@link io.github.readonly.command.CommandListener CommandListener}, and
	 * {@link io.github.readonly.command.Command Command}s can be changed.
	 *
	 * @return The Client built.
	 */
	public Client build()
	{
		// @noformat
		Client client = new Client(
			ownerId, coOwnerIds, prefix, prefixes,
			prefixFunction, commandPreProcessFunction,
			commandPreProcessBiFunction, activity, status,
			serverInvite, success, warning, error,
			new ArrayList<>(commands), serverCommands,
			globalSlashCommands, globalUserInteractions, embedAllReplies,
			useHelp, shutdownAutomatically, helpConsumer,
			helpWord, executor, linkedCacheSize, manager
			);
		if (listener != null)
		{
			client.setListener(listener);
		}
		return client;
		// @format
	}

	/**
	 * Sets the owner for the bot. <br> Make sure to verify that the ID provided is ISnowflake compatible when setting
	 * this. If it is not, this will warn the developer.
	 *
	 * @param ownerId
	 *                The ID of the owner.
	 *
	 * @return This builder
	 */
	public ClientBuilder setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
		return this;
	}

	/**
	 * Sets the one or more CoOwners of the bot. <br> Make sure to verify that all of the IDs provided are ISnowflake
	 * compatible when setting this. If it is not, this will warn the developer which ones are not.
	 *
	 * @param coOwnerIds
	 *                   The ID(s) of the CoOwners
	 *
	 * @return This builder
	 */
	public ClientBuilder setCoOwnerIds(String... coOwnerIds)
	{
		this.coOwnerIds = coOwnerIds;
		return this;
	}

	/**
	 * Sets the bot's prefix. <br> If set null, empty, or not set at all, the bot will use a mention {@literal @Botname}
	 * as a prefix.
	 *
	 * @param prefix
	 *               The prefix for the bot to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefix(String prefix)
	{
		this.prefix = prefix;
		return this;
	}

	/**
	 * Sets an array of prefixes in case it's not enough. Be careful.
	 *
	 * @param prefixes
	 *                 The prefixes to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefixes(String[] prefixes)
	{
		this.prefixes = prefixes;
		return this;
	}

	/**
	 * Sets the Collection of prefixes in case it's not enough. Be careful.
	 *
	 * @param prefixCollection
	 *                         The Collection of prefixes to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefixes(Collection<String> prefixCollection)
	{
		this.prefixes = prefixCollection.toArray(new String[prefixCollection.size()]);
		return this;
	}

	/**
	 * Sets the Prefix Function. Used if you want custom prefixes per server. <br> Be careful, this function should be
	 * quick, as it's executed every time MessageReceivedEvent is called. <br> If function returns null, it will be
	 * ignored.
	 *
	 * @param prefixFunction
	 *                       The prefix function to execute to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefixFunction(Function<MessageReceivedEvent, String> prefixFunction)
	{
		this.prefixFunction = prefixFunction;
		return this;
	}

	/**
	 * Sets the pre-process function. This code is executed before every command.<br> Returning "true" will allow
	 * processing to proceed.<br> Returning "false" or "null" will prevent the Command from executing.<br> You can use
	 * Command to see which command will run.<br>
	 *
	 * @param commandPreProcessBiFunction
	 *                                    The function to execute
	 *
	 * @return This builder
	 */
	public ClientBuilder setCommandPreProcessBiFunction(BiFunction<MessageReceivedEvent, Command, Boolean> commandPreProcessBiFunction)
	{
		this.commandPreProcessBiFunction = commandPreProcessBiFunction;
		return this;
	}

	/**
	 * Sets whether the {@link io.github.readonly.api.ClientInterface Client} will use the builder to automatically create
	 * a help command or not.
	 *
	 * @param useHelp
	 *                {@code false} to disable the help command builder, otherwise the Client will use either the
	 *                default or one provided via
	 *                {@link io.github.readonly.command.ClientBuilder#setHelpConsumer(Consumer)}}.
	 *
	 * @return This builder
	 */
	public ClientBuilder useHelpBuilder(boolean useHelp)
	{
		this.useHelp = useHelp;
		return this;
	}

	/**
	 * Sets the consumer to run as the bot's help command. <br> Setting it to {@code null} or not setting this at all
	 * will cause the bot to use the default help builder.
	 *
	 * @param helpConsumer
	 *                     A consumer to accept a {@link io.github.readonly.command.event.CommandEvent CommandEvent} when a
	 *                     help command is called.
	 *
	 * @return This builder
	 */
	public ClientBuilder setHelpConsumer(Consumer<CommandEvent> helpConsumer)
	{
		this.helpConsumer = helpConsumer;
		return this;
	}

	/**
	 * Sets the word used to trigger the command list. <br> Setting this to {@code null} or not setting this at all will
	 * set the help word to {@code "help"}.
	 *
	 * @param helpWord
	 *                 The word to trigger the help command
	 *
	 * @return This builder
	 */
	public ClientBuilder setHelpWord(String helpWord)
	{
		this.helpWord = helpWord;
		return this;
	}

	/**
	 * Sets the bot's support server invite.
	 *
	 * @param serverInvite
	 *                     The support server invite
	 *
	 * @return This builder
	 */
	public ClientBuilder setServerInvite(String serverInvite)
	{
		this.serverInvite = serverInvite;
		return this;
	}

	/**
	 * Sets the emojis for success, warning, and failure.
	 *
	 * @param success
	 *                Emoji for success
	 * @param warning
	 *                Emoji for warning
	 * @param error
	 *                Emoji for failure
	 *
	 * @return This builder
	 */
	public ClientBuilder setEmojis(String success, String warning, String error)
	{
		this.success = success;
		this.warning = warning;
		this.error = error;
		return this;
	}

	/**
	 * Sets the {@link net.dv8tion.jda.api.entities.Activity Activity} to use when the bot is ready. <br> Can be set to
	 * {@code null} for JDA Utilities to not set it.
	 *
	 * @param activity
	 *                 The Game to use when the bot is ready
	 *
	 * @return This builder
	 */
	public ClientBuilder setActivity(Activity activity)
	{
		this.activity = activity;
		return this;
	}

	/**
	 * Sets the {@link net.dv8tion.jda.api.entities.Activity Activity} the bot will use as the default: 'Playing <b>Type
	 * [prefix]help</b>'
	 *
	 * @return This builder
	 */
	public ClientBuilder useDefaultActivity()
	{
		this.activity = Activity.playing("default");
		return this;
	}

	/**
	 * Sets the {@link net.dv8tion.jda.api.OnlineStatus OnlineStatus} the bot will use once Ready This defaults to
	 * ONLINE
	 *
	 * @param status
	 *               The status to set
	 *
	 * @return This builder
	 */
	public ClientBuilder setStatus(OnlineStatus status)
	{
		this.status = status;
		return this;
	}

	/**
	 * Adds a {@link io.github.readonly.command.Command Command} and registers it to the
	 * {@link io.github.readonly.command.Client ClientImpl} for this session.
	 *
	 * @param command
	 *                The command to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addCommand(Command command)
	{
		if (categoryToCommandListMap.containsKey(command.getCategory()))
		{
			categoryToCommandListMap.get(command.getCategory()).add(command);
		} else
		{
			List<Command> newCmdList = new ArrayList<>(Arrays.asList(command));
			categoryToCommandListMap.put(command.getCategory(), newCmdList);
		}
		commands.add(command);
		return this;
	}

	/**
	 * Adds and registers all {@link io.github.readonly.command.Command Command}s in the collection to the
	 * {@link io.github.readonly.command.Client ClientImpl} for this session. <br> This is the same as running a
	 * forEach loop on the Collection {@link io.github.readonly.command.ClientBuilder#addCommand(Command)} multiple
	 * times.
	 *
	 * @param commands
	 *                 The Commands Collection to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addCommands(Collection<Command> commands)
	{
		commands.forEach(command -> this.addCommand(command));
		return this;
	}

	/**
	 * Adds and registers multiple {@link io.github.readonly.command.Command Command}s to the
	 * {@link io.github.readonly.command.Client ClientImpl} for this session. <br> This is the same as calling
	 * {@link io.github.readonly.command.ClientBuilder#addCommand(Command)} multiple times.
	 *
	 * @param commands
	 *                 The Commands to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addCommands(Command... commands)
	{
		for (Command command : commands)
		{
			this.addCommand(command);
		}
		return this;
	}

	/**
	 * Adds a {@link io.github.readonly.command.SlashCommand SlashCommand} and registers it to the
	 * {@link io.github.readonly.command.Client ClientImpl} for this session.
	 *
	 * @param slashCommand
	 *                     The SlashCommand to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addGlobalSlashCommand(SlashCommand slashCommand)
	{
		if (!this.globalSlashCommands.contains(slashCommand))
		{
			this.globalSlashCommands.add(slashCommand);
		}
		return this;
	}

	/**
	 * Adds and registers all {@link io.github.readonly.command.SlashCommand SlashCommand}s in the array to the
	 * {@link io.github.readonly.command.Client ClientImpl} for this session. <br> This is the same as running a
	 * forEach loop on the Collection {@link io.github.readonly.command.ClientBuilder#addSlashCommand(SlashCommand)}
	 * multiple times.
	 *
	 * @param slashCommands
	 *                      The SlashCommands Array to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addGlobalSlashCommands(SlashCommand... slashCommands)
	{
		for (SlashCommand command : slashCommands)
		{
			if (!this.globalSlashCommands.contains(command))
			{
				this.globalSlashCommands.add(command);
			}
		}
		return this;
	}

	/**
	 * Adds and registers all {@link io.github.readonly.command.SlashCommand SlashCommand}s in the collection to the
	 * {@link io.github.readonly.command.Client ClientImpl} for this session. <br> This is the same as running a
	 * forEach loop on the Collection {@link io.github.readonly.command.ClientBuilder#addSlashCommand(SlashCommand)}
	 * multiple times.
	 *
	 * @param slashCommands
	 *                      The SlashCommands Collection to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addGlobalSlashCommands(Collection<SlashCommand> slashCommands)
	{
		for (SlashCommand command : slashCommands)
		{
			if (!this.globalSlashCommands.contains(command))
			{
				this.globalSlashCommands.add(command);
			}
		}
		return this;
	}

	/**
	 * Adds a {@link io.github.readonly.command.SlashCommand SlashCommand} and registers it to the
	 * {@link io.github.readonly.command.Client Client} for this session.
	 *
	 * @param contextMenu
	 *                    The Context Menu to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addContextMenu(ContextMenu contextMenu)
	{
		globalUserInteractions.add(contextMenu);
		return this;
	}

	/**
	 * Adds and registers multiple {@link io.github.readonly.command.SlashCommand SlashCommand}s to the
	 * {@link io.github.readonly.command.Client Client} for this session. <br> This is the same as calling
	 * {@link io.github.readonly.command.ClientBuilder#addSlashCommand(SlashCommand)} multiple times.
	 *
	 * @param contextMenus
	 *                     The Context Menus to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addContextMenus(ContextMenu... contextMenus)
	{
		for (ContextMenu contextMenu : contextMenus)
		{
			this.addContextMenu(contextMenu);
		}
		return this;
	}

	public ClientBuilder addAllServerCommands(ServerCommands... serverCommands)
	{
		for (ServerCommands svrCmds : serverCommands)
		{
			this.serverCommands.add(svrCmds);
		}
		return this;
	}

	public ClientBuilder addServerCommands(ServerCommands serverCommands)
	{
		this.serverCommands.add(serverCommands);
		return this;
	}

	/**
	 * Sets every reply from the bot as an embeded message.
	 *
	 * @return This Builder
	 */
	public ClientBuilder setAllRepliesAsEmbed()
	{
		this.embedAllReplies = true;
		return this;
	}

	/**
	 * Sets the {@link io.github.readonly.command.CommandListener CommandListener} for the
	 * {@link io.github.readonly.command.Client ClientImpl}.
	 *
	 * @param listener
	 *                 The CommandListener for the ClientImpl
	 *
	 * @return This builder
	 */
	public ClientBuilder setListener(CommandListener listener)
	{
		this.listener = listener;
		return this;
	}

	/**
	 * Sets the {@link java.util.concurrent.ScheduledExecutorService ScheduledExecutorService} for the
	 * {@link io.github.readonly.command.Client ClientImpl}.
	 *
	 * @param executor
	 *                 The ScheduledExecutorService for the ClientImpl
	 *
	 * @return This builder
	 */
	public ClientBuilder setScheduleExecutor(ScheduledExecutorService executor)
	{
		this.executor = executor;
		return this;
	}

	/**
	 * Sets the Command Client to shut down internals automatically when a
	 * {@link net.dv8tion.jda.api.events.session.ShutdownEvent ShutdownEvent} is received.
	 *
	 * @param shutdownAutomatically
	 *                              {@code false} to disable calling the shutdown method when a ShutdownEvent is
	 *                              received
	 *
	 * @return This builder
	 */
	public ClientBuilder setShutdownAutomatically(boolean shutdownAutomatically)
	{
		this.shutdownAutomatically = shutdownAutomatically;
		return this;
	}

	/**
	 * Sets the internal size of the client's {@link io.github.readonly.common.util.FixedSizeCache FixedSizeCache} used
	 * for caching and pairing the bot's response {@link net.dv8tion.jda.api.entities.Message Message}s with the calling
	 * Message's ID. <p> Higher cache size means that decay of cache contents will most likely occur later, allowing the
	 * deletion of responses when the call is deleted to last for a longer duration. However this also means larger
	 * memory usage. <p> Setting {@code 0} or negative will cause the client to not use linked caching <b>at all</b>.
	 *
	 * @param linkedCacheSize
	 *                        The maximum number of paired responses that can be cached, or {@code <1} if the built
	 *                        {@link io.github.readonly.api.ClientInterface Client} will not use linked caching.
	 *
	 * @return This builder
	 */
	public ClientBuilder setLinkedCacheSize(int linkedCacheSize)
	{
		this.linkedCacheSize = linkedCacheSize;
		return this;
	}

	/**
	 * Sets the {@link io.github.readonly.settings.GuildSettingsManager GuildSettingsManager} for the ClientImpl built
	 * using this builder.
	 *
	 * @param manager
	 *                The GuildSettingsManager to set.
	 *
	 * @return This builder
	 */
	public ClientBuilder setGuildSettingsManager(GuildSettingsManager<?> manager)
	{
		this.manager = manager;
		return this;
	}
}
