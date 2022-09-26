package com.readonlydev.doc.annotation.docstandard;

import java.lang.annotation.*;

import com.readonlydev.doc.ConvertedBy;
import com.readonlydev.doc.DocMultiple;
import com.readonlydev.doc.annotation.DocConverter;

/**
 * A CommandDoc {@link java.lang.annotation.Annotation Annotation} that
 * describes a possible error or termination clause a Command might have during
 * it's runtime.
 * <p>
 * These are formatted ways to describe errors and provide the
 * {@link com.readonlydev.doc.annotation.docstandard.Error#response()} method
 * for
 * specifying the bot's response if the error occurs.
 * <p>
 * Multiples of these can be applied using the
 * {@link com.readonlydev.doc.annotation.docstandard.Errors @Errors} annotation,
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
 * @see com.readonlydev.doc.annotation.docstandard.Errors
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
	 * {@link com.readonlydev.doc.annotation.docstandard.Error @Error}
	 * annotations, for the
	 * purpose of listing.
	 *
	 * @return A prefix for the conversion, useful when multiple @Errors are
	 *         specified.
	 */
	String prefix() default "";

	/**
	 * The {@link com.readonlydev.doc.annotation.DocConverter DocConverter} for
	 * the
	 * {@link com.readonlydev.doc.annotation.docstandard.Error @Error}
	 * annotation.
	 */
	class Converter implements DocConverter<Error>
	{
		@Override
		public String read(Error annotation)
		{
			StringBuilder b = new StringBuilder(annotation.prefix());
			if (!annotation.response().isEmpty())
				b.append("\"").append(annotation.response()).append("\" - ");
			b.append(annotation.value());
			return b.toString();
		}
	}
}
