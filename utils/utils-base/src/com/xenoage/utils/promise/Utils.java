package com.xenoage.utils.promise;

/**
 * Utils for promises.
 */
public class Utils {

	/**
	 * Turns a {@link Consumer} into a {@link Function} that returns null.
	 */
	public static Function toFunction(final Consumer consumer) {
		return value -> {
			consumer.run(value);
			return null;
		};
	}

}
