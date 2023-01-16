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

package com.readonlydev.common.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;

public class ColorUtil
{

	public static final int	BLACK		= 0xFF000000;
	public static final int	DKGRAY		= 0xFF444444;
	public static final int	GRAY		= 0xFF888888;
	public static final int	LTGRAY		= 0xFFCCCCCC;
	public static final int	WHITE		= 0xFFFFFFFF;
	public static final int	RED			= 0xFFFF0000;
	public static final int	GREEN		= 0xFF00FF00;
	public static final int	BLUE		= 0xFF0000FF;
	public static final int	YELLOW		= 0xFFFFFF00;
	public static final int	CYAN		= 0xFF00FFFF;
	public static final int	MAGENTA		= 0xFFFF00FF;
	public static final int	TRANSPARENT	= 0;

	private static final HashMap<String, Integer> sColorNameMap;

	static
	{
		sColorNameMap = new HashMap<>();
		sColorNameMap.put("black", BLACK);
		sColorNameMap.put("darkgray", DKGRAY);
		sColorNameMap.put("gray", GRAY);
		sColorNameMap.put("lightgray", LTGRAY);
		sColorNameMap.put("white", WHITE);
		sColorNameMap.put("red", RED);
		sColorNameMap.put("green", GREEN);
		sColorNameMap.put("blue", BLUE);
		sColorNameMap.put("yellow", YELLOW);
		sColorNameMap.put("cyan", CYAN);
		sColorNameMap.put("magenta", MAGENTA);
		sColorNameMap.put("aqua", 0xFF00FFFF);
		sColorNameMap.put("fuchsia", 0xFFFF00FF);
		sColorNameMap.put("darkgrey", DKGRAY);
		sColorNameMap.put("grey", GRAY);
		sColorNameMap.put("lightgrey", LTGRAY);
		sColorNameMap.put("lime", 0xFF00FF00);
		sColorNameMap.put("maroon", 0xFF800000);
		sColorNameMap.put("navy", 0xFF000080);
		sColorNameMap.put("olive", 0xFF808000);
		sColorNameMap.put("purple", 0xFF800080);
		sColorNameMap.put("silver", 0xFFC0C0C0);
		sColorNameMap.put("teal", 0xFF008080);
	}

	public static int parseColor(String hex)
	{
		if (hex.charAt(0) == '#')
		{
			long color = Long.parseLong(hex.substring(1), 16);
			if (hex.length() == 7)
			{
				// Set the alpha value
				color |= 0x00000000ff000000;
			} else if (hex.length() != 9)
			{
				throw new IllegalArgumentException("Unknown color");
			}
			return (int) color;
		} else
		{
			Integer color = sColorNameMap.get(hex.toLowerCase(Locale.ROOT));
			if (color != null)
			{
				return color;
			}
		}
		throw new IllegalArgumentException("Unknown color");
	}

	public static String getHexValue(Color color)
	{
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}
