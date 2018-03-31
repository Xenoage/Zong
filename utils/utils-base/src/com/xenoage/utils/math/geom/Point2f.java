package com.xenoage.utils.math.geom;

/**
 * Class for a 2D point.
 *
 * @author Andreas Wenger
 */
public final class Point2f {

	/** Origin point, (0, 0). */
	public static final Point2f origin = new Point2f(0, 0);

	public final float x;
	public final float y;


	public Point2f(Size2f size) {
		this.x = size.width;
		this.y = size.height;
	}

	public Point2f(Point2i p) {
		this.x = p.x;
		this.y = p.y;
	}

	public Point2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point2f(float xy) {
		this.x = xy;
		this.y = xy;
	}

	public static Point2f p(float x, float y) {
		return new Point2f(x, y);
	}

	public Point2f add(Point2f p) {
		return new Point2f(x + p.x, y + p.y);
	}

	public Point2f add(Point2i p) {
		return new Point2f(x + p.x, y + p.y);
	}

	public Point2f add(Size2f s) {
		return new Point2f(x + s.width, y + s.height);
	}

	public Point2f add(float x, float y) {
		return new Point2f(this.x + x, this.y + y);
	}

	public Point2f sub(Point2f p) {
		return new Point2f(x - p.x, y - p.y);
	}

	public Point2f sub(Size2f s) {
		return new Point2f(x - s.width, y - s.height);
	}

	public Point2f sub(float x, float y) {
		return new Point2f(this.x - x, this.y - y);
	}

	public Point2f scale(float s) {
		return new Point2f(x * s, y * s);
	}

	public Point2f withX(float x) {
		return new Point2f(x, y);
	}

	public Point2f withY(float y) {
		return new Point2f(x, y);
	}

	public float length() {
		return (float) (Math.sqrt(x * x + y * y));
	}

	public Point2f normalize() {
		double length = Math.sqrt(x * x + y * y);
		return new Point2f((float) (x / length), (float) (y / length));
	}

	public float dotProduct(Point2f p) {
		return x * p.x + y * p.y;
	}

	/**
	 * Rotates this point around another one.
	 * @param around   the point to rotate around
	 * @param radians  the angle in radians
	 */
	public Point2f rotate(Point2f around, float radians) {
		float x = this.x - around.x;
		float y = this.y - around.y;
		float cos = (float) Math.cos(radians);
		float sin = (float) Math.sin(radians);
		float xr = x * cos - y * sin;
		float yr = y * cos + x * sin;
		return p(xr + around.x, yr + around.y);
	}

	public static float distance(Point2f p1, Point2f p2) {
		float x = p1.x - p2.x;
		float y = p1.y - p2.y;
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns the normalized normal vector to the vector between the
	 * given two points.
	 */
	public static Point2f normalVector(Point2f start, Point2f end) {
		return normalVector(end.sub(start));
	}

	/**
	 * Returns the normalized normal vector to the given vector.
	 */
	public static Point2f normalVector(Point2f v) {
		return p(-v.y, v.x).normalize();
	}

	/**
	 * Returns the normalized normal vector at the second given point.
	 * It is the mean value of the normal vector between the first and
	 * second and of the normal vector of the second and third point.
	 */
	public static Point2f normalVectorMean(Point2f start, Point2f middle, Point2f end) {
		Point2f n1 = normalVector(start, middle);
		Point2f n2 = normalVector(middle, end);
		return n1.add(n2).normalize();
	}

	/**
	 * Returns true, if the given point p is within the triangle
	 * defined by the given points t1, t2 and t3.
	 */
	public static boolean isPointInTriangle(Point2f p, Point2f t1, Point2f t2, Point2f t3) {
		boolean b1 = sign(p, t1, t2) < 0;
		boolean b2 = sign(p, t2, t3) < 0;
		boolean b3 = sign(p, t3, t1) < 0;
		return ((b1 == b2) && (b2 == b3));
	}

	private static float sign(Point2f p1, Point2f p2, Point2f p3) {
		return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}

	@Override public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override public boolean equals(Object o) {
		if (o instanceof Point2f) {
			Point2f p = (Point2f) o;
			return (x == p.x && y == p.y);
		}
		else {
			return false;
		}
	}

}
