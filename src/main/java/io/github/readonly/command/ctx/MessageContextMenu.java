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

package io.github.readonly.command.ctx;

import io.github.readonly.command.event.MessageContextMenuEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

public abstract class MessageContextMenu extends ContextMenu
{
	/**
	 * Runs checks for the {@link MessageContextMenu} with the given
	 * {@link MessageContextMenuEvent} that called it.
	 * <br>
	 * Will terminate, and possibly respond with a failure message, if any
	 * checks fail.
	 *
	 * @param event
	 *            The MessageContextMenuEvent that triggered this menu
	 */
	public final void run(MessageContextMenuEvent event)
	{
		// owner check
		if (ownerCommand && !(event.isOwner()))
		{
			terminate(event, null);
			return;
		}

		// cooldown check, ignoring owner
		if ((cooldown > 0) && !(event.isOwner()))
		{
			String	key			= getCooldownKey(event);
			int		remaining	= event.getClient().getRemainingCooldown(key);
			if (remaining > 0)
			{
				terminate(event, getCooldownError(event, remaining, event.getClient()));
				return;
			} else
			{
				event.getClient().applyCooldown(key, cooldown);
			}
		}

		// availability check
		if (event.isFromGuild())
		{
			// user perms
			for (Permission p : userPermissions)
			{
				// Member will never be null because this is only ran in a
				// server (text channel)
				if (event.getMember() == null)
				{
					continue;
				}

				if (p.isChannel())
				{
					if (!event.getMember().hasPermission(event.getGuildChannel(), p))
					{
						terminate(event, String.format("%s%s%s", event.getClient().getError(), p.getName(), "channel"));
						return;
					}
				} else
				{
					if (!event.getMember().hasPermission(p))
					{
						terminate(event, String.format("%s%s%s", event.getClient().getError(), p.getName(), "server"));
						return;
					}
				}
			}

			// bot perms
			for (Permission p : botPermissions)
			{
				// We can ignore this permission because bots can reply with
				// embeds even without either of these perms.
				// The only thing stopping them is the user's ability to use
				// Application Commands.
				// It's extremely dumb, but what more can you do.
				if ((p == Permission.VIEW_CHANNEL) || (p == Permission.MESSAGE_EMBED_LINKS))
				{
					continue;
				}

				Member selfMember = event.getGuild() == null ? null : event.getGuild().getSelfMember();
				if (p.isChannel())
				{
					if (p.name().startsWith("VOICE"))
					{
						GuildVoiceState	gvc	= event.getMember().getVoiceState();
						AudioChannel	vc	= gvc == null ? null : gvc.getChannel();
						if (vc == null)
						{
							terminate(event, event.getClient().getError() + " You must be in a voice channel to use that!");
							return;
						} else if (!selfMember.hasPermission(vc, p))
						{
							terminate(event, String.format("%s%s%s", event.getClient().getError(), p.getName(), "voice channel"));
							return;
						}
					} else
					{
						if (!selfMember.hasPermission(event.getGuildChannel(), p))
						{
							terminate(event, String.format("%s%s%s", event.getClient().getError(), p.getName(), "channel"));
							return;
						}
					}
				} else
				{
					if (!selfMember.hasPermission(p))
					{
						terminate(event, String.format("%s%s%s", event.getClient().getError(), p.getName(), "server"));
						return;
					}
				}
			}
		}

		// run
		try
		{
			execute(event);
		} catch (Throwable t)
		{
			if (event.getClient().getListener() != null)
			{
				event.getClient().getListener().onMessageContextMenuException(event, this, t);
				return;
			}
			// otherwise we rethrow
			throw t;
		}

		if (event.getClient().getListener() != null)
		{
			event.getClient().getListener().onCompletedMessageContextMenu(event, this);
		}
	}

	/**
	 * The main body method of a {@link MessageContextMenu}.
	 * <br>
	 * This is the "response" for a successful
	 * {@link MessageContextMenu#run(MessageContextMenuEvent)}
	 *
	 * @param event
	 *            The {@link MessageContextMenuEvent} that triggered this menu.
	 */
	protected abstract void execute(MessageContextMenuEvent event);

	private void terminate(MessageContextMenuEvent event, String message)
	{
		if (message != null)
		{
			event.reply(message).setEphemeral(true).queue();
		}
		if (event.getClient().getListener() != null)
		{
			event.getClient().getListener().onTerminatedMessageContextMenu(event, this);
		}
	}
}
