package io.github.readonly.common.util.async.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class CompletableThread<V> extends CompletableFuture<V>
{
	private static int		count	= 0;
	private final Thread	thread;

	public CompletableThread(Callable<V> callable)
	{
		this("CompletableThread-" + count++, callable);
	}

	public CompletableThread(String task, Callable<V> callable)
	{
		thread = new Thread(() ->
		{
			try
			{
				this.complete(callable.call());
			} catch (Exception e)
			{
				this.completeExceptionally(e);
			}
		}, task);

		thread.start();
	}

	@Override
	public boolean cancel(boolean ignored)
	{
		if (!thread.isAlive())
		{
			return false;
		}
		thread.interrupt();
		return true;
	}

	public Thread getThread()
	{
		return thread;
	}
}
