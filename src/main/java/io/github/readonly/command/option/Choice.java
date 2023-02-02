package io.github.readonly.command.option;

import net.dv8tion.jda.api.interactions.commands.Command;

public class Choice
{
	public static Command.Choice add(String key, String value)
	{
		return new Command.Choice(key, value);
	}

	public static Command.Choice add(String key)
	{
		return add(key, key.toLowerCase());
	}

	public static Command.Choice add(String key, long value)
	{
		return new Command.Choice(key, value);
	}

	public static Command.Choice add(String key, double value)
	{
		return new Command.Choice(key, value);
	}
}
