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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class OptionsList extends ArrayList<OptionData>
{
	private static final long serialVersionUID = 7636927564248226686L;

	private final List<String> nameList = new ArrayList<>();

	public OptionsList()
	{
		super();
	}

	public OptionsList(OptionData... choices)
	{
		this(Arrays.asList(choices));
	}

	public OptionsList(Collection<? extends OptionData> c)
	{
		super(c);
	}

	@Override
	public boolean addAll(Collection<? extends OptionData> c)
	{
		return super.addAll(c.stream().filter(t -> !this.contains(t)).collect(Collectors.toList()));
	}

	@Override
	public boolean add(OptionData e)
	{
		if(!this.contains(e))
		{
			this.nameList.add(e.getName());
			return super.add(e);
		}
		return false;
	}

	@Override
	public boolean contains(Object o)
	{
		if(!(o instanceof OptionData))
		{
			return false;
		}

		OptionData data = (OptionData) o;
		return this.nameList.contains(data.getName());
	}
}
