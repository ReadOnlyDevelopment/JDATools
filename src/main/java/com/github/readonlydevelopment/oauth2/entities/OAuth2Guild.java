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

package com.github.readonlydevelopment.oauth2.entities;

import java.util.EnumSet;

import com.github.readonlydevelopment.oauth2.OAuth2Client;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ISnowflake;

/**
 * OAuth2 representation of a Discord Server/Guild.
 * <p>
 * Note that this is
 * effectively a wrapper for both the Guild info, as well as the info on the
 * user in the guild represented by the session that got this Guild.
 */
public interface OAuth2Guild extends ISnowflake
{

	/**
	 * Gets the underlying {@link com.github.readonlydevelopment.oauth2.OAuth2Client
	 * OAuth2Client} that created this OAuth2Guild.
	 *
	 * @return The OAuth2Client that created this OAuth2Guild.
	 */
	OAuth2Client getClient();

	/**
	 * Gets the Guild's name.
	 *
	 * @return The Guild's name.
	 */
	String getName();

	/**
	 * Gets the Guild's icon ID, or {@code null} if the Guild does not have an
	 * icon.
	 *
	 * @return The Guild's icon ID.
	 */
	String getIconId();

	/**
	 * Gets the Guild's icon URL, or {@code null} if the Guild does not have an
	 * icon.
	 *
	 * @return The Guild's icon URL.
	 */
	String getIconUrl();

	/**
	 * Gets the Session User's raw permission value for the Guild.
	 *
	 * @return The Session User's raw permission value for the Guild.
	 */
	int getPermissionsRaw();

	/**
	 * Gets the Session User's {@link Permission
	 * Permissions} for the Guild.
	 *
	 * @return The Session User's Permissions for the Guild.
	 */
	EnumSet<Permission> getPermissions();

	/**
	 * Whether or not the Session User is the owner of the Guild.
	 *
	 * @return {@code true} if the Session User is the owner of the Guild,
	 *         {@code false} otherwise.
	 */
	boolean isOwner();

	/**
	 * Whether or not the Session User has all of the specified
	 * {@link Permission Permissions} in the Guild.
	 *
	 * @param perms
	 *            The Permissions to check for.
	 *
	 * @return {@code true} if and only if the Session User has all of the
	 *         specified Permissions, {@code false} otherwise.
	 */
	boolean hasPermission(Permission... perms);
}
