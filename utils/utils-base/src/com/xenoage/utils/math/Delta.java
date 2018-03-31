package com.xenoage.utils.math;

/**
 * This class contains delta values (epsilon)
 * for unit tests.
 *
 * @author Andreas Wenger
 */
public class Delta {

	/** Can be used to compare floats for equality. High precision. */
	public static final float DELTA_FLOAT = 0.000001f;

	/** Can be used to compare floats for equality. Low precision. */
	public static final float DELTA_FLOAT_ROUGH = 0.001f;

	/** Abbreviation for {@link #DELTA_FLOAT} */
	public static final float Df = DELTA_FLOAT;

	/** Abbreviation for {@link #DELTA_FLOAT_ROUGH} */
	public static final float DRf = DELTA_FLOAT;
	
	/** Abbreviation for {@link #DELTA_FLOAT} */
	public static final float df = DELTA_FLOAT;

	/** Abbreviation for {@link #DELTA_FLOAT_ROUGH} */
	public static final float drf = DELTA_FLOAT;


	public static boolean equals(float v1, float v2) {
		return Math.abs(v1 - v2) < DELTA_FLOAT;
	}

}
