package com.xenoage.utils.math;

import com.xenoage.utils.math.geom.Point2f;

import static com.xenoage.utils.math.geom.Point2f.p;

/**
 * Some useful mathematical functions.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class MathUtils {

	/** PI in float precision. */
	public static final float pi = (float) Math.PI;


	/**
	 * Returns max, if x > max, min, if x < min, else x.
	 */
	public static int clamp(int x, int min, int max) {
		if (x < min)
			return min;
		else if (x > max)
			return max;
		else
			return x;
	}

	/**
	 * Returns min, if x < min, else x.
	 */
	public static int clampMin(int x, int min) {
		if (x < min)
			return min;
		else
			return x;
	}

	/**
	 * Returns max, if x > max, min, if x < min, else x.
	 */
	public static float clamp(float x, float min, float max) {
		if (x < min)
			return min;
		else if (x > max)
			return max;
		else
			return x;
	}

	/**
	 * Returns min, if x < min, else x.
	 */
	public static float clampMin(float x, float min) {
		if (x < min)
			return min;
		else
			return x;
	}

	/**
	 * Returns max, if x > max, min, if x < min, else x.
	 */
	public static double clamp(double x, double min, double max) {
		if (x < min)
			return min;
		else if (x > max)
			return max;
		else
			return x;
	}

	/**
	 * Returns min, if x < min, else x.
	 */
	public static double clampMin(double x, double min) {
		if (x < min)
			return min;
		else
			return x;
	}

	/**
	 * Returns the greatest common divisor of the given numbers.
	 */
	public static int gcd(int n1, int n2) {
		if (n2 == 0)
			return n1;
		else
			return gcd(n2, n1 % n2);
	}

	/**
	 * Returns the least common multiple of the given numbers.
	 */
	public static int lcm(int n1, int n2) {
		int ret = gcd(n1, n2);
		ret = (n1 * n2) / ret;
		return ret;
	}
	
	/**
	 * Like the % operator, but also returns positive results for negative numbers.
	 * E.g. -3 mod 4 = 1.
	 */
	public static int mod(int n, int mod) {
		return ((n % mod) + mod) % mod;
	}

	/**
	 * Returns the lowest result of n modulo mod, where the result is still
	 * greater or equal than min. Also negative values are allowed, and n may be
	 * smaller than min.
	 */
	public static int modMin(int n, int mod, int min) {
		if (mod < 1)
			throw new IllegalArgumentException("mod must be > 0");
		while (n < min)
			n += mod;
		while (n - mod >= min)
			n -= mod;
		return n;
	}

	/**
	 * Rotates the given point by the given angle in degrees in counter
	 * clockwise order around the origin and returnes the rotated point.
	 */
	public static Point2f rotate(final Point2f p, float angle) {
		if (angle == 0f)
			return p;
		double rot = angle * Math.PI / 180f;
		double cos = Math.cos(rot);
		double sin = Math.sin(rot);
		float x = (float) (p.x * +cos + p.y * +sin);
		float y = (float) (p.x * -sin + p.y * +cos);
		return new Point2f(x, y);
	}

	/**
	 * Returns the position of the given cubic Bézier curve at the given t value
	 * between 0 and 1. The Bézier curve is defined by the start and end point
	 * (named p1 and p2) and two control points (named c1 and c2).
	 */
	public static Point2f bezier(Point2f p1, Point2f p2, Point2f c1, Point2f c2, float t) {
		return new Point2f((-p1.x + 3 * c1.x - 3 * c2.x + p2.x) * t * t * t +
			(3 * p1.x - 6 * c1.x + 3 * c2.x) * t * t + (-3 * p1.x + 3 * c1.x) * t + p1.x, (-p1.y + 3 *
			c1.y - 3 * c2.y + p2.y) *
			t * t * t + (3 * p1.y - 6 * c1.y + 3 * c2.y) * t * t + (-3 * p1.y + 3 * c1.y) * t + p1.y);
	}

	/**
	 * Linear interpolation between p1 and p2, at position t between t1 and t2,
	 * where t1 is the coordinate of p1 and t2 is the coordinate of p2.
	 */
	public static float interpolateLinear(float p1, float p2, float t1, float t2, float t) {
		return p1 + (p2 - p1) * (t - t1) / (t2 - t1);
	}

	/**
	 * Linear interpolation between p1 and p2, at position t between t1 and t2,
	 * where t1 is the coordinate of p1 and t2 is the coordinate of p2.
	 */
	public static float interpolateLinear(LinearInterpolationPoints points, float t) {
		return interpolateLinear(points.p1, points.p2, points.t1, points.t2, t);
	}

	public static class LinearInterpolationPoints {
		public final float p1, p2, t1, t2;

		public LinearInterpolationPoints(float p1, float p2, float t1, float t2) {
			this.p1 = p1;
			this.p2 = p2;
			this.t1 = t1;
			this.t2 = t2;
		}
	}

	public static int lowestPrimeNumber(int number) {
		for (int i = 2; i <= Math.sqrt(number); i++) {
			if (number % i == 0) {
				return i;
			}
		}
		return number;
	}

	/**
	 * @deprecated Use {@link Point2f#distance(Point2f, Point2f)} instead
	 */
	public static float distance(Point2f p1, Point2f p2) {
		float x = p1.x - p2.x;
		float y = p1.y - p2.y;
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Computes and returns a rotated rectangle, that encloses the given two
	 * points with the given width.
	 * This is shown here:
	 * 
	 * <pre>
	 *    [0]---___
	 *    /        ---___
	 *   p1              ---[1]   _ _
	 *  /                   /     /
	 * [3]---___           p2    / width
	 *          ---___    /     /
	 *                ---[2]  _/_
	 * </pre>
	 * 
	 * The result is returned as four Point2f values as shown on the above
	 * sketch.
	 */
	public static Point2f[] computeRectangleFromTo(Point2f p1, Point2f p2, float width) {
		// compute the line from p1 to p2
		Point2f p1Top2 = p2.sub(p1);
		// compute the line from p1 to [0]
		Point2f p1To0 = new Point2f(p1Top2.y, -p1Top2.x).normalize().scale(width / 2);
		// compute return values
		Point2f[] ret = new Point2f[] { p1.add(p1To0), p2.add(p1To0), p2.sub(p1To0), p1.sub(p1To0) };
		return ret;
	}

	public static boolean between(int value, int min, int max) {
		return value >= min && value <= max;
	}

	public static boolean between(float value, float min, float max) {
		return value >= min && value <= max;
	}

	/**
	 * Returns -1 if the given value is negative, 1 if the given value is
	 * positive, 0 otherwise.
	 */
	public static float sign(float v) {
		if (v < 0)
			return -1;
		else if (v > 0)
			return 1;
		else
			return 0;
	}
	
	/**
	 * Returns the minimum element of the given {@link Comparable}s.
	 * If all elements are null or if no element is given, null is returned.
	 * If more then one element qualifies, the first one is returned.
	 */
	public static <T extends Comparable<T>> T min(T... ts) {
		T ret = null;
		for (T t : ts)
			if (ret == null || (t != null && t.compareTo(ret) < 0))
				ret = t;
		return ret;
	}

	/**
	 * Returns the maximum element of the given {@link Comparable}s.
	 * If all elements are null or if no element is given, null is returned.
	 * If more then one element qualifies, the first one is returned.
	 */
	public static <T extends Comparable<T>> T max(T... ts) {
		T ret = null;
		for (T t : ts)
			if (ret == null || (t != null && t.compareTo(ret) > 0))
				ret = t;
		return ret;
	}

	/**
	 * Returns the normalized normal vector at the second given point.
	 * It is the mean value of the normal vector between the first and
	 * second and of the normal vector of the second and third point.
	 * @deprecated use {@link Point2f#normalVectorMean(Point2f, Point2f, Point2f)} instead
	 */
	public static Point2f normalVectorMean(Point2f start, Point2f middle, Point2f end) {
		Point2f n1 = normalVector(start, middle);
		Point2f n2 = normalVector(middle, end);
		return n1.add(n2).normalize();
	}

	/**
	 * Returns the normalized normal vector to the vector between the
	 * given two points.
	 * @deprecated use {@link Point2f#normalVector(Point2f, Point2f)} instead
	 */
	public static Point2f normalVector(Point2f start, Point2f end) {
		return normalVector(end.sub(start));
	}

	/**
	 * Returns the normalized normal vector to the given vector.
	 * @deprecated use {@link Point2f#normalVector(Point2f)} instead
	 */
	public static Point2f normalVector(Point2f v) {
		return p(-v.y, v.x).normalize();
	}

	/**
	 * For 2 ^ x = number, returns x. For example. 2 ^ x = 8 returns 3.
	 * When there is no integer solution, the next smaller integer is returned,
	 * for example 2 ^ x = 5 returns 2.
	 */
	public static int log2(long number) {
		if (number < 1)
			throw new IllegalArgumentException("log2(x) = n for n < 1 not possible");
		long n = 1;
		int ret = 0;
		while (n <= number) {
			n *= 2;
			ret++;
		}
		return ret - 1;
	}

	private MathUtils() {
	}

}
