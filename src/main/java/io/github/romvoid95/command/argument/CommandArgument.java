package io.github.romvoid95.command.argument;

public abstract class CommandArgument<T extends CommandArgument<T>> {

	protected ArgumentType argumentType;

	protected String argumentName;

	protected String argumentDescription;

	public CommandArgument(String argumentName, ArgumentType argumentType) {
		this.argumentName = argumentName;
		this.argumentType = argumentType;
	}

	protected abstract T setDescription(String description);

	protected boolean isRequiredArgument() {
		return this.argumentType == ArgumentType.REQUIRED ? true : false;
	}

	public String getDescription() {
		return argumentDescription;
	}

	public enum ArgumentType {
		REQUIRED, OPTIONAL;
	}
}
