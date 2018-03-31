package com.xenoage.utils.math.geom;

/**
 * Class for a 2D point.
 *
 * @author Andreas Wenger
 */
public final class Point2d {

	/** Origin point, (0, 0). */
	public static final Point2d origin = new Point2d(0, 0);

	public final double x;
	public final double y;


	public Point2d(Point2i p) {
		this.x = p.x;
		this.y = p.y;
	}

	public Point2d(Point2f p) {
		this.x = p.x;
		this.y = p.y;
	}

	public Point2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2d(double xy) {
		this.x = xy;
		this.y = xy;
	}

	public static Point2d p(double x, double y) {
		return new Point2d(x, y);
	}

	public Point2d add(Point2d p) {
		return new Point2d(x + p.x, y + p.y);
	}

	public Point2d add(Point2i p) {
		return new Point2d(x + p.x, y + p.y);
	}

	public Point2d add(Size2f s) {
		return new Point2d(x + s.width, y + s.height);
	}

	public Point2d add(double x, double y) {
		return new Point2d(this.x + x, this.y + y);
	}

	public Point2d sub(Point2d p) {
		return new Point2d(x - p.x, y - p.y);
	}

	public Point2d sub(double x, double y) {
		return new Point2d(this.x - x, this.y - y);
	}

	public Point2d scale(double s) {
		return new Point2d(x * s, y * s);
	}

	public Point2d withX(double x) {
		return new Point2d(x, y);
	}

	public Point2d withY(double y) {
		return new Point2d(x, y);
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public Point2d normalize() {
		double length = Math.sqrt(x * x + y * y);
		return new Point2d(x / length, y / length);
	}

	public double dotProduct(Point2d p) {
		return x * p.x + y * p.y;
	}

	/**
	 * Rotates this point around another one.
	 * @param around   the point to rotate around
	 * @param radians  the angle in radians
	 */
	public Point2d rotate(Point2d around, double radians) {
		double x = this.x - around.x;
		double y = this.y - around.y;
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		double xr = x * cos - y * sin;
		double yr = y * cos + x * sin;
		return p(xr + around.x, yr + around.y);
	}

	public static double distance(Point2d p1, Point2d p2) {
		double x = p1.x - p2.x;
		double y = p1.y - p2.y;
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns the normalized normal vector to the vector between the
	 * given two points.
	 */
	public static Point2d normalVector(Point2d start, Point2d end) {
		return normalVector(end.sub(start));
	}

	/**
	 * Returns the normalized normal vector to the given vector.
	 */
	public static Point2d normalVector(Point2d v) {
		return p(-v.y, v.x).normalize();
	}

	/**
	 * Returns the normalized normal vector at the second given point.
	 * It is the mean value of the normal vector between the first and
	 * second and of the normal vector of the second and third point.
	 */
	public static Point2d normalVectorMean(Point2d start, Point2d middle, Point2d end) {
		Point2d n1 = normalVector(start, middle);
		Point2d n2 = normalVector(middle, end);
		return n1.add(n2).normalize();
	}

	/**
	 * Returns true, if the given point p is within the triangle
	 * defined by the given points t1, t2 and t3.
	 */
	public static boolean isPointInTriangle(Point2d p, Point2d t1, Point2d t2, Point2d t3) {
		boolean b1 = sign(p, t1, t2) < 0;
		boolean b2 = sign(p, t2, t3) < 0;
		boolean b3 = sign(p, t3, t1) < 0;
		return ((b1 == b2) && (b2 == b3));
	}

	private static double sign(Point2d p1, Point2d p2, Point2d p3) {
		return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}

	@Override public String toString() {
		return x + ", " + y;
	}

	@Override public boolean equals(Object o) {
		if (o instanceof Point2d) {
			Point2d p = (Point2d) o;
			return (x == p.x && y == p.y);
		}
		else {
			return false;
		}
	}

}
