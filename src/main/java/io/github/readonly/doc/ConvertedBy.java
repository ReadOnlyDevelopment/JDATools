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

package io.github.readonly.doc;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.readonly.doc.annotation.DocConverter;

/**
 * Specifies an {@link java.lang.annotation.Annotation Annotation} can be
 * converted using the specified
 * {@link io.github.readonly.doc.annotation.DocConverter DocConverter} value.
 * <p>
 * Only annotations with this annotation applied to it are valid for processing
 * via an instance of {@link io.github.readonly.doc.DocGenerator DocGenerator}.
 *
 * @see DocConverter
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ConvertedBy
{

	/**
	 * The {@link io.github.readonly.doc.annotation.DocConverter DocConverter}
	 * Class that the annotation this is applied to provides to
	 * {@link io.github.readonly.doc.annotation.DocConverter#read(Annotation)
	 * DocConverter#read(Annotation)}.
	 *
	 * @return The DocConverter Class to use.
	 */
	Class<? extends DocConverter<? extends Annotation>> value();
}
