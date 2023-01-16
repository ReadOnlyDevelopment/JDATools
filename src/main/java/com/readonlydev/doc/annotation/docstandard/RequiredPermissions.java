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

package com.readonlydev.doc.annotation.docstandard;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.*;

import com.readonlydev.doc.ConvertedBy;
import com.readonlydev.doc.annotation.DocConverter;

/**
 * A CommandDoc {@link java.lang.annotation.Annotation Annotation} that lists
 * required {@link Permission Permission}s a bot must have
 * to use a command on a {@link net.dv8tion.jda.api.entities.Guild Guild}.
 */
@ConvertedBy(RequiredPermissions.Converter.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE, ElementType.METHOD })
public @interface RequiredPermissions
{
	/**
	 * An array of {@link Permission Permission}s a bot must
	 * have to run the command.
	 *
	 * @return The array of permissions
	 */
	Permission[] value();

	/**
	 * The {@link com.readonlydev.doc.annotation.DocConverter DocConverter} for
	 * the
	 * {@link com.readonlydev.doc.annotation.docstandard.RequiredPermissions @RequiredPermissions}
	 * annotation.
	 */
	class Converter implements DocConverter<RequiredPermissions>
	{
		@Override
		public String read(RequiredPermissions annotation)
		{
			Permission[] permissions = annotation.value();

			StringBuilder b = new StringBuilder();

			b.append("Bot must have permissions:");
			switch (permissions.length)
			{
			case 0:
				b.append(" None");
				break;
			case 1:
				b.append(" `").append(permissions[0].getName()).append("`");
				break;
			default:
				for (int i = 0; i < permissions.length; i++)
				{
					b.append(" `").append(permissions[i]).append("`");
					if (i != permissions.length - 1)
						b.append(",");
				}
				break;
			}
			return b.toString();
		}
	}
}
