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

package com.github.readonlydevelopment.doc.annotation;

import java.lang.annotation.Annotation;

import com.github.readonlydevelopment.doc.ConvertedBy;

/**
 * Converts an annotation of the specified type {@code T} into a String to be
 * collected with other conversions into a single String documenting a class or
 * method representing a command for a bot.
 * <p>
 * These are the fundamental building blocks behind command doc annotations, and
 * can be applied using the
 * {@link com.github.readonlydevelopment.doc.ConvertedBy @ConvertedBy} annotation:
 *
 * <pre>
 *    {@literal @ConvertedBy(MyCommandDocAnn.Converter.class)}
 *    {@literal @Retention(RetentionPolicy.RUNTIME)}
 *    {@literal @Target(ElementType.ANNOTATION_TYPE)}
 *     public @interface MyCommandDocAnn
 *     {
 *         String value();
 *
 *         class Converter implements DocConverter{@literal <MyCommandDocAnn>}
 *         {
 *             public String read(MyCommandDocAnn annotation)
 *             {
 *                 return "**"+annotation.value()+"**";
 *             }
 *         }
 *     }
 * </pre>
 *
 * It is also notably recommended you follow the standards for DocConverters
 * listed below:
 * <ul>
 * <li>1)
 * {@link com.github.readonlydevelopment.doc.annotation.DocConverter#read(java.lang.annotation.Annotation)}
 * should not throw any exceptions, nor otherwise halt a process due to one
 * being thrown.</li>
 * <li>2) When possible and practical, DocConverter implementations should be
 * classes nested within the {@code @interface} they are used to convert (the
 * example above demonstrates this).</li>
 * <li>3) If at all possible, developers should avoid any variables to
 * instantiate (IE: no-constructor).</li>
 * </ul>
 *
 * @see ConvertedBy
 */
@FunctionalInterface
public interface DocConverter<T extends Annotation>
{
	/**
	 * Returns a String processed from the contents of the provided
	 * {@link java.lang.annotation.Annotation Annotation}. <br>
	 * <b>Should never throw and/or encounter uncaught exceptions.</b>
	 *
	 * @param annotation
	 *            The annotation to process.
	 *
	 * @return A String processed from the Annotation provided.
	 */
	String read(T annotation);

}
