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

package com.github.readonlydevelopment.oauth2.requests;

import java.io.IOException;
import java.util.function.Consumer;

import org.slf4j.Logger;

import com.github.readonlydevelopment.common.JDAToolsInfo;

import net.dv8tion.jda.internal.utils.JDALogger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 */
public class OAuth2Requester
{

	protected static final Logger LOGGER = JDALogger.getLog(OAuth2Requester.class);

	protected static final String USER_AGENT = "JDA-Utils Oauth2(" + JDAToolsInfo.GITHUB + " | " + JDAToolsInfo.VERSION + ")";

	protected static final RequestBody EMPTY_BODY = RequestBody.create(new byte[0], null);

	private final OkHttpClient httpClient;

	public OAuth2Requester(OkHttpClient httpClient)
	{
		this.httpClient = httpClient;
	}

	<T> void submitAsync(OAuth2Action<T> request, Consumer<T> success, Consumer<Throwable> failure)
	{
		httpClient.newCall(request.buildRequest()).enqueue(new Callback()
		{

			@Override
			public void onResponse(Call call, Response response)
			{
				try
				{
					T value = request.handle(response);
					logSuccessfulRequest(request);

					// Handle end-user exception differently
					try
					{
						if (value != null)
						{
							success.accept(value);
						}
					} catch (Throwable t)
					{
						LOGGER.error("OAuth2Action success callback threw an exception!", t);
					}
				} catch (Throwable t)
				{
					// Handle end-user exception differently
					try
					{
						failure.accept(t);
					} catch (Throwable t1)
					{
						LOGGER.error("OAuth2Action success callback threw an exception!", t1);
					}
				} finally
				{
					response.close();
				}
			}

			@Override
			public void onFailure(Call call, IOException e)
			{
				LOGGER.error("Requester encountered an error when submitting a request!", e);
			}
		});
	}

	<T> T submitSync(OAuth2Action<T> request) throws IOException
	{
		try (Response response = httpClient.newCall(request.buildRequest()).execute())
		{
			T value = request.handle(response);
			logSuccessfulRequest(request);
			return value;
		}
	}

	private static void logSuccessfulRequest(OAuth2Action<?> request)
	{
		LOGGER.debug("Got a response for {} - {}\nHeaders: {}", request.getMethod(), request.getUrl(), request.getHeaders());
	}
}
