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

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.utils.MiscUtil;

/**
 * A Utilities class for safely checking and converting String IDs to longs usable with
 * {@link MiscUtil#parseSnowflake(String) MiscUtil.parseSnowflake(String)}, a utility used in several
 * {@code Object#getXById(String)} methods.
 * <p>
 * This class contains two static methods:
 * <ul>
 * <li>{@link SafeIdUtil#safeConvert(String) SafeIdUtil.safeConvert(String)} - Safely converts a String to a format
 * usable with {@code MiscUtil.parseSnowflake(String)}.</li>
 * <li>{@link SafeIdUtil#checkId(String) SafeIdUtil.checkId(String)} - Checks if a String is safe to use with
 * {@code MiscUtil.parseSnowflake(String)} as it is.</li>
 * </ul>
 *
 * @since 1.2
 */
@UtilityClass
public class SafeIdUtil
{
	/**
	 * Safely convert the provided String ID to a {@code long} usable with {@link MiscUtil#parseSnowflake(String)
	 * MiscUtil.parseSnowflake(String)}.
	 *
	 * @param id
	 *            The String ID to be converted
	 *
	 * @return If the String can be converted into a non-negative {@code long}, then it will return the conversion. <br>
	 *         However, if one of the following criteria is met, then this method will return {@code 0L}:
	 *         <ul>
	 *         <li>If the provided String throws a {@link java.lang.NumberFormatException NumberFormatException} when
	 *         used with {@link java.lang.Long#parseLong(String) Long.parseLong(String)}.</li>
	 *         <li>If the provided String is converted, but the returned {@code long} is negative.</li>
	 *         </ul>
	 */
	public static long safeConvert(String id)
	{
		try
		{
			long l = Long.parseLong(id.trim());
			if (l < 0)
			{
				return 0L;
			}
			return l;
		} catch (NumberFormatException e)
		{
			return 0L;
		}
	}

	public static String safeConvert(long id)
	{
		try
		{
			String s = Long.toString(id);
			if (FinderUtil.DISCORD_ID.matcher(s).matches())
			{
				return s;
			}
			return "";
		} catch (Exception e)
		{
			return "";
		}
	}

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
