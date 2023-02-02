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

