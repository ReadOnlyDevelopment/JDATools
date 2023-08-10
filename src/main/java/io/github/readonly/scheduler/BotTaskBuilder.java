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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.github.readonly.api.BotContainer;
import io.github.readonly.api.scheduler.ITask;

public class BotTaskBuilder implements ITask.Builder {

	private final BotScheduler scheduler;
	private Consumer<ITask> consumer;
	private ScheduledTask.TaskSynchronicity syncType;
	private String name;
	private long delay;
	private long interval;

	public BotTaskBuilder(BotScheduler scheduler) {
		this.scheduler = scheduler;
		this.syncType = ScheduledTask.TaskSynchronicity.SYNCHRONOUS;
	}

	@Override
	public ITask.Builder async() {
		this.syncType = ScheduledTask.TaskSynchronicity.ASYNCHRONOUS;
		return this;
	}

	@Override
	public ITask.Builder execute(Consumer<ITask> executor) {
		this.consumer = checkNotNull(executor, "executor");
		return this;
	}

	@Override
	public ITask.Builder delay(long delay, TimeUnit unit) {
		checkArgument(delay >= 0, "Delay cannot be negative");
		this.delay = checkNotNull(unit, "unit").toNanos(delay);
		return this;
	}

	@Override
	public ITask.Builder interval(long interval, TimeUnit unit) {
		checkArgument(interval >= 0, "Interval cannot be negative");
		this.interval = checkNotNull(unit, "unit").toNanos(interval);
		return this;
	}

	@Override
	public ITask.Builder name(String name) {
		checkArgument(checkNotNull(name, "name").length() > 0, "Name cannot be empty");
		this.name = name;
		return this;
	}

	@Override
	public ITask submit(BotContainer instance) {
		BotContainer pluginContainer = this.scheduler.checkBotInstance(instance);
		checkState(this.consumer != null, "Runnable task not set");
		String name;
		if (this.name == null) {
			name = this.scheduler.getNameFor(pluginContainer, this.syncType);
		} else {
			name = this.name;
		}
		long delay = this.delay;
		long interval = this.interval;
		ScheduledTask task = new ScheduledTask(this.syncType, this.consumer, name, delay, interval, pluginContainer);
		this.scheduler.submit(task);
		return task;
	}

	@Override
	public ITask.Builder from(ITask value) {
		this.syncType = value.isAsynchronous() ? ScheduledTask.TaskSynchronicity.ASYNCHRONOUS : ScheduledTask.TaskSynchronicity.SYNCHRONOUS;
		this.consumer = value.getConsumer();
		this.interval = value.getInterval();
		this.delay = value.getDelay();
		this.name = value.getName();
		return this;
	}

	@Override
	public ITask.Builder reset() {
		this.syncType = ScheduledTask.TaskSynchronicity.SYNCHRONOUS;
		this.consumer = null;
		this.interval = 0;
		this.delay = 0;
		this.name = null;
		return this;
	}
}
