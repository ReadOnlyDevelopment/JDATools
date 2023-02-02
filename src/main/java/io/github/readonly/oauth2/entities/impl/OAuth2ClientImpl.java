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

package io.github.readonly.oauth2.entities.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import io.github.readonly.oauth2.OAuth2Client;
import io.github.readonly.oauth2.Scope;
import io.github.readonly.oauth2.entities.OAuth2Guild;
import io.github.readonly.oauth2.entities.OAuth2User;
import io.github.readonly.oauth2.exceptions.InvalidStateException;
import io.github.readonly.oauth2.exceptions.MissingScopeException;
import io.github.readonly.oauth2.requests.OAuth2Action;
import io.github.readonly.oauth2.requests.OAuth2Requester;
import io.github.readonly.oauth2.requests.OAuth2URL;
import io.github.readonly.oauth2.session.DefaultSessionController;
import io.github.readonly.oauth2.session.Session;
import io.github.readonly.oauth2.session.SessionController;
import io.github.readonly.oauth2.session.SessionData;
import io.github.readonly.oauth2.state.DefaultStateController;
import io.github.readonly.oauth2.state.StateController;
import net.dv8tion.jda.api.exceptions.HttpException;
import net.dv8tion.jda.internal.requests.Method;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.EncodingUtil;
import net.dv8tion.jda.internal.utils.IOUtil;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OAuth2ClientImpl implements OAuth2Client
{

	private final long clientId;

	private final String clientSecret;

	private final SessionController<?> sessionController;

	private final StateController stateController;

	private final OkHttpClient httpClient;

	private final OAuth2Requester requester;

	public OAuth2ClientImpl(long clientId, String clientSecret, SessionController<?> sessionController, StateController stateController, OkHttpClient httpClient)
	{
		Checks.check(clientId >= 0, "Invalid Client ID");
		Checks.notNull(clientSecret, "Client Secret");

		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.sessionController = sessionController == null ? new DefaultSessionController() : sessionController;
		this.stateController = stateController == null ? new DefaultStateController() : stateController;
		this.httpClient = httpClient == null ? new OkHttpClient.Builder().build() : httpClient;
		this.requester = new OAuth2Requester(this.httpClient);
	}

	@Override
	public String generateAuthorizationURL(String redirectUri, Scope... scopes)
	{
		Checks.notNull(redirectUri, "Redirect URI");

		return OAuth2URL.AUTHORIZE.compile(clientId, EncodingUtil.encodeUTF8(redirectUri), Scope.join(scopes), stateController.generateNewState(redirectUri));
	}

	@Override
	public OAuth2Action<Session> startSession(String code, String state, String identifier, Scope... scopes) throws InvalidStateException
	{
		Checks.notEmpty(code, "code");
		Checks.notEmpty(state, "state");

		String redirectUri = stateController.consumeState(state);
		if (redirectUri == null)
		{
			throw new InvalidStateException(String.format("No state '%s' exists!", state));
		}

		OAuth2URL oAuth2URL = OAuth2URL.TOKEN;

		return new OAuth2Action<Session>(this, Method.POST, oAuth2URL.getRouteWithBaseUrl())
		{

			@Override
			protected Headers getHeaders()
			{
				return Headers.of("Content-Type", "x-www-form-urlencoded");
			}

			@Override
			protected RequestBody getBody()
			{
				return RequestBody.create(oAuth2URL.compileQueryParams(clientId, EncodingUtil.encodeUTF8(redirectUri), code, clientSecret, Scope.join(true, scopes)), MediaType.parse("application/x-www-form-urlencoded"));
			}

			@Override
			protected Session handle(Response response) throws IOException
			{
				if (!response.isSuccessful())
				{
					throw failure(response);
				}

				JSONObject body = new JSONObject(new JSONTokener(IOUtil.getBody(response)));

				String[]	scopeStrings	= body.getString("scope").split(" ");
				Scope[]		scopes			= new Scope[scopeStrings.length];
				for (int i = 0; i < scopeStrings.length; i++)
				{
					scopes[i] = Scope.from(scopeStrings[i]);
				}

				return sessionController.createSession(new SessionData(identifier, body.getString("access_token"), body.getString("refresh_token"), body.getString("token_type"), OffsetDateTime.now().plusSeconds(body.getInt("expires_in")), scopes));
			}
		};
	}

	@Override
	public OAuth2Action<OAuth2User> getUser(Session session)
	{
		Checks.notNull(session, "Session");
		return new OAuth2Action<OAuth2User>(this, Method.GET, OAuth2URL.CURRENT_USER.compile())
		{

			@Override
			protected Headers getHeaders()
			{
				return Headers.of("Authorization", generateAuthorizationHeader(session));
			}

			@Override
			protected OAuth2User handle(Response response) throws IOException
			{
				if (!response.isSuccessful())
				{
					throw failure(response);
				}
				JSONObject body = new JSONObject(new JSONTokener(IOUtil.getBody(response)));
				return new OAuth2UserImpl(OAuth2ClientImpl.this, session, body.getLong("id"), body.getString("username"), body.getString("discriminator"), body.optString("avatar", null), body.optString("email", null), body.optBoolean("verified", false), body.getBoolean("mfa_enabled"));
			}
		};
	}

	@Override
	public OAuth2Action<List<OAuth2Guild>> getGuilds(Session session)
	{
		Checks.notNull(session, "session");
		if (!Scope.contains(session.getScopes(), Scope.GUILDS))
		{
			throw new MissingScopeException("get guilds for a Session", Scope.GUILDS);
		}
		return new OAuth2Action<List<OAuth2Guild>>(this, Method.GET, OAuth2URL.CURRENT_USER_GUILDS.compile())
		{

			@Override
			protected Headers getHeaders()
			{
				return Headers.of("Authorization", generateAuthorizationHeader(session));
			}

			@Override
			protected List<OAuth2Guild> handle(Response response) throws IOException
			{
				if (!response.isSuccessful())
				{
					throw failure(response);
				}

				JSONArray			body	= new JSONArray(new JSONTokener(IOUtil.getBody(response)));
				List<OAuth2Guild>	list	= new LinkedList<>();
				JSONObject			obj;
				for (int i = 0; i < body.length(); i++)
				{
					obj = body.getJSONObject(i);
					list.add(new OAuth2GuildImpl(OAuth2ClientImpl.this, obj.getLong("id"), obj.getString("name"), obj.optString("icon", null), obj.getBoolean("owner"), obj.getInt("permissions")));
				}
				return list;
			}
		};
	}

	@Override
	public long getId()
	{
		return clientId;
	}

	@Override
	public String getSecret()
	{
		return clientSecret;
	}

	@Override
	public StateController getStateController()
	{
		return stateController;
	}

	@Override
	public SessionController<?> getSessionController()
	{
		return sessionController;
	}

	public void shutdown()
	{
		httpClient.dispatcher().executorService().shutdown();
	}

	/**
	 * Gets the internal OAuth2Requester used by this OAuth2Client.
	 *
	 * @return The internal OAuth2Requester used by this OAuth2Client.
	 */
	public OAuth2Requester getRequester()
	{
		return requester;
	}

	protected static HttpException failure(Response response) throws IOException
	{
		final InputStream	stream			= IOUtil.getBody(response);
		final String		responseBody	= new String(IOUtil.readFully(stream));
		return new HttpException("Request returned failure " + response.code() + ": " + responseBody);
	}

	// Generates an authorization header 'X Y', where 'X' is the session's
	// token-type and 'Y' is the session's access token.
	private String generateAuthorizationHeader(Session session)
	{
		return String.format("%s %s", session.getTokenType(), session.getAccessToken());
	}
}
