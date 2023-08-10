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

package io.github.readonly.common.util;

import okhttp3.HttpUrl;

public class WebLink
{
	public static HttpUrl http(String host)
	{
		return new HttpUrl.Builder().scheme("http").host(host).build();
	}

	public static HttpUrl http(String host, String... paths)
	{
		HttpUrl.Builder ilder = new HttpUrl.Builder().scheme("http").host(host);
		for(String path : paths)
		{
			ilder.addPathSegment(path);
		}
		return ilder.build();
	}

	public static HttpUrl https(String host)
	{
		return new HttpUrl.Builder().scheme("https").host(host).build();
	}

	public static HttpUrl https(String host, String... paths)
	{
		HttpUrl.Builder ilder = new HttpUrl.Builder().scheme("https").host(host);
		for(String path : paths)
		{
			ilder.addPathSegment(path);
		}
		return ilder.build();
	}

	public static HttpUrl parse(String urlString)
	{
		return HttpUrl.parse(urlString);
	}
}
