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

package io.github.readonly.doc.annotation.docstandard;

import java.lang.annotation.*;

import io.github.readonly.doc.ConvertedBy;
import io.github.readonly.doc.annotation.DocConverter;

/**
 * A CommandDoc {@link java.lang.annotation.Annotation Annotation} that contains
 * basic information on command usage, declaration, and requirements.
 * <p>
 * This annotation should be used for "primary" documented information, and when
 * applicable, developers who are using the
 * {@link io.github.readonly.doc.DocGenerator DocGenerator} returned by
 * {@link io.github.readonly.doc.DocGenerator#getDefaultGenerator()}, and who
 * are not implementing their own CommandDoc systems, should use this in place
 * of creating a new annotation and converter if possible.
 */
@ConvertedBy(CommandInfo.Converter.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE, ElementType.METHOD })
public @interface CommandInfo
{
	/**
	 * The name and aliases of a command.
	 * The first one should be the official name, and following elements should
	 * be
	 * aliases (if any are allowed).
	 *
	 * @return The name and aliases
	 */
	String[] name() default {};

	/**
	 * A short format or explanation of a command's usage.
	 *
	 * @return The usage
	 */
	String usage() default "";

	/**
	 * A description of this command, what it does, and (if needed) elaboration
	 * on
	 * the
	 * {@link io.github.readonly.doc.annotation.docstandard.CommandInfo#usage()}.
	 *
	 * @return The description of this command
	 */
	String description() default "";

	/**
	 * A series of prerequisites or requirements a user must have to use this
	 * command.
	 *
	 * @return The requirements to use the command
	 */
	String[] requirements() default {};

	/**
	 * The {@link io.github.readonly.doc.annotation.DocConverter DocConverter} for
	 * the
	 * {@link io.github.readonly.doc.annotation.docstandard.CommandInfo @CommandInfo}
	 * annotation.
	 */
	class Converter implements DocConverter<CommandInfo>
	{
		@Override
		public String read(CommandInfo annotation)
		{
			String[]	names			= annotation.name();
			String		usage			= annotation.usage();
			String		description		= annotation.description();
			String[]	requirements	= annotation.requirements();

			StringBuilder b = new StringBuilder();
			if (names.length > 0)
			{
				b.append("**Name:** `").append(names[0]).append("`").append("\n\n");
				if (names.length > 1)
				{
					b.append("**Aliases:**");
					for (int i = 1; i < names.length; i++)
					{
						b.append(" `").append(names[i]).append("`").append(i != names.length - 1 ? "," : "\n\n");
					}
				}
			}
			if (!usage.isEmpty())
				b.append("**Usage:** ").append(usage).append("\n\n");

			if (!description.isEmpty())
				b.append("**Description:** ").append(description).append("\n\n");
			if (requirements.length == 1)
			{
				b.append("**Requirement:** ").append(requirements[0]).append("\n\n");
			} else if (requirements.length > 1)
			{
				b.append("**Requirements:**\n");
				for (int i = 1; i <= requirements.length; i++)
				{
					b.append(i).append(") ").append(requirements[i - 1]);
					if (i != requirements.length)
						b.append("\n");
				}
			}
			return b.toString();
		}
	}
}
