package com.readonlydev.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public record  NoDuplicateList<T>(Collection<T> collection)
{
	@Override
	public Collection<T> collection()
	{
		return collection.stream().distinct().toList();
	}

	public <R> List<R> mapped(Function<T, R> function)
	{
		return collection().stream().map(function).toList();
	}
}
