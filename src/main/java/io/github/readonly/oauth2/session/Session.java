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

package io.github.readonly.oauth2.session;

import java.time.OffsetDateTime;

import io.github.readonly.oauth2.Scope;

/**
 * Implementable data type used to allow access to data regarding OAuth2
 * sessions.
 * <p>
 * This can be used with a proper
 * {@link io.github.readonly.oauth2.OAuth2Client OAuth2Client} to get information
 * on the logged in {@link io.github.readonly.oauth2.entities.OAuth2User User}, as
 * well as {@link io.github.readonly.oauth2.entities.OAuth2Guild Guilds} they are
 * on.
 */
public interface Session
{

	/**
	 * Gets the session's access token.
	 *
	 * @return The session's access token.
	 */
	String getAccessToken();

	/**
	 * Gets the session's refresh token.
	 *
	 * @return The session's refresh token.
	 */
	String getRefreshToken();

	/**
	 * Gets the session's {@link io.github.readonly.oauth2.Scope Scopes}.
	 *
	 * @return The session's Scopes.
	 */
	Scope[] getScopes();

	/**
	 * Gets the session's token type.
	 *
	 * @return The session's token type.
	 */
	String getTokenType();

	/**
	 * Gets the session's expiration time.
	 *
	 * @return The session's expiration time.
	 */
	OffsetDateTime getExpiration();
}
