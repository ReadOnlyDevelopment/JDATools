package io.github.readonly.api;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.readonly.api.identity.Bot;

public interface BotContainer
{
	/**
	 * Gets the qualified ClientID of the {@link Bot} within this container.
	 *
	 * @return The plugin ID
	 * @see Bot#id()
	 */
	String getId();

	/**
	 * Gets the name of the {@link Bot} within this container.
	 *
	 * @return The plugin name, or {@link #getId()} if unknown
	 * @see Bot#name()
	 */
	default String getName() {
		return getId();
	}

	/**
	 * Gets the version of the {@link Bot} within this container.
	 *
	 * @return The plugin version, or {@link Optional#empty()} if unknown
	 * @see Bot#version()
	 */
	default Optional<String> getVersion() {
		return Optional.empty();
	}

	/**
	 * Returns the created instance of {@link Bot} if it is available.
	 *
	 * @return The instance if available
	 */
	Optional<?> getInstance();

	/**
	 * Returns the assigned logger to this {@link Bot}.
	 *
	 * @return The assigned logger
	 */
	default Logger getLogger() {
		return LoggerFactory.getLogger(getId());
	}
}
