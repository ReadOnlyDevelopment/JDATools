/*
 * This file is part of JDATools, licensed under the MIT License (MIT).
 *
 * Copyright (c) ROMVoid95
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.readonlydevelopment.command.arg;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class CommandArgument<T extends CommandArgument<T>>
{

	protected ArgumentType	argumentType;
	protected String		argumentName;
	protected String		argumentDescription;
	protected boolean		isMultiOption	= false;

	public CommandArgument(String argumentName, ArgumentType argumentType, String description)
	{
		this(argumentName, argumentType);
		this.argumentDescription = description;
	}

	public CommandArgument(String argumentName, ArgumentType argumentType)
	{
		this.argumentName = argumentName;
		this.argumentType = argumentType;
		this.argumentDescription = "";
	}

	protected CommandArgument<T> setName(String name)
	{
		this.argumentName = name;
		return this;
	}

	public String getArgumentForHelp()
	{
		return argumentType.appendParts(argumentName);
	}

	public boolean isRequired()
	{
		return this.argumentType == ArgumentType.REQUIRED ? true : false;
	}

	public String getDescription()
	{
		return argumentDescription;
	}

	public enum ArgumentType
	{

		REQUIRED("<", ">"),
		OPTIONAL("[", "]");

		private final String prefix, suffix;

		ArgumentType(String p, String s)
		{
			this.prefix = p;
			this.suffix = s;
		}

		public String appendParts(String appendTo)
		{
			return "%s%s%s".formatted(prefix, appendTo, suffix);
		}
	}

	public Predicate<String> validate(String arg)
	{
		if (!isMultiOption)
		{
			List<String> opts = Arrays.asList(argumentName.split("|"));
			opts = opts.stream().filter(arg::equalsIgnoreCase).toList();
			if (!opts.isEmpty())
			{
				return x -> x.equalsIgnoreCase(arg);
			}
			return null;
		}
		return x -> x.equalsIgnoreCase(arg);
	}
}
