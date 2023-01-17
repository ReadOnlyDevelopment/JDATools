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

package com.readonlydev.command.slash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.readonlydev.api.CooldownScope;
import com.readonlydev.command.Command;
import com.readonlydev.command.client.Client;
import com.readonlydev.command.client.ClientBuilder;
import com.readonlydev.command.event.CommandEvent;
import com.readonlydev.common.utils.SafeIdUtil;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

/**
 * <pre><code> public class ExampleCmd extends Command {
 *
 *      public ExampleCmd() {
 *          this.name = "example";
 *          this.help = "gives an example of commands do";
 *      }
 *
 *      {@literal @Override}
 *      protected void execute(SlashCommandEvent event) {
 *          event.reply("Hey look! This would be the bot's reply if this was a command!").queue();
 *      }
 *
 * }</code></pre>
 *
 * Execution is with the provision of the SlashCommandEvent is performed in two steps:
 * <ul>
 * <li>{@link SlashCommand#run(SlashCommandEvent) run} - The command runs through a series of conditionals,
 * automatically terminating the command instance if one is not met, and possibly providing an error response.</li>
 *
 * <li>{@link SlashCommand#execute(SlashCommandEvent) execute} - The command, now being cleared to run, executes and
 * performs whatever lies in the abstract body method.</li>
 * </ul>
 */
public abstract class SlashCommand extends Command
{
	protected List<Long> requiredRoles = new LinkedList<>();

	/**
	 * Localization of slash command name. Allows discord to change the language of the name of slash commands in the
	 * client.<br>
	 * Example:<br>
	 *
	 * <pre>
	 * <code>
	 *     public Command() {
	 *          this.name = "help"
	 *          this.nameLocalization = Map.of(DiscordLocale.GERMAN, "hilfe", DiscordLocale.RUSSIAN, "помощь");
	 *     }
	 *</code>
	 * </pre>
	 */
	@Getter
	protected Map<DiscordLocale, String> nameLocalization = new HashMap<>();

	/**
	 * Localization of slash command description. Allows discord to change the language of the description of slash
	 * commands in the client.<br>
	 * Example:<br>
	 *
	 * <pre>
	 * <code>
	 *     public Command() {
	 *          this.description = "all commands"
	 *          this.descriptionLocalization = Map.of(DiscordLocale.GERMAN, "alle Befehle", DiscordLocale.RUSSIAN, "все команды");
	 *     }
	 *</code>
	 * </pre>
	 */
	@Getter
	protected Map<DiscordLocale, String> descriptionLocalization = new HashMap<>();

	protected String guildId = null;

	@Getter
	@Setter
	public CommandData commandData;

	/**
	 * The child commands of the command. These are used in the format {@code /<parent name>
	 * <child name>}. This is synonymous with sub commands. Additionally, sub-commands cannot have children.<br>
	 */
	protected SlashCommand[] children = new SlashCommand[0];

	/**
	 * The subcommand/child group this is associated with. Will be in format
	 * {@code /<parent name> <subcommandGroup name> <subcommand name>}.
	 *
	 * <b>This only works in a child/subcommand.</b>
	 *
	 * To instantiate: <code>{@literal new SubcommandGroupData(name, description)}</code><br>
	 * It's important the instantiations are the same across children if you intend to keep them in the same group.
	 *
	 * Can be null, and it will not be assigned to a group.
	 */
	@Getter
	protected SubcommandGroupData subcommandGroup = null;

	/**
	 * An array list of OptionData.
	 *
	 * <b>This is incompatible with children. You cannot have a child AND options.</b>
	 *
	 * This is to specify different options for arguments and the stuff.
	 *
	 * For example, to add an argument for "input", you can do this:<br>
	 *
	 * <pre><code>
	 *     OptionData data = new OptionData(OptionType.STRING, "input", "The input for the command").setRequired(true);
	 *    {@literal List<OptionData> dataList = new ArrayList<>();}
	 *     dataList.add(data);
	 *     this.options = dataList;</code></pre>
	 */
	@Getter
	protected List<OptionData> options = new ArrayList<>();

	/**
	 * The main body method of a {@link SlashCommand SlashCommand}. <br>
	 * This is the "response" for a successful {@link SlashCommand#run(SlashCommandEvent) #run(CommandEvent)}.
	 *
	 * @param event
	 *            The {@link SlashCommandEvent SlashCommandEvent} that triggered this Command
	 */
	protected abstract void execute(SlashCommandEvent event);

	/**
	 * This body is executed when an auto-complete event is received. This only ever gets executed if an auto-complete
	 * {@link #options option} is set.
	 *
	 * @param event
	 *            The event to handle.
	 *
	 * @see OptionData#setAutoComplete(boolean)
	 */
	public void onAutoComplete(CommandAutoCompleteInteractionEvent event)
	{
	}

	/**
	 * The main body method of a {@link com.readonlydev.command.Command Command}. <br>
	 * This is the "response" for a successful {@link com.readonlydev.command.Command#run(CommandEvent)
	 * #run(CommandEvent)}. <b> Because this is a SlashCommand, this is called, but does nothing. You can still override
	 * this if you want to have a separate response for normal [prefix][name]. Keep in mind you must add it as a Command
	 * via {@link ClientBuilder#addCommand(Command)} for it to work properly. </b>
	 *
	 * @param event
	 *            The {@link com.readonlydev.command.event.CommandEvent CommandEvent} that triggered this Command
	 */
	@Override
	protected void execute(CommandEvent event)
	{
	}

	/**
	 * Runs checks for the {@link SlashCommand SlashCommand} with the given {@link SlashCommandEvent SlashCommandEvent}
	 * that called it. <br>
	 * Will terminate, and possibly respond with a failure message, if any checks fail.
	 *
	 * @param event
	 *            The SlashCommandEvent that triggered this Command
	 */
	public final void run(SlashCommandEvent event)
	{
		// set the client
		Client client = event.getClient();

		// owner check
		if (ownerCommand && !(isOwner(event, client)))
		{
			terminate(event, "Only an owner may run this command. Sorry.", client);
			return;
		}

		// is allowed check
		if ((event.getChannelType() == ChannelType.TEXT) && !isAllowed(event.getChannel().asTextChannel()))
		{
			terminate(event, "That command cannot be used in this channel!", client);
			return;
		}

		if (!requiredRoles.isEmpty())
		{
			if (event.getMember().getRoles().stream().map(Role::getIdLong).noneMatch(requiredRoles.stream().collect(Collectors.toSet())::contains))
			{
				terminate(event, client.getError() + " You do not have any of the required Roles to perform this command!", client);
				return;
			}
		}

		// availability check
		if (event.getChannelType() != ChannelType.PRIVATE)
		{
			// user perms
			for (Permission p : userPermissions)
			{
				// Member will never be null because this is only ran in a
				// server (text channel)
				if (event.getMember() == null)
				{
					continue;
				}

				if (p.isChannel())
				{
					if (!event.getMember().hasPermission(event.getGuildChannel(), p))
					{
						terminate(event, String.format(userMissingPermMessage, client.getError(), p.getName(), "channel"), client);
						return;
					}
				} else
				{
					if (!event.getMember().hasPermission(p))
					{
						terminate(event, String.format(userMissingPermMessage, client.getError(), p.getName(), "server"), client);
						return;
					}
				}
			}

			// bot perms
			for (Permission p : botPermissions)
			{
				// We can ignore this permission because bots can reply with
				// embeds even without either of these perms.
				// The only thing stopping them is the user's ability to use
				// Application Commands.
				// It's extremely dumb, but what more can you do.
				if ((p == Permission.VIEW_CHANNEL) || (p == Permission.MESSAGE_EMBED_LINKS))
				{
					continue;
				}

				Member selfMember = event.getGuild() == null ? null : event.getGuild().getSelfMember();
				if (p.isChannel())
				{
					if (p.isVoice())
					{
						GuildVoiceState	gvc	= event.getMember().getVoiceState();
						AudioChannel	vc	= gvc == null ? null : gvc.getChannel();
						if (vc == null)
						{
							terminate(event, client.getError() + " You must be in a voice channel to use that!", client);
							return;
						} else if (!selfMember.hasPermission(vc, p))
						{
							terminate(event, String.format(botMissingPermMessage, client.getError(), p.getName(), "voice channel"), client);
							return;
						}
					} else
					{
						if (!selfMember.hasPermission(event.getGuildChannel(), p))
						{
							terminate(event, String.format(botMissingPermMessage, client.getError(), p.getName(), "channel"), client);
							return;
						}
					}
				} else
				{
					if (!selfMember.hasPermission(p))
					{
						terminate(event, String.format(botMissingPermMessage, client.getError(), p.getName(), "server"), client);
						return;
					}
				}
			}

			// nsfw check
			if (nsfwOnly && (event.getChannelType() == ChannelType.TEXT) && !event.getGuildChannel().asTextChannel().isNSFW())
			{
				terminate(event, "This command may only be used in NSFW text channels!", client);
				return;
			}
		} else if (guildOnly)
		{
			terminate(event, client.getError() + " This command cannot be used in direct messages", client);
			return;
		}

		// cooldown check, ignoring owner
		if ((cooldown > 0) && !(isOwner(event, client)))
		{
			String	key			= getCooldownKey(event);
			int		remaining	= client.getRemainingCooldown(key);
			if (remaining > 0)
			{
				terminate(event, getCooldownError(event, remaining, client), client);
				return;
			} else
			{
				client.applyCooldown(key, cooldown);
			}
		}

		// run
		try
		{
			execute(event);
		} catch (Throwable t)
		{
			if (client.getListener() != null)
			{
				client.getListener().onSlashCommandException(event, this, t);
				return;
			}
			// otherwise we rethrow
			throw t;
		}

		if (client.getListener() != null)
		{
			client.getListener().onCompletedSlashCommand(event, this);
		}
	}

	/**
	 * Tests whether or not the {@link net.dv8tion.jda.api.entities.User User} who triggered this event is an owner of
	 * the bot.
	 *
	 * @param event
	 *            the event that triggered the command
	 * @param client
	 *            the command client for checking stuff
	 *
	 * @return {@code true} if the User is the Owner, else {@code false}
	 */
	public boolean isOwner(SlashCommandEvent event, Client client)
	{
		if (event.getUser().getId().equals(client.getOwnerId()))
		{
			return true;
		}
		if (client.getCoOwnerIds() == null)
		{
			return false;
		}
		for (String id : client.getCoOwnerIds())
		{
			if (id.equals(event.getUser().getId()))
			{
				return true;
			}
		}
		return false;
	}

	protected void addRequiredRoles(long... roleIds)
	{
		for (long id : roleIds)
		{
			this.requiredRoles.add(id);
		}
	}

	/**
	 * Builds and sets the CommandData for the SlashCommand upsert.
	 */
	public void buildCommandData()
	{
		// Make the command data
		SlashCommandData data = Commands.slash(getName(), getHelp());
		if (!getOptions().isEmpty())
		{
			data.addOptions(getOptions());
		}
		// Check for children
		if (children.length != 0)
		{
			// Temporary map for easy group storage
			Map<String, SubcommandGroupData> groupData = new HashMap<>();
			for (SlashCommand child : children)
			{
				// Create subcommand data
				SubcommandData subcommandData = new SubcommandData(child.getName(), child.getHelp());
				// Add options
				if (!child.getOptions().isEmpty())
				{
					subcommandData.addOptions(child.getOptions());
				}

				// Check child name localizations
				if (!child.getNameLocalization().isEmpty())
				{
					// Add localizations
					subcommandData.setNameLocalizations(child.getNameLocalization());
				}
				// Check child description localizations
				if (!child.getDescriptionLocalization().isEmpty())
				{
					// Add localizations
					subcommandData.setDescriptionLocalizations(child.getDescriptionLocalization());
				}

				// If there's a subcommand group
				if (child.getSubcommandGroup() != null)
				{
					SubcommandGroupData	group	= child.getSubcommandGroup();
					SubcommandGroupData	newData	= groupData.getOrDefault(group.getName(), group).addSubcommands(subcommandData);

					groupData.put(group.getName(), newData);
				}
				// Just add to the command
				else
				{
					data.addSubcommands(subcommandData);
				}
			}
			if (!groupData.isEmpty())
			{
				data.addSubcommandGroups(groupData.values());
			}
		}

		if (this.getUserPermissions() == null)
		{
			data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
		} else
		{
			data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(this.getUserPermissions()));
		}

		data.setGuildOnly(this.guildOnly);

		this.setCommandData(data);
	}

	public void setGuildId(long guildId)
	{
		this.guildId = SafeIdUtil.safeConvert(guildId);
	}

	public boolean isGuildRestricted()
	{
		return guildId != null;
	}

	public boolean isGlobalCommand()
	{
		return guildId == null;
	}

	/**
	 * Gets the {@link SlashCommand#children Command.children} for the Command.
	 *
	 * @return The children for the Command
	 */
	@Override
	public SlashCommand[] getChildren()
	{
		return children;
	}

	private void terminate(SlashCommandEvent event, String message, Client client)
	{
		if (message != null)
		{
			event.reply(message).setEphemeral(true).queue();
		}
		if (client.getListener() != null)
		{
			client.getListener().onTerminatedSlashCommand(event, this);
		}
	}

	/**
	 * Gets the proper cooldown key for this Command under the provided {@link SlashCommandEvent SlashCommandEvent}.
	 *
	 * @param event
	 *            The CommandEvent to generate the cooldown for.
	 *
	 * @return A String key to use when applying a cooldown.
	 */
	public String getCooldownKey(SlashCommandEvent event)
	{
		switch (cooldownScope)
		{
		case USER:
			return cooldownScope.genKey(name, event.getUser().getIdLong());
		case USER_GUILD:
			return event.getGuild() != null ? cooldownScope.genKey(name, event.getUser().getIdLong(), event.getGuild().getIdLong()) : CooldownScope.USER_CHANNEL.genKey(name, event.getUser().getIdLong(), event.getChannel().getIdLong());
		case USER_CHANNEL:
			return cooldownScope.genKey(name, event.getUser().getIdLong(), event.getChannel().getIdLong());
		case GUILD:
			return event.getGuild() != null ? cooldownScope.genKey(name, event.getGuild().getIdLong()) : CooldownScope.CHANNEL.genKey(name, event.getChannel().getIdLong());
		case CHANNEL:
			return cooldownScope.genKey(name, event.getChannel().getIdLong());
		case SHARD:
			event.getJDA().getShardInfo();
			return cooldownScope.genKey(name, event.getJDA().getShardInfo().getShardId());
		case USER_SHARD:
			event.getJDA().getShardInfo();
			return cooldownScope.genKey(name, event.getUser().getIdLong(), event.getJDA().getShardInfo().getShardId());
		case GLOBAL:
			return cooldownScope.genKey(name, 0);
		default:
			return "";
		}
	}

	/**
	 * Gets an error message for this Command under the provided {@link SlashCommandEvent SlashCommandEvent}.
	 *
	 * @param event
	 *            The CommandEvent to generate the error message for.
	 * @param remaining
	 *            The remaining number of seconds a command is on cooldown for.
	 * @param client
	 *            The Client for checking stuff
	 *
	 * @return A String error message for this command if {@code remaining > 0}, else {@code null}.
	 */
	public String getCooldownError(SlashCommandEvent event, int remaining, Client client)
	{
		if (remaining <= 0)
		{
			return null;
		}
		String front = client.getWarning() + " That command is on cooldown for " + remaining + " more seconds";
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
}
