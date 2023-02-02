package io.github.readonly.common.util.holding.objects;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Pointer<T> extends Object implements Supplier<T>, Consumer<T>, UnaryOperator<T> {
	private WeakReference<T> var = new WeakReference<>(null);

	public Pointer() {
	}

	public Pointer(T object) {
		var = new WeakReference<>(object);
	}

	@Override
	public void accept(T t) {
		var = new WeakReference<>(t);
	}

	@Override
	public T apply(T t) {
		T r = get();
		accept(t);
		return r;
	}

	@Override
	public T get() {
		return var.get();
	}

	@Override
	public int hashCode() {
		return var.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Pointer && Objects.equals(((Pointer<?>) obj).var.get(), var.get());
	}

	@Override
	public String toString() {
		return String.format("Pointer{var=%s}", var);
	}

	public void accept(Pointer<T> pointer) {
		accept(pointer.get());
	}
}

