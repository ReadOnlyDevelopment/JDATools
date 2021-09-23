package io.github.romvoid95.doc;

import java.lang.annotation.*;

/**
 * A helper {@link java.lang.annotation.Annotation Annotation}, useful for
 * formatting multiple occurrences of the same CommandDoc annotation.
 * <p>
 * This is best coupled with usage of an
 * {@link java.lang.annotation.Repeatable @Repeatable} annotation and a
 * similarly named holder annotation for multiple occurrences. <br>
 * {@link io.github.romvoid95.doc.standard.Error @Error} and
 * {@link io.github.romvoid95.doc.standard.Errors @Errors} are an example of
 * such practice.
 *
 * @see io.github.romvoid95.doc.standard.Error
 * @see io.github.romvoid95.doc.standard.Errors
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DocMultiple {
	/**
	 * Text that occurs before all occurrences of the annotation this is applied to.
	 * <br>
	 * Default this is an empty String.
	 *
	 * @return The preface text
	 */
	String preface() default "";

	/**
	 * A prefix annotation appended to the front of each occurrence. <br>
	 * Default this is an empty string.
	 *
	 * @return The prefix String.
	 */
	String prefixEach() default "";

	/**
	 * A separator String applied in-between occurrences. <br>
	 * Default this is an empty string.
	 *
	 * @return The separator String.
	 */
	String separateBy() default "";
}
