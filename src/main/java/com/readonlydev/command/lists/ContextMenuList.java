package com.readonlydev.command.lists;

import java.util.List;

import com.readonlydev.command.ctx.ContextMenu;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ContextMenuList extends SpecialList<ContextMenuList, ContextMenu>
{
    private static final long serialVersionUID = -6654979706221716102L;

    public static ContextMenuList from(List<ContextMenu> list)
    {
        return new ContextMenuList(list);
    }

    private ContextMenuList(List<ContextMenu> fromList)
    {
        super(fromList);
    }

    @Override
    public ContextMenuList getSubListFrom(ContextMenuList specialList)
    {
        ContextMenuList newList = new ContextMenuList();
        for(ContextMenu cmd : specialList)
        {
            if(this.contains(cmd))
            {
                newList.add(cmd);
            }
        }
        return newList;
    }
}
