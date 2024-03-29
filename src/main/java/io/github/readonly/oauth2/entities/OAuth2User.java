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

package io.github.readonly.oauth2.entities;

import net.dv8tion.jda.api.sharding.ShardManager;
import io.github.readonly.oauth2.OAuth2Client;
import io.github.readonly.oauth2.session.Session;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;

/**
 * OAuth2 representation of a Discord User. <br>
 * More specifically, this is the
 * User that the session is currently managing when retrieved using
 * {@link io.github.readonly.oauth2.OAuth2Client#getUser(Session)
 * OAuth2Client#getUser}.
 */
public interface OAuth2User extends ISnowflake, IMentionable
{

	/**
	 * Gets the underlying {@link io.github.readonly.oauth2.OAuth2Client
	 * OAuth2Client} that created this OAuth2User.
	 *
	 * @return The OAuth2Client that created this OAuth2User.
	 */
	OAuth2Client getClient();

	/**
	 * Gets the originating {@link io.github.readonly.oauth2.session.Session} that
	 * is responsible for this OAuth2User.
	 *
	 * @return The Session responsible for this OAuth2User.
	 */
	Session getSession();

	/**
	 * Gets the user's Snowflake ID as a String.
	 *
	 * @return The user's Snowflake ID as a String.
	 */
	@Override
	String getId();

	/**
	 * Gets the user's Snowflake ID as a {@code long}.
	 *
	 * @return The user's Snowflake ID as a {@code long}.
	 */
	@Override
	long getIdLong();

	/**
	 * Gets the user's account name.
	 *
	 * @return The user's account name.
	 */
	String getName();

	/**
	 * Gets the user's email address that is associated with their Discord
	 * account.
	 * <p>
	 * Note that if this user is acquired without the
	 * '{@link io.github.readonly.oauth2.Scope#EMAIL email}' OAuth
	 * {@link io.github.readonly.oauth2.Scope Scope}, this will throw a
	 * {@link io.github.readonly.oauth2.exceptions.MissingScopeException
	 * MissingScopeException}.
	 *
	 * @return The user's email.
	 *
	 * @throws io.github.readonly.oauth2.exceptions.MissingScopeException
	 *             If the corresponding {@link OAuth2User#getSession() session}
	 *             does
	 *             not have the proper 'email' OAuth2 scope
	 */
	String getEmail();

	/**
	 * Returns {@code true} if the user's Discord account has been verified via
	 * email.
	 * <p>
	 * This is required to send messages in guilds where certain
	 * moderation levels are used.
	 *
	 * @return {@code true} if the user has verified their account,
	 *         {@code false} otherwise.
	 */
	boolean isVerified();

	/**
	 * Returns {@code true} if this user has multi-factor authentication
	 * enabled.
	 * <p>
	 * Some guilds require mfa for administrative actions.
	 *
	 * @return {@code true} if the user has mfa enabled, {@code false}
	 *         otherwise.
	 */
	boolean isMfaEnabled();

	/**
	 * Gets the user's discriminator.
	 *
	 * @return The user's discriminator.
	 */
	String getDiscriminator();

	/**
	 * Gets the user's avatar ID, or {@code null} if they have not set one.
	 *
	 * @return The user's avatar ID, or {@code null} if they have not set one.
	 */
	String getAvatarId();

	/**
	 * Gets the user's avatar URL, or {@code null} if they have not set one.
	 *
	 * @return The user's avatar URL, or {@code null} if they have not set one.
	 */
	String getAvatarUrl();

	/**
	 * Gets the user's avatar URL.
	 *
	 * @return The user's avatar URL.
	 */
	String getDefaultAvatarId();

	/**
	 * Gets the user's default avatar ID.
	 *
	 * @return The user's default avatar ID.
	 */
	String getDefaultAvatarUrl();

	/**
	 * Gets the user's avatar URL, or their {@link #getDefaultAvatarUrl()
	 * default avatar URL} if they do not have a custom avatar set on their
	 * account.
	 *
	 * @return The user's effective avatar URL.
	 */
	String getEffectiveAvatarUrl();

	/**
	 * Gets the user as a discord formatted mention: <br>
	 * {@code <@SNOWFLAKE_ID> }
	 *
	 * @return A discord formatted mention of this user.
	 */
	@Override
	String getAsMention();

	/**
	 * Gets the corresponding {@link User JDA User}
	 * from the provided instance of {@link net.dv8tion.jda.api.JDA JDA}.
	 * <p>
	 * Note that there is no guarantee that this will not return {@code null} as
	 * the instance of JDA may not have access to the User.
	 * <p>
	 * For sharded
	 * bots, use {@link OAuth2User#getJDAUser(ShardManager)}.
	 *
	 * @param jda
	 *            The instance of JDA to get from.
	 *
	 * @return A JDA User, possibly {@code null}.
	 */
	User getJDAUser(JDA jda);

	/**
	 * Gets the corresponding {@link User JDA User}
	 * from the provided {@link net.dv8tion.jda.api.sharding.ShardManager
	 * ShardManager}.
	 * <p>
	 * Note that there is no guarantee that this will not
	 * return {@code null} as the ShardManager may not have access to the User.
	 * <p>
	 * For un-sharded bots, use {@link OAuth2User#getJDAUser(JDA)}.
	 *
	 * @param shardManager
	 *            The ShardManager to get from.
	 *
	 * @return A JDA User, possibly {@code null}.
	 */
	User getJDAUser(ShardManager shardManager);
}
