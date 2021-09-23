package io.github.romvoid95.command.argument;

public class OptionalArgument extends CommandArgument<OptionalArgument> {

	public static final OptionalArgument of(String name, String description) {
		return new OptionalArgument(name).setDescription(description);
	}

	private OptionalArgument(String argumentName) {
		super(argumentName, ArgumentType.OPTIONAL);
	}

	public String getArgument() {
		return argumentName;
	}

	public String getArgumentForHelp() {
		return "<" + getArgument() + ">";
	}

	@Override
	protected OptionalArgument setDescription(String description) {
		this.argumentDescription = description;
		return this;
	}
}
