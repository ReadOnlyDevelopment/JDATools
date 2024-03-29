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

package io.github.readonly.command.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.readonly.command.ctx.ContextMenu;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContextMenuList extends SpecialList<ContextMenuList, ContextMenu>
{
	private static final long serialVersionUID = -6654979706221716102L;

	private List<String> commandNames;

	public static ContextMenuList from(Collection<ContextMenu> list)
	{
		return new ContextMenuList(new NoDuplicateList<>(list));
	}

	private ContextMenuList(NoDuplicateList<ContextMenu> noDuplicateList)
	{
		super(noDuplicateList.distinctCollection());
		this.commandNames = noDuplicateList.mapped(ContextMenu::getName);
	}

	private boolean notInList(String e)
	{
		return this.commandNames.lastIndexOf(e) == -1;
	}

	@Override
	public boolean add(ContextMenu element)
	{
		if(notInList(element.getName()))
		{
			return super.add(element);
		}

		return false;
	}

	@Override
	public boolean addAll(Collection<? extends ContextMenu> c)
	{
		List<ContextMenu> temp = new ArrayList<>();
		for(ContextMenu sc : c)
		{
			if(notInList(sc.getName()))
			{
				temp.add(sc);
			}
		}
		return super.addAll(temp);
	}

	@Override
	public ContextMenuList getSubListFrom(ContextMenuList specialList)
	{
		ContextMenuList newList = new ContextMenuList();
		for (ContextMenu cmd : specialList)
		{
			if (this.contains(cmd))
			{
				newList.add(cmd);
			}
		}
		return newList;
	}
}
