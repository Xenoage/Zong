package com.xenoage.utils.math;

/**
 * This class contains the parameters a, b and c of a quadratic curve
 * y = ax² + bx + c.
 * 
 * @author Andreas Wenger
 */
public class QuadraticCurve {

	private final float a, b, c;


	public QuadraticCurve(float a, float b, float c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public float getA() {
		return a;
	}

	public float getB() {
		return b;
	}

	public float getC() {
		return c;
	}

	public float getY(float x) {
		return a * x * x + b * x + c;
	}

	public float getArea(float startX, float endX) {
		return (1f / 3 * a * endX * endX * endX + 1f / 2 * b * endX * endX + c * endX) -
			(1f / 3 * a * startX * startX * startX + 1f / 2 * b * startX * startX + c * startX);
	}

}
