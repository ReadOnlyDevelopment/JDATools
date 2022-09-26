package com.readonlydev.discordbot;

import javax.security.auth.login.LoginException;

public interface DiscordBot
{
	void start() throws LoginException;

	void shutdown();
}
