package com.readonlydev.command.arg;

public class OptionalArgument extends CommandArgument<OptionalArgument> {

    public static final OptionalArgument of(String name, String description) {
        return new OptionalArgument(name, description);
    }

    private OptionalArgument(String argumentName, String description) {
        super(argumentName, ArgumentType.OPTIONAL, description);
    }

    public OptionalArgument multi() {
        this.isMultiOption = true;
        return this;
    }
}
