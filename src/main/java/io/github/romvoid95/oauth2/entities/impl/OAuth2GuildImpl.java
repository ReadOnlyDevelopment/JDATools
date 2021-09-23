package io.github.romvoid95.oauth2.entities.impl;

import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

import io.github.romvoid95.oauth2.OAuth2Client;
import io.github.romvoid95.oauth2.entities.OAuth2Guild;

/**
 *
 * 
 */
public class OAuth2GuildImpl implements OAuth2Guild {
    private final OAuth2Client client;
    private final long id;
    private final String name, icon;
    private final boolean owner;
    private final int permissions;

    public OAuth2GuildImpl(OAuth2Client client, long id, String name, String icon, boolean owner, int permissions) {
	this.client = client;
	this.id = id;
	this.name = name;
	this.icon = icon;
	this.owner = owner;
	this.permissions = permissions;
    }

    @Override
    public OAuth2Client getClient() {
	return client;
    }

    @Override
    public long getIdLong() {
	return id;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getIconId() {
	return icon;
    }

    @Override
    public String getIconUrl() {
	return icon == null ? null : "https://cdn.discordapp.com/icons/" + id + "/" + icon + ".png";
    }

    @Override
    public int getPermissionsRaw() {
	return permissions;
    }

    @Override
    public EnumSet<Permission> getPermissions() {
	return Permission.getPermissions(permissions);
    }

    @Override
    public boolean isOwner() {
	return owner;
    }

    @Override
    public boolean hasPermission(Permission... perms) {
	if (isOwner())
	    return true;

	long adminPermRaw = Permission.ADMINISTRATOR.getRawValue();
	int permissions = getPermissionsRaw();

	if ((permissions & adminPermRaw) == adminPermRaw)
	    return true;

	long checkPermsRaw = Permission.getRaw(perms);

	return (permissions & checkPermsRaw) == checkPermsRaw;
    }
}
