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

package io.github.readonly.scheduler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.common.base.MoreObjects;

import io.github.readonly.api.BotContainer;
import io.github.readonly.api.scheduler.ITask;

/**
 * An internal representation of a {@link ITask} created by a plugin.
 */
public class ScheduledTask implements ITask
{

	final long						offset;			//nanoseconds or ticks
	final long						period;			//nanoseconds or ticks
	private final BotContainer		owner;
	private final Consumer<ITask>	consumer;
	private long					timestamp;

	// As this state is going to be read by multiple threads
	// potentially very quickly, marking this a volatile will
	// give the JVM a hint to not cache this
	private volatile ScheduledTaskState	state;
	private final UUID					id;
	private final String				name;
	private final TaskSynchronicity		syncType;
	private final String				stringRepresentation;

	// Internal Task state. Not for user-service use.
	public enum ScheduledTaskState
	{
		/**
		 * Never ran before, waiting for the offset to pass.
		 */
		WAITING(false),
		/**
		 * In the process of switching to the execution state.
		 */
		SWITCHING(true),
		/**
		 * Has ran, and will continue to unless removed from the task map.
		 */
		RUNNING(true),
		/**
		 * Is being executed.
		 */
		EXECUTING(true),
		/**
		 * Task cancelled, scheduled to be removed from the task map.
		 */
		CANCELED(false);

		public final boolean isActive;

		ScheduledTaskState(boolean active)
		{
			this.isActive = active;
		}
	}

	ScheduledTask(TaskSynchronicity syncType, Consumer<ITask> task, String taskName, long delay, long interval, BotContainer BotContainer)
	{
		// All tasks begin waiting.
		this.setState(ScheduledTaskState.WAITING);
		this.offset = delay;
		this.period = interval;
		this.owner = BotContainer;
		this.consumer = task;
		this.id = UUID.randomUUID();
		this.name = taskName;
		this.syncType = syncType;

		this.stringRepresentation = MoreObjects.toStringHelper(this).add("name", this.name).add("delay", this.offset).add("interval", this.period).add("owner", this.owner).add("id", this.id).add("isAsync", this.isAsynchronous()).toString();
	}

	@Override
	public BotContainer getOwner()
	{
		return this.owner;
	}

	@Override
	public long getDelay()
	{
		return TimeUnit.NANOSECONDS.toMillis(this.offset);
	}

	@Override
	public long getInterval()
	{
		return TimeUnit.NANOSECONDS.toMillis(this.period);
	}

	@Override
	public boolean cancel()
	{
		boolean success = false;
		if ((getState() != ScheduledTask.ScheduledTaskState.RUNNING) && (getState() != ScheduledTaskState.EXECUTING))
		{
			success = true;
		}
		this.setState(ScheduledTask.ScheduledTaskState.CANCELED);
		return success;
	}

	@Override
	public Consumer<ITask> getConsumer()
	{
		return this.consumer;
	}

	@Override
	public UUID getUniqueId()
	{
		return this.id;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public boolean isAsynchronous()
	{
		return this.syncType == TaskSynchronicity.ASYNCHRONOUS;
	}

	long getTimestamp()
	{
		return this.timestamp;
	}

	/**
	 * Returns a timestamp after which the next execution will take place. Should only be compared to
	 * {@link AbstractScheduler#getTimestamp(ScheduledTask)}.
	 *
	 * @return The next execution timestamp
	 */
	long nextExecutionTimestamp()
	{
		if (this.state.isActive)
		{
			return this.timestamp + this.period;
		}
		return this.timestamp + this.offset;
	}

	void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	ScheduledTaskState getState()
	{
		return this.state;
	}

	void setState(ScheduledTaskState state)
	{
		if (this.state != ScheduledTaskState.CANCELED)
		{
			this.state = state;
		}
	}

	@Override
	public String toString()
	{
		return this.stringRepresentation;
	}

	public enum TaskSynchronicity
	{
		SYNCHRONOUS,
		ASYNCHRONOUS
	}
}
