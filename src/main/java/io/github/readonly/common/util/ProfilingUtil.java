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

import java.util.List;
import java.util.regex.Pattern;

public class ProfilingUtil
{
	private static final int KB_SIZE = 1024;
	private static final int MB_SIZE = 1024 * 1024;
	private static final int GB_SIZE = 1024 * 1024 * 1024;

	private static final Pattern XMX_REGEX = Pattern.compile("-[xX][mM][xX]([a-zA-Z0-9]+)");

	public static Long getJvmXmxBytes(List<String> jvmArgs) {
		Long result = null;
		if (jvmArgs == null) {
			return null;
		}

		for (String entry : jvmArgs) {
			final var matcher = XMX_REGEX.matcher(entry);
			if (matcher.matches()) {
				String str = matcher.group(1);
				result = ProfilingUtil.getBytesValueOrNull(str);
			}
		}

		return result;
	}

	public static Long getBytesValueOrNull(String str) {
		if ((str == null) || str.isEmpty()) {
			return null;
		}

		str = str.toLowerCase();
		int scale = 1;

		try {
			if (str.endsWith("kb")) {
				str = str.substring(0, str.length() - 2).trim();
				scale = KB_SIZE;
			}
			final String trim = str.substring(0, str.length() - 1).trim();
			if (str.endsWith("k")) {
				str = trim;
				scale = KB_SIZE;
			} else if (str.endsWith("mb")) {
				str = str.substring(0, str.length() - 2).trim();
				scale = MB_SIZE;
			} else if (str.endsWith("m")) {
				str = trim;
				scale = MB_SIZE;
			} else if (str.endsWith("gb")) {
				str = str.substring(0, str.length() - 2).trim();
				scale = GB_SIZE;
			} else if (str.endsWith("g")) {
				str = trim;
				scale = GB_SIZE;
			} else if (str.endsWith("bytes")) {
				str = str.substring(0, str.length() - "bytes".length()).trim();
				scale = 1;
			}

			str = str.replace(",", "");

			if (!str.chars().allMatch(Character::isDigit)) {
				return null;
			}

			double doubleValue = Double.parseDouble(str);
			return (long) (doubleValue * scale);
		} catch (Throwable ex) {
			return null;
		}
	}
}
