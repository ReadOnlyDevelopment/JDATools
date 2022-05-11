package com.readonlydev.command.arg;

public class Required extends CommandArgument<Required> {

	public static final Required of(String name, String description) {
		return new Required(name).description(description);
	}

	private Required(String argumentName) {
		super(argumentName, ArgumentType.REQUIRED);
	}

	public String getArgument() {
		return argumentName;
	}

	public String getArgumentForHelp() {
		return "[" + getArgument() + "]";
	}

	@Override
	protected Required description(String description) {
		this.argumentDescription = description;
		return this;
	}

	public Required multi() {
		this.isMultiOption = true;
		return this;
	}
}
