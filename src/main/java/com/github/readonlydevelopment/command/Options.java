package com.github.readonlydevelopment.command;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Options
{

	/**
	 * Text.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData text(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.STRING, name, description);
	}

	/**
	 * Text.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param maxLength
	 *                    the max length
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength)
	{
		return Options.text(name, description)
			.setMaxLength(maxLength);
	}

	/**
	 * Text.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param minLength
	 *                    the min length
	 * @param maxLength
	 *                    the max length
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength)
	{
		return Options.text(name, description)
			.setMinLength(minLength)
			.setMaxLength(maxLength);
	}

	/**
	 * Text.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
	{
		return Options.text(name, description)
			.addChoices(choices)
			.setAutoComplete(true);
	}

	/**
	 * Text.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param maxLength
	 *                    the max length
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength, @Nonnull Choice... choices)
	{
		return Options.text(name, description, choices)
			.setMaxLength(maxLength);
	}

	/**
	 * Text.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param minLength
	 *                    the min length
	 * @param maxLength
	 *                    the max length
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength, @Nonnull Choice... choices)
	{
		return Options.text(name, description, choices)
			.setMinLength(minLength)
			.setMaxLength(maxLength);
	}

	/**
	 * Number.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData number(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.INTEGER, name, description);
	}

	/**
	 * Number.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param maxValue
	 *                    the max value
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue)
	{
		return Options.number(name, description)
			.setMaxValue(Integer.valueOf(maxValue).doubleValue());
	}

	/**
	 * Number.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param minValue
	 *                    the min value
	 * @param maxValue
	 *                    the max value
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue)
	{
		return Options.number(name, description)
			.setMinValue(Integer.valueOf(minValue).doubleValue())
			.setMaxValue(Integer.valueOf(maxValue).doubleValue());
	}

	/**
	 * Number.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
	{
		return Options.number(name, description)
			.addChoices(choices)
			.setAutoComplete(true);
	}

	/**
	 * Number.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param maxValue
	 *                    the max value
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue, @Nonnull Choice... choices)
	{
		return Options.number(name, description, choices)
			.setMaxValue(Integer.valueOf(maxValue).doubleValue());
	}

	/**
	 * Number.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param minValue
	 *                    the min value
	 * @param maxValue
	 *                    the max value
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue, @Nonnull Choice... choices)
	{
		return Options.number(name, description, choices)
			.setMinValue(Integer.valueOf(minValue).doubleValue())
			.setMaxValue(Integer.valueOf(maxValue).doubleValue());
	}

	/**
	 * Channel.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData channel(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.CHANNEL, name, description);
	}

	/**
	 * Channel.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData channel(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
	{
		return Options.channel(name, description)
			.addChoices(choices)
			.setAutoComplete(true);
	}

	/**
	 * User.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData user(@Nonnull String name, @Nonnull String description)
	{
		return new OptionData(OptionType.USER, name, description);
	}

	/**
	 * User.
	 *
	 * @param name
	 *                    The name for this option
	 * @param description
	 *                    The description for this option
	 * @param choices
	 *                    The choices for this option.
	 *
	 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
	 */
	public static final OptionData user(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
	{
		return Options.user(name, description)
			.addChoices(choices)
			.setAutoComplete(true);
	}

	public static final class Required
	{

		/**
		 * Text.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData text(@Nonnull String name, @Nonnull String description)
		{
			return Options.text(name, description)
				.setRequired(true);
		}

		/**
		 * Text.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param maxLength
		 *                    the max length
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength)
		{
			return Options.text(name, description)
				.setRequired(true);
		}

		/**
		 * Text.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param minLength
		 *                    the min length
		 * @param maxLength
		 *                    the max length
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength)
		{
			return Required.text(name, description)
				.setMinLength(minLength)
				.setMaxLength(maxLength);
		}

		/**
		 * Text.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
		{
			return Required.text(name, description)
				.addChoices(choices)
				.setAutoComplete(true);
		}

		/**
		 * Text.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param maxLength
		 *                    the max length
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int maxLength, @Nonnull Choice... choices)
		{
			return Required.text(name, description, choices)
				.setMaxLength(maxLength);
		}

		/**
		 * Text.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param minLength
		 *                    the min length
		 * @param maxLength
		 *                    the max length
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData text(@Nonnull String name, @Nonnull String description, @Nonnull int minLength, @Nonnull int maxLength, @Nonnull Choice... choices)
		{
			return Required.text(name, description, choices)
				.setMinLength(minLength)
				.setMaxLength(maxLength);
		}

		/**
		 * Number.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData number(@Nonnull String name, @Nonnull String description)
		{
			return Options.number(name, description)
				.setRequired(true);
		}

		/**
		 * Number.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
		{
			return Required.number(name, description)
				.addChoices(choices)
				.setAutoComplete(true);
		}

		/**
		 * Number.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param maxValue
		 *                    the max value
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue)
		{
			return Required.number(name, description)
				.setMaxValue(Integer.valueOf(maxValue).doubleValue());
		}

		/**
		 * Number.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param minValue
		 *                    the min value
		 * @param maxValue
		 *                    the max value
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue)
		{
			return Required.number(name, description)
				.setMinValue(Integer.valueOf(minValue).doubleValue())
				.setMaxValue(Integer.valueOf(maxValue).doubleValue());
		}

		/**
		 * Number.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param maxValue
		 *                    the max value
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int maxValue, @Nonnull Choice... choices)
		{
			return Required.number(name, description, choices)
				.setMaxValue(Integer.valueOf(maxValue).doubleValue());
		}

		/**
		 * Number.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param minValue
		 *                    the min value
		 * @param maxValue
		 *                    the max value
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData number(@Nonnull String name, @Nonnull String description, @Nonnull int minValue, @Nonnull int maxValue, @Nonnull Choice... choices)
		{
			return Required.number(name, description, choices)
				.setMinValue(Integer.valueOf(minValue).doubleValue())
				.setMaxValue(Integer.valueOf(maxValue).doubleValue());
		}

		/**
		 * Channel.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData channel(@Nonnull String name, @Nonnull String description)
		{
			return Options.channel(name, description)
				.setRequired(true);
		}

		/**
		 * Channel.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData channel(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
		{
			return Required.channel(name, description)
				.addChoices(choices)
				.setAutoComplete(true);
		}

		/**
		 * User.
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData user(@Nonnull String name, @Nonnull String description)
		{
			return Options.user(name, description)
				.setRequired(true);
		}

		/**
		 * Creates a new required {@link net.dv8tion.jda.api.interactions.commands.OptionType.USER USER} {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 * with the priovided {@link The choices for this option.}
		 *
		 * @param name
		 *                    The name for this option
		 * @param description
		 *                    The description for this option
		 * @param choices
		 *                    The choices for this option.
		 *
		 * @return {@link net.dv8tion.jda.api.interactions.commands.build.OptionData OptionData}
		 */
		public static final OptionData user(@Nonnull String name, @Nonnull String description, @Nonnull Choice... choices)
		{
			return Required.user(name, description)
				.addChoices(choices)
				.setAutoComplete(true);
		}
	}
}
