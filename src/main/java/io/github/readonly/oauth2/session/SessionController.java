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

/**
 * An abstract controller for {@link io.github.readonly.oauth2.session.Session
 * Sessions}, Implementations should be able to create their respectively
 * controlled implementations using an instance of
 * {@link io.github.readonly.oauth2.session.SessionData SessionData} and maintain
 * the created instances for the entire lifetime of the session.
 *
 * @param <S>
 *            The type of the Session for this to handle.
 */
public interface SessionController<S extends Session>
{

	/**
	 * Gets a {@link io.github.readonly.oauth2.session.Session Session} that was
	 * previously created using the provided identifier.
	 * <p>
	 * It is very
	 * important for implementations of SessionController to hold a contract
	 * that Sessions created using {@link #createSession(SessionData)} will be
	 * maintained and retrievable by external sources at any time.
	 * <p>
	 * Note that
	 * Sessions that have elapsed their effective
	 * {@link io.github.readonly.oauth2.session.SessionData#getExpiration()
	 * expiration} are not necessary to maintain, unless they have been
	 * refreshed in which case they should be updated to reflect this.
	 *
	 * @param identifier
	 *            The identifier to get a Session by.
	 *
	 * @return The Session mapped to the identifier provided.
	 */
	S getSession(String identifier);

	/**
	 * Creates a new {@link io.github.readonly.oauth2.session.Session Session}
	 * using the specified {@link io.github.readonly.oauth2.session.SessionData
	 * SessionData}.
	 * <p>
	 * Sessions should be kept mapped outside of just creation
	 * so that they can be retrieved using
	 * {@link SessionController#getSession(String)} later for further
	 * manipulation, as well as to keep updated if they are refreshed.
	 *
	 * @param data
	 *            The data to create a Session using.
	 *
	 * @return A new Session.
	 */
	S createSession(SessionData data);
}
