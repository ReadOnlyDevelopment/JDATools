package com.readonlydev.doc;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.readonlydev.doc.annotation.DocConverter;

/**
 * Specifies an {@link java.lang.annotation.Annotation Annotation} can be
 * converted using the specified {@link com.readonlydev.doc.annotation.DocConverter
 * DocConverter} value.
 * <p>
 * Only annotations with this annotation applied to it are valid for processing
 * via an instance of {@link com.readonlydev.doc.DocGenerator DocGenerator}.
 *
 * @see DocConverter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ConvertedBy {
	/**
	 * The {@link com.readonlydev.doc.annotation.DocConverter DocConverter} Class that the
	 * annotation this is applied to provides to
	 * {@link com.readonlydev.doc.annotation.DocConverter#read(Annotation)
	 * DocConverter#read(Annotation)}.
	 *
	 * @return The DocConverter Class to use.
	 */
	Class<? extends DocConverter<? extends Annotation>> value();
}
