package com.readonlydev.command.lists;

import java.util.LinkedList;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class SpecialList<T, V> extends LinkedList<V>
{

    private static final long serialVersionUID = 1L;

    public SpecialList(List<V> fromList)
    {
        this.addAll(fromList);
    }

    @Override
    public boolean add(V e)
    {
        if(!this.contains(e))
        {
            return super.add(e);
        }

        return false;
    }

    public abstract T getSubListFrom(T specialList);

}
