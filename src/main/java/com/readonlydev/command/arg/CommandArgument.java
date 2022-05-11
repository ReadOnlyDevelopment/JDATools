package com.readonlydev.command.arg;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class CommandArgument<T extends CommandArgument<T>> {

	protected ArgumentType argumentType;
	protected String argumentName;
	protected String argumentDescription;
	protected boolean isMultiOption = false;


	public CommandArgument(String argumentName, ArgumentType argumentType) {
		this.argumentName = argumentName;
		this.argumentType = argumentType;
	}

	protected CommandArgument<T> setName(String name) {
		this.argumentName = name;
		return this;
	}

	protected abstract T description(String description);

	protected boolean isRequiredArgument() {
		return this.argumentType == ArgumentType.REQUIRED ? true : false;
	}

	public String getDescription() {
		return argumentDescription;
	}

	public enum ArgumentType {
		REQUIRED, OPTIONAL;
	}

	public Predicate<String> validate(String arg) {
		if(!isMultiOption) {
			List<String> opts = Arrays.asList(argumentName.split("|"));
			opts = opts.stream().filter(arg::equalsIgnoreCase).toList();
			if(!opts.isEmpty()) {
				return x -> x.equalsIgnoreCase(arg);
			}
			return null;
		}
		return x -> x.equalsIgnoreCase(arg);
	}
}
