package io.github.readonly.api.rmi;

import java.lang.instrument.Instrumentation;

public interface StatusListener
{
	/**
	 * Called before a process is shut down.
	 */
	default void onShutdown() {
	}

	/**
	 * Called before a process is started up. Precisely,
	 * this method is called by the agent, after the RMI connection has been started.
	 */
	default void onStartup() {
	}

	/**
	 * This method is called by the agent in its premain method before the RMI connection
	 * is established with the launcher.
	 *
	 * @param instrumentation the instrumentation
	 */
	default void premain(Instrumentation instrumentation) {
	}

}
