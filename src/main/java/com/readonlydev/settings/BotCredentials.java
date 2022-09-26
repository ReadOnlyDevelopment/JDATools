
package com.readonlydev.settings;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class BotCredentials
{

	private BotToken	botToken;
	private String		clientId;
	private String		secret;

	@Data
	@Builder
	private static class BotToken
	{
		private DiscordToken	mainBotToken;
		private DiscordToken	devBotToken;
	}
}
