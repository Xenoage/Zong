package com.xenoage.utils.math

import com.xenoage.utils.math.Point2f.Companion.p
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/** PI in float precision.  */
val pi = PI.toFloat()

/**
 * Returns max, if x > max, min, if x < min, else x.
 */
fun clamp(x: Int, min: Int, max: Int): Int {
	return if (x < min)
		min
	else if (x > max)
		max
	else
		x
}

/**
 * Returns min, if x < min, else x.
 */
fun clampMin(x: Int, min: Int): Int {
	return if (x < min)
		min
	else
		x
}

/**
 * Returns max, if x > max, min, if x < min, else x.
 */
fun clamp(x: Float, min: Float, max: Float): Float {
	return if (x < min)
		min
	else if (x > max)
		max
	else
		x
}

/**
 * Returns min, if x < min, else x.
 */
fun clampMin(x: Float, min: Float): Float {
	return if (x < min)
		min
	else
		x
}

/**
 * Returns max, if x > max, min, if x < min, else x.
 */
fun clamp(x: Double, min: Double, max: Double): Double {
	return if (x < min)
		min
	else if (x > max)
		max
	else
		x
}

/**
 * Returns min, if x < min, else x.
 */
fun clampMin(x: Double, min: Double): Double {
	return if (x < min)
		min
	else
		x
}

/**
 * Returns the greatest common divisor of the given numbers.
 */
fun gcd(n1: Int, n2: Int): Int {
	return if (n2 == 0)
		n1
	else
		gcd(n2, n1 % n2)
}

/**
 * Returns the least common multiple of the given numbers.
 */
fun lcm(n1: Int, n2: Int): Int {
	var ret = gcd(n1, n2)
	ret = n1 * n2 / ret
	return ret
}

/**
 * Like the % operator, but also returns positive results for negative numbers.
 * E.g. -3 mod 4 = 1.
 */
fun mod(n: Int, mod: Int): Int {
	return (n % mod + mod) % mod
}

/**
 * Returns the lowest result of n modulo mod, where the result is still
 * greater or equal than min. Also negative values are allowed, and n may be
 * smaller than min.
 */
fun modMin(n: Int, mod: Int, min: Int): Int {
	var n = n
	if (mod < 1)
		throw IllegalArgumentException("mod must be > 0")
	while (n < min)
		n += mod
	while (n - mod >= min)
		n -= mod
	return n
}

/**
 * Rotates the given point by the given angle in degrees in counter
 * clockwise order around the origin and returnes the rotated point.
 */
fun rotate(p: Point2f, angle: Float): Point2f {
	if (angle == 0f)
		return p
	val rot = angle * PI / 180
	val cos = cos(rot)
	val sin = sin(rot)
	val x = (p.x * +cos + p.y * +sin).toFloat()
	val y = (p.x * -sin + p.y * +cos).toFloat()
	return Point2f(x, y)
}

/**
 * Returns the position of the given cubic Bézier curve at the given t value
 * between 0 and 1. The Bézier curve is defined by the start and end point
 * (named p1 and p2) and two control points (named c1 and c2).
 */
fun bezier(p1: Point2f, p2: Point2f, c1: Point2f, c2: Point2f, t: Float) =
	Point2f((-p1.x + 3 * c1.x - 3 * c2.x + p2.x) * t * t * t +
			(3 * p1.x - 6 * c1.x + 3 * c2.x) * t * t + (-3 * p1.x + 3 * c1.x) * t + p1.x, (-p1.y + 3 * c1.y - 3 * c2.y + p2.y) *
			t * t * t + (3 * p1.y - 6 * c1.y + 3 * c2.y) * t * t + (-3 * p1.y + 3 * c1.y) * t + p1.y)

/**
 * Linear interpolation between p1 and p2, at position t between t1 and t2,
 * where t1 is the coordinate of p1 and t2 is the coordinate of p2.
 */
fun interpolateLinear(p1: Float, p2: Float, t1: Float, t2: Float, t: Float): Float {
	return p1 + (p2 - p1) * (t - t1) / (t2 - t1)
}

/**
 * Linear interpolation between p1 and p2, at position t between t1 and t2,
 * where t1 is the coordinate of p1 and t2 is the coordinate of p2.
 */
fun interpolateLinear(points: LinearInterpolationPoints, t: Float): Float {
	return interpolateLinear(points.p1, points.p2, points.t1, points.t2, t)
}

class LinearInterpolationPoints(val p1: Float, val p2: Float, val t1: Float, val t2: Float)

fun lowestPrimeNumber(number: Int): Int {
	var i = 2
	while (i <= sqrt(number.toDouble())) {
		if (number % i == 0)
			return i
		i++
	}
	return number
}

/**
 * Computes and returns a rotated rectangle, that encloses the given two
 * points with the given width.
 * This is shown here:
 *
 * <pre>
 * [0]---___
 * /        ---___
 * p1              ---[1]   _ _
 * /                   /     /
 * [3]---___           p2    / width
 * ---___    /     /
 * ---[2]  _/_
</pre> *
 *
 * The result is returned as four Point2f values as shown on the above
 * sketch.
 */
fun computeRectangleFromTo(p1: Point2f, p2: Point2f, width: Float): Array<Point2f> {
	// compute the line from p1 to p2
	val p1Top2 = p2.sub(p1)
	// compute the line from p1 to [0]
	val p1To0 = Point2f(p1Top2.y, -p1Top2.x).normalize().scale(width / 2)
	// compute return values
	return arrayOf<Point2f>(p1.add(p1To0), p2.add(p1To0), p2.sub(p1To0), p1.sub(p1To0))
}

/**
 * Returns -1 if the given value is negative, 1 if the given value is
 * positive, 0 otherwise.
 */
fun sign(v: Float) =
	if (v < 0)
		-1f
	else if (v > 0)
		1f
	else
		0f

/**
 * Returns the minimum element of the given [Comparable]s.
 * If all elements are null or if no element is given, null is returned.
 * If more then one element qualifies, the first one is returned.
 */
fun <T : Comparable<T>> min_OBSOLETE(vararg ts: T): T? {
	var ret: T? = null
	for (t in ts)
		if (ret == null || t != null && t.compareTo(ret) < 0)
			ret = t
	return ret
}

/**
 * Returns the maximum element of the given [Comparable]s.
 * If all elements are null or if no element is given, null is returned.
 * If more then one element qualifies, the first one is returned.
 */
fun <T : Comparable<T>> max_OBSOLETE(vararg ts: T): T? {
	var ret: T? = null
	for (t in ts)
		if (ret == null || t != null && t.compareTo(ret) > 0)
			ret = t
	return ret
}

/**
 * For 2 ^ x = number, returns x. For example. 2 ^ x = 8 returns 3.
 * When there is no integer solution, the next smaller integer is returned,
 * for example 2 ^ x = 5 returns 2.
 */
fun log2(number: Long): Int {
	if (number < 1)
		throw IllegalArgumentException("log2(x) = n for n < 1 not possible")
	var n: Long = 1
	var ret = 0
	while (n <= number) {
		n *= 2
		ret++
	}
	return ret - 1
}
