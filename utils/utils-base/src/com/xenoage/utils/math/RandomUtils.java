package com.xenoage.utils.math;

/**
 * Some useful methods with random numbers.
 * 
 * @author Andreas Wenger
 */
public class RandomUtils {

	public static int randomInt(int minInclusive, int maxInclusive) {
		return minInclusive + (int) (Math.random() * (maxInclusive + 1 - minInclusive));
	}

}
