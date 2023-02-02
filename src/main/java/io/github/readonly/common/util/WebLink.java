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
