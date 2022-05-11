package com.readonlydev.command.arg;

public class Args {

	public static Required required(String arg, String desc) {
		return Required.of(arg, desc);
	}

	public static Required requiredMulti(String arg, String desc) {
		return Required.of(arg, desc).multi();
	}

	public static Optional optional(String arg, String desc) {
		return Optional.of(arg, desc);
	}

	public static Optional optionalMulti(String arg, String desc) {
		return Optional.of(arg, desc).multi();
	}

}
