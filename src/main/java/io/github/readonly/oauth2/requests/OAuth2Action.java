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

package io.github.readonly.oauth2.requests;

import java.io.IOException;
import java.util.function.Consumer;

import javax.annotation.WillClose;

import io.github.readonly.oauth2.entities.impl.OAuth2ClientImpl;
import net.dv8tion.jda.api.requests.Method;
import net.dv8tion.jda.internal.utils.Checks;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An adaptable lookalike of JDA's
 * {@link net.dv8tion.jda.api.requests.RestAction RestAction}.
 * <p>
 * OAuth2Actions
 * can either be completed <i>asynchronously</i> using
 * {@link OAuth2Action#queue() queue}, or synchronously using
 * {@link OAuth2Action#complete() complete}.
 * <p>
 * Note that OAuth2Action does not
 * extend JDA's RestAction.
 */
public abstract class OAuth2Action<T>
{

	protected static final Consumer<?> DEFAULT_SUCCESS = t ->
	{
	};

	protected static final Consumer<Throwable> DEFAULT_FAILURE = t ->
	{
		OAuth2Requester.LOGGER.error("Requester encountered an error while processing response!", t);
	};

	protected final OAuth2ClientImpl client;

	protected final Method method;

	protected final String url;

	public OAuth2Action(OAuth2ClientImpl client, Method method, String url)
	{
		Checks.notNull(client, "OAuth2Client");
		Checks.notNull(method, "Request method");
		Checks.notEmpty(url, "URL");

		this.client = client;
		this.method = method;
		this.url = url;
	}

	protected RequestBody getBody()
	{
		return OAuth2Requester.EMPTY_BODY;
	}

	protected Headers getHeaders()
	{
		return Headers.of();
	}

	protected Request buildRequest()
	{
		Request.Builder builder = new Request.Builder();

		switch (method)
		{
		case GET:
			builder.get();
			break;
		case POST:
			builder.post(getBody());
			break;
		default:
			throw new IllegalArgumentException(method.name() + " requests are not supported!");
		}

		builder.url(url);
		builder.header("User-Agent", OAuth2Requester.USER_AGENT);
		builder.headers(getHeaders());

		return builder.build();
	}

	protected Method getMethod()
	{
		return method;
	}

	protected String getUrl()
	{
		return url;
	}

	/**
	 * Asynchronously executes this OAuth2Action.
	 */
	@SuppressWarnings("unchecked")
	public void queue()
	{
		queue((Consumer<T>) DEFAULT_SUCCESS);
	}

	/**
	 * Asynchronously executes this OAuth2Action, providing the value
	 * constructed from the response as the parameter given to the success
	 * {@link java.util.function.Consumer Consumer}.
	 *
	 * @param success
	 *            The success consumer, executed when this OAuth2Action gets a
	 *            successful response.
	 */
	public void queue(Consumer<T> success)
	{
		queue(success, DEFAULT_FAILURE);
	}

	/**
	 * Asynchronously executes this OAuth2Action, providing the value
	 * constructed from the response as the parameter given to the success
	 * {@link java.util.function.Consumer Consumer} if the response is
	 * successful, or the exception to the failure Consumer if it's not.
	 *
	 * @param success
	 *            The success consumer, executed when this OAuth2Action gets a
	 *            successful response.
	 * @param failure
	 *            The failure consumer, executed when this OAuth2Action gets a
	 *            failed response.
	 */
	public void queue(Consumer<T> success, Consumer<Throwable> failure)
	{
		client.getRequester().submitAsync(this, success, failure);
	}

	/**
	 * Synchronously executes this OAuth2Action, returning the value constructed
	 * from the response if it was successful, or throwing the
	 * {@link java.lang.Exception Exception} if it was not.
	 * <p>
	 * Bear in mind
	 * when using this, that this method blocks the thread it is called in.
	 *
	 * @return the value constructed from the response
	 *
	 * @throws java.io.IOException
	 *             on unsuccessful execution
	 */
	public T complete() throws IOException
	{
		return client.getRequester().submitSync(this);
	}

	/**
	 * Gets the {@link io.github.readonly.oauth2.OAuth2Client client} responsible
	 * for creating this OAuth2Action.
	 *
	 * @return The OAuth2Client responsible for creating this.
	 */
	public OAuth2ClientImpl getClient()
	{
		return client;
	}

	protected abstract T handle(@WillClose Response response) throws IOException;
}
