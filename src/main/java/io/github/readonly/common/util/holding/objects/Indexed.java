package io.github.readonly.common.util.holding.objects;

import java.util.concurrent.atomic.AtomicInteger;

public class Indexed<T> {
	public static class Indexer {
		private final AtomicInteger indexer;

		public Indexer() {
			this(0);
		}

		public Indexer(int initialIndex) {
			indexer = new AtomicInteger(initialIndex);
		}

		public <T> Indexed<T> index(T t) {
			return new Indexed<T>(indexer.getAndIncrement(), t);
		}
	}

	private final int index;
	private final T value;

	public Indexed(int index, T value) {
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public T getValue() {
		return value;
	}

}

