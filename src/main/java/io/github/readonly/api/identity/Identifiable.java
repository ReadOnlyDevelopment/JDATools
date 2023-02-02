package io.github.readonly.api.identity;

import java.util.UUID;

public interface Identifiable
{
	UUID getUniqueId();

	default UUID newRandom()
	{
		return UUID.randomUUID();
	}
}
