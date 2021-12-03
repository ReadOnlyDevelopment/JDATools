package com.readonlydev.cmd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A simple builder used to create a
 * {@link com.readonlydev.cmd.CommandClientImpl CommandClientImpl}.
 * <p>
 * Once built, add the {@link com.readonlydev.cmd.CommandClient
 * CommandClient} as an EventListener to {@link net.dv8tion.jda.api.JDA JDA} and
 * it will automatically handle commands with ease!
 */
public class CommandClientBuilder {
	private Activity activity = Activity.playing("default");
	private OnlineStatus status = OnlineStatus.ONLINE;
	private String ownerId;
	private String[] coOwnerIds;
	private String prefix;
	private String altprefix;
	private String[] prefixes;
	private Function<MessageReceivedEvent, String> prefixFunction;
	private Function<MessageReceivedEvent, Boolean> commandPreProcessFunction;
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
	 * Builds a {@link com.readonlydev.cmd.CommandClientImpl
	 * CommandClientImpl} with the provided settings. <br>
	 * Once built, only the {@link com.readonlydev.cmd.CommandListener
	 * CommandListener}, and {@link com.readonlydev.cmd.Command Command}s
	 * can be changed.
	 *
	 * @return The CommandClient built.
	 */
	public CommandClient build() {
		CommandClient client = new CommandClientImpl(ownerId, coOwnerIds, prefix, altprefix, prefixes, prefixFunction,
				commandPreProcessFunction, activity, status, serverInvite, success, warning, error, new ArrayList<>(commands),
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
	public CommandClientBuilder setOwnerId(String ownerId) {
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
	public CommandClientBuilder setCoOwnerIds(String... coOwnerIds) {
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
	public CommandClientBuilder setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * Sets the bot's alternative prefix. <br>
	 * If set null, the bot will only use its primary prefix prefix.
	 *
	 * @param prefix The alternative prefix for the bot to use
	 *
	 * @return This builder
	 */
	public CommandClientBuilder setAlternativePrefix(String prefix) {
		this.altprefix = prefix;
		return this;
	}

	/**
	 * Sets an array of prefixes in case it's not enough. Be careful.
	 *
	 * @param prefixes The prefixes to use
	 *
	 * @return This builder
	 */
	public CommandClientBuilder setPrefixes(String[] prefixes) {
		this.prefixes = prefixes;
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
	public CommandClientBuilder setPrefixFunction(Function<MessageReceivedEvent, String> prefixFunction) {
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
	 */
	public CommandClientBuilder setCommandPreProcessFunction(Function<MessageReceivedEvent, Boolean> commandPreProcessFunction) {
		this.commandPreProcessFunction = commandPreProcessFunction;
		return this;
	}

	/**
	 * Sets whether the {@link com.readonlydev.cmd.CommandClient
	 * CommandClient} will use the builder to automatically create a help command or
	 * not.
	 *
	 * @param useHelp {@code false} to disable the help command builder, otherwise
	 * the CommandClient will use either the default or one provided
	 * via
	 * {@link com.readonlydev.cmd.CommandClientBuilder#setHelpConsumer(Consumer)}}.
	 *
	 * @return This builder
	 */
	public CommandClientBuilder useHelpBuilder(boolean useHelp) {
		this.useHelp = useHelp;
		return this;
	}

	/**
	 * Sets the consumer to run as the bot's help command. <br>
	 * Setting it to {@code null} or not setting this at all will cause the bot to
	 * use the default help builder.
	 *
	 * @param helpConsumer A consumer to accept a
	 * {@link com.readonlydev.cmd.CommandEvent
	 * CommandEvent} when a help command is called.
	 *
	 * @return This builder
	 */
	public CommandClientBuilder setHelpConsumer(Consumer<CommandEvent> helpConsumer) {
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
	public CommandClientBuilder setHelpWord(String helpWord) {
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
	public CommandClientBuilder setServerInvite(String serverInvite) {
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
	public CommandClientBuilder setEmojis(String success, String warning, String error) {
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
	public CommandClientBuilder setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	/**
	 * Sets the {@link net.dv8tion.jda.api.entities.Activity Game} the bot will use
	 * as the default: 'Playing <b>Type [prefix]help</b>'
	 *
	 * @return This builder
	 */
	public CommandClientBuilder useDefaultGame() {
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
	public CommandClientBuilder setStatus(OnlineStatus status) {
		this.status = status;
		return this;
	}

	/**
	 * Adds a {@link com.readonlydev.cmd.Command Command} and registers it
	 * to the {@link com.readonlydev.cmd.CommandClientImpl
	 * CommandClientImpl} for this session.
	 *
	 * @param command The command to add
	 *
	 * @return This builder
	 */
	public CommandClientBuilder addCommand(Command command) {
		commands.add(command);
		return this;
	}

	/**
	 * Adds and registers multiple {@link com.readonlydev.cmd.Command
	 * Command}s to the {@link com.readonlydev.cmd.CommandClientImpl
	 * CommandClientImpl} for this session. <br>
	 * This is the same as calling
	 * {@link com.readonlydev.cmd.CommandClientBuilder#addCommand(Command)}
	 * multiple times.
	 *
	 * @param commands The Commands to add
	 *
	 * @return This builder
	 */
	public CommandClientBuilder addCommands(Command... commands) {
		for (Command command : commands) {
			this.addCommand(command);
		}
		return this;
	}

	/**
	 * Adds a {@link com.readonlydev.cmd.SlashCommand SlashCommand} and
	 * registers it to the {@link com.readonlydev.cmd.CommandClientImpl
	 * CommandClientImpl} for this session.
	 *
	 * @param command The SlashCommand to add
	 *
	 * @return This builder
	 */
	public CommandClientBuilder addSlashCommand(SlashCommand command) {
		slashCommands.add(command);
		return this;
	}

	/**
	 * Adds and registers multiple {@link com.readonlydev.cmd.SlashCommand
	 * SlashCommand}s to the
	 * {@link com.readonlydev.cmd.CommandClientImpl CommandClientImpl}
	 * for this session. <br>
	 * This is the same as calling
	 * {@link com.readonlydev.cmd.CommandClientBuilder#addSlashCommand(SlashCommand)}
	 * multiple times.
	 *
	 * @param commands The Commands to add
	 *
	 * @return This builder
	 */
	public CommandClientBuilder addSlashCommands(SlashCommand... commands) {
		for (SlashCommand command : commands) {
			this.addSlashCommand(command);
		}
		return this;
	}

	/**
	 * Forces Guild Only for SlashCommands. This is the same as setting
	 * this.guildOnly = true and this.guildId = your value for every command.
	 * Setting this to null disables the feature, but it is off by default.
	 *
	 * @param guildId the guild ID.
	 *
	 * @return This Builder
	 */
	public CommandClientBuilder forceGuildOnly(String guildId) {
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
	public CommandClientBuilder setManualUpsert(boolean manualUpsert) {
		this.manualUpsert = manualUpsert;
		return this;
	}

	/**
	 * Sets every reply from the bot as an embeded message.
	 *
	 * @return This Builder
	 */
	public CommandClientBuilder setAllRepliesAsEmbed() {
		this.embedAllReplies = true;
		return this;
	}

	/**
	 * Sets the {@link com.readonlydev.cmd.CommandListener CommandListener}
	 * for the {@link com.readonlydev.cmd.CommandClientImpl
	 * CommandClientImpl}.
	 *
	 * @param listener The CommandListener for the CommandClientImpl
	 *
	 * @return This builder
	 */
	public CommandClientBuilder setListener(CommandListener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * Sets the {@link java.util.concurrent.ScheduledExecutorService
	 * ScheduledExecutorService} for the
	 * {@link com.readonlydev.cmd.CommandClientImpl CommandClientImpl}.
	 *
	 * @param executor The ScheduledExecutorService for the CommandClientImpl
	 *
	 * @return This builder
	 */
	public CommandClientBuilder setScheduleExecutor(ScheduledExecutorService executor) {
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
	public CommandClientBuilder setShutdownAutomatically(boolean shutdownAutomatically) {
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
	 * {@link com.readonlydev.cmd.CommandClient
	 * CommandClient} will not use linked caching.
	 *
	 * @return This builder
	 */
	public CommandClientBuilder setLinkedCacheSize(int linkedCacheSize) {
		this.linkedCacheSize = linkedCacheSize;
		return this;
	}

	/**
	 * Sets the {@link com.readonlydev.cmd.GuildSettingsManager
	 * GuildSettingsManager} for the CommandClientImpl built using this builder.
	 *
	 * @param manager The GuildSettingsManager to set.
	 *
	 * @return This builder
	 */
	public CommandClientBuilder setGuildSettingsManager(GuildSettingsManager<?> manager) {
		this.manager = manager;
		return this;
	}
}
