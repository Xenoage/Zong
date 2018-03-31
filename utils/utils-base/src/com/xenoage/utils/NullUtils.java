package com.xenoage.utils;

/**
 * Some useful functions dealing with {@code null} values.
 * 
 * @author Andreas Wenger
 */
public class NullUtils {

	/**
	 * Returns the given object, if not null, otherwise the given replacement.
	 */
	public static <T> T notNull(T object, T replacement) {
		return (object != null ? object : replacement);
	}

	/**
	 * Returns the given object, if not null, otherwise an empty string.
	 */
	public static String notNull(String s) {
		return (s != null ? s : "");
	}

	/**
	 * Throws an IllegalArgumentException if one of the given arguments is null.
	 * @deprecated use {@link CheckUtils#checkArgsNotNull(Object...)}
	 */
	public static void throwNullArg(Object... o) {
		for (int i = 0; i < o.length; i++) {
			if (o[i] == null) {
				throw new IllegalArgumentException(
					"Argument may not be null (checked argument with index " + i + ")");
			}
		}
	}

	/**
	 * Throws an IllegalArgumentException if the given argument is null.
	 * @deprecated use {@link CheckUtils#checkNotNull(Object)}
	 */
	public static <T> T assertNotNull(T o) {
		if (o == null)
			throw new IllegalArgumentException("May not be null");
		return o;
	}

}
