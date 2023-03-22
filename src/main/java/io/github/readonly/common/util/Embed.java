package io.github.readonly.common.util;

import java.util.function.Predicate;
import java.util.function.Supplier;

import io.github.readonly.command.event.Event;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.User;
import okhttp3.HttpUrl;

public class Embed
{
	private final String		NEWLINE	= "\n";
	private final EmbedBuilder	builder;

	public static Embed fromMessageEmbed(MessageEmbed m)
	{
		return new Embed(m);
	}

	public static Embed newBuilder()
	{
		return new Embed();
	}

	private Embed(MessageEmbed messageEmbed)
	{
		this.builder = new EmbedBuilder(messageEmbed);
	}

	private Embed()
	{
		this.builder = new EmbedBuilder();
	}

	public Embed title(String title)
	{
		this.builder.setTitle(title);
		return this;
	}

	public Embed title(String title, HttpUrl url)
	{
		this.builder.setTitle(title, url.toString());
		return this;
	}

	public Embed description(String description)
	{
		this.builder.setDescription(description);
		return this;
	}

	public Embed appendDescription(String toAppend, boolean newLine)
	{
		this.builder.appendDescription(newLine ? NEWLINE + toAppend : toAppend);
		return this;
	}

	public Embed color(RGB color)
	{
		this.builder.setColor(color.getColor());
		return this;
	}

	public Embed thumbnail(HttpUrl url)
	{
		this.builder.setThumbnail(url.toString());
		return this;
	}

	public Embed image(HttpUrl url)
	{
		this.builder.setImage(url.toString());
		return this;
	}

	public Embed setAuthor(String author)
	{
		this.builder.setAuthor(author);
		return this;
	}

	public Embed setAuthor(User user)
	{
		this.builder.setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl());
		return this;
	}

	public <T extends Event<T>> Embed setAuthor(T event)
	{
		if (event != null)
		{
			User author = event.getAuthor();
			this.builder.setAuthor(author.getAsTag(), null, author.getEffectiveAvatarUrl());
		}
		return this;
	}

	public Embed footer(String text, HttpUrl iconUrl)
	{
		this.builder.setFooter(text, iconUrl.toString());
		return this;
	}

	public Embed footer(String text)
	{
		this.builder.setFooter(text);
		return this;
	}

	public Embed addFieldConditionally(Condition<Field> predicate)
	{
		if (predicate.pass())
		{
			field(predicate.get());
		}

		return this;
	}

	public Embed field(Field field)
	{
		this.builder.addField(field);
		return this;
	}

	public Embed field(String title, String content)
	{
		this.builder.addField(title, content, false);
		return this;
	}

	public Embed fieldInline(String title, String content)
	{
		this.builder.addField(title, content, true);
		return this;
	}

	public Embed blankField()
	{
		this.builder.addBlankField(false);
		return this;
	}

	public final MessageEmbed toEmbed()
	{
		return this.builder.build();
	}

	public static class Condition<T> implements Supplier<T>
	{
		private final T				obj;
		private final Predicate<T>	predicate;

		private Condition(@NonNull T obj, @NonNull Predicate<T> predicate)
		{
			this.obj = obj;
			this.predicate = predicate;
		}

		public static <T> Condition<T> NotNull(@NonNull T obj)
		{
			return new Condition<T>(obj, p -> p != null);
		}

		public boolean pass()
		{
			return predicate.test(obj);
		}

		@Override
		public T get()
		{
			return obj;
		}
	}
}
