package com.readonlydev.settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

public final class DiscordToken
{
	private static final String	tokenRegex	= "[A-Za-z\\d]{23,}\\.[\\w-]{6}\\.[\\w-]{27,}";
	private final String		validatedToken;

	public DiscordToken(String token) throws InvalidTokenException
	{
		final Matcher regex = Pattern.compile(tokenRegex).matcher(token);
		if (regex.find())
		{
			this.validatedToken = regex.group(0);
		}
		throw new InvalidTokenException(token);
	}

	@Nonnull
	public String token()
	{
		return validatedToken;
	}
}
