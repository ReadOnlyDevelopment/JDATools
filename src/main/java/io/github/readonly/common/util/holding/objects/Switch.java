package io.github.readonly.common.util.holding.objects;

import java.util.function.BooleanSupplier;

public class Switch implements BooleanSupplier {
	public boolean value;

	public Switch() {
		this(false);
	}

	public Switch(boolean value) {
		this.value = value;
	}

	@Override
	public boolean getAsBoolean() {
		return value;
	}

	@Override
	public int hashCode() {
		return (value ? 1 : 0);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Switch && ((Switch) obj).value == value;
	}

	public boolean is(boolean state) {
		return value == state;
	}

	public boolean isOff() {
		return is(false);
	}

	public boolean isOn() {
		return is(true);
	}

	public void set(boolean v) {
		value = v;
	}

	public void setOff() {
		set(false);
	}

	public void setOn() {
		set(true);
	}

	public boolean toggle() {
		value = !value;
		return value;
	}
}

