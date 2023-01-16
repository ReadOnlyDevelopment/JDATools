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
