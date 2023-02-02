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

package io.github.readonly.oauth2.state;

/**
 * Implementable state controller, used for registering states and generating
 * redirect URIs using them.
 * <p>
 * Naturally, states should be unique, and
 * attempting to generate a redirect using a previously used state should return
 * {@code null} instead of a new redirect URI.
 */
public interface StateController
{

	/**
	 * Generates a new state string using the provided redirect URI.
	 * 
	 * @param redirectUri
	 *            The redirect URI that will be used with this state.
	 *
	 * @return The state string.
	 */
	String generateNewState(String redirectUri);

	/**
	 * Consumes a state to get the corresponding redirect URI.
	 * <p>
	 * Once this
	 * method is called for a specific state, it should return null for all
	 * future calls of that same state.
	 * 
	 * @param state
	 *            The state.
	 *
	 * @return The redirect URI, or {@code null} if the state does not exist.
	 */
	String consumeState(String state);
}
