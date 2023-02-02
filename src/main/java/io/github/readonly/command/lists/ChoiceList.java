package io.github.readonly.command.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.github.readonly.command.option.Choice;
import io.github.readonly.common.util.KeyValueSupplier;
import net.dv8tion.jda.api.interactions.commands.Command;

public class ChoiceList extends ArrayList<Command.Choice>
{
	private static final long serialVersionUID = -6992807012985366031L;
	private final List<String> nameList = new ArrayList<>();

	public ChoiceList()
	{
		super();
	}

	public ChoiceList(Command.Choice... choices)
	{
		this(Arrays.asList(choices));
	}

	public ChoiceList(Collection<? extends Command.Choice> c)
	{
		super(c);
	}

	public static ChoiceList of(Command.Choice... choices)
	{
		return new ChoiceList(choices);
	}

	public static <T extends Enum<T> & KeyValueSupplier> ChoiceList toList(Class<T> e)
	{
		final ChoiceList list = new ChoiceList();
		if(e.isEnum())
		{
			for(T t : e.getEnumConstants())
			{
				list.add(Choice.add(((KeyValueSupplier)t).key(), ((KeyValueSupplier)t).value()));
			}
		}

		return list;
	}

	@Override
	public boolean addAll(Collection<? extends Command.Choice> c)
	{
		return super.addAll(c.stream().filter(t -> !this.contains(t)).collect(Collectors.toList()));
	}

	@Override
	public boolean add(Command.Choice e)
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
		if(!(o instanceof Command.Choice))
		{
			return false;
		}

		Command.Choice data = (Command.Choice) o;
		return this.nameList.contains(data.getName());
	}
}
