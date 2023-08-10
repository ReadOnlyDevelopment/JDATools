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
