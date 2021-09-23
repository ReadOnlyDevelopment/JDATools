package io.github.romvoid95.doc;

import java.lang.annotation.Annotation;

/**
 * Converts an annotation of the specified type {@code T} into a String to be
 * collected with other conversions into a single String documenting a class or
 * method representing a command for a bot.
 * <p>
 * These are the fundamental building blocks behind command doc annotations, and
 * can be applied using the
 * {@link io.github.romvoid95.doc.ConvertedBy @ConvertedBy} annotation:
 *
 * <pre>
 *    {@literal @ConvertedBy(MyCommandDocAnn.Converter.class)}
 *    {@literal @Retention(RetentionPolicy.RUNTIME)}
 *    {@literal @Target(ElementType.ANNOTATION_TYPE)}
 *     public @interface MyCommandDocAnn
 *     {
 *         String value();
 *
 *         class Converter implements DocConverter{@literal <MyCommandDocAnn>}
 *         {
 *             public String read(MyCommandDocAnn annotation)
 *             {
 *                 return "**"+annotation.value()+"**";
 *             }
 *         }
 *     }
 * </pre>
 *
 * It is also notably recommended you follow the standards for DocConverters
 * listed below:
 * <ul>
 * <li>1)
 * {@link io.github.romvoid95.doc.DocConverter#read(java.lang.annotation.Annotation)}
 * should not throw any exceptions, nor otherwise halt a process due to one
 * being thrown.</li>
 * <li>2) When possible and practical, DocConverter implementations should be
 * classes nested within the {@code @interface} they are used to convert (the
 * example above demonstrates this).</li>
 * <li>3) If at all possible, developers should avoid any variables to
 * instantiate (IE: no-constructor).</li>
 * </ul>
 *
 * @see ConvertedBy
 */
@FunctionalInterface
public interface DocConverter<T extends Annotation> {
	/**
	 * Returns a String processed from the contents of the provided
	 * {@link java.lang.annotation.Annotation Annotation}. <br>
	 * <b>Should never throw and/or encounter uncaught exceptions.</b>
	 *
	 * @param annotation The annotation to process.
	 *
	 * @return A String processed from the Annotation provided.
	 */
	String read(T annotation);
}
