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

package io.github.readonly.common.util.holding.objects;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Holder<T> extends Object implements Supplier<T>, Consumer<T>, UnaryOperator<T> {
	public T var;

	public Holder() {
	}

	public Holder(T object) {
		var = object;
	}

	@Override
	public void accept(T t) {
		var = t;
	}

	@Override
	public T apply(T t) {
		T r = get();
		accept(t);
		return r;
	}

	@Override
	public T get() {
		return var;
	}

	@Override
	public int hashCode() {
		return var.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Holder) && Objects.equals(((Holder<?>) obj).var, var);
	}

	@Override
	public String toString() {
		return String.format("Holder{var=%s}", var);
	}

	public void accept(Holder<T> holder) {
		var = holder.var;
	}
}

