package io.github.readonly.common.event.jda;

import net.dv8tion.jda.api.events.session.ReadyEvent;

public class PostReadyEvent extends JDAEvent<ReadyEvent>
{
	public PostReadyEvent(ReadyEvent event)
	{
		super(event);
	}
}
