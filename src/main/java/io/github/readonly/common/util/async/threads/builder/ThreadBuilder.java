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

package io.github.readonly.common.util.async.threads.builder;

import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Function;

public class ThreadBuilder implements ThreadFactory, Function<Runnable, Thread> {

	private transient Consumer<Thread> builder = thread -> {};

	@Override
	public Thread apply(Runnable runnable) {
		return build(runnable);
	}

	@Override
	public Thread newThread(Runnable runnable) {
		return build(runnable);
	}

	public Thread build(Runnable runnable) {
		Thread thread = new Thread(runnable);
		builder.accept(thread);
		return thread;
	}


	public ThreadBuilder setContextClassLoader(ClassLoader cl) {
		builder = builder.andThen(thread -> thread.setContextClassLoader(cl));
		return this;
	}

	public ThreadBuilder setDaemon(boolean on) {
		builder = builder.andThen(thread -> thread.setDaemon(on));
		return this;
	}

	public ThreadBuilder setName(String name) {
		builder = builder.andThen(thread -> thread.setName(name));
		return this;
	}

	public ThreadBuilder setPriority(int newPriority) {
		builder = builder.andThen(thread -> thread.setPriority(newPriority));
		return this;
	}

	public ThreadBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler eh) {
		builder = builder.andThen(thread -> thread.setUncaughtExceptionHandler(eh));
		return this;
	}

	public ThreadBuilder startAfterBuilt() {
		builder = builder.andThen(Thread::start);
		return this;
	}
}
