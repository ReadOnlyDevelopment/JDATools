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

public class SyncScheduler extends AbstractScheduler {

	SyncScheduler() {
		super(ScheduledTask.TaskSynchronicity.SYNCHRONOUS);
	}

	/**
	 * The hook to update the Ticks known by the SyncScheduler.
	 */
	void tick() {
		this.runTick();
	}

	@Override
	protected long getTimestamp(ScheduledTask task) {
		if (task.getState() == ScheduledTask.ScheduledTaskState.WAITING) {
			return super.getTimestamp(task);
		} else if (task.getState().isActive) {
			return super.getTimestamp(task);
		}
		return 0L;
	}

	@Override
	protected void executeTaskRunnable(ScheduledTask task, Runnable runnable) {
		runnable.run();
	}
}
