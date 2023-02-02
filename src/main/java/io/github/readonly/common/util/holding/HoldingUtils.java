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

