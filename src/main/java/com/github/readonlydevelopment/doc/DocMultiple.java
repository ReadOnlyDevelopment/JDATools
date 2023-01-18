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

package com.github.readonlydevelopment.doc;

import java.lang.annotation.*;

/**
 * A helper {@link java.lang.annotation.Annotation Annotation}, useful for
 * formatting multiple occurrences of the same CommandDoc annotation.
 * <p>
 * This
 * is best coupled with usage of an
 * {@link java.lang.annotation.Repeatable @Repeatable} annotation and a
 * similarly named holder annotation for multiple occurrences. <br>
 * {@link com.github.readonlydevelopment.doc.annotation.docstandard.Error @Error} and
 * {@link com.github.readonlydevelopment.doc.annotation.docstandard.Errors @Errors} are an
 * example of such practice.
 *
 * @see com.github.readonlydevelopment.doc.annotation.docstandard.Error
 * @see com.github.readonlydevelopment.doc.annotation.docstandard.Errors
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DocMultiple
{

	/**
	 * Text that occurs before all occurrences of the annotation this is applied
	 * to. <br>
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
	 * Default this is
	 * an empty string.
	 *
	 * @return The separator String.
	 */
	String separateBy() default "";
}
