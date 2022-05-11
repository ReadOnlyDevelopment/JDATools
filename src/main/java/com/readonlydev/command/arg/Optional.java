package com.readonlydev.command.arg;

public class Optional extends CommandArgument<Optional> {

	public static final Optional of(String name, String description) {
		return new Optional(name).description(description);
	}

	private Optional(String argumentName) {
		super(argumentName, ArgumentType.OPTIONAL);
	}

	public String getArgument() {
		return argumentName;
	}

	public String getArgumentForHelp() {
		return "<" + getArgument() + ">";
	}

	@Override
	protected Optional description(String description) {
		this.argumentDescription = description;
		return this;
	}

	public Optional multi() {
		this.isMultiOption = true;
		return this;
	}
}
