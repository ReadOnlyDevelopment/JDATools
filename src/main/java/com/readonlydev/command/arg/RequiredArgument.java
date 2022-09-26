package com.readonlydev.command.arg;

public class RequiredArgument extends CommandArgument<RequiredArgument>
{

	public static final RequiredArgument of(String name, String description)
	{
		return new RequiredArgument(name, description);
	}

	private RequiredArgument(String argumentName, String description)
	{
		super(argumentName, ArgumentType.REQUIRED, description);
	}

	public RequiredArgument multi()
	{
		this.isMultiOption = true;
		return this;
	}
}
