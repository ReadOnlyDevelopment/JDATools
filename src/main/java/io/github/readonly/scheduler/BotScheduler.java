/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

import io.github.readonly.api.BotContainer;
import io.github.readonly.api.scheduler.Scheduler;
import io.github.readonly.api.scheduler.ITask;
import io.github.readonly.api.scheduler.TaskService;
import io.github.readonly.common.util.Functional;
import io.github.readonly.discordbot.DiscordBot;

public class BotScheduler implements Scheduler {

	public static final int TICK_DURATION_MS = 50;
	public static final long TICK_DURATION_NS = TimeUnit.NANOSECONDS.convert(TICK_DURATION_MS, TimeUnit.MILLISECONDS);
	private final AsyncScheduler asyncScheduler = new AsyncScheduler();
	private final SyncScheduler syncScheduler = new SyncScheduler();

	public static BotScheduler instance = new BotScheduler();

	@Override
	public ITask.Builder createTaskBuilder() {
		return new BotTaskBuilder(this);
	}

	@Override
	public Optional<ITask> getTaskById(UUID id) {
		Optional<ITask> optTask = this.syncScheduler.getTask(id);
		if (optTask.isPresent()) {
			return optTask;
		}
		return this.asyncScheduler.getTask(id);
	}

	@Override
	public Set<ITask> getTasksByName(String pattern) {
		Pattern searchPattern = Pattern.compile(checkNotNull(pattern, "pattern"));
		Set<ITask> matchingTasks = this.getScheduledTasks();

		Iterator<ITask> it = matchingTasks.iterator();
		while (it.hasNext()) {
			Matcher matcher = searchPattern.matcher(it.next().getName());
			if (!matcher.matches()) {
				it.remove();
			}
		}

		return matchingTasks;
	}

	@Override
	public Set<ITask> getScheduledTasks() {
		Set<ITask> allTasks = Sets.newHashSet();
		allTasks.addAll(this.asyncScheduler.getScheduledTasks());
		allTasks.addAll(this.syncScheduler.getScheduledTasks());
		return allTasks;
	}

	@Override
	public Set<ITask> getScheduledTasks(boolean async) {
		if (async) {
			return this.asyncScheduler.getScheduledTasks();
		}
		return this.syncScheduler.getScheduledTasks();
	}

	@Override
	public Set<ITask> getScheduledTasks(Object plugin) {
		String testOwnerId = checkBotInstance(plugin).getId();

		Set<ITask> allTasks = this.getScheduledTasks();
		Iterator<ITask> it = allTasks.iterator();

		while (it.hasNext()) {
			String taskOwnerId = it.next().getOwner().getId();
			if (!testOwnerId.equals(taskOwnerId)) {
				it.remove();
			}
		}

		return allTasks;
	}

	@Override
	public int getPreferredTickInterval() {
		return TICK_DURATION_MS;
	}

	@Override
	public TaskService createSyncExecutor(Object plugin) {
		return new TaskExecutorService(() -> createTaskBuilder(), this.syncScheduler, checkBotInstance(plugin));
	}

	@Override
	public TaskService createAsyncExecutor(Object plugin) {
		return new TaskExecutorService(() -> createTaskBuilder().async(), this.asyncScheduler, checkBotInstance(plugin));
	}

	/**
	 * Check the object is a plugin instance.
	 *
	 * @param plugin The plugin to check
	 * @return The plugin container of the plugin instance
	 * @throws NullPointerException If the passed in plugin instance is null
	 * @throws IllegalArgumentException If the object is not a plugin instance
	 */
	BotContainer checkBotInstance(Object plugin) {
		Optional<BotContainer> optPlugin = plugin instanceof DiscordBot ? Optional.of((BotContainer)plugin) : Optional.empty();
		checkArgument(optPlugin.isPresent(), "Provided object is not a plugin instance");
		return optPlugin.get();
	}

	private AbstractScheduler getDelegate(ITask task) {
		if (task.isAsynchronous()) {
			return this.asyncScheduler;
		}
		return this.syncScheduler;
	}

	private AbstractScheduler getDelegate(ScheduledTask.TaskSynchronicity syncType) {
		if (syncType == ScheduledTask.TaskSynchronicity.ASYNCHRONOUS) {
			return this.asyncScheduler;
		}
		return this.syncScheduler;
	}

	String getNameFor(BotContainer plugin, ScheduledTask.TaskSynchronicity syncType) {
		return getDelegate(syncType).nextName(plugin);
	}

	void submit(ScheduledTask task) {
		getDelegate(task).addTask(task);
	}

	/**
	 * Ticks the synchronous scheduler.
	 */
	public void tickSyncScheduler() {
		this.syncScheduler.tick();
	}

	public <T> CompletableFuture<T> submitAsyncTask(Callable<T> callable) {
		return Functional.asyncFailableFuture(callable, this.asyncScheduler.getExecutor());
	}

	//	public Future<?> callSync(Runnable runnable) {
	//		return callSync(() -> {
	//			runnable.run();
	//			return null;
	//		});
	//	}
	//
	//	public <V> Future<V> callSync(Callable<V> callable) {
	//		final FutureTask<V> runnable = new FutureTask<>(callable);
	//		createTaskBuilder().execute(runnable).submit(SpongeImpl.getPlugin());
	//		return runnable;
	//	}
}
