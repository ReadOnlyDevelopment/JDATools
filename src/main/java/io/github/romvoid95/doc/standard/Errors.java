package io.github.romvoid95.doc.standard;

import java.lang.annotation.*;

/**
 * The {@link java.lang.annotation.Repeatable @Repeatable} value for
 * {@link io.github.romvoid95.doc.standard.Error @Error}. <br>
 * Useful for organizing multiple @Error annotations
 *
 * @see Error
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Errors {
	/**
	 * One or more {@link io.github.romvoid95.doc.standard.Error @Error}
	 * annotations.
	 *
	 * @return One or more @Error annotations
	 */
	Error[] value();
}
