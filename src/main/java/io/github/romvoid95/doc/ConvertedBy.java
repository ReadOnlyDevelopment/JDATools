package io.github.romvoid95.doc;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies an {@link java.lang.annotation.Annotation Annotation} can be
 * converted using the specified {@link io.github.romvoid95.doc.DocConverter
 * DocConverter} value.
 * <p>
 * Only annotations with this annotation applied to it are valid for processing
 * via an instance of {@link io.github.romvoid95.doc.DocGenerator DocGenerator}.
 *
 * @see DocConverter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ConvertedBy {
	/**
	 * The {@link io.github.romvoid95.doc.DocConverter DocConverter} Class that the
	 * annotation this is applied to provides to
	 * {@link io.github.romvoid95.doc.DocConverter#read(Annotation)
	 * DocConverter#read(Annotation)}.
	 *
	 * @return The DocConverter Class to use.
	 */
	Class<? extends DocConverter<? extends Annotation>> value();
}
