package com.readonlydev.discordbot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BotContainer
{
	private static Map<String, DiscordBot> BOTS = new ConcurrentHashMap<>();

	public static DiscordBot register(String name, DiscordBot discordBot)
	{
		BOTS.put(name, discordBot);
		return discordBot;
	}

	public static int botCount()
	{
		return BOTS.size();
	}
}
