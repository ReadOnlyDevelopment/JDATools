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
