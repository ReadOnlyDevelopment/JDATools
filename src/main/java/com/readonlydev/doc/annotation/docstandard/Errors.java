package com.readonlydev.doc.annotation.docstandard;

import java.lang.annotation.*;

/**
 * The {@link java.lang.annotation.Repeatable @Repeatable} value for
 * {@link com.readonlydev.doc.annotation.docstandard.Error @Error}. <br>
 * Useful for organizing multiple @Error annotations
 *
 * @see Error
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE, ElementType.METHOD })
public @interface Errors
{
	/**
	 * One or more
	 * {@link com.readonlydev.doc.annotation.docstandard.Error @Error}
	 * annotations.
	 *
	 * @return One or more @Error annotations
	 */
	Error[] value();
}
