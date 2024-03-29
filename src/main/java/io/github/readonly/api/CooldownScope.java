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

package io.github.readonly.api;

public enum CooldownScope
{

	/**
	 * Applies the cooldown to the calling
	 * {@link net.dv8tion.jda.api.entities.User User} across all locations on
	 * this instance (IE: TextChannels, PrivateChannels, etc).
	 * <p>
	 * The key for
	 * this is generated in the format
	 * <ul>
	 * <li>{@code <command-name>|U:<userID>}</li>
	 * </ul>
	 */
	USER("U:%d", ""),

	/**
	 * Applies the cooldown to the
	 * {@link net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
	 * MessageChannel} the
	 * command is called in.
	 * <p>
	 * The key for this is generated in the format
	 * <ul>
	 * <li>{@code <command-name>|C:<channelID>}</li>
	 * </ul>
	 */
	CHANNEL("C:%d", "in this channel"),

	/**
	 * Applies the cooldown to the calling
	 * {@link net.dv8tion.jda.api.entities.User User} local to the
	 * {@link net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
	 * MessageChannel} the
	 * command is called in.
	 * <p>
	 * The key for this is generated in the format
	 * <ul>
	 * <li>{@code <command-name>|U:<userID>|C:<channelID>}</li>
	 * </ul>
	 */
	USER_CHANNEL("U:%d|C:%d", "in this channel"),

	/**
	 * Applies the cooldown to the {@link net.dv8tion.jda.api.entities.Guild
	 * Guild} the command is called in.
	 * <p>
	 * The key for this is generated in the
	 * format
	 * <ul>
	 * <li>{@code <command-name>|G:<guildID>}</li>
	 * </ul>
	 * <p>
	 * <b>NOTE:</b> This will automatically default back to
	 * {@link CooldownScope#CHANNEL CooldownScope.CHANNEL} when called in a
	 * private channel. This is done in order to prevent internal
	 * {@link java.lang.NullPointerException NullPointerException}s from being
	 * thrown while generating cooldown keys!
	 */
	GUILD("G:%d", "in this server"),

	/**
	 * Applies the cooldown to the calling
	 * {@link net.dv8tion.jda.api.entities.User User} local to the
	 * {@link net.dv8tion.jda.api.entities.Guild Guild} the command is called
	 * in.
	 * <p>
	 * The key for this is generated in the format
	 * <ul>
	 * <li>{@code <command-name>|U:<userID>|G:<guildID>}</li>
	 * </ul>
	 * <p>
	 * <b>NOTE:</b> This will automatically default back to
	 * {@link CooldownScope#CHANNEL CooldownScope.CHANNEL} when called in a
	 * private channel. This is done in order to prevent internal
	 * {@link java.lang.NullPointerException NullPointerException}s from being
	 * thrown while generating cooldown keys!
	 */
	USER_GUILD("U:%d|G:%d", "in this server"),

	/**
	 * Applies the cooldown to the calling Shard the command is called on.
	 * <p>
	 * The key for this is generated in the format
	 * <ul>
	 * <li>{@code <command-name>|S:<shardID>}</li>
	 * </ul>
	 * <p>
	 * <b>NOTE:</b> This
	 * will automatically default back to {@link CooldownScope#GLOBAL
	 * CooldownScope.GLOBAL} when {@link net.dv8tion.jda.api.JDA#getShardInfo()
	 * JDA#getShardInfo()} returns {@code null}. This is done in order to
	 * prevent internal {@link java.lang.NullPointerException
	 * NullPointerException}s from being thrown while generating cooldown keys!
	 */
	SHARD("S:%d", "on this shard"),

	/**
	 * Applies the cooldown to the calling
	 * {@link net.dv8tion.jda.api.entities.User User} on the Shard the command
	 * is called on.
	 * <p>
	 * The key for this is generated in the format
	 * <ul>
	 * <li>{@code <command-name>|U:<userID>|S:<shardID>}</li>
	 * </ul>
	 * <p>
	 * <b>NOTE:</b> This will automatically default back to
	 * {@link CooldownScope#USER CooldownScope.USER} when
	 * {@link net.dv8tion.jda.api.JDA#getShardInfo() JDA#getShardInfo()} returns
	 * {@code null}. This is done in order to prevent internal
	 * {@link java.lang.NullPointerException NullPointerException}s from being
	 * thrown while generating cooldown keys!
	 */
	USER_SHARD("U:%d|S:%d", "on this shard"),

	/**
	 * Applies this cooldown globally.
	 * <p>
	 * As this implies: the command will be
	 * unusable on the instance of JDA in all types of
	 * {@link net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
	 * MessageChannel}s until
	 * the cooldown has ended.
	 * <p>
	 * The key for this is
	 * {@code <command-name>|globally}
	 */
	GLOBAL("Global", "globally");

	private final String format;

	final String errorSpecification;

	CooldownScope(String format, String errorSpecification)
	{
		this.format = format;
		this.errorSpecification = errorSpecification;
	}

	public String genKey(String name, long id)
	{
		return genKey(name, id, -1);
	}

	public String genKey(String name, long idOne, long idTwo)
	{
		if (this.equals(GLOBAL))
		{
			return name + "|" + format;
		} else if (idTwo == -1)
		{
			return name + "|" + String.format(format, idOne);
		} else
		{
			return name + "|" + String.format(format, idOne, idTwo);
		}
	}

	/**
	 * @return the errorSpecification
	 */
	public String getErrorSpecification()
	{
		return errorSpecification;
	}
}
