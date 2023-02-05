package io.github.readonly.api.rmi;

import java.io.Serializable;

public record ThreadInfo(Group group, long id, String name, int priority, boolean daemon, Thread.State state, StackTraceElement[] stackElements) implements Serializable {

	public static ThreadInfo fromThread(Thread thread, StackTraceElement[] stackElements) {
		return new ThreadInfo(
			new Group(thread.getThreadGroup().getName()),
			thread.getId(),
			thread.getName(),
			thread.getPriority(),
			thread.isDaemon(),
			thread.getState(),
			stackElements
			);
	}
}
