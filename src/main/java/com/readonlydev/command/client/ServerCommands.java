package com.readonlydev.command.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.readonlydev.command.ctx.ContextMenu;
import com.readonlydev.command.slash.SlashCommand;
import com.readonlydev.common.utils.SafeIdUtil;

public final class ServerCommands
{
	private final long			serverId;
	private List<SlashCommand>	slashCommands;
	private List<ContextMenu>	contextMenus;

	public ServerCommands(long serverId)
	{
		this.serverId = serverId;
		this.slashCommands = new LinkedList<>();
		this.contextMenus = new LinkedList<>();
	}

	public ServerCommands(String serverId)
	{
		this(SafeIdUtil.safeConvert(serverId));
	}

	public ServerCommands addAllCommands(Collection<SlashCommand> commandCollection)
	{
		slashCommands.addAll(commandCollection);
		return this;
	}

	public ServerCommands addAllCommands(SlashCommand... commands)
	{
		slashCommands.addAll(Arrays.asList(commands));
		return this;
	}

	public void addContextMenus(ContextMenu... menus)
	{
		contextMenus.addAll(Arrays.asList(menus));
	}

	long getServerId()
	{
		return serverId;
	}

	List<SlashCommand> getSlashCommands()
	{
		return slashCommands;
	}

	List<ContextMenu> getContextMenus()
	{
		return contextMenus;
	}
}
