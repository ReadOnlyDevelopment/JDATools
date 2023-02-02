package io.github.readonly.common.event.jda;

import io.github.readonly.common.event.JDAToolsEvent;
import net.dv8tion.jda.api.events.Event;

public class JDAEvent<T extends Event> implements JDAToolsEvent
{
	private T event;

	public JDAEvent(T event)
	{
		this.event = event;
	}

	public T getEvent()
	{
		return event;
	}
}