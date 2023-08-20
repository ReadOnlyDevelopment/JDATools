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

package io.github.readonly.command;

import io.github.readonly.command.event.SlashCommandEvent;
import net.dv8tion.jda.api.entities.channel.ChannelType;

public abstract class DirectMessageSlashCommand extends SlashCommand
{
	private final String descriptionPrefix = "[DM ONLY] ";

	DirectMessageSlashCommand()
	{
		this.directMessagesAllowed();
	}

	@Override
	protected void description(String description)
	{
		this.help = descriptionPrefix + description;
	}

	@Override
	void run(SlashCommandEvent event)
	{
		if(!event.getChannelType().equals(ChannelType.PRIVATE))
		{
			terminate(event, "This command can only be used in DM's");
		}

		super.run(event);
	}
}