package com.readonlydev.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.sticker.Sticker;
import net.dv8tion.jda.api.entities.sticker.StickerSnowflake;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.internal.utils.Checks;

public class MessageContentBuilder implements Appendable
{
    protected final StringBuilder builder = new StringBuilder();

    protected final List<MessageEmbed> embeds = new ArrayList<>();
    protected final List<LayoutComponent> components = new ArrayList<>();
    protected final List<StickerSnowflake> stickers = new ArrayList<>();
    protected boolean isTTS = false;
    protected String nonce;
    protected EnumSet<Message.MentionType> allowedMentions = null;
    protected Set<String> mentionedUsers = new HashSet<>();
    protected Set<String> mentionedRoles = new HashSet<>();

    public MessageContentBuilder() {}

    public MessageContentBuilder(@Nullable CharSequence content)
    {
        if (content != null)
        {
            builder.append(content);
        }
    }

    public MessageContentBuilder(@Nullable EmbedBuilder builder)
    {
        if (builder != null)
        {
            this.embeds.add(builder.build());
        }
    }

    public MessageContentBuilder(@Nullable MessageEmbed embed)
    {
        if (embed != null)
        {
            this.embeds.add(embed);
        }
    }

    /**
     * Makes the created Message a TTS message.
     * <br>TTS stands for Text-To-Speech. When a TTS method is received by the Discord client,
     * it is vocalized so long as the user has not disabled TTS.
     *
     * @param  tts
     *         whether the created Message should be a tts message
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder setTTS(boolean tts)
    {
        this.isTTS = tts;
        return this;
    }

    /**
     * Adds up to {@value Message#MAX_EMBED_COUNT} {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbeds} to the Message. Embeds can be built using
     * the {@link net.dv8tion.jda.api.EmbedBuilder} and offer specialized formatting.
     *
     * @param  embeds
     *         the embeds to add, or empty array to remove
     *
     * @throws java.lang.IllegalArgumentException
     *         If any of the provided MessageEmbeds is null or not sendable according to {@link net.dv8tion.jda.api.entities.MessageEmbed#isSendable() MessageEmbed.isSendable()}!
     *         The sum of all {@link MessageEmbed#getLength()} must not be greater than {@link MessageEmbed#EMBED_MAX_LENGTH_BOT}!
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder setEmbeds(@Nonnull MessageEmbed... embeds)
    {
        Checks.noneNull(embeds, "MessageEmbeds");
        return setEmbeds(Arrays.asList(embeds));
    }

    /**
     * Adds up to {@value Message#MAX_EMBED_COUNT} {@link net.dv8tion.jda.api.entities.MessageEmbed MessageEmbeds} to the Message. Embeds can be built using
     * the {@link net.dv8tion.jda.api.EmbedBuilder} and offer specialized formatting.
     *
     * @param  embeds
     *         the embeds to add, or empty list to remove
     *
     * @throws java.lang.IllegalArgumentException
     *         If any of the provided MessageEmbeds is null or not sendable according to {@link net.dv8tion.jda.api.entities.MessageEmbed#isSendable() MessageEmbed.isSendable()}!
     *         The sum of all {@link MessageEmbed#getLength()} must not be greater than {@link MessageEmbed#EMBED_MAX_LENGTH_BOT}!
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder setEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds)
    {

        Checks.noneNull(embeds, "MessageEmbeds");
        embeds.forEach(embed ->
        Checks.check(embed.isSendable(),
            "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!",
            MessageEmbed.EMBED_MAX_LENGTH_BOT)
            );
        Checks.check(embeds.size() <= Message.MAX_EMBED_COUNT, "Cannot have more than %d embeds in a message!", Message.MAX_EMBED_COUNT);
        Checks.check(embeds.stream().mapToInt(MessageEmbed::getLength).sum() <= MessageEmbed.EMBED_MAX_LENGTH_BOT, "The sum of all MessageEmbeds may not exceed %d!", MessageEmbed.EMBED_MAX_LENGTH_BOT);
        this.embeds.clear();
        this.embeds.addAll(embeds);
        return this;
    }

    /**
     * Set the action rows for the message.
     *
     * @param  rows
     *         The new action rows, or null to reset the components
     *
     * @throws IllegalArgumentException
     *         If null is provided in the collection or more than 5 actions rows are provided
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder setActionRows(@Nullable Collection<? extends ActionRow> rows)
    {
        if (rows == null)
        {
            this.components.clear();
            return this;
        }
        Checks.noneNull(rows, "ActionRows");
        Checks.check(rows.size() <= 5, "Can only have 5 action rows per message!");
        this.components.clear();
        this.components.addAll(rows);
        return this;
    }

    /**
     * Set the action rows for the message.
     *
     * @param  rows
     *         The new action rows, or null to reset the components
     *
     * @throws IllegalArgumentException
     *         If null is provided in the array or more than 5 actions rows are provided
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder setActionRows(@Nullable ActionRow... rows)
    {
        if (rows == null)
        {
            this.components.clear();
            return this;
        }
        return setActionRows(Arrays.asList(rows));
    }

    /**
     * Set the stickers to send alongside this message.
     * <br>This is not supported for message edits.
     *
     * @param  stickers
     *         The stickers to send, or null to not send any stickers
     *
     * @throws IllegalArgumentException
     *         If more than {@value Message#MAX_STICKER_COUNT} stickers or null stickers are provided
     *
     * @return The MessageBuilder instance. Useful for chaining.
     *
     * @see    Sticker#fromId(long)
     */
    @Nonnull
    public MessageContentBuilder setStickers(@Nullable Collection<? extends StickerSnowflake> stickers)
    {
        this.stickers.clear();
        if ((stickers == null) || stickers.isEmpty())
        {
            return this;
        }
        Checks.noneNull(stickers, "Stickers");
        Checks.check(stickers.size() <= Message.MAX_STICKER_COUNT,
            "Cannot send more than %d stickers in a message!", Message.MAX_STICKER_COUNT);

        stickers.stream()
        .map(StickerSnowflake::getId)
        .map(StickerSnowflake::fromId)
        .forEach(this.stickers::add);
        return this;
    }

    /**
     * Set the stickers to send alongside this message.
     * <br>This is not supported for message edits.
     *
     * @param  stickers
     *         The stickers to send, or null to not send any stickers
     *
     * @throws IllegalArgumentException
     *         If more than {@value Message#MAX_STICKER_COUNT} stickers or null stickers are provided
     *
     * @return The MessageBuilder instance. Useful for chaining.
     *
     * @see    Sticker#fromId(long)
     */
    @Nonnull
    public MessageContentBuilder setStickers(@Nullable StickerSnowflake... stickers)
    {
        if (stickers != null)
        {
            Checks.noneNull(stickers, "Stickers");
        }
        return setStickers(stickers == null ? null : Arrays.asList(stickers));
    }

    /**
     * Sets the <a href="https://en.wikipedia.org/wiki/Cryptographic_nonce" target="_blank">nonce</a>
     * of the built message(s). It is recommended to have only 100% unique strings to validate messages via this nonce.
     * <br>The nonce will be available from the resulting message via {@link net.dv8tion.jda.api.entities.Message#getNonce() Message.getNonce()}
     * in message received by events and RestAction responses.
     * <br>When {@code null} is provided no nonce will be used.
     *
     * @param  nonce
     *         Validation nonce string
     *
     * @return The MessageBuilder instance. Useful for chaining.
     *
     * @see    net.dv8tion.jda.api.entities.Message#getNonce()
     * @see    <a href="https://en.wikipedia.org/wiki/Cryptographic_nonce" target="_blank">Cryptographic Nonce - Wikipedia</a>
     */
    @Nonnull
    public MessageContentBuilder setNonce(@Nullable String nonce)
    {
        this.nonce = nonce;
        return this;
    }

    /**
     * Sets the content of the resulting Message
     * <br>This will replace already added content.
     *
     * @param  content
     *         The content to use, or {@code null} to reset the content
     *
     * @return The MessageBuilder instance. Useful for chaining.
     *
     * @see    net.dv8tion.jda.api.entities.Message#getContentRaw()
     */
    @Nonnull
    public MessageContentBuilder setContent(@Nullable String content)
    {
        if (content == null)
        {
            builder.setLength(0);
        }
        else
        {
            final int newLength = Math.max(builder.length(), content.length());
            builder.replace(0, newLength, content);
        }
        return this;
    }

    @Nonnull
    @Override
    public MessageContentBuilder append(@Nullable CharSequence text)
    {
        builder.append(text);
        return this;
    }

    @Nonnull
    @Override
    public MessageContentBuilder append(@Nullable CharSequence text, int start, int end)
    {
        builder.append(text, start, end);
        return this;
    }

    @Nonnull
    @Override
    public MessageContentBuilder append(char c)
    {
        builder.append(c);
        return this;
    }

    /**
     * Appends the string representation of an object to the Message.
     * <br>This is the same as {@link #append(CharSequence) append(String.valueOf(object))}
     *
     * @param  object
     *         the object to append
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder append(@Nullable Object object)
    {
        return append(String.valueOf(object));
    }

    /**
     * Appends a mention to the Message.
     * <br>Typical usage would be providing an {@link net.dv8tion.jda.api.entities.IMentionable IMentionable} like
     * {@link net.dv8tion.jda.api.entities.User User} or {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel TextChannel}.
     *
     * <p>This will not add a rule to mention a {@link User} or {@link Role}. You have to use {@link #mention(IMentionable...)}
     * in addition to this method.
     *
     * @param  mention
     *         the mention to append
     *
     * @throws java.lang.IllegalArgumentException
     *         If provided with null
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder append(@Nonnull IMentionable mention)
    {
        Checks.notNull(mention, "Mentionable");
        builder.append(mention.getAsMention());
        return this;
    }

    /**
     * Appends a String using the specified chat {@link net.dv8tion.jda.api.MessageBuilder.Formatting Formatting(s)}.
     *
     * @param  text
     *         the text to append.
     * @param  format
     *         the format(s) to apply to the text.
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder append(@Nullable CharSequence text, @Nonnull Formatting... format)
    {
        boolean blockPresent = false;
        for (Formatting formatting : format)
        {
            if (formatting == Formatting.BLOCK)
            {
                blockPresent = true;
                continue;
            }
            builder.append(formatting.getTag());
        }
        if (blockPresent)
        {
            builder.append(Formatting.BLOCK.getTag());
        }

        builder.append(text);

        if (blockPresent)
        {
            builder.append(Formatting.BLOCK.getTag());
        }
        for (int i = format.length - 1; i >= 0; i--)
        {
            if (format[i] == Formatting.BLOCK)
            {
                continue;
            }
            builder.append(format[i].getTag());
        }
        return this;
    }

    /**
     * This method is an extended form of {@link String#format(String, Object...)}. It allows for all of
     * the token replacement functionality that String.format(String, Object...) supports.
     * <br>A lot of JDA entities implement {@link java.util.Formattable Formattable} and will provide
     * specific format outputs for their specific type.
     * <ul>
     *     <li>{@link net.dv8tion.jda.api.entities.IMentionable IMentionable}
     *     <br>These will output their {@link net.dv8tion.jda.api.entities.IMentionable#getAsMention() getAsMention} by default,
     *         some implementations have alternatives such as {@link net.dv8tion.jda.api.entities.User User} and {@link net.dv8tion.jda.api.entities.channel.concrete.TextChannel TextChannel}.</li>
     *     <li>{@link net.dv8tion.jda.api.entities.channel.middleman.MessageChannel MessageChannel}
     *     <br>All message channels format to {@code "#" + getName()} by default, TextChannel has special handling
     *         and uses the getAsMention output by default and the MessageChannel output as alternative ({@code #} flag).</li>
     *     <li>{@link net.dv8tion.jda.api.entities.Message Message}
     *     <br>Messages by default output their {@link net.dv8tion.jda.api.entities.Message#getContentDisplay() getContentDisplay()} value and
     *         as alternative use the {@link net.dv8tion.jda.api.entities.Message#getContentRaw() getContentRaw()} value</li>
     * </ul>
     *
     * <p>Example:
     * <br>If you placed the following code in an method handling a
     * {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent MessageReceivedEvent}
     * <br><pre>{@code
     * User user = event.getAuthor();
     * MessageBuilder builder = new MessageBuilder();
     * builder.appendFormat("%#s is really cool!", user);
     * builder.build();
     * }</pre>
     *
     * It would build a message that mentions the author and says that he is really cool!. If the user's
     * name was "Minn" and his discriminator "6688", it would say:
     * <br><pre>  "Minn#6688 is really cool!"</pre>
     * <br>Note that this uses the {@code #} flag to utilize the alternative format for {@link net.dv8tion.jda.api.entities.User User}.
     * <br>By default it would fallback to {@link net.dv8tion.jda.api.entities.IMentionable#getAsMention()}
     *
     * @param  format
     *         a format string.
     * @param  args
     *         an array objects that will be used to replace the tokens, they must be
     *         provided in the order that the tokens appear in the provided format string.
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided format string is {@code null} or empty
     * @throws java.util.IllegalFormatException
     *         If a format string contains an illegal syntax,
     *         a format specifier that is incompatible with the given arguments,
     *         insufficient arguments given the format string, or other illegal conditions.
     *         For specification of all possible formatting errors,
     *         see the <a href="../util/Formatter.html#detail">Details</a>
     *         section of the formatter class specification.
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder appendFormat(@Nonnull String format, @Nonnull Object... args)
    {
        Checks.notEmpty(format, "Format String");
        this.append(String.format(format, args));
        return this;
    }

    /**
     * Appends a code-line to the Message.
     * Code Lines are similar to code-blocks, however they are displayed in-line and do not support language specific highlighting.
     *
     * @param  text
     *         the code to append
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder appendCodeLine(@Nullable CharSequence text)
    {
        this.append(text, Formatting.BLOCK);
        return this;
    }

    /**
     * Appends a code-block to the Message.
     * <br>Discord uses <a href="https://highlightjs.org/">Highlight.js</a> for its language highlighting support. You can find out what
     * specific languages are supported <a href="https://github.com/isagalaev/highlight.js/tree/master/src/languages">here</a>.
     *
     * @param  text
     *         the code to append
     * @param  language
     *         the language of the code. If unknown use an empty string
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder appendCodeBlock(@Nullable CharSequence text, @Nullable CharSequence language)
    {
        builder.append("```").append(language).append('\n').append(text).append("\n```");
        return this;
    }

    /**
     * Returns the current length of the content that will be built into a {@link net.dv8tion.jda.api.entities.Message Message}
     * when {@link #build()} is called.
     * <br>If this value is {@code 0} (and there is no embed) or greater than {@code 2000} when {@link #build()} is called, an exception
     * will be raised as you cannot send an empty message to Discord and Discord has a hard limit of 2000 characters per message.
     *
     * <p><b>Hint:</b> You can use {@link #build(int, int)} or
     * {@link #buildAll(net.dv8tion.jda.api.MessageBuilder.SplitPolicy...) buildAll(SplitPolicy...)} as possible ways to
     * deal with the 2000 character cap.
     *
     * @return the current length of the content that will be built into a Message.
     */
    public int length()
    {
        return builder.length();
    }

    /**
     * Checks if the message contains any contend. This includes text as well as embeds.
     *
     * @return whether the message contains content
     */
    public boolean isEmpty() {
        return (builder.length() == 0) && embeds.isEmpty();
    }

    /**
     * Replaces each substring that matches the target string with the specified replacement string.
     * The replacement proceeds from the beginning of the string to the end, for example, replacing
     * "aa" with "b" in the message "aaa" will result in "ba" rather than "ab".
     *
     * @param  target
     *         the sequence of char values to be replaced
     * @param  replacement
     *         the replacement sequence of char values
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder replace(@Nonnull String target, @Nonnull String replacement)
    {
        int index = builder.indexOf(target);
        while (index != -1)
        {
            builder.replace(index, index + target.length(), replacement);
            index = builder.indexOf(target, index + replacement.length());
        }
        return this;
    }

    /**
     * Replaces the first substring that matches the target string with the specified replacement string.
     *
     * @param  target
     *         the sequence of char values to be replaced
     * @param  replacement
     *         the replacement sequence of char values
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder replaceFirst(@Nonnull String target, @Nonnull String replacement)
    {
        int index = builder.indexOf(target);
        if (index != -1)
        {
            builder.replace(index, index + target.length(), replacement);
        }
        return this;
    }

    /**
     * Replaces the last substring that matches the target string with the specified replacement string.
     *
     * @param  target
     *         the sequence of char values to be replaced
     * @param  replacement
     *         the replacement sequence of char values
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder replaceLast(@Nonnull String target, @Nonnull String replacement)
    {
        int index = builder.lastIndexOf(target);
        if (index != -1)
        {
            builder.replace(index, index + target.length(), replacement);
        }
        return this;
    }

    /**
     * Returns the underlying {@link StringBuilder}.
     *
     * @return The {@link StringBuilder} used by this {@link MessageBuilder}
     */
    @Nonnull
    public StringBuilder getStringBuilder()
    {
        return this.builder;
    }

    /**
     * Clears the current builder. Useful for mass message creation.
     *
     * <p>This will not clear the allowed mentions.
     *
     * @return The MessageBuilder instance. Useful for chaining.
     */
    @Nonnull
    public MessageContentBuilder clear() {
        this.builder.setLength(0);
        this.embeds.clear();
        this.isTTS = false;
        return this;
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring between the specified indices.
     *
     * <p>If no such value of {@code target} exists, then {@code -1} is returned.
     *
     * @param  target
     *         the substring to search for.
     * @param  fromIndex
     *         the index from which to start the search.
     * @param  endIndex
     *         the index at which to end the search.
     *
     * @throws java.lang.IndexOutOfBoundsException
     *         <ul>
     *             <li>If the {@code fromIndex} is outside of the range of {@code 0} to {@link #length()}</li>
     *             <li>If the {@code endIndex} is outside of the range of {@code 0} to {@link #length()}</li>
     *             <li>If the {@code fromIndex} is greater than {@code endIndex}</li>
     *         </ul>
     *
     * @return the index of the first occurrence of the specified substring between
     *         the specified indices or {@code -1} if there is no such occurrence.
     */
    public int indexOf(@Nonnull CharSequence target, int fromIndex, int endIndex)
    {
        if (fromIndex < 0)
        {
            throw new IndexOutOfBoundsException("index out of range: " + fromIndex);
        }
        if (endIndex < 0)
        {
            throw new IndexOutOfBoundsException("index out of range: " + endIndex);
        }
        if (fromIndex > length())
        {
            throw new IndexOutOfBoundsException("fromIndex > length()");
        }
        if (fromIndex > endIndex)
        {
            throw new IndexOutOfBoundsException("fromIndex > endIndex");
        }

        if (endIndex >= builder.length())
        {
            endIndex = builder.length() - 1;
        }

        int targetCount = target.length();
        if (targetCount == 0)
        {
            return fromIndex;
        }

        char strFirstChar = target.charAt(0);
        int max = (endIndex + targetCount) - 1;

        lastCharSearch:
            for (int i = fromIndex; i <= max; i++)
            {
                if (builder.charAt(i) == strFirstChar)
                {
                    for (int j = 1; j < targetCount; j++)
                    {
                        if (builder.charAt(i + j) != target.charAt(j))
                        {
                            continue lastCharSearch;
                        }
                    }
                    return i;
                }
            }
        return -1;
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring between the specified indices.
     *
     * If no such value of {@code target} exists, then {@code -1} is returned.
     *
     * @param  target
     *         the substring to search for.
     * @param  fromIndex
     *         the index from which to start the search.
     * @param  endIndex
     *         the index at which to end the search.
     *
     * @throws java.lang.IndexOutOfBoundsException
     *         <ul>
     *             <li>If the {@code fromIndex} is outside of the range of {@code 0} to {@link #length()}</li>
     *             <li>If the {@code endIndex} is outside of the range of {@code 0} to {@link #length()}</li>
     *             <li>If the {@code fromIndex} is greater than {@code endIndex}</li>
     *         </ul>
     *
     * @return the index of the last occurrence of the specified substring between
     *         the specified indices or {@code -1} if there is no such occurrence.
     */
    public int lastIndexOf(@Nonnull CharSequence target, int fromIndex, int endIndex)
    {
        if (fromIndex < 0)
        {
            throw new IndexOutOfBoundsException("index out of range: " + fromIndex);
        }
        if (endIndex < 0)
        {
            throw new IndexOutOfBoundsException("index out of range: " + endIndex);
        }
        if (fromIndex > length())
        {
            throw new IndexOutOfBoundsException("fromIndex > length()");
        }
        if (fromIndex > endIndex)
        {
            throw new IndexOutOfBoundsException("fromIndex > endIndex");
        }

        if (endIndex >= builder.length())
        {
            endIndex = builder.length() - 1;
        }

        int targetCount = target.length();
        if (targetCount == 0)
        {
            return endIndex;
        }

        int rightIndex = endIndex - targetCount;

        if (fromIndex > rightIndex)
        {
            fromIndex = rightIndex;
        }

        int strLastIndex = targetCount - 1;
        char strLastChar = target.charAt(strLastIndex);

        int min = (fromIndex + targetCount) - 1;

        lastCharSearch:
            for (int i = endIndex; i >= min; i--)
            {
                if (builder.charAt(i) == strLastChar)
                {
                    for (int j = strLastIndex - 1, k = 1; j >= 0; j--, k++)
                    {
                        if (builder.charAt(i - k) != target.charAt(j))
                        {
                            continue lastCharSearch;
                        }
                    }
                    return (i - target.length()) + 1;
                }
            }
        return -1;
    }

    /**
     * Creates a {@link net.dv8tion.jda.api.entities.Message Message} object from this MessageBuilder
     *
     * <p><b>Hint:</b> You can use {@link #build(int, int)} or
     * {@link #buildAll(net.dv8tion.jda.api.MessageBuilder.SplitPolicy...) buildAll(SplitPolicy...)} as possible ways to
     * deal with the 2000 character cap.
     *
     * @throws java.lang.IllegalStateException
     *         <ul>
     *             <li>If you attempt to build() an empty Message ({@link #length()} is {@code 0} and no
     *             {@link net.dv8tion.jda.api.entities.MessageEmbed} was provided to {@link #setEmbeds(MessageEmbed...)} </li>
     *             <li>If you attempt to build() a Message with more than 2000 characters of content.</li>
     *         </ul>
     *
     * @return the created {@link net.dv8tion.jda.api.entities.Message Message}
     */
    @Nonnull
    public String build()
    {
        String message = builder.toString();
        if (this.isEmpty())
        {
            throw new IllegalStateException("Cannot build a Message with no content. (You never added any content to the message)");
        }
        if (message.length() > Message.MAX_CONTENT_LENGTH)
        {
            throw new IllegalStateException("Cannot build a Message with more than " + Message.MAX_CONTENT_LENGTH + " characters. Please limit your input.");
        }
        return builder.toString();
    }

    /**
     * Holds the available formatting used in {@link MessageBuilder#append(java.lang.CharSequence, net.dv8tion.jda.api.MessageBuilder.Formatting...)}
     */
    public enum Formatting
    {
        ITALICS("*"),
        BOLD("**"),
        STRIKETHROUGH("~~"),
        UNDERLINE("__"),
        BLOCK("`");

        private final String tag;

        Formatting(String tag)
        {
            this.tag = tag;
        }

        @Nonnull
        private String getTag()
        {
            return tag;
        }
    }
}
