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

package io.github.readonly.command.operation;

import io.github.readonly.api.CooldownScope;
import net.dv8tion.jda.api.Permission;

public abstract class UserInteraction
{

	/**
	 * {@code true} if the command may only be used in a {@link net.dv8tion.jda.api.entities.Guild Guild}, {@code false}
	 * if it may be used in both a Guild and a DM. <br>
	 * Default {@code true}.
	 */
	protected boolean guildOnly = true;

	/**
	 * Any {@link Permission Permissions} a Member must have to use this interaction. <br>
	 * These are only checked in a {@link net.dv8tion.jda.api.entities.Guild Guild} environment. <br>
	 * To disable the command for everyone (for interactions), set this to {@code null}. <br>
	 * Keep in mind, commands may still show up if the channel permissions are updated in settings. Otherwise, commands
	 * will automatically be hidden unless a user has these perms. However, permissions are always checked, just in
	 * case. A user must have these permissions regardless.
	 */
	protected Permission[] userPermissions = new Permission[0];

	/**
	 * Any {@link Permission Permissions} the bot must have to use a command. <br>
	 * These are only checked in a {@link net.dv8tion.jda.api.entities.Guild Guild} environment.
	 */
	protected Permission[] botPermissions = new Permission[0];

	/**
	 * {@code true} if the interaction may only be used by a User with an ID matching the Owners or any of the
	 * CoOwners.<br>
	 * If enabled for a Slash Command, only owners (owner + up to 9 co-owners) will be added to the SlashCommand. All
	 * other permissions will be ignored. <br>
	 * Default {@code false}.
	 */
	protected boolean ownerCommand = false;

	/**
	 * An {@code int} number of seconds users must wait before using this command again.
	 */
	protected int cooldown = 0;

	/**
	 * The {@link CooldownScope CooldownScope} of the command. This defines how far the scope the cooldowns have. <br>
	 * Default {@link CooldownScope#USER CooldownScope.USER}.
	 */
	protected CooldownScope cooldownScope = CooldownScope.USER;

	/**
	 * The permission message used when the bot does not have the required permission. Requires 3 "%s", first is user
	 * mention, second is the permission needed, third is type, e.g. server.
	 */
	protected String botMissingPermMessage = "%s I need the %s permission in this %s!";

	/**
	 * The permission message used when the user does not have the required permission. Requires 3 "%s", first is user
	 * mention, second is the permission needed, third is type, e.g. server.
	 */
	protected String userMissingPermMessage = "%s You must have the %s permission in this %s to use that!";

	/**
	 * Gets the {@link UserInteraction#cooldown cooldown} for the Interaction.
	 *
	 * @return The cooldown for the Interaction
	 */
	public int getCooldown()
	{
		return cooldown;
	}

	/**
	 * Gets the {@link UserInteraction#cooldown cooldown} for the Interaction.
	 *
	 * @return The cooldown for the Interaction
	 */
	public CooldownScope getCooldownScope()
	{
		return cooldownScope;
	}

	/**
	 * Gets the {@link UserInteraction#userPermissions userPermissions} for the Interaction.
	 *
	 * @return The userPermissions for the Interaction
	 */
	public Permission[] getUserPermissions()
	{
		return userPermissions;
	}

	/**
	 * Gets the {@link UserInteraction#botPermissions botPermissions} for the Interaction.
	 *
	 * @return The botPermissions for the Interaction
	 */
	public Permission[] getBotPermissions()
	{
		return botPermissions;
	}

	/**
	 * Checks whether this is an owner only Interaction, meaning only the owner and co-owners can use it.
	 *
	 * @return {@code true} if the command is an owner interaction, otherwise {@code false} if it is not
	 */
	public boolean isOwnerCommand()
	{
		return ownerCommand;
	}
}
