package com.xenoage.utils.jse;

/**
 * Useful methods to cast classes.
 * 
 * @author Andreas Wenger
 */
public class CastUtils {

	/**
	 * Returns the given object casted to the given class,
	 * if possible, otherwise null.
	 * This is like the "as" operator in C#.
	 */
	public static <T> T as(Object o, Class<T> cls) {
		if (o != null && cls.isInstance(o))
			return cls.cast(o);
		else
			return null;
	}

}
