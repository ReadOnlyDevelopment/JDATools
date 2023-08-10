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
	default Optional<?> getInstance() {
		return Optional.empty();
	}

	/**
	 * Returns the assigned logger to this {@link Bot}.
	 *
	 * @return The assigned logger
	 */
	default Logger getLogger() {
		return LoggerFactory.getLogger(getId());
	}
}
