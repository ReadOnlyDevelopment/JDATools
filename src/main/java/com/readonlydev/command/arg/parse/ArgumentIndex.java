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

package com.readonlydev.command.arg.parse;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentIndex
{

	private List<IArgument<String>>	idx;
	private String					args;
	private final Pattern			multiWord	= Pattern.compile("(?>\")\\s*(?:.*?)\\s*(?>\")");

	public ArgumentIndex(String args)
	{
		this.idx = new LinkedList<>();
		buildIndex(args);
	}

	private void buildIndex(String args)
	{
		if (args.length() > 0)
		{
			this.args = args;
			String	removeFromArr	= "";
			Matcher	matcher			= multiWord.matcher(args);
			if (matcher.find())
			{
				removeFromArr = matcher.group(0).replace("'", "");
				args = args.replace(matcher.group(0), "");
			}
			String[]	idxArray	= args.split("\\s+");
			int			c			= 0;
			for (int i = 0; i < idxArray.length; i++)
			{
				idx.add(i, new Argument(idxArray[i]));
				c = +1;
			}
			if (removeFromArr.length() > 0)
			{
				idx.add(c, new Argument(removeFromArr));
			}
		}
	}

	public Argument getArg(Integer index)
	{
		try
		{
			return (Argument) idx.get(index);
		} catch (IndexOutOfBoundsException e)
		{
			return new Argument("");
		}
	}

	public boolean isEmpty()
	{
		return idx.isEmpty();
	}

	public int count()
	{
		return idx.size();
	}

	public List<IArgument<String>> list()
	{
		return idx;
	}

	public String[] getChildArgArray()
	{
		return args.split("\\s+", 2);
	}
}
