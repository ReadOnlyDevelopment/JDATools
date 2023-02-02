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

package io.github.readonly.command.included;

import java.util.HashMap;
import java.util.Map;

public enum CommandButton
{

	LEFT("\u25C0"),
	STOP("\u23F9"),
	RIGHT("\u25B6"),
	LEAVE("\uD83D\uDEAA");

	private final String emote;

	public final static Map<String, CommandButton> CONSTANTS = new HashMap<String, CommandButton>();

	static
	{
		for (CommandButton c : values())
		{
			CONSTANTS.put(c.emote, c);
		}
	}

	CommandButton(String fromFormatted)
	{
		this.emote = fromFormatted;
	}

	public String get()
	{
		return emote;
	}

	public static CommandButton get(String value)
	{
		CommandButton constant = CONSTANTS.get(value);
		if (constant == null)
		{
			throw new IllegalArgumentException(value);
		} else
		{
			return constant;
		}
	}
}
