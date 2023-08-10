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


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.readonly.doc.ConvertedBy;
import io.github.readonly.doc.DocMultiple;
import io.github.readonly.doc.annotation.DocConverter;

/**
 * A CommandDoc {@link java.lang.annotation.Annotation Annotation} that
 * describes a possible error or termination clause a Command might have during
 * it's runtime.
 * <p>
 * These are formatted ways to describe errors and provide the
 * {@link io.github.readonly.doc.annotation.docstandard.Error#response()} method
 * for
 * specifying the bot's response if the error occurs.
 * <p>
 * Multiples of these can be applied using the
 * {@link io.github.readonly.doc.annotation.docstandard.Errors @Errors} annotation,
 * or simply
 * multiples of these can be attached to a class or method.
 * <p>
 * Below is a visual of what this should generally look like:
 *
 * <pre>
 *     <b>Possible Errors:</b>
 *     • "I encountered an issue while processing this command!" - Houston had a problem!
 *     • "You used this command too fast" - Slow down cowboy!
 *     • "An unexpected error occurred!" - Let's just blame dev!
 * </pre>
 *
 * @see io.github.readonly.doc.annotation.docstandard.Errors
 */
@ConvertedBy(Error.Converter.class)
@DocMultiple(preface = "**Possible Errors:**\n\n", prefixEach = "+ ", separateBy = "\n\n")
@Documented
@Repeatable(Errors.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(
	{ ElementType.TYPE, ElementType.METHOD })
public @interface Error
{
	/**
	 * A brief description of what caused the error.
	 *
	 * @return A description of what caused the error.
	 */
	String value();

	/**
	 * A response message that would normally be sent if this error occurs, as a
	 * means of users identifying the error without an idea of what exactly went
	 * wrong.
	 *
	 * @return A response message.
	 */
	String response() default "";

	/**
	 * A prefix appended to the front of the produced String during conversion.
	 * <br>
	 * Only really useful or needed when a Command has multiple
	 * {@link io.github.readonly.doc.annotation.docstandard.Error @Error}
	 * annotations, for the
	 * purpose of listing.
	 *
	 * @return A prefix for the conversion, useful when multiple @Errors are
	 *         specified.
	 */
	String prefix() default "";

	/**
	 * The {@link io.github.readonly.doc.annotation.DocConverter DocConverter} for
	 * the
	 * {@link io.github.readonly.doc.annotation.docstandard.Error @Error}
	 * annotation.
	 */
	class Converter implements DocConverter<Error>
	{
		@Override
		public String read(Error annotation)
		{
			StringBuilder b = new StringBuilder(annotation.prefix());
			if (!annotation.response().isEmpty())
			{
				b.append("\"").append(annotation.response()).append("\" - ");
			}
			b.append(annotation.value());
			return b.toString();
		}
	}
}
