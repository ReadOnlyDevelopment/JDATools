package io.github.readonly.api.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface TaskService extends ScheduledExecutorService
{

	@Override
	TaskFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

	@Override
	<V> TaskFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

	@Override
	TaskFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);

	@Override
	TaskFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);

	interface TaskFuture<V> extends RunnableScheduledFuture<V>
	{
		Task getTask();
	}
}
