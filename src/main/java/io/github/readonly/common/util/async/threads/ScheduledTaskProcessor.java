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
