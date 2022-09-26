package com.readonlydev.command.arg.parse;

import java.util.function.Predicate;

public class Argument implements IArgument<String>
{

	private String argument;

	public Argument(String arg)
	{
		this.argument = arg;
	}

	@Override
	public String val()
	{
		return argument;
	}

	public boolean validate(Predicate<String> predicate)
	{
		return predicate.test(argument);
	}
}
