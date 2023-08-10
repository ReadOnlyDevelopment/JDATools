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

package io.github.readonly.common.util.holding;

import java.util.Iterator;
import java.util.function.Function;

import io.github.readonly.common.util.holding.objects.Holder;
import io.github.readonly.common.util.holding.objects.Indexed;
import io.github.readonly.common.util.holding.objects.Indexed.Indexer;
import io.github.readonly.common.util.holding.objects.Pointer;
import io.github.readonly.common.util.holding.objects.Switch;

public class HoldingUtils {
	public static <T> Holder<T> hold(T obj) {
		return new Holder<>(obj);
	}

	public static <T> Function<T, Indexed<T>> index() {
		Indexer indexer = new Indexer();
		return indexer::index;
	}

	public static <T> Iterator<Indexed<T>> indexed(Iterator<T> iterator) {
		Indexer indexer = new Indexer();
		return new Iterator<Indexed<T>>() {
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Indexed<T> next() {
				return indexer.index(iterator.next());
			}
		};
	}

	public static <T> Iterable<Indexed<T>> indexed(Iterable<T> iterable) {
		return () -> indexed(iterable.iterator());
	}

	public static <T> Pointer<T> point(T obj) {
		return new Pointer<>(obj);
	}

	public static Switch switchFor(boolean value) {
		return new Switch(value);
	}
}

