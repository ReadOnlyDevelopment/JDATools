package io.github.readonly.api.rmi;

import java.io.Serializable;

public record RamUsage(long totalMemory, long freeMemory) implements Serializable
{

}
