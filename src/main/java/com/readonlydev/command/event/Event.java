package com.readonlydev.command.event;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public interface Event<E>
{
	public E getEvent();

	public User getAuthor();

	public MessageChannel getChannel();
}
