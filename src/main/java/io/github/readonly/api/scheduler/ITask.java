package io.github.readonly.api.scheduler;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.common.util.concurrent.AbstractScheduledService.Scheduler;

import io.github.readonly.api.BotContainer;
import io.github.readonly.api.identity.Identifiable;
import io.github.readonly.api.util.ResettableBuilder;
import io.github.readonly.scheduler.BotScheduler;

public interface ITask extends Identifiable {

	/**
	 * Creates a new {@link Builder} to build a {@link ITask}.
	 *
	 * @return The new builder
	 */
	static Builder builder() {
		return BotScheduler.instance.createTaskBuilder();
	}

	/**
	 * Gets the name of this task.
	 *
	 * @return The name of the task
	 */
	String getName();

	/**
	 * Returns the plugin that scheduled this task.
	 *
	 * @return The plugin that scheduled the task
	 */
	BotContainer getOwner();

	/**
	 * Gets the delay that the task was scheduled to run after. A delay of 0
	 * represents that the task started immediately.
	 *
	 * @return The delay (offset) in either milliseconds or ticks (ticks are
	 *         exclusive to synchronous tasks)
	 */
	long getDelay();

	/**
	 * Gets the interval for repeating tasks. An interval of 0 represents that
	 * the task does not repeat.
	 *
	 * @return The interval (period) in either milliseconds or ticks (ticks are
	 *         exclusive to synchronous tasks)
	 */
	long getInterval();

	/**
	 * Cancels the task. Cancelling a repeating task will prevent any further
	 * repetitions of the task.
	 *
	 * @return If the task is not running and was cancelled
	 */
	boolean cancel();

	/**
	 * Gets the {@link Consumer}<{@link ITask}> that this task is running.
	 *
	 * @return The consumer
	 */
	Consumer<ITask> getConsumer();

	/**
	 * Gets whether this task is asynchronous.
	 *
	 * @return True if asynchronous, false if synchronous
	 */
	boolean isAsynchronous();

	/**
	 * Represents a builder to create a {@link ITask}.
	 */
	interface Builder extends ResettableBuilder<ITask, Builder> {

		/**
		 * Sets whether the task should run asynchronous, outside of the main
		 * loop, and in it's own thread. By default, tasks are synchronous.
		 *
		 * <p>A synchronous task is ran in alignment with the game's main loop,
		 * in the same thread. Each synchronous task is ran in series with the
		 * tick cycle. It is safe to manipulate game data when running in this
		 * mode.</p>
		 *
		 * <p>In contrast, a task set to run asynchronously will run
		 * independently of the tick cycle and in it's own thread. Therefore the
		 * task is <b>not thread safe</b> with game data and care must be taken.
		 * It is strongly advised to <b>not</b> manipulate game data in
		 * asynchronous tasks.</p>
		 *
		 * <p>It is not possible to schedule a task in unit ticks when running
		 * asynchronously. If the delay or interval is specified in ticks, it
		 * will be converted to the equivalent wall clock time by multiplying
		 * the value by {@link Scheduler#getPreferredTickInterval()}.</p>
		 *
		 * @return This builder, for chaining
		 */
		Builder async();

		/**
		 * Sets the {@link Runnable} to run when this task executes.
		 *
		 * @param runnable The actual task to run
		 * @return This builder, for chaining
		 */
		default Builder execute(Runnable runnable) {
			return this.execute(task -> runnable.run());
		}

		/**
		 * Sets the consumer that runs when this task executes.
		 *
		 * @param executor The executor to run
		 * @return This builder, for chaining
		 */
		Builder execute(Consumer<ITask> executor);

		/**
		 * Sets the delay before the task runs. This delay is an initial offset,
		 * subsequent runs (when the interval is not 0) will not be offset. By
		 * default, the delay is 0.
		 *
		 * @param delay The delay in the given {@link TimeUnit}
		 * @param unit The unit the delay is in
		 * @return This builder, for chaining
		 * @throws IllegalArgumentException If the delay is below 0
		 */
		Builder delay(long delay, TimeUnit unit);

		/**
		 * Sets the interval between repetitions of the task. The task will not
		 * repeat if the interval is 0. By default, the interval is 0.
		 *
		 * <p>If the scheduler detects that two tasks will overlap, the 2nd task
		 * will not be started. The next time the task is due to run, the test
		 * will be made again to determine if the previous occurrence of the
		 * task is still alive (running). As long as a previous occurrence is
		 * running no new occurrences of that specific task will start, although
		 * the scheduler will never cease in trying to start it a 2nd time.</p>
		 *
		 * @param interval The interval in the given {@link TimeUnit}
		 * @param unit The unit the interval is in
		 * @return This builder, for chaining
		 * @throws IllegalArgumentException If the interval is below 0
		 */
		Builder interval(long interval, TimeUnit unit);

		/**
		 * Sets the name of the task, the name cannot be blank.
		 *
		 * <p>If the name is not set in the builder, the name of the task
		 * will be the form:<br> <tt>PLUGIN_ID "-" ( "A-" | "S-" ) SERIAL_ID
		 * </tt></p>
		 *
		 * <p>Examples of default Task names:<br>
		 *
		 * <tt>"FooPlugin-A-12"</tt><br><tt>"BarPlugin-S-4322"</tt></p>
		 *
		 * <p>No two active tasks will have the same serial ID for the same
		 * synchronisation type.<br>i.e <tt>APlugin-A-15</tt> and
		 * <tt>BPlugin-A-15</tt> is not possible but <tt>BPlugin-S-15</tt>
		 * is.</p>
		 *
		 * @param name The task name
		 * @return This builder, for chaining
		 * @throws IllegalArgumentException If the name is blank
		 */
		Builder name(String name);

		/**
		 * Submits the task to the scheduler and returns the task that was
		 * created.
		 *
		 * @param plugin The owner of the task
		 * @return A new instance of a {@link ITask}
		 * @throws IllegalArgumentException If the object passed in is not
		 *     a plugin instance
		 * @throws IllegalStateException If the builder is incomplete
		 */
		ITask submit(BotContainer instance);
	}
}
