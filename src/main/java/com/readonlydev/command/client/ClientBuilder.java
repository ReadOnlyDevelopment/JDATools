package com.readonlydev.command.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.readonlydev.api.ClientInterface;
import com.readonlydev.command.Command;
import com.readonlydev.command.CommandListener;
import com.readonlydev.command.event.CommandEvent;
import com.readonlydev.command.slash.SlashCommand;
import com.readonlydev.settings.GuildSettingsManager;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A simple builder used to create a
 * {@link com.readonlydev.command.client.Client ClientImpl}.
 * <p>
 * Once built, add the {@link com.readonlydev.api.ClientInterface
 * Client} as an EventListener to {@link net.dv8tion.jda.api.JDA JDA} and
 * it will automatically handle commands with ease!
 */
public class ClientBuilder {
	private Activity activity = Activity.playing("default");
	private OnlineStatus status = OnlineStatus.ONLINE;
	private String ownerId;
	private String[] coOwnerIds;
	private String prefix;
	private String[] prefixes;
	private Function<MessageReceivedEvent, String> prefixFunction;
	private Function<MessageReceivedEvent, Boolean> commandPreProcessFunction;
	private BiFunction<MessageReceivedEvent, Command, Boolean> commandPreProcessBiFunction;
	private String serverInvite;
	private String success;
	private String warning;
	private String error;
	private final LinkedList<Command> commands = new LinkedList<>();
	private final LinkedList<SlashCommand> slashCommands = new LinkedList<>();
	private String forcedGuildId = null;
	private boolean manualUpsert = false;
	private boolean embedAllReplies = false;
	private CommandListener listener;
	private boolean useHelp = true;
	private boolean shutdownAutomatically = true;
	private Consumer<CommandEvent> helpConsumer;
	private String helpWord;
	private ScheduledExecutorService executor;
	private int linkedCacheSize = 0;
	private GuildSettingsManager<?> manager = null;

	/**
	 * Builds a {@link com.readonlydev.command.client.Client
	 * ClientImpl} with the provided settings. <br>
	 * Once built, only the {@link com.readonlydev.command.CommandListener
	 * CommandListener}, and {@link com.readonlydev.command.Command Command}s
	 * can be changed.
	 *
	 * @return The Client built.
	 */
	public ClientInterface build() {
		ClientInterface client = new Client(ownerId, coOwnerIds, prefix, prefixes, prefixFunction,
				commandPreProcessFunction, commandPreProcessBiFunction, activity, status, serverInvite, success, warning, error, new ArrayList<>(commands),
				new ArrayList<>(slashCommands), forcedGuildId, manualUpsert, embedAllReplies, useHelp,
				shutdownAutomatically, helpConsumer, helpWord, executor, linkedCacheSize, manager);
		if (listener != null) {
			client.setListener(listener);
		}
		return client;
	}

	/**
	 * Sets the owner for the bot. <br>
	 * Make sure to verify that the ID provided is ISnowflake compatible when
	 * setting this. If it is not, this will warn the developer.
	 *
	 * @param ownerId The ID of the owner.
	 *
	 * @return This builder
	 */
	public ClientBuilder setOwnerId(String ownerId) {
		this.ownerId = ownerId;
		return this;
	}

	/**
	 * Sets the one or more CoOwners of the bot. <br>
	 * Make sure to verify that all of the IDs provided are ISnowflake compatible
	 * when setting this. If it is not, this will warn the developer which ones are
	 * not.
	 *
	 * @param coOwnerIds The ID(s) of the CoOwners
	 *
	 * @return This builder
	 */
	public ClientBuilder setCoOwnerIds(String... coOwnerIds) {
		this.coOwnerIds = coOwnerIds;
		return this;
	}

	/**
	 * Sets the bot's prefix. <br>
	 * If set null, empty, or not set at all, the bot will use a mention
	 * {@literal @Botname} as a prefix.
	 *
	 * @param prefix The prefix for the bot to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * Sets an array of prefixes in case it's not enough. Be careful.
	 *
	 * @param prefixes The prefixes to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefixes(String[] prefixes) {
		this.prefixes = prefixes;
		return this;
	}

	/**
	 * Sets the Collection of prefixes in case it's not enough. Be careful.
	 *
	 * @param prefixCollection The Collection of prefixes to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefixes(Collection<String> prefixCollection) {
		this.prefixes = prefixCollection.toArray(new String[prefixCollection.size()]);
		return this;
	}

	/**
	 * Sets the Prefix Function. Used if you want custom prefixes per server. <br>
	 * Be careful, this function should be quick, as it's executed every time
	 * MessageReceivedEvent is called. <br>
	 * If function returns null, it will be ignored.
	 *
	 * @param prefixFunction The prefix function to execute to use
	 *
	 * @return This builder
	 */
	public ClientBuilder setPrefixFunction(Function<MessageReceivedEvent, String> prefixFunction) {
		this.prefixFunction = prefixFunction;
		return this;
	}

	/**
	 * Sets the pre-process function. This code is executed before every
	 * command.<br>
	 * Returning "true" will allow processing to proceed.<br>
	 * Returning "false" or "null" will prevent the Command from executing.
	 *
	 * @param commandPreProcessFunction The function to execute
	 *
	 * @return This builder
	 * @deprecated Please use {@link #setCommandPreProcessBiFunction(BiFunction)} instead.
	 *             You can simply add a new parameter for the command, it doesn't have to be used.
	 *             This cannot be used in conjunction with {@link #setCommandPreProcessBiFunction(BiFunction)}.
	 */
	@Deprecated
	public ClientBuilder setCommandPreProcessFunction(Function<MessageReceivedEvent, Boolean> commandPreProcessFunction) {
		this.commandPreProcessFunction = commandPreProcessFunction;
		return this;
	}

	/**
	 * Sets the pre-process function. This code is executed before every command.<br>
	 * Returning "true" will allow processing to proceed.<br>
	 * Returning "false" or "null" will prevent the Command from executing.<br>
	 * You can use Command to see which command will run.<br>
	 * <b>This cannot be used in conjunction with {@link #setCommandPreProcessFunction(Function)}.</b>
	 *
	 * @param commandPreProcessBiFunction
	 *        The function to execute
	 *
	 * @return This builder
	 */
	public ClientBuilder setCommandPreProcessBiFunction(BiFunction<MessageReceivedEvent, Command, Boolean> commandPreProcessBiFunction) {
		this.commandPreProcessBiFunction = commandPreProcessBiFunction;
		return this;
	}

	/**
	 * Sets whether the {@link com.readonlydev.api.ClientInterface
	 * Client} will use the builder to automatically create a help command or
	 * not.
	 *
	 * @param useHelp {@code false} to disable the help command builder, otherwise
	 * the Client will use either the default or one provided
	 * via
	 * {@link com.readonlydev.command.client.ClientBuilder#setHelpConsumer(Consumer)}}.
	 *
	 * @return This builder
	 */
	public ClientBuilder useHelpBuilder(boolean useHelp) {
		this.useHelp = useHelp;
		return this;
	}

	/**
	 * Sets the consumer to run as the bot's help command. <br>
	 * Setting it to {@code null} or not setting this at all will cause the bot to
	 * use the default help builder.
	 *
	 * @param helpConsumer A consumer to accept a
	 * {@link com.readonlydev.command.event.CommandEvent
	 * CommandEvent} when a help command is called.
	 *
	 * @return This builder
	 */
	public ClientBuilder setHelpConsumer(Consumer<CommandEvent> helpConsumer) {
		this.helpConsumer = helpConsumer;
		return this;
	}

	/**
	 * Sets the word used to trigger the command list. <br>
	 * Setting this to {@code null} or not setting this at all will set the help
	 * word to {@code "help"}.
	 *
	 * @param helpWord The word to trigger the help command
	 *
	 * @return This builder
	 */
	public ClientBuilder setHelpWord(String helpWord) {
		this.helpWord = helpWord;
		return this;
	}

	/**
	 * Sets the bot's support server invite.
	 *
	 * @param serverInvite The support server invite
	 *
	 * @return This builder
	 */
	public ClientBuilder setServerInvite(String serverInvite) {
		this.serverInvite = serverInvite;
		return this;
	}

	/**
	 * Sets the emojis for success, warning, and failure.
	 *
	 * @param success Emoji for success
	 * @param warning Emoji for warning
	 * @param error Emoji for failure
	 *
	 * @return This builder
	 */
	public ClientBuilder setEmojis(String success, String warning, String error) {
		this.success = success;
		this.warning = warning;
		this.error = error;
		return this;
	}

	/**
	 * Sets the {@link net.dv8tion.jda.api.entities.Activity Game} to use when the
	 * bot is ready. <br>
	 * Can be set to {@code null} for JDA Utilities to not set it.
	 *
	 * @param activity The Game to use when the bot is ready
	 *
	 * @return This builder
	 */
	public ClientBuilder setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	/**
	 * Sets the {@link net.dv8tion.jda.api.entities.Activity Game} the bot will use
	 * as the default: 'Playing <b>Type [prefix]help</b>'
	 *
	 * @return This builder
	 */
	public ClientBuilder useDefaultGame() {
		this.activity = Activity.playing("default");
		return this;
	}

	/**
	 * Sets the {@link net.dv8tion.jda.api.OnlineStatus OnlineStatus} the bot will
	 * use once Ready This defaults to ONLINE
	 *
	 * @param status The status to set
	 *
	 * @return This builder
	 */
	public ClientBuilder setStatus(OnlineStatus status) {
		this.status = status;
		return this;
	}

	/**
	 * Adds a {@link com.readonlydev.command.Command Command} and registers it
	 * to the {@link com.readonlydev.command.client.Client
	 * ClientImpl} for this session.
	 *
	 * @param command The command to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addCommand(Command command) {
		commands.add(command);
		return this;
	}

	/**
	 * Adds and registers all {@link com.readonlydev.command.Command
	 * Command}s in the collection to the {@link com.readonlydev.command.client.Client
	 * ClientImpl} for this session. <br>
	 * This is the same as running a forEach loop on the Collection
	 * {@link com.readonlydev.command.client.ClientBuilder#addCommand(Command)}
	 * multiple times.
	 *
	 * @param commands The Commands Collection to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addCommands(Collection<Command> commands) {
		commands.forEach(command -> addCommand(command));
		return this;
	}

	/**
	 * Adds and registers multiple {@link com.readonlydev.command.Command
	 * Command}s to the {@link com.readonlydev.command.client.Client
	 * ClientImpl} for this session. <br>
	 * This is the same as calling
	 * {@link com.readonlydev.command.client.ClientBuilder#addCommand(Command)}
	 * multiple times.
	 *
	 * @param commands The Commands to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addCommands(Command... commands) {
		for (Command command : commands) {
			this.addCommand(command);
		}
		return this;
	}

	/**
	 * Adds a {@link com.readonlydev.command.slash.SlashCommand SlashCommand} and
	 * registers it to the {@link com.readonlydev.command.client.Client
	 * ClientImpl} for this session.
	 *
	 * @param command The SlashCommand to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addSlashCommand(SlashCommand command) {
		slashCommands.add(command);
		return this;
	}

	/**
	 * Adds and registers multiple {@link com.readonlydev.command.slash.SlashCommand
	 * SlashCommand}s to the
	 * {@link com.readonlydev.command.client.Client ClientImpl}
	 * for this session. <br>
	 * This is the same as calling
	 * {@link com.readonlydev.command.client.ClientBuilder#addSlashCommand(SlashCommand)}
	 * multiple times.
	 *
	 * @param commands The Commands to add
	 *
	 * @return This builder
	 */
	public ClientBuilder addSlashCommands(SlashCommand... commands) {
		for (SlashCommand command : commands) {
			this.addSlashCommand(command);
		}
		return this;
	}

	/**
	 * Forces Guild Only for SlashCommands.
	 * Setting this to null disables the feature, but it is off by default.
	 *
	 * @param guildId the guild ID.
	 *
	 * @return This Builder
	 */
	public ClientBuilder forceGuildOnly(String guildId) {
		this.forcedGuildId = guildId;
		return this;
	}

	/**
	 * Whether or not to manually upsert slash commands. This is designed if you
	 * want to handle upserting, instead of doing it every boot. False by default.
	 *
	 * @param manualUpsert your option.
	 *
	 * @return This Builder
	 */
	public ClientBuilder setManualUpsert(boolean manualUpsert) {
		this.manualUpsert = manualUpsert;
		return this;
	}

	/**
	 * Sets every reply from the bot as an embeded message.
	 *
	 * @return This Builder
	 */
	public ClientBuilder setAllRepliesAsEmbed() {
		this.embedAllReplies = true;
		return this;
	}

	/**
	 * Sets the {@link com.readonlydev.command.CommandListener CommandListener}
	 * for the {@link com.readonlydev.command.client.Client
	 * ClientImpl}.
	 *
	 * @param listener The CommandListener for the ClientImpl
	 *
	 * @return This builder
	 */
	public ClientBuilder setListener(CommandListener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * Sets the {@link java.util.concurrent.ScheduledExecutorService
	 * ScheduledExecutorService} for the
	 * {@link com.readonlydev.command.client.Client ClientImpl}.
	 *
	 * @param executor The ScheduledExecutorService for the ClientImpl
	 *
	 * @return This builder
	 */
	public ClientBuilder setScheduleExecutor(ScheduledExecutorService executor) {
		this.executor = executor;
		return this;
	}

	/**
	 * Sets the Command Client to shut down internals automatically when a
	 * {@link net.dv8tion.jda.api.events.ShutdownEvent ShutdownEvent} is received.
	 *
	 * @param shutdownAutomatically {@code false} to disable calling the shutdown
	 * method when a ShutdownEvent is received
	 *
	 * @return This builder
	 */
	public ClientBuilder setShutdownAutomatically(boolean shutdownAutomatically) {
		this.shutdownAutomatically = shutdownAutomatically;
		return this;
	}

	/**
	 * Sets the internal size of the client's
	 * {@link com.readonlydev.common.utils.FixedSizeCache FixedSizeCache} used for
	 * caching and pairing the bot's response
	 * {@link net.dv8tion.jda.api.entities.Message Message}s with the calling
	 * Message's ID.
	 * <p>
	 * Higher cache size means that decay of cache contents will most likely occur
	 * later, allowing the deletion of responses when the call is deleted to last
	 * for a longer duration. However this also means larger memory usage.
	 * <p>
	 * Setting {@code 0} or negative will cause the client to not use linked caching
	 * <b>at all</b>.
	 *
	 * @param linkedCacheSize The maximum number of paired responses that can be
	 * cached, or {@code <1} if the built
	 * {@link com.readonlydev.api.ClientInterface
	 * Client} will not use linked caching.
	 *
	 * @return This builder
	 */
	public ClientBuilder setLinkedCacheSize(int linkedCacheSize) {
		this.linkedCacheSize = linkedCacheSize;
		return this;
	}

	/**
	 * Sets the {@link com.readonlydev.settings.GuildSettingsManager
	 * GuildSettingsManager} for the ClientImpl built using this builder.
	 *
	 * @param manager The GuildSettingsManager to set.
	 *
	 * @return This builder
	 */
	public ClientBuilder setGuildSettingsManager(GuildSettingsManager<?> manager) {
		this.manager = manager;
		return this;
	}
}
