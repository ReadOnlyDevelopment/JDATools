package io.github.readonly.command.option;

import javax.annotation.Nonnull;

import io.github.readonly.command.lists.ChoiceList;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@UtilityClass
public class Option extends Opt
{
	public static final OptionData text(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.STRING, name, description);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength)
	{
		return Option.text(name, description).setMaxLength(maxLength);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength)
	{
		return Option.text(name, description).setMinLength(minLength).setMaxLength(maxLength);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull ChoiceList choices)
	{
		return Option.text(name, description).addChoices(choices);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength, @Nonnull ChoiceList choices)
	{
		return Option.text(name, description, maxLength).addChoices(choices);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength, @Nonnull ChoiceList choices)
	{
		return Option.text(name, description, minLength, maxLength).addChoices(choices);
	}

	public static final OptionData trueFalse(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.BOOLEAN, name, description);
	}

	public static final OptionData number(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.NUMBER, name, description);
	}

	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue)
	{
		return Option.number(name, description).setRequiredRange(OptionData.MIN_NEGATIVE_NUMBER, toDouble(maxValue));
	}

	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue)
	{
		return Option.number(name, description, maxValue).setRequiredRange(toDouble(minValue), toDouble(maxValue));
	}

	public static final OptionData integer(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.INTEGER, name, description);
	}

	public static final OptionData integer(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue)
	{
		return Option.number(name, description).setRequiredRange(OptionData.MIN_NEGATIVE_NUMBER, toDouble(maxValue));
	}

	public static final OptionData integer(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue)
	{
		return Option.number(name, description, maxValue).setRequiredRange(toDouble(minValue), toDouble(maxValue));
	}

	public static final OptionData role(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.ROLE, name, description);
	}

	public static final OptionData channel(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.CHANNEL, name, description);
	}

	public static final OptionData user(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.USER, name, description);
	}
}
