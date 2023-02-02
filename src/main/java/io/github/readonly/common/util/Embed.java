package io.github.readonly.common.util;

import io.github.readonly.command.event.Event;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import okhttp3.HttpUrl;

public class Embed
{
	private final String NEWLINE = "\n";
	private final EmbedBuilder builder;
	private Event<?> event;

	public static <T extends Event<T>> Embed newBuilder(T event)
	{
		return new Embed();
	}

	public static Embed newBuilder()
	{
		return new Embed();
	}

	private <T extends Event<T>> Embed(T event)
	{
		this.builder = new EmbedBuilder();
		this.event = event;
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

	public Embed setAuthor()
	{
		if(event != null)
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
}
