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

package io.github.readonly.oauth2;

import net.dv8tion.jda.internal.utils.Checks;
import okhttp3.OkHttpClient;

import javax.annotation.CheckReturnValue;

import io.github.readonly.oauth2.entities.OAuth2Guild;
import io.github.readonly.oauth2.entities.OAuth2User;
import io.github.readonly.oauth2.entities.impl.OAuth2ClientImpl;
import io.github.readonly.oauth2.exceptions.InvalidStateException;
import io.github.readonly.oauth2.requests.OAuth2Action;
import io.github.readonly.oauth2.session.Session;
import io.github.readonly.oauth2.session.SessionController;
import io.github.readonly.oauth2.state.StateController;

import java.util.List;

/**
 * The central controller for OAuth2 state and session management using the
 * Discord API.
 * <p>
 * OAuth2Client's are made using a
 * {@link io.github.readonly.oauth2.OAuth2Client.Builder OAuth2Client.Builder}, and
 * sessions can be appended using
 * {@link OAuth2Client#startSession(String, String, String, Scope...)}.
 */
public interface OAuth2Client
{

	/**
	 * The REST version targeted by JDATools OAuth2.
	 */
	int DISCORD_REST_VERSION = 8;

	/**
	 * Generates a formatted authorization URL from the provided redirect URI
	 * fragment and {@link io.github.readonly.oauth2.Scope Scopes}.
	 *
	 * @param redirectUri
	 *            The redirect URI.
	 * @param scopes
	 *            The provided scopes.
	 *
	 * @return The generated authorization URL.
	 */
	String generateAuthorizationURL(String redirectUri, Scope... scopes);

	/**
	 * Starts a {@link io.github.readonly.oauth2.session.Session Session} with the
	 * provided code, state, and identifier. The state provided should be
	 * <i>unique</i> and provided through an implementation of
	 * {@link io.github.readonly.oauth2.state.StateController StateController}.
	 * <p>
	 * If the state has already been consumed by the StateController using
	 * {@link io.github.readonly.oauth2.state.StateController#consumeState(String)
	 * StateController#consumeState}, then it should return {@code null} when
	 * provided the same state, so that this may throw a
	 * {@link InvalidStateException InvalidStateException} to signify it has
	 * been consumed.
	 *
	 * @param code
	 *            The code for the Session to start.
	 * @param state
	 *            The state for the Session to start.
	 * @param identifier
	 *            The identifier for the Session to start.
	 * @param scopes
	 *            The provided scopes.
	 *
	 * @return A {@link io.github.readonly.oauth2.requests.OAuth2Action
	 *         OAuth2Action} for the Session to start.
	 *
	 * @throws InvalidStateException
	 *             If the state, when consumed by this client's StateController,
	 *             results in a {@code null} redirect URI.
	 */
	@CheckReturnValue
	OAuth2Action<Session> startSession(String code, String state, String identifier, Scope... scopes) throws InvalidStateException;

	/**
	 * Requests a {@link io.github.readonly.oauth2.entities.OAuth2User OAuth2User}
	 * from the {@link io.github.readonly.oauth2.session.Session Session}.
	 * <p>
	 * All
	 * Sessions should handle an individual Discord User, and as such this
	 * method retrieves data on that User when the session is provided.
	 *
	 * @param session
	 *            The Session to get a OAuth2User for.
	 *
	 * @return A {@link io.github.readonly.oauth2.requests.OAuth2Action
	 *         OAuth2Action} for the OAuth2User to be retrieved.
	 */
	@CheckReturnValue
	OAuth2Action<OAuth2User> getUser(Session session);

	/**
	 * Requests a list of {@link io.github.readonly.oauth2.entities.OAuth2Guild
	 * OAuth2Guilds} from the {@link io.github.readonly.oauth2.session.Session
	 * Session}.
	 * <p>
	 * All Sessions should handle an individual Discord User, and
	 * as such this method retrieves data on all the various Discord Guilds that
	 * user is a part of when the session is provided.
	 * <p>
	 * Note that this can
	 * only be performed for Sessions who have the necessary
	 * {@link com.readonlydev.oauth2.Scope#GUILDS 'guilds'} scope. <br>
	 * Trying
	 * to call this using a Session without the scope will cause a
	 * {@link io.github.readonly.oauth2.exceptions.MissingScopeException
	 * MissingScopeException} to be thrown.
	 *
	 * @param session
	 *            The Session to get OAuth2Guilds for.
	 *
	 * @return A {@link io.github.readonly.oauth2.requests.OAuth2Action
	 *         OAuth2Action} for the OAuth2Guilds to be retrieved.
	 *
	 * @throws io.github.readonly.oauth2.exceptions.MissingScopeException
	 *             If the provided Session does not have the 'guilds' scope.
	 */
	@CheckReturnValue
	OAuth2Action<List<OAuth2Guild>> getGuilds(Session session);

	/**
	 * Gets the client ID for this OAuth2Client.
	 *
	 * @return The client ID.
	 */
	long getId();

	/**
	 * Gets the client's secret.
	 *
	 * @return The client's secret.
	 */
	String getSecret();

	/**
	 * Gets the client's {@link io.github.readonly.oauth2.state.StateController
	 * StateController}.
	 *
	 * @return The client's StateController.
	 */
	StateController getStateController();

	/**
	 * Gets the client's {@link io.github.readonly.oauth2.session.SessionController
	 * SessionController}.
	 *
	 * @return The client's SessionController.
	 */
	SessionController<?> getSessionController();

	/**
	 * Builder for creating OAuth2Client instances.
	 * <p>
	 * At minimum, the
	 * developer must provide a valid Client ID, as well as a valid secret.
	 */
	class Builder
	{

		private long clientId = -1;

		private String clientSecret;

		private SessionController<?> sessionController;

		private StateController stateController;

		private OkHttpClient client;

		/**
		 * Finalizes and builds an {@link io.github.readonly.oauth2.OAuth2Client
		 * OAuth2Client} instance using this builder.
		 *
		 * @return The OAuth2Client instance build.
		 *
		 * @throws java.lang.IllegalArgumentException
		 *             If either:
		 *             <ul>
		 *             <li>The Client ID is not valid.</li>
		 *             <li>The
		 *             Client Secret is empty.</li>
		 *             </ul>
		 */
		public OAuth2Client build()
		{
			Checks.check(clientId >= 0, "Client ID is invalid!");
			Checks.notEmpty(clientSecret, "Client Secret");
			return new OAuth2ClientImpl(clientId, clientSecret, sessionController, stateController, client);
		}

		/**
		 * Sets the OAuth2Client's ID.
		 *
		 * @param clientId
		 *            The OAuth2Client's ID.
		 *
		 * @return This builder.
		 */
		public Builder setClientId(long clientId)
		{
			this.clientId = clientId;
			return this;
		}

		/**
		 * Sets the OAuth2Client's secret.
		 *
		 * @param clientSecret
		 *            The OAuth2Client's secret.
		 *
		 * @return This builder.
		 */
		public Builder setClientSecret(String clientSecret)
		{
			this.clientSecret = clientSecret;
			return this;
		}

		/**
		 * Sets the OAuth2Client's
		 * {@link io.github.readonly.oauth2.session.SessionController
		 * SessionController}.
		 *
		 * @param sessionController
		 *            The OAuth2Client's SessionController.
		 *
		 * @return This builder.
		 */
		public Builder setSessionController(SessionController<?> sessionController)
		{
			this.sessionController = sessionController;
			return this;
		}

		/**
		 * Sets the OAuth2Client's
		 * {@link io.github.readonly.oauth2.state.StateController StateController}.
		 *
		 * @param stateController
		 *            The OAuth2Client's StateController.
		 *
		 * @return This builder.
		 */
		public Builder setStateController(StateController stateController)
		{
			this.stateController = stateController;
			return this;
		}

		/**
		 * Sets the client's internal {@link okhttp3.OkHttpClient OkHttpClient}
		 * used for all requests and interactions with Discord.
		 *
		 * @param client
		 *            The OAuth2Client's OkHttpClient.
		 *
		 * @return This builder.
		 */
		public Builder setOkHttpClient(OkHttpClient client)
		{
			this.client = client;
			return this;
		}
	}
}
