package io.github.readonly.command.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class OptionsList extends ArrayList<OptionData>
{
	private static final long serialVersionUID = 7636927564248226686L;

	private final List<String> nameList = new ArrayList<>();

	public OptionsList()
	{
		super();
	}

	public OptionsList(OptionData... choices)
	{
		this(Arrays.asList(choices));
	}

	public OptionsList(Collection<? extends OptionData> c)
	{
		super(c);
	}

	@Override
	public boolean addAll(Collection<? extends OptionData> c)
	{
		return super.addAll(c.stream().filter(t -> !this.contains(t)).collect(Collectors.toList()));
	}

	@Override
	public boolean add(OptionData e)
	{
		if(!this.contains(e))
		{
			this.nameList.add(e.getName());
			return super.add(e);
		}
		return false;
	}

	@Override
	public boolean contains(Object o)
	{
		if(!(o instanceof OptionData))
		{
			return false;
		}

		OptionData data = (OptionData) o;
		return this.nameList.contains(data.getName());
	}
}
