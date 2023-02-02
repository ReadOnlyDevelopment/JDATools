package io.github.readonly.common.util.async.threads;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import io.github.readonly.common.util.holding.objects.Switch;

public class ScheduledTaskProcessor
{
	private transient final Map<Long, List<Runnable>>	TASKS;
	private transient final Consumer<Runnable>			onExpired;
	private transient boolean							updated	= false;

	public ScheduledTaskProcessor(Consumer<Runnable> onExpired, String name)
	{
		this.onExpired = onExpired;
		TASKS = new ConcurrentHashMap<>();

		Thread thread = new Thread(this::threadCode, name);
		thread.setDaemon(true);
		thread.start();
	}

	public ScheduledTaskProcessor(String name)
	{
		this(r -> new Thread(r, "Scheduled Task Thread").start(), name);
	}

	public Runnable addTask(long milis, Runnable onExpire)
	{
		Objects.requireNonNull(onExpire);
		TASKS.computeIfAbsent(milis, k -> new ArrayList<>()).add(onExpire);
		updated = true;
		synchronized (this)
		{
			notify();
		}

		return () ->
		{
			Switch r = new Switch();
			TASKS.computeIfPresent(milis, (k, v) ->
			{
				r.set(v.remove(onExpire));
				return v;
			});

			if (r.is(true))
			{
				updated = true;
				synchronized (this)
				{
					notify();
				}
			}
		};
	}

	private void threadCode()
	{
		//noinspection InfiniteLoopStatement
		while (true)
		{
			if (TASKS.isEmpty())
			{
				try
				{
					synchronized (this)
					{
						wait();
						updated = false;
					}
				} catch (InterruptedException ignored)
				{
				}
			}

			Entry<Long, List<Runnable>> firstEntry = TASKS.entrySet().stream().sorted(Comparator.comparingLong(Entry::getKey)).findFirst().orElse(null);

			long timeout = firstEntry.getKey() - System.currentTimeMillis();
			if (timeout > 0)
			{
				synchronized (this)
				{
					try
					{
						wait(timeout);
					} catch (InterruptedException ignored)
					{
					}
				}
			}

			if (!updated)
			{
				TASKS.remove(firstEntry.getKey());
				List<Runnable> runnables = firstEntry.getValue();
				runnables.remove(null);
				runnables.forEach(onExpired);
			}
			else {
				updated = false; //and the loop will restart and resolve it
			}
		}
	}
}
