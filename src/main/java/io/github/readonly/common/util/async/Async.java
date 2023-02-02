package io.github.readonly.common.util.async;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.github.readonly.common.util.async.threads.CompletableThread;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Async {

	/**
	 * Current.
	 *
	 * @return the thread
	 */
	public static Thread current() {
		return Thread.currentThread();
	}

	/**
	 * Future.
	 *
	 * @param <T>      the generic type
	 * @param task     the task
	 * @param callable the callable
	 * @return the future
	 */
	public static <T> Future<T> future(String task, Callable<T> callable) {
		return new CompletableThread<>(task, callable);
	}

	/**
	 * Future.
	 *
	 * @param <T>      the generic type
	 * @param callable the callable
	 * @return the future
	 */
	public static <T> Future<T> future(Callable<T> callable) {
		return new CompletableThread<>(callable);
	}

	/**
	 * Sleep.
	 *
	 * @param milis the milis
	 */
	public static void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sleep.
	 *
	 * @param time the time
	 * @param unit the unit
	 */
	public static void sleep(long time, TimeUnit unit) {
		sleep(unit.toMillis(time));
	}

	public static ScheduledExecutorService task(String task) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, task));
		return executor;
	}

	/**
	 * Task.
	 *
	 * @param task      the task
	 * @param scheduled the scheduled
	 * @param everyTime the every time
	 * @param unit      the unit
	 * @return the scheduled executor service
	 */
	public static ScheduledExecutorService task(String task, Consumer<ScheduledExecutorService> scheduled, long everyTime, TimeUnit unit) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, task));
		executor.scheduleAtFixedRate(() -> scheduled.accept(executor), 0, everyTime, unit);
		return executor;
	}

	/**
	 * Task.
	 *
	 * @param task      the task
	 * @param scheduled the scheduled
	 * @param delay     the delay
	 * @param everyTime the every time
	 * @param unit      the unit
	 * @return the scheduled executor service
	 */
	public static ScheduledExecutorService task(String task, Consumer<ScheduledExecutorService> scheduled, long delay, long everyTime, TimeUnit unit) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, task));
		executor.scheduleAtFixedRate(() -> scheduled.accept(executor), delay, everyTime, unit);
		return executor;
	}

	/**
	 * Task.
	 *
	 * @param task      the task
	 * @param scheduled the scheduled
	 * @param delay     the delay
	 * @param everyTime the every time
	 * @param unit      the unit
	 * @return the scheduled executor service
	 */
	public static ScheduledExecutorService task(String task, Runnable scheduled, long delay, long everyTime, TimeUnit unit) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, task));
		executor.scheduleAtFixedRate(scheduled, delay, everyTime, unit);
		return executor;
	}

	/**
	 * Task.
	 *
	 * @param task      the task
	 * @param scheduled the scheduled
	 * @param everyTime the every time
	 * @param unit      the unit
	 * @return the scheduled executor service
	 */
	public static ScheduledExecutorService task(String task, Runnable scheduled, long everyTime, TimeUnit unit) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, task));
		executor.scheduleAtFixedRate(scheduled, 0, everyTime, unit);
		return executor;
	}

	/**
	 * Task.
	 *
	 * @param scheduled the scheduled
	 * @param everyTime the every time
	 * @param unit      the unit
	 * @return the scheduled executor service
	 */
	public static ScheduledExecutorService task(Consumer<ScheduledExecutorService> scheduled, long everyTime, TimeUnit unit) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(() -> scheduled.accept(executor), 0, everyTime, unit);
		return executor;
	}

	/**
	 * Task.
	 *
	 * @param scheduled the scheduled
	 * @param everyTime the every time
	 * @param unit      the unit
	 * @return the scheduled executor service
	 */
	public static ScheduledExecutorService task(Runnable scheduled, long everyTime, TimeUnit unit) {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(scheduled, 0, everyTime, unit);
		return executor;
	}

	/**
	 * Creates a thread with a Runnable and immediately starts the Thread.
	 *
	 * @param doAsync the Runnable that the thread will run
	 * @return the Thread that is now executing the Runnable
	 */
	public static Thread thread(Runnable doAsync) {
		Thread thread = new Thread(doAsync);
		thread.start();
		return thread;
	}

	/**
	 * Thread.
	 *
	 * @param time    the time
	 * @param unit    the unit
	 * @param doAfter the do after
	 * @return the thread
	 */
	public static Thread thread(long time, TimeUnit unit, Runnable doAfter) {
		Objects.requireNonNull(doAfter);

		return thread(() -> {
			sleep(time, unit);
			doAfter.run();
		});
	}

	/**
	 * Creates a named thread with a Runnable.
	 *
	 * @param name    the name
	 * @param time    the time
	 * @param unit    the unit
	 * @param doAfter the do after
	 * @return the thread
	 */
	public static Thread thread(String name, long time, TimeUnit unit, Runnable doAfter) {
		Objects.requireNonNull(doAfter);

		return thread(name, () -> {
			sleep(time, unit);
			doAfter.run();
		});
	}

	/**
	 * Creates a thread with a Runnable and immediately starts the Thread.
	 *
	 * @param name    The thread name
	 * @param doAsync The Runnable that the thread will run
	 * @return the Thread that is now executing the Runnable
	 */
	public static Thread thread(String name, Runnable doAsync) {
		Objects.requireNonNull(doAsync);

		Thread thread = new Thread(doAsync, name);
		thread.start();
		return thread;
	}
}

