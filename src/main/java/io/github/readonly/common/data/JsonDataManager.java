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

package io.github.readonly.common.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataManager<T> implements DataManager<T>
{
	private static final Charset		UTF8	= StandardCharsets.UTF_8;
	private static final ObjectMapper	mapper	= new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

	private static final Logger	log	= LoggerFactory.getLogger(JsonDataManager.class);
	private final Path			filePath;
	private final T				data;

	public JsonDataManager(Class<T> clazz, String file, Supplier<T> constructor)
	{
		this.filePath = Paths.get(file);

		if (!filePath.toFile().exists())
		{
			log.info("Could not find file at " + filePath.toFile().getAbsolutePath() + ", creating a new one...");
			try
			{
				if (filePath.toFile().createNewFile())
				{
					log.info("Generated new file at " + filePath.toFile().getAbsolutePath() + ".");
					write(filePath, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(constructor.get()));
					log.info("Please, fill the file with valid properties.");
				} else
				{
					log.warn("Could not create file at " + file);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
				System.exit(1);
			}

			System.exit(0);
		}

		try
		{
			this.data = fromJson(read(filePath), clazz);
		} catch (IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public T get()
	{
		return data;
	}

	@Override
	public void save()
	{
		try
		{
			write(filePath, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
		} catch (IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	public static <T> String toJson(T object) throws JsonProcessingException
	{
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}

	public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException
	{
		return mapper.readValue(json, clazz);
	}

	public static <T> T fromJson(String json, TypeReference<T> type) throws JsonProcessingException
	{
		return mapper.readValue(json, type);
	}

	public static <T> T fromJson(String json, JavaType type) throws JsonProcessingException
	{
		return mapper.readValue(json, type);
	}

	private String read(Path path) throws IOException
	{
		return Files.readString(path, UTF8);
	}

	private void write(Path path, String contents) throws IOException
	{
		Files.writeString(path, contents, UTF8);
	}
}
