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

package io.github.readonly.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import io.github.readonly.api.CooldownScope;
import io.github.readonly.command.arg.CommandArgument;
import io.github.readonly.command.arg.parse.ArgumentIndex;
import io.github.readonly.command.event.CommandEvent;
import io.github.readonly.command.operation.UserInteraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

/**
 * <h1><b>Commands In JDATools</b></h1>
 * <p>
 * The internal inheritance for Commands used in JDATools is that of the Command object.
 * <p>
 * Classes created inheriting this class gain the unique traits of commands operated using the Commands Extension. <br>
 * Using several fields, a command can define properties that make it unique and complex while maintaining a low level
 * of development. <br>
 * All Commands extending this class can define any number of these fields in a object constructor and then create the
 * command action/response in the abstract
 * {@link io.github.readonly.command.Command#execute(com.readonlydev.command.CommandEvent) #execute(CommandEvent)} body:
 *
 * <pre>
 * <code> public class ExampleCmd extends Command {
 *
 *      public ExampleCmd() {
 *          this.name = "example";
 *          this.aliases = new String[]{"test","demo"};
 *          this.help = "gives an example of commands do";
 *      }
 *
 *      {@literal @Override}
 *      protected void execute(CommandEvent) {
 *          event.reply("Hey look! This would be the bot's reply if this was a command!");
 *      }
 *
 * }</code>
 * </pre>
 *
 * Execution is with the provision of a MessageReceivedEvent-Client wrapper called a
 * {@link io.github.readonly.command.event.CommandEvent CommandEvent} and is performed in two steps:
 * <ul>
 * <li>{@link io.github.readonly.command.Command#run(CommandEvent) run} - The command runs through a series of
 * conditionals, automatically terminating the command instance if one is not met, and possibly providing an error
 * response.</li>
 * <li>{@link io.github.readonly.command.Command#execute(CommandEvent) execute} - The command, now being cleared to run,
 * executes and performs whatever lies in the abstract body method.</li>
 * </ul>
 */
public abstract class Command extends UserInteraction
{

	/**
	 * The name of the command, allows the command to be called the formats: <br>
	 * Normal Command: {@code [prefix]<command name>}. <br>
	 * Slash Command: {@code /<command name>}
	 */
	protected String name = "null";

	/**
	 * A small help String that summarizes the function of the command, used in the default help builder, and shown in
	 * the client for Slash Commands.
	 */
	protected String help = "no help available";

	/**
	 * The {@link io.github.readonly.command.Command.Category Category} of the command. <br>
	 * This can perform any other checks not completed by the default conditional fields.
	 */
	protected Category category = null;

	/**
	 * An arguments format String for the command, used in the default help builder. Not supported for SlashCommands.
	 *
	 * @see SlashCommand#options
	 */
	protected List<CommandArgument<?>> arguments = new ArrayList<>();

	/**
	 * {@code true} if the command may only be used in an NSFW
	 * {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel TextChannel} or DMs. {@code false} if it may be
	 * used anywhere <br>
	 * Default: {@code false}
	 */
	protected boolean nsfwOnly = false;

	/**
	 * A String name of a role required to use this command.
	 */
	protected List<String> requiredRoles = new ArrayList<>();

	/**
	 * {@code true} if the command may only be used by a User with an ID matching the Owners or any of the CoOwners.<br>
	 * If enabled for a Slash Command, only owners (owner + up to 9 co-owners) will be added to the SlashCommand. All
	 * other permissions will be ignored. <br>
	 * Default {@code false}.
	 */
	protected boolean ownerCommand = false;

	/**
	 * An {@code int} number of seconds users must wait before using this command again.
	 */
	protected int cooldown = 0;

	/**
	 * Any {@link net.dv8tion.jda.api.Permission Permission}s a Member must have to use this command. <br>
	 * These are only checked in a {@link net.dv8tion.jda.api.entities.Guild Guild} environment.
	 */
	protected Permission[] userPermissions = new Permission[0];

	/**
	 * Any {@link net.dv8tion.jda.api.Permission Permission}s the bot must have to use a command. <br>
	 * These are only checked in a {@link net.dv8tion.jda.api.entities.Guild Guild} environment.
	 */
	protected Permission[] botPermissions = new Permission[0];

	/**
	 * The aliases of the command, when calling a command these function identically to calling the
	 * {@link io.github.readonly.command.Command#name Command.name}. This options only works for normal commands, not slash
	 * commands.
	 */
	protected String[] aliases = new String[0];

	/**
	 * The child commands of the command. These are used in the format {@code [prefix]<parent name>
	 * <child name>}.
	 */
	protected Command[] children = new Command[0];

	/**
	 * The {@link java.util.function.BiConsumer BiConsumer} for creating a help response to the format
	 * {@code [prefix]<command name> help}.
	 */
	protected BiConsumer<CommandEvent, Command> helpBiConsumer = null;

	/**
	 * {@code true} if this command checks a channel topic for topic-tags. <br>
	 * This means that putting {@code {-commandname}}, {@code {-command category}}, {@code {-all}} in a channel topic
	 * will cause this command to terminate. <br>
	 * Default {@code true}.
	 */
	protected boolean usesTopicTags = true;

	/**
	 * {@code true} if this command should be hidden from the help. <br>
	 * Default {@code false}<br>
	 * <b>This has no effect for SlashCommands.</b>
	 */
	protected boolean hidden = false;

	/**
	 * The {@link io.github.readonly.api.CooldownScope CooldownScope} of the command. This defines how far of a scope
	 * cooldowns have. <br>
	 * Default {@link io.github.readonly.api.CooldownScope#USER CooldownScope.USER}.
	 */
	protected CooldownScope cooldownScope = CooldownScope.USER;

	/**
	 * The permission message used when the bot does not have the requires permission. Requires 3 "%s", first is user
	 * mention, second is the permission needed, third is type, e.g. Guild.
	 */
	protected String botMissingPermMessage = "%s I need the %s permission in this %s!";

	/**
	 * The permission message used when the user does not have the requires permission. Requires 3 "%s", first is user
	 * mention, second is the permission needed, third is type, e.g. Guild.
	 */
	protected String userMissingPermMessage = "%s You must have the %s permission in this %s to use that!";

	protected void addAguments(CommandArgument<?>... argumentArray)
	{
		arguments.addAll(Arrays.asList(argumentArray));
	}

	protected void addAgument(CommandArgument<?> CommandArgument)
	{
		arguments.add(CommandArgument);
	}

	protected void addRequiredRoles(String... roles)
	{
		requiredRoles.addAll(Arrays.asList(roles));
	}

	/**
	 * The main body method of a {@link io.github.readonly.command.Command Command}. <br>
	 * This is the "response" for a successful {@link io.github.readonly.command.Command#run(CommandEvent)
	 * #run(CommandEvent)}.
	 *
	 * @param event
	 *            The {@link io.github.readonly.command.event.CommandEvent CommandEvent} that triggered this Command
	 */
	protected abstract void execute(CommandEvent event);

	/**
	 * Runs checks for the {@link io.github.readonly.command.Command Command} with the given
	 * {@link io.github.readonly.command.event.CommandEvent CommandEvent} that called it. <br>
	 * Will terminate, and possibly respond with a failure message, if any checks fail.
	 *
	 * @param event
	 *            The CommandEvent that triggered this Command
	 */
	public final void run(CommandEvent event)
	{
		// child check
		if (!event.getArgumentIndex().isEmpty())
		{
			String[] parts = Arrays.copyOf(event.getArgumentIndex().getChildArgArray(), 2);
			if ((helpBiConsumer != null) && parts[0].equalsIgnoreCase(event.getClient().getHelpWord()))
			{
				helpBiConsumer.accept(event, this);
				return;
			}
			for (Command cmd : getChildren())
			{
				if (cmd.isCommandFor(parts[0]))
				{
					event.setArgumentIndex(new ArgumentIndex(parts[1] == null ? "" : parts[1]));
					cmd.run(event);
					return;
				}
			}
		}
		// owner check
		if (ownerCommand && !(event.isOwner()))
		{
			terminate(event, null);
			return;
		}
		// category check
		if ((category != null) && !category.test(event))
		{
			terminate(event, category.getFailureResponse());
			return;
		}
		// is allowed check
		if (event.isFromType(ChannelType.TEXT) && !isAllowed(event.getTextChannel()))
		{
			terminate(event, "That command cannot be used in this channel!");
			return;
		}
		// required role check
		if (!requiredRoles.isEmpty())
		{
			requiredRoles.forEach(role ->
			{
				if (!event.isFromType(ChannelType.TEXT) || event.getMember().getRoles().stream().noneMatch(r -> r.getName().equalsIgnoreCase(role)))
				{
					terminate(event, event.getClient().getError() + " You must have a role called `" + role + "` to use that!");
					return;
				}
			});
		}

		// availability check
		if (event.getChannelType() == ChannelType.TEXT)
		{
			// user perms
			for (Permission p : userPermissions)
			{
				if (p.isChannel())
				{
					if (!event.getMember().hasPermission(event.getTextChannel(), p))
					{
						terminate(event, String.format(userMissingPermMessage, event.getClient().getError(), p.getName(), "channel"));
						return;
					}
				} else
				{
					if (!event.getMember().hasPermission(p))
					{
						terminate(event, String.format(userMissingPermMessage, event.getClient().getError(), p.getName(), "server"));
						return;
					}
				}
			}
			// bot perms
			for (Permission p : botPermissions)
			{
				if (p.isChannel())
				{
					if (p.name().startsWith("VOICE"))
					{
						GuildVoiceState	gvc	= event.getMember().getVoiceState();
						AudioChannel	vc	= gvc == null ? null : gvc.getChannel();
						if (vc == null)
						{
							terminate(event, event.getClient().getError() + " You must be in a voice channel to use that!");
							return;
						} else if (!event.getSelfMember().hasPermission(vc, p))
						{
							terminate(event, String.format(botMissingPermMessage, event.getClient().getError(), p.getName(), "voice channel"));
							return;
						}
					} else
					{
						if (!event.getSelfMember().hasPermission(event.getTextChannel(), p))
						{
							terminate(event, String.format(botMissingPermMessage, event.getClient().getError(), p.getName(), "channel"));
							return;
						}
					}
				} else
				{
					if (!event.getSelfMember().hasPermission(p))
					{
						terminate(event, String.format(botMissingPermMessage, event.getClient().getError(), p.getName(), "server"));
						return;
					}
				}
			}
			// nsfw check
			if (nsfwOnly && !event.getTextChannel().isNSFW())
			{
				terminate(event, "This command may only be used in NSFW text channels!");
				return;
			}
		} else if (guildOnly)
		{
			terminate(event, event.getClient().getError() + " This command cannot be used in direct messages");
			return;
		}
		// cooldown check, ignoring owner
		if ((cooldown > 0) && !(event.isOwner()))
		{
			String	key			= getCooldownKey(event);
			int		remaining	= event.getClient().getRemainingCooldown(key);
			if (remaining > 0)
			{
				terminate(event, getCooldownError(event, remaining));
				return;
			} else
			{
				event.getClient().applyCooldown(key, cooldown);
			}
		}
		// run
		try
		{
			execute(event);
		} catch (Throwable t)
		{
			if (event.getClient().getListener() != null)
			{
				event.getClient().getListener().onCommandException(event, this, t);
				return;
			}
			// otherwise we rethrow
			throw t;
		}
		if (event.getClient().getListener() != null)
		{
			event.getClient().getListener().onCompletedCommand(event, this);
		}
	}

	/**
	 * Checks if the given input represents this Command
	 *
	 * @param input
	 *            The input to check
	 *
	 * @return {@code true} if the input is the name or an alias of the Command
	 */
	public boolean isCommandFor(String input)
	{
		if (name.equalsIgnoreCase(input))
		{
			return true;
		}
		for (String alias : aliases)
		{
			if (alias.equalsIgnoreCase(input))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether a command is allowed in a {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel
	 * TextChannel} by searching the channel topic for topic tags relating to the command.
	 * <p>
	 * {-{@link io.github.readonly.command.Command#name name}}, {-{@link io.github.readonly.command.Command.Category category
	 * name}}, or {-{@code all}} are valid examples of ways that this method would return {@code false} if placed in a
	 * channel topic.
	 * <p>
	 * <b>NOTE:</b>Topic tags are <b>case sensitive</b> and proper usage must be in lower case! <br>
	 * Also note that setting {@link io.github.readonly.command.Command#usesTopicTags usesTopicTags} to {@code false} will
	 * cause this method to always return {@code true}, as the feature would not be applicable in the first place.
	 *
	 * @param channel
	 *            The TextChannel to test.
	 *
	 * @return {@code true} if the channel topic doesn't specify any topic-tags that would cause this command to be
	 *         cancelled, or if {@code usesTopicTags} has been set to {@code false}.
	 */
	public boolean isAllowed(TextChannel channel)
	{
		if (!usesTopicTags)
		{
			return true;
		}
		if (channel == null)
		{
			return true;
		}
		String topic = channel.getTopic();
		if ((topic == null) || topic.isEmpty())
		{
			return true;
		}
		topic = topic.toLowerCase(Locale.ROOT);
		String lowerName = name.toLowerCase(Locale.ROOT);
		if (topic.contains("{" + lowerName + "}"))
		{
			return true;
		}
		if (topic.contains("{-" + lowerName + "}"))
		{
			return false;
		}
		String lowerCat = category == null ? null : category.getName().toLowerCase(Locale.ROOT);
		if (lowerCat != null)
		{
			if (topic.contains("{" + lowerCat + "}"))
			{
				return true;
			}
			if (topic.contains("{-" + lowerCat + "}"))
			{
				return false;
			}
		}
		return !topic.contains("{-all}");
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#name Command.name} for the Command.
	 *
	 * @return The name for the Command
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#help Command.help} for the Command.
	 *
	 * @return The help for the Command
	 */
	public String getHelp()
	{
		return help;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#category Command.category} for the Command.
	 *
	 * @return The category for the Command
	 */
	public Category getCategory()
	{
		return category;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#arguments Command.arguments} for the Command.
	 *
	 * @return The arguments for the Command
	 */
	public List<CommandArgument<?>> getArguments()
	{
		return arguments;
	}

	/**
	 * Checks if this Command can only be used in a {@link net.dv8tion.jda.api.entities.Guild Guild}.
	 *
	 * @return {@code true} if this Command can only be used in a Guild, else {@code false} if it can be used outside of
	 *         one
	 */
	public boolean isGuildOnly()
	{
		return guildOnly;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#requiredRoles Command.requiredRoles} for the Command.
	 *
	 * @return The requiredRoles for the Command
	 */
	public List<String> getRequiredRoles()
	{
		return requiredRoles;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#cooldown Command.cooldown} for the Command.
	 *
	 * @return The cooldown for the Command
	 */
	@Override
	public int getCooldown()
	{
		return cooldown;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#userPermissions Command.userPermissions} for the Command.
	 *
	 * @return The userPermissions for the Command
	 */
	@Override
	public Permission[] getUserPermissions()
	{
		return userPermissions;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#botPermissions Command.botPermissions} for the Command.
	 *
	 * @return The botPermissions for the Command
	 */
	@Override
	public Permission[] getBotPermissions()
	{
		return botPermissions;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#aliases Command.aliases} for the Command.
	 *
	 * @return The aliases for the Command
	 */
	public String[] getAliases()
	{
		return aliases;
	}

	/**
	 * Gets the {@link io.github.readonly.command.Command#children Command.children} for the Command.
	 *
	 * @return The children for the Command
	 */
	public Command[] getChildren()
	{
		return children;
	}

	/**
	 * Checks whether or not this command is an owner only Command.
	 *
	 * @return {@code true} if the command is an owner command, otherwise {@code false} if it is not
	 */
	@Override
	public boolean isOwnerCommand()
	{
		return ownerCommand;
	}

	/**
	 * Checks whether or not this command should be hidden from the help.
	 *
	 * @return {@code true} if the command should be hidden, otherwise {@code false}
	 */
	public boolean isHidden()
	{
		return hidden;
	}

	public String getArgumentsUseageString()
	{
		StringBuilder b = new StringBuilder();
		arguments.forEach(arg ->
		{
			b.append(arg.getArgumentForHelp() + " ");
		});
		return b.toString();
	}

	private void terminate(CommandEvent event, String message)
	{
		if (message != null)
		{
			event.reply(message);
		}
		if (event.getClient().getListener() != null)
		{
			event.getClient().getListener().onTerminatedCommand(event, this);
		}
	}

	/**
	 * Gets the proper cooldown key for this Command under the provided
	 * {@link io.github.readonly.command.event.CommandEvent CommandEvent}.
	 *
	 * @param event
	 *            The CommandEvent to generate the cooldown for.
	 *
	 * @return A String key to use when applying a cooldown.
	 */
	public String getCooldownKey(CommandEvent event)
	{
		switch (cooldownScope)
		{
		case USER:
			return cooldownScope.genKey(name, event.getAuthor().getIdLong());
		case USER_GUILD:
			return event.getGuild() != null ? cooldownScope.genKey(name, event.getAuthor().getIdLong(), event.getGuild().getIdLong()) : CooldownScope.USER_CHANNEL.genKey(name, event.getAuthor().getIdLong(), event.getChannel().getIdLong());
		case USER_CHANNEL:
			return cooldownScope.genKey(name, event.getAuthor().getIdLong(), event.getChannel().getIdLong());
		case GUILD:
			return event.getGuild() != null ? cooldownScope.genKey(name, event.getGuild().getIdLong()) : CooldownScope.CHANNEL.genKey(name, event.getChannel().getIdLong());
		case CHANNEL:
			return cooldownScope.genKey(name, event.getChannel().getIdLong());
		case SHARD:
			return event.getJDA().getShardInfo() != null ? cooldownScope.genKey(name, event.getJDA().getShardInfo().getShardId()) : CooldownScope.GLOBAL.genKey(name, 0);
		case USER_SHARD:
			return event.getJDA().getShardInfo() != null ? cooldownScope.genKey(name, event.getAuthor().getIdLong(), event.getJDA().getShardInfo().getShardId()) : CooldownScope.USER.genKey(name, event.getAuthor().getIdLong());
		case GLOBAL:
			return cooldownScope.genKey(name, 0);
		default:
			return "";
		}
	}

	/**
	 * Gets an error message for this Command under the provided {@link io.github.readonly.command.event.CommandEvent
	 * CommanEvent}.
	 *
	 * @param event
	 *            The CommandEvent to generate the error message for.
	 * @param remaining
	 *            The remaining number of seconds a command is on cooldown for.
	 *
	 * @return A String error message for this command if {@code remaining > 0}, else {@code null}.
	 */
	public String getCooldownError(CommandEvent event, int remaining)
	{
		if (remaining <= 0)
		{
			return null;
		}
		String front = event.getClient().getWarning() + " That command is on cooldown for " + remaining + " more seconds";
		if (cooldownScope.equals(CooldownScope.USER))
		{
			return front + "!";
		} else if (cooldownScope.equals(CooldownScope.USER_GUILD) && (event.getGuild() == null))
		{
			return front + " " + CooldownScope.USER_CHANNEL.getErrorSpecification() + "!";
		} else if (cooldownScope.equals(CooldownScope.GUILD) && (event.getGuild() == null))
		{
			return front + " " + CooldownScope.CHANNEL.getErrorSpecification() + "!";
		} else
		{
			return front + " " + cooldownScope.getErrorSpecification() + "!";
		}
	}

	/**
	 * To be used in {@link io.github.readonly.command.Command Command}s as a means of organizing commands into
	 * "Categories" as well as terminate command usage when the calling
	 * {@link io.github.readonly.command.event.CommandEvent CommandEvent} doesn't meet certain requirements.
	 */
	public static class Category
	{

		private final String name;

		private final String failResponse;

		private final Predicate<CommandEvent> predicate;

		private final boolean ownerOnly;

		/**
		 * A Command Category containing a name.
		 *
		 * @param name
		 *            The name of the Category
		 */
		public Category(String name)
		{
			this.name = name;
			this.failResponse = null;
			this.predicate = null;
			this.ownerOnly = false;
		}

		public Category(String name, boolean ownerOnly)
		{
			this.name = name;
			this.failResponse = null;
			this.predicate = null;
			this.ownerOnly = ownerOnly;
		}

		/**
		 * A Command Category containing a name and a {@link java.util.function.Predicate}.
		 * <p>
		 * The command will be terminated if
		 * {@link io.github.readonly.command.Command.Category#test(com.readonlydev.command.CommandEvent)} returns
		 * {@code false}.
		 *
		 * @param name
		 *            The name of the Category
		 * @param predicate
		 *            The Category predicate to test
		 */
		public Category(String name, Predicate<CommandEvent> predicate)
		{
			this.name = name;
			this.failResponse = null;
			this.predicate = predicate;
			this.ownerOnly = false;
		}

		public Category(String name, Predicate<CommandEvent> predicate, boolean ownerOnly)
		{
			this.name = name;
			this.failResponse = null;
			this.predicate = predicate;
			this.ownerOnly = ownerOnly;
		}

		/**
		 * A Command Category containing a name, a {@link java.util.function.Predicate}, and a failure response.
		 * <p>
		 * The command will be terminated if
		 * {@link io.github.readonly.command.Command.Category#test(com.readonlydev.command.CommandEvent)} returns
		 * {@code false}, and the failure response will be sent.
		 *
		 * @param name
		 *            The name of the Category
		 * @param failResponse
		 *            The response if the test fails
		 * @param predicate
		 *            The Category predicate to test
		 */
		public Category(String name, String failResponse, Predicate<CommandEvent> predicate)
		{
			this.name = name;
			this.failResponse = failResponse;
			this.predicate = predicate;
			this.ownerOnly = false;
		}

		public Category(String name, String failResponse, Predicate<CommandEvent> predicate, boolean ownerOnly)
		{
			this.name = name;
			this.failResponse = failResponse;
			this.predicate = predicate;
			this.ownerOnly = ownerOnly;
		}

		/**
		 * Gets the name of the Category.
		 *
		 * @return The name of the Category
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * Gets the failure response of the Category.
		 *
		 * @return The failure response of the Category
		 */
		public String getFailureResponse()
		{
			return failResponse;
		}

		/**
		 * Checks if this command is OwnerOnly
		 *
		 * @return returns true only, and only if ownerOnly is true, otherwise returns false
		 */
		public boolean isOwnerOnly()
		{
			return ownerOnly;
		}

		/**
		 * Runs a test of the provided {@link java.util.function.Predicate}. Does not support SlashCommands.
		 *
		 * @param event
		 *            The {@link io.github.readonly.command.event.CommandEvent CommandEvent} that was called when this
		 *            method is invoked
		 *
		 * @return {@code true} if the Predicate was not set, was set as null, or was tested and returned true,
		 *         otherwise returns {@code false}
		 */
		public boolean test(CommandEvent event)
		{
			return (predicate == null) || predicate.test(event);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof Category))
			{
				return false;
			}
			Category other = (Category) obj;
			return Objects.equals(name, other.name) && Objects.equals(predicate, other.predicate) && Objects.equals(failResponse, other.failResponse);
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = (17 * hash) + Objects.hashCode(this.name);
			hash = (17 * hash) + Objects.hashCode(this.failResponse);
			hash = (17 * hash) + Objects.hashCode(this.predicate);
			return hash;
		}
	}
}
