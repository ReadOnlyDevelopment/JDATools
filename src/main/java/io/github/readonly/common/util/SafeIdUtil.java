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

package io.github.readonly.common.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.utils.MiscUtil;

@UtilityClass
public class SafeIdUtil
{
	/**
	 * Checks if the provided String ID is usable with {@link MiscUtil#parseSnowflake(String)
	 * MiscUtil.parseSnowflake(String)}.
	 *
	 * @param id
	 *            The String ID to be converted
	 *
	 * @return {@code true} if both of the following criteria are not met:
	 *         <ul>
	 *         <li>If the provided String throws a {@link java.lang.NumberFormatException NumberFormatException} when
	 *         used with {@link java.lang.Long#parseLong(String) Long.parseLong(String)}.</li>
	 *         <li>If the provided String is converted, but the returned {@code long} is negative.</li>
	 *         </ul>
	 */
	public static boolean checkId(String id)
	{
		try
		{
			long l = Long.parseLong(id.trim());
			return l >= 0;
		} catch (NumberFormatException e)
		{
			return false;
		}
	}
}
