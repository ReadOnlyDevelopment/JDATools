package io.github.readonly.common.event;

import com.google.common.eventbus.EventBus;

public final class EventHandler
{
	private static EventHandler _instance;

	public static EventHandler instance()
	{
		if(EventHandler._instance == null)
		{
			EventHandler._instance = new EventHandler();
		}

		return EventHandler._instance;
	}

	private EventBus eventBus = new EventBus("JDATools EventBus");

	public void register(Object object)
	{
		this.eventBus.register(object);
	}

	public <T extends JDAToolsEvent> void post(T event)
	{
		this.eventBus.post(event);
	}

	private EventHandler() {}
}
