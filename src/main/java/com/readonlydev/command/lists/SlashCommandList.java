package com.readonlydev.command.lists;

import java.util.List;

import com.readonlydev.command.slash.SlashCommand;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SlashCommandList extends SpecialList<SlashCommandList, SlashCommand>
{
	private static final long serialVersionUID = -7049482075284773427L;

	public static SlashCommandList from(List<SlashCommand> list)
	{
		return new SlashCommandList(list);
	}

	private SlashCommandList(List<SlashCommand> fromList)
	{
		super(fromList);
	}

	@Override
	public SlashCommandList getSubListFrom(SlashCommandList specialList)
	{
		SlashCommandList newList = new SlashCommandList();
		for (SlashCommand cmd : specialList)
		{
			if (this.contains(cmd))
			{
				newList.add(cmd);
			}
		}
		return newList;
	}
}
