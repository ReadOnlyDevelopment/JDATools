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

package com.github.readonlydevelopment.oauth2.entities.impl;

import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

import com.github.readonlydevelopment.oauth2.OAuth2Client;
import com.github.readonlydevelopment.oauth2.entities.OAuth2Guild;

/**
 *
 * 
 */
public class OAuth2GuildImpl implements OAuth2Guild
{

	private final OAuth2Client client;

	private final long id;

	private final String name, icon;

	private final boolean owner;

	private final int permissions;

	public OAuth2GuildImpl(OAuth2Client client, long id, String name, String icon, boolean owner, int permissions)
	{
		this.client = client;
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.owner = owner;
		this.permissions = permissions;
	}

	@Override
	public OAuth2Client getClient()
	{
		return client;
	}

	@Override
	public long getIdLong()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getIconId()
	{
		return icon;
	}

	@Override
	public String getIconUrl()
	{
		return icon == null ? null : "https://cdn.discordapp.com/icons/" + id + "/" + icon + ".png";
	}

	@Override
	public int getPermissionsRaw()
	{
		return permissions;
	}

	@Override
	public EnumSet<Permission> getPermissions()
	{
		return Permission.getPermissions(permissions);
	}

	@Override
	public boolean isOwner()
	{
		return owner;
	}

	@Override
	public boolean hasPermission(Permission... perms)
	{
		if (isOwner())
			return true;

		long	adminPermRaw	= Permission.ADMINISTRATOR.getRawValue();
		int		permissions		= getPermissionsRaw();

		if ((permissions & adminPermRaw) == adminPermRaw)
			return true;

		long checkPermsRaw = Permission.getRaw(perms);

		return (permissions & checkPermsRaw) == checkPermsRaw;
	}
}
