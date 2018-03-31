package com.xenoage.utils.math.geom;

import static com.xenoage.utils.math.geom.Point2d.p;

/**
 * Class for a 2D rectangle.
 *
 * @author Andreas Wenger
 */
public final class Rectangle2d {

	public final Point2d position;
	public final Size2d size;


	public Rectangle2d(Point2d position, Size2d size) {
		//convert negative size
		if (size.width < 0 || size.height < 0) {
			double x = position.x;
			double y = position.y;
			if (size.width < 0)
				x = position.x + size.width;
			if (size.height < 0)
				y = position.y + size.height;
			this.position = new Point2d(x, y);
			this.size = new Size2d(Math.abs(size.width), Math.abs(size.height));
		}
		else {
			this.position = position;
			this.size = size;
		}
	}

	public Rectangle2d(double x, double y, double width, double height) {
		this(new Point2d(x, y), new Size2d(width, height));
	}

	public static Rectangle2d fromX1Y1X2Y2(double x1, double y1, double x2, double y2) {
		return new Rectangle2d(new Point2d(x1, y1), new Size2d(x2 - x1, y2 - y1));
	}

	public double width() {
		return size.width;
	}

	public double height() {
		return size.height;
	}

	public static Rectangle2d rf(double x, double y, double width, double height) {
		return new Rectangle2d(x, y, width, height);
	}

	public Rectangle2d scale(double f) {
		return new Rectangle2d(position.x * f, position.y * f, size.width * f, size.height * f);
	}

	public Rectangle2d scale(double fx, double fy) {
		return new Rectangle2d(position.x * fx, position.y * fy, size.width * fx, size.height * fy);
	}

	public Rectangle2d scaleX(double f) {
		return new Rectangle2d(position.x * f, position.y, size.width * f, size.height);
	}

	public Rectangle2d scaleY(double f) {
		return new Rectangle2d(position.x, position.y * f, size.width, size.height * f);
	}

	public Rectangle2d move(Point2d p) {
		return new Rectangle2d(position.add(p), size);
	}

	public Rectangle2d move(double x, double y) {
		return new Rectangle2d(position.add(x, y), size);
	}

	public boolean contains(Point2d point) {
		return (point.x >= position.x && point.y >= position.y && point.x <= position.x + size.width && point.y <= position.y +
			size.height);
	}

	/**
	 * Expands this rectangle by the given rectangle.
	 * After the operation both rectangles are optimally enclosed
	 * by this rectangle.
	 */
	public Rectangle2d extend(Rectangle2d r) {
		double newX1 = Math.min(this.position.x, r.position.x);
		double newX2 = Math.max(this.position.x + this.size.width, r.position.x + r.size.width);
		double newY1 = Math.min(this.position.y, r.position.y);
		double newY2 = Math.max(this.position.y + this.size.height, r.position.y + r.size.height);
		return new Rectangle2d(newX1, newY1, newX2 - newX1, newY2 - newY1);
	}

	/**
	 * Expands this rectangle by the given point.
	 * After the operation the given point is enclosed by this rectangle
	 * (optimally, if the rectangle had to be extended).
	 */
	public Rectangle2d extend(Point2d p) {
		double newX1 = Math.min(this.position.x, p.x);
		double newX2 = Math.max(this.position.x + this.size.width, p.x);
		double newY1 = Math.min(this.position.y, p.y);
		double newY2 = Math.max(this.position.y + this.size.height, p.y);
		return new Rectangle2d(newX1, newY1, newX2 - newX1, newY2 - newY1);
	}

	public Point2d center() {
		return new Point2d(centerX(), centerY());
	}

	public double x1() {
		return position.x;
	}

	public double y1() {
		return position.y;
	}

	public double x2() {
		return position.x + size.width;
	}

	public double y2() {
		return position.y + size.height;
	}

	public double centerX() {
		return position.x + size.width / 2;
	}

	public double centerY() {
		return position.y + size.height / 2;
	}

	public Point2d nw() {
		return p(x1(), y1());
	}

	public Point2d ne() {
		return p(x2(), y1());
	}

	public Point2d sw() {
		return p(x1(), y2());
	}

	public Point2d se() {
		return p(x2(), y2());
	}

	@Override public String toString() {
		return "[" + position + " / " + size + "]";
	}

}
