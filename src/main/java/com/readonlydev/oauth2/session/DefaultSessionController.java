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

package com.readonlydev.oauth2.session;

import java.time.OffsetDateTime;
import java.util.HashMap;

import com.readonlydev.oauth2.Scope;
import com.readonlydev.oauth2.session.DefaultSessionController.DefaultSession;

/**
 * The default {@link com.readonlydev.oauth2.session.SessionController
 * SessionController} implementation.
 */
public class DefaultSessionController implements SessionController<DefaultSession>
{

	private final HashMap<String, DefaultSession> sessions = new HashMap<>();

	@Override
	public DefaultSession getSession(String identifier)
	{
		return sessions.get(identifier);
	}

	@Override
	public DefaultSession createSession(SessionData data)
	{
		DefaultSession created = new DefaultSession(data);
		sessions.put(data.getIdentifier(), created);
		return created;
	}

	public class DefaultSession implements Session
	{

		private final String accessToken, refreshToken, tokenType;

		private final OffsetDateTime expiration;

		private final Scope[] scopes;

		private DefaultSession(String accessToken, String refreshToken, String tokenType, OffsetDateTime expiration, Scope[] scopes)
		{
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
			this.tokenType = tokenType;
			this.expiration = expiration;
			this.scopes = scopes;
		}

		private DefaultSession(SessionData data)
		{
			this(data.getAccessToken(), data.getRefreshToken(), data.getTokenType(), data.getExpiration(), data.getScopes());
		}

		@Override
		public String getAccessToken()
		{
			return accessToken;
		}

		@Override
		public String getRefreshToken()
		{
			return refreshToken;
		}

		@Override
		public Scope[] getScopes()
		{
			return scopes;
		}

		@Override
		public String getTokenType()
		{
			return tokenType;
		}

		@Override
		public OffsetDateTime getExpiration()
		{
			return expiration;
		}
	}
}
