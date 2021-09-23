package io.github.romvoid95.command.argument;

public class RequiredArgument extends CommandArgument<RequiredArgument> {

	public static final RequiredArgument of(String name, String description) {
		return new RequiredArgument(name).setDescription(description);
	}

	private RequiredArgument(String argumentName) {
		super(argumentName, ArgumentType.REQUIRED);
	}

	public String getArgument() {
		return argumentName;
	}

	public String getArgumentForHelp() {
		return "[" + getArgument() + "]";
	}

	@Override
	protected RequiredArgument setDescription(String description) {
		this.argumentDescription = description;
		return this;
	}
}
