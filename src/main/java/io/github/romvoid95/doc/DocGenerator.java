package io.github.romvoid95.doc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.github.romvoid95.doc.standard.CommandInfo;
import io.github.romvoid95.doc.standard.Error;
import io.github.romvoid95.doc.standard.RequiredPermissions;
import io.github.romvoid95.utils.FixedSizeCache;

/**
 * An instance based documentation engine for bot commands written in JDA.
 * <p>
 * Instances of this can read {@link java.lang.annotation.Annotation
 * Annotation}s on {@link java.lang.Class Class}es and/or
 * {@link java.lang.reflect.Method}s to format and document the command's they
 * represent.
 * <p>
 * The most basic usage of this can be shown below:
 *
 * <pre>
 *    {@link io.github.romvoid95.doc.standard.CommandInfo @CommandInfo}(
 *     name = {"MyCommand", "MC"},
 *     usage = "MyCommand {@literal <Usage>}",
 *     description = "This is an example of CommandDoc's standard @CommandInfo annotation"
 * )
 * public class MyCommand {
 *     // ...
 * }
 * </pre>
 *
 * Then...
 *
 * <pre>
 * <code>
 *     DocGenerator generator = {@link #getDefaultGenerator() DocGenerator.getDefaultGenerator()};
 *     String documentation = generator.getDocForClass(MyCommand.class);
 * </code>
 * </pre>
 * <p>
 * Note: This documentation system is <b>universal</b>, can be applied to any
 * command system that uses Class and/or Method based commands, and works in any
 * JVM language that supports annotations.
 *
 * @see io.github.romvoid95.doc.ConvertedBy
 * @see io.github.romvoid95.doc.DocConverter
 */
public class DocGenerator {
	private final HashMap<Class<? extends Annotation>, DocConverter<? extends Annotation>> map;
	private final FixedSizeCache<AnnotatedElement, String> cache;
	private final String separator;

	/**
	 * Gets a default DocGenerator with standard conversions loaded.
	 * <p>
	 * This is the simplest way to get a prebuilt working CommandDoc generator with
	 * standard annotations.
	 * <p>
	 * Additional annotations can be added using
	 * {@link #register(Class, Object...)}.
	 *
	 * @return The default DocGenerator with standard conversions loaded.
	 */
	public static DocGenerator getDefaultGenerator() {
		return new DocGenerator().register(CommandInfo.class).register(Error.class).register(RequiredPermissions.class);
	}

	/**
	 * Gets a blank DocGenerator with no conversions loaded.
	 */
	public DocGenerator() {
		this(20);
	}

	/**
	 * Gets a blank DocGenerator with no conversions loaded, and a cache with the
	 * specified max-size.
	 * <p>
	 * Calls to
	 * {@link io.github.romvoid95.doc.DocGenerator#getDocFor(java.lang.Class)},
	 * {@link io.github.romvoid95.doc.DocGenerator#getDocFor(java.lang.reflect.Method)},
	 * and
	 * {@link io.github.romvoid95.doc.DocGenerator#getDocForMethods(java.lang.Class)}
	 * also cache the values retrieved from the invocation as a way to reduce
	 * reflection overhead for repeated calls.
	 *
	 * @param cacheSize The of the cache size that contains previously generated
	 * CommandDoc to reduce reflection overhead for repeated calls.
	 */
	public DocGenerator(int cacheSize) {
		this("\n\n", cacheSize);
	}

	/**
	 * Gets a blank DocGenerator with no conversions loaded and with the specified
	 * separator, and a cache with the specified max-size.
	 * <p>
	 * A separator will be appended to the documentation returned by
	 * {@link io.github.romvoid95.doc.DocGenerator#getDocFor(java.lang.Class)}
	 * inbetween annotation conversions. <br>
	 * By default this is a double newline ({@literal \n\n}).
	 * <p>
	 * Calls to
	 * {@link io.github.romvoid95.doc.DocGenerator#getDocFor(java.lang.Class)},
	 * {@link io.github.romvoid95.doc.DocGenerator#getDocFor(java.lang.reflect.Method)},
	 * and
	 * {@link io.github.romvoid95.doc.DocGenerator#getDocForMethods(java.lang.Class)}
	 * also cache the values retrieved from the invocation as a way to reduce
	 * reflection overhead for repeated calls.
	 *
	 * @param separator The separator that occurs inbetween annotation conversions.
	 * @param cacheSize The of the cache size that contains previously generated
	 * CommandDoc to reduce reflection overhead for repeated calls.
	 */
	public DocGenerator(String separator, int cacheSize) {
		this.separator = separator;
		map = new HashMap<>();
		cache = new FixedSizeCache<>(cacheSize);
	}

	/**
	 * Reads CommandDoc from the provided {@link java.lang.Class Class} and returns
	 * the String formatted and from it.
	 *
	 * @param cla The Class to get CommandDoc from.
	 *
	 * @return The documentation read from the Class.
	 */
	public String getDocFor(Class<?> cla) {
		return read(cla);
	}

	/**
	 * Reads CommandDoc from the provided {@link java.lang.reflect.Method Method}
	 * and returns the String formatted and from it.
	 *
	 * @param method The Method to get CommandDoc from.
	 *
	 * @return The documentation read from the Method.
	 */
	public String getDocFor(Method method) {
		return read(method);
	}

	/**
	 * Reads all {@link java.lang.reflect.Method Method}s from the provided
	 * {@link java.lang.Class Class} and returns a List of CommandDoc of each.
	 * <p>
	 * Methods read that return empty Strings are not added to the list.
	 *
	 * @param cla The Class to get CommandDoc from each method
	 *
	 * @return A List of individual Method CommandDocs
	 */
	public List<String> getDocForMethods(Class<?> cla) {
		List<String> list = new ArrayList<>();
		for (Method method : cla.getMethods()) {
			String doc = read(method);
			if (!doc.isEmpty()) {
				list.add(doc);
			}
		}
		return list;
	}

	/**
	 * Registers a CommandDoc {@link java.lang.annotation.Annotation Annotation} to
	 * this DocGenerator.
	 * <p>
	 * An example of a custom CommandDoc conversion annotation can be found in the
	 * {@link io.github.romvoid95.doc.DocConverter DocConverter} documentation.
	 *
	 * @param <T> The type of annotation
	 * @param type The annotation Class type.
	 * @param converterParams The parameters necessary to instantiate the proper
	 * DocConverter. <br>
	 * DocConverters can have multiple constructors, although
	 * it's discouraged.
	 *
	 * @throws IllegalArgumentException The annotation class provided is not
	 * annotated with
	 * {@link io.github.romvoid95.doc.ConvertedBy @ConvertedBy},
	 * or an exception is thrown while
	 * instantiating the value said ConvertedBy
	 * annotation. <br>
	 * <b>NOTE:</b> that a DocConverter
	 * instantiation will fail if it's type
	 * parameter is the provided Annotation class.
	 *
	 * @return This DocGenerator
	 */
	public <T extends Annotation> DocGenerator register(Class<T> type, Object... converterParams) {
		ConvertedBy convertedBy = type.getAnnotation(ConvertedBy.class);
		if (convertedBy == null) {
			throw new IllegalArgumentException("Illegal annotation type! Not annotated with @ConvertedBy!");
		}
		final DocConverter<? extends Annotation> instance;
		try {
			// If parameters are specified
			if (converterParams.length > 0) {
				Class<?>[] tArray = Arrays.stream(converterParams).map(Object::getClass).collect(Collectors.toList())
						.toArray(new Class[converterParams.length]);
				instance = convertedBy.value().getDeclaredConstructor(tArray).newInstance(converterParams);
			} else {
				instance = convertedBy.value().getConstructor().newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException
				| InvocationTargetException e) {
			throw new IllegalArgumentException("Instance of " + convertedBy.value() + " could not be instantiated!", e);
		}
		return register(type, instance);
	}

	/**
	 * Registers a CommandDoc {@link java.lang.annotation.Annotation Annotation} to
	 * this DocGenerator with the provided DocConverter.
	 * <p>
	 * This is not recommended unless you for some reason want to pass an instance
	 * variable to the DocConverter you are providing.
	 * <p>
	 * An example of a custom CommandDoc conversion annotation can be found in the
	 * {@link io.github.romvoid95.doc.DocConverter DocConverter} documentation.
	 *
	 * @param <T> The type of annotation.
	 * @param type The annotation Class type.
	 * @param converter The DocConverter to use.
	 *
	 * @return This DocGenerator
	 */
	public <T extends Annotation> DocGenerator register(Class<T> type, DocConverter<T> converter) {
		synchronized (map) {
			map.put(type, converter);
		}
		return this;
	}

	private String read(AnnotatedElement ae) {
		// Have we already read this?
		if (cache.contains(ae)) {
			return cache.get(ae);
		}
		StringBuilder b = new StringBuilder();
		synchronized (map) {
			int lastIndex = map.keySet().size() - 1;
			int index = 0;
			for (Class<? extends Annotation> key : map.keySet()) {
				DocMultiple docMultiple = key.getAnnotation(DocMultiple.class);
				if (docMultiple == null) {
					Annotation a = ae.getAnnotation(key);

					// Is not annotated with that particular annotation
					if (a == null) {
						continue;
					}
					b.append(((DocConverter<Annotation>) map.get(key)).read(a));
					if (index < lastIndex) {
						b.append(separator);
					}
				} else {
					Annotation[] ans = ae.getAnnotationsByType(key);
					int len = ans.length;
					for (int i = 0; i < len; i++) {
						if (i == 0) {
							b.append(docMultiple.preface());
						}
						b.append(docMultiple.prefixEach())
								.append(((DocConverter<Annotation>) map.get(key)).read(ans[i]));
						if (i < (len - 1)) {
							b.append(docMultiple.separateBy());
						} else if (index < lastIndex) {
							b.append(separator);
						}
					}
				}
			}
		}
		// Trim this down
		String doc = b.toString().trim();

		// Cache the read value, even if it's empty.
		cache.add(ae, doc);

		return doc;
	}
}
