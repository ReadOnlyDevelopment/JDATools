package com.readonlydev.command.arg;

public class Args
{

	public static RequiredArgument required(String arg, String desc)
	{
		return RequiredArgument.of(arg, desc);
	}

	public static RequiredArgument requiredMulti(String arg, String desc)
	{
		return RequiredArgument.of(arg, desc).multi();
	}

	public static OptionalArgument optional(String arg, String desc)
	{
		return OptionalArgument.of(arg, desc);
	}

	public static OptionalArgument optionalMulti(String arg, String desc)
	{
		return OptionalArgument.of(arg, desc).multi();
	}

}
