package io.github.romvoid95.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.github.romvoid95.command.Command.Category;
import io.github.romvoid95.command.Command.CooldownScope;
import io.github.romvoid95.command.argument.CommandArgument;
import net.dv8tion.jda.api.Permission;

/**
 * A chain-setter based builder for {@link io.github.romvoid95.command.Command
 * Commands}.
 * <p>
 * This is more useful for creation of commands "mid-runtime". <br>
 * A good usage would be to create a Command via eval and register it via
 * {@link io.github.romvoid95.command.CommandClient#addCommand(Command)
 * CommandClient#addCommand(Command)}.
 * <p>
 * While useful during runtime, this is completely inferior to extending Command
 * as a superclass before compilation, and shouldn't be used in place of the
 * ladder.
 *
 * @since 1.6
 */
public class CommandBuilder {
	private String name = "null";
	private String help = "no help available";
	private Category category = null;
	private List<CommandArgument<?>> arguments = new ArrayList<>();
	private boolean guildOnly = true;
	private List<String> requiredRoles = new ArrayList<String>();
	private boolean ownerCommand = false;
	private int cooldown = 0;
	private Permission[] userPermissions = new Permission[0];
	private Permission[] botPermissions = new Permission[0];
	private final LinkedList<String> aliases = new LinkedList<>();
	private final LinkedList<Command> children = new LinkedList<>();
	private BiConsumer<CommandEvent, Command> helpBiConsumer = null;
	private boolean usesTopicTags = true;
	private CooldownScope cooldownScope = CooldownScope.USER;
	private boolean hidden = false;

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#name name} of the Command
	 * built from this CommandBuilder.
	 *
	 * @param name The name of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setName(String name) {
		if (name == null)
			this.name = "null";
		else
			this.name = name;
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#help help} snippet of the
	 * Command built from this CommandBuilder.
	 *
	 * @param help The help snippet of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setHelp(String help) {
		if (help == null)
			this.help = "no help available";
		else
			this.help = help;
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#category category} of the
	 * Command built from this CommandBuilder.
	 *
	 * @param category The category of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setCategory(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * Adds the {@link io.github.romvoid95.command.argument.CommandArgument
	 * CommandArgument} to the list of
	 * {@link io.github.romvoid95.command.Command#arguments arguments} for this
	 * command.
	 *
	 * @param argument The CommandArgument to add
	 *
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addArgument(CommandArgument<?> argument) {
		arguments.add(argument);
		return this;
	}

	/**
	 * Adds the specified array of
	 * {@link io.github.romvoid95.command.argument.CommandArgument CommandArgument}
	 * to the list of {@link io.github.romvoid95.command.Command#arguments
	 * arguments} for this command.
	 *
	 * @param arguments The array of CommandArguments to add
	 * 
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addArguments(CommandArgument<?>... arguments) {
		for (CommandArgument<?> argument : arguments)
			addArgument(argument);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#arguments arguments} for
	 * this command.
	 *
	 * @param arguments The array of CommandArguments to set
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setArguments(CommandArgument<?>... arguments) {
		this.arguments.clear();
		if (arguments != null)
			for (CommandArgument<?> argument : arguments)
				addArgument(argument);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#arguments arguments} of
	 * the Command built from this CommandBuilder.
	 *
	 * @param arguments The Collection of CommandArguments to set
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setArguments(Collection<CommandArgument<?>> arguments) {
		this.arguments.clear();
		if (arguments != null)
			this.arguments.addAll(arguments);
		return this;
	}

	/**
	 * Sets the Command built to be
	 * {@link io.github.romvoid95.command.Command#guildOnly guild only}.
	 *
	 * @param guildOnly {@code true} if the Command built is guild only,
	 * {@code false} if it is not.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setGuildOnly(boolean guildOnly) {
		this.guildOnly = guildOnly;
		return this;
	}

	/**
	 * Adds the name of a {@link net.dv8tion.jda.api.entities.Role role} to the list
	 * of {@link io.github.romvoid95.command.Command#requiredRoles requiredRoles}
	 * needed to use this command.
	 *
	 * @param role The name of a required role
	 *
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addRequiredRole(String role) {
		requiredRoles.add(role);
		return this;
	}

	/**
	 * Adds the names of many {@link net.dv8tion.jda.api.entities.Role roles} to the
	 * list of {@link io.github.romvoid95.command.Command#requiredRoles
	 * requiredRoles} needed to use this command.
	 *
	 * @param roles The name of a required role
	 * 
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addRequiredRoles(String... roles) {
		for (String role : roles)
			addRequiredRole(role);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#requiredRoles
	 * requiredRoles} needed to use this command.
	 *
	 * @param roles the names of required roles to set
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setRquiredRoles(String... roles) {
		this.requiredRoles.clear();
		if (roles != null)
			for (String role : roles)
				addRequiredRole(role);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#requiredRoles
	 * requiredRoles} of the Command built from this CommandBuilder.
	 *
	 * @param roles The roles of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setRquiredRoles(Collection<String> roles) {
		this.requiredRoles.clear();
		if (roles != null)
			this.requiredRoles.addAll(roles);
		return this;
	}

	/**
	 * Sets the Command built to be
	 * {@link io.github.romvoid95.command.Command#ownerCommand owner only}.
	 *
	 * @param ownerCommand {@code true} if the Command built is owner only,
	 * {@code false} if it is not.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setOwnerCommand(boolean ownerCommand) {
		this.ownerCommand = ownerCommand;
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#cooldown cooldown} of the
	 * Command built from this CommandBuilder.
	 *
	 * @param cooldown The number of seconds the built Command will be on cooldown.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setCooldown(int cooldown) {
		this.cooldown = cooldown;
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#userPermissions required
	 * user permissions} of the Command built from this CommandBuilder.
	 *
	 * @param userPermissions The required Permissions a User must have when using
	 * the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setUserPermissions(Permission... userPermissions) {
		if (userPermissions == null)
			this.userPermissions = new Permission[0];
		else
			this.userPermissions = userPermissions;
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#userPermissions required
	 * user permissions} of the Command built from this CommandBuilder.
	 *
	 * @param userPermissions The required Permissions a User must have when using
	 * the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setUserPermissions(Collection<Permission> userPermissions) {
		if (userPermissions == null)
			this.userPermissions = new Permission[0];
		else
			this.userPermissions = (Permission[]) userPermissions.toArray();
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#botPermissions required
	 * bot permissions} of the Command built from this CommandBuilder.
	 *
	 * @param botPermissions The required Permissions the bot must have when using
	 * the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setBotPermissions(Permission... botPermissions) {
		if (botPermissions == null)
			this.botPermissions = new Permission[0];
		else
			this.botPermissions = botPermissions;
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#botPermissions required
	 * bot permissions} of the Command built from this CommandBuilder.
	 *
	 * @param botPermissions The required Permissions the bot must have when using
	 * the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setBotPermissions(Collection<Permission> botPermissions) {
		if (botPermissions == null)
			this.botPermissions = new Permission[0];
		else
			this.botPermissions = (Permission[]) botPermissions.toArray();
		return this;
	}

	/**
	 * Adds a {@link io.github.romvoid95.command.Command#aliases alias} for the
	 * Command built from this CommandBuilder.
	 *
	 * @param alias The Command alias to add.
	 *
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addAlias(String alias) {
		aliases.add(alias);
		return this;
	}

	/**
	 * Adds {@link io.github.romvoid95.command.Command#aliases aliases} for the
	 * Command built from this CommandBuilder.
	 *
	 * @param aliases The Command aliases to add.
	 *
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addAliases(String... aliases) {
		for (String alias : aliases)
			addAlias(alias);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#aliases aliases} of the
	 * Command built from this CommandBuilder.
	 *
	 * @param aliases The aliases of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setAliases(String... aliases) {
		this.aliases.clear();
		if (aliases != null)
			for (String alias : aliases)
				addAlias(alias);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#aliases aliases} of the
	 * Command built from this CommandBuilder.
	 *
	 * @param aliases The aliases of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setAliases(Collection<String> aliases) {
		this.aliases.clear();
		if (aliases != null)
			this.aliases.addAll(aliases);
		return this;
	}

	/**
	 * Adds a {@link io.github.romvoid95.command.Command#children child} Command to
	 * the Command built from this CommandBuilder.
	 *
	 * @param child The child Command to add.
	 *
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addChild(Command child) {
		children.add(child);
		return this;
	}

	/**
	 * Adds {@link io.github.romvoid95.command.Command#children child} Commands to
	 * the Command built from this CommandBuilder.
	 *
	 * @param children The child Commands to add.
	 *
	 * @return This CommandBuilder.
	 */
	public CommandBuilder addChildren(Command... children) {
		for (Command child : children)
			addChild(child);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#children children} of the
	 * Command built from this CommandBuilder.
	 *
	 * @param children The children of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setChildren(Command... children) {
		this.children.clear();
		if (children != null)
			for (Command child : children)
				addChild(child);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#children children} of the
	 * Command built from this CommandBuilder.
	 *
	 * @param children The children of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setChildren(Collection<Command> children) {
		this.children.clear();
		if (children != null)
			this.children.addAll(children);
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#helpBiConsumer help
	 * BiConsumer} of the Command built from this CommandBuilder.
	 *
	 * @param helpBiConsumer The help BiConsumer of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setHelpBiConsumer(BiConsumer<CommandEvent, Command> helpBiConsumer) {
		this.helpBiConsumer = helpBiConsumer;
		return this;
	}

	/**
	 * Sets the Command built to
	 * {@link io.github.romvoid95.command.Command#usesTopicTags use TopicTags}.
	 *
	 * @param usesTopicTags {@code true} if the Command built is uses topic tags,
	 * {@code false} if it does not.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setUsesTopicTags(boolean usesTopicTags) {
		this.usesTopicTags = usesTopicTags;
		return this;
	}

	/**
	 * Sets the {@link io.github.romvoid95.command.Command#cooldownScope cooldown
	 * scope} of the Command built from this CommandBuilder.
	 *
	 * @param cooldownScope The CooldownScope of the Command to be built.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setCooldownScope(CooldownScope cooldownScope) {
		if (cooldownScope == null)
			this.cooldownScope = CooldownScope.USER;
		else
			this.cooldownScope = cooldownScope;
		return this;
	}

	/**
	 * Sets the Command built to be
	 * {@link io.github.romvoid95.command.Command#hidden hidden} from the help
	 * builder.
	 *
	 * @param hidden {@code true} if this will be hidden from the help builder,
	 * {@code false} otherwise.
	 *
	 * @return This CommandBuilder
	 */
	public CommandBuilder setHidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	/**
	 * Builds the {@link io.github.romvoid95.command.Command Command} using the
	 * previously provided information.
	 * <p>
	 * This uses the only the {@link io.github.romvoid95.command.CommandEvent
	 * CommandEvent} parameter that would be provided during
	 * {@link io.github.romvoid95.command.Command#execute(CommandEvent)
	 * #execute(CommandEvent)}, and no information about the Command can be
	 * retrieved using this.
	 * <p>
	 * An alternate method
	 * {@link io.github.romvoid95.command.CommandBuilder#build(java.util.function.BiConsumer)}
	 * exists if you wish to retrieve information about the Command built during
	 * execution.
	 *
	 * @param execution The {@link java.util.function.Consumer} that runs on
	 * Command#execute(CommandEvent).
	 *
	 * @return The Command built
	 */
	public Command build(Consumer<CommandEvent> execution) {
		return build((c, e) -> execution.accept(e));
	}

	/**
	 * Builds the {@link io.github.romvoid95.command.Command Command} using the
	 * previously provided information.
	 * <p>
	 * This uses the both the {@link io.github.romvoid95.command.CommandEvent
	 * CommandEvent} parameter that would be provided during
	 * {@link io.github.romvoid95.command.Command#execute(CommandEvent)
	 * #execute(CommandEvent)}, and the Command built when, allowing info on the
	 * Command to be retrieved during execution.
	 *
	 * @param execution The {@link java.util.function.BiConsumer} that runs on
	 * {@link io.github.romvoid95.command.Command#execute(CommandEvent)}.
	 *
	 * @return The Command built
	 */
	public Command build(BiConsumer<Command, CommandEvent> execution) {
		return new BlankCommand(name, help, category, arguments, guildOnly, requiredRoles, ownerCommand, cooldown,
				userPermissions, botPermissions, aliases.toArray(new String[aliases.size()]),
				children.toArray(new Command[children.size()]), helpBiConsumer, usesTopicTags, cooldownScope, hidden) {
			@Override
			protected void execute(CommandEvent event) {
				execution.accept(this, event);
			}
		};
	}

	private abstract class BlankCommand extends Command {
		BlankCommand(String name, String help, Category category, List<CommandArgument<?>> arguments, boolean guildOnly,
				List<String> requiredRole, boolean ownerCommand, int cooldown, Permission[] userPermissions,
				Permission[] botPermissions, String[] aliases, Command[] children,
				BiConsumer<CommandEvent, Command> helpBiConsumer, boolean usesTopicTags, CooldownScope cooldownScope,
				boolean hidden) {
			this.name = name;
			this.help = help;
			this.category = category;
			this.arguments = arguments;
			this.guildOnly = guildOnly;
			this.requiredRoles = requiredRole;
			this.ownerCommand = ownerCommand;
			this.cooldown = cooldown;
			this.userPermissions = userPermissions;
			this.botPermissions = botPermissions;
			this.aliases = aliases;
			this.children = children;
			this.helpBiConsumer = helpBiConsumer;
			this.usesTopicTags = usesTopicTags;
			this.cooldownScope = cooldownScope;
			this.hidden = hidden;
		}
	}
}
