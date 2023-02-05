package io.github.readonly.command.option;

import javax.annotation.Nonnull;

import io.github.readonly.command.lists.ChoiceList;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@UtilityClass
public class RequiredOption extends Opt
{
	public static final OptionData trueFalse(@Nonnull String name, @Nonnull String description)
	{
		return Option.trueFalse(name, description).setRequired(true);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description)
	{
		return Option.text(name, description).setRequired(true);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength)
	{
		return text(name, description).setMaxLength(maxLength);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength)
	{
		return text(name, description).setMinLength(minLength).setMaxLength(maxLength);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull ChoiceList choices)
	{
		return text(name, description).addChoices(choices);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength, @Nonnull ChoiceList choices)
	{
		return text(name, description, maxLength).addChoices(choices);
	}

	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength, @Nonnull ChoiceList choices)
	{
		return text(name, description, minLength, maxLength).addChoices(choices);
	}

	public static final OptionData number(@Nonnull String name, @Nonnull String description)
	{
		return Option.number(name, description).setRequired(true);
	}

	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue)
	{
		return number(name, description).setRequiredRange(OptionData.MIN_NEGATIVE_NUMBER, toDouble(maxValue));
	}

	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue)
	{
		return number(name, description).setRequiredRange(toDouble(minValue), toDouble(maxValue));
	}

	public static final OptionData integer(@Nonnull String name, @Nonnull String description)
	{
		return Option.integer(name, description).setRequired(true);
	}

	public static final OptionData integer(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue)
	{
		return integer(name, description).setRequiredRange(OptionData.MIN_NEGATIVE_NUMBER, toDouble(maxValue));
	}

	public static final OptionData integer(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue)
	{
		return integer(name, description).setRequiredRange(toDouble(minValue), toDouble(maxValue));
	}

	public static final OptionData role(@Nonnull String name, @Nonnull String description)
	{
		return Option.role(name, description).setRequired(true);
	}

	public static final OptionData channel(@Nonnull String name, @Nonnull String description)
	{
		return Option.channel(name, description).setRequired(true);
	}

	public static final OptionData user(@Nonnull String name, @Nonnull String description)
	{
		return Option.user(name, description).setRequired(true);
	}
}
