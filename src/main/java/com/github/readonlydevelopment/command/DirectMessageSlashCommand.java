package com.github.readonlydevelopment.command;

import com.github.readonlydevelopment.command.event.SlashCommandEvent;

import net.dv8tion.jda.api.entities.channel.ChannelType;

public abstract class DirectMessageSlashCommand extends SlashCommand
{
	private final String descriptionPrefix = "[DM ONLY] ";

	DirectMessageSlashCommand()
	{
		this.directMessagesAllowed();
	}

	@Override
	protected void description(String description)
	{
		this.help = descriptionPrefix + description;
	}

	@Override
	void run(SlashCommandEvent event)
	{
		if(!event.getChannelType().equals(ChannelType.PRIVATE))
		{
			terminate(event, "This command can only be used in DM's");
		}

		super.run(event);
	}
}
