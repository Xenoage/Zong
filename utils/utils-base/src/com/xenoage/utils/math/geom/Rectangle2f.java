package com.xenoage.utils.math.geom;

import static com.xenoage.utils.math.geom.Point2f.p;

/**
 * Class for a 2D rectangle.
 *
 * @author Andreas Wenger
 */
public final class Rectangle2f
	implements Shape {

	public final Point2f position;
	public final Size2f size;


	public Rectangle2f(Point2f position, Size2f size) {
		//convert negative size
		if (size.width < 0 || size.height < 0) {
			float x = position.x;
			float y = position.y;
			if (size.width < 0)
				x = position.x + size.width;
			if (size.height < 0)
				y = position.y + size.height;
			this.position = new Point2f(x, y);
			this.size = new Size2f(Math.abs(size.width), Math.abs(size.height));
		}
		else {
			this.position = position;
			this.size = size;
		}
	}

	public Rectangle2f(float x, float y, float width, float height) {
		this(new Point2f(x, y), new Size2f(width, height));
	}

	public static Rectangle2f fromX1Y1X2Y2(float x1, float y1, float x2, float y2) {
		return new Rectangle2f(new Point2f(x1, y1), new Size2f(x2 - x1, y2 - y1));
	}

	public float width() {
		return size.width;
	}

	public float height() {
		return size.height;
	}

	public static Rectangle2f rf(float x, float y, float width, float height) {
		return new Rectangle2f(x, y, width, height);
	}

	public Rectangle2f scale(float f) {
		return new Rectangle2f(position.x * f, position.y * f, size.width * f, size.height * f);
	}

	public Rectangle2f scale(float fx, float fy) {
		return new Rectangle2f(position.x * fx, position.y * fy, size.width * fx, size.height * fy);
	}

	public Rectangle2f scaleX(float f) {
		return new Rectangle2f(position.x * f, position.y, size.width * f, size.height);
	}

	public Rectangle2f scaleY(float f) {
		return new Rectangle2f(position.x, position.y * f, size.width, size.height * f);
	}

	public Rectangle2f move(Point2f p) {
		return new Rectangle2f(position.add(p), size);
	}

	public Rectangle2f move(float x, float y) {
		return new Rectangle2f(position.add(x, y), size);
	}

	@Override public boolean contains(Point2f point) {
		return (point.x >= position.x && point.y >= position.y && point.x <= position.x + size.width && point.y <= position.y +
			size.height);
	}

	/**
	 * Expands this rectangle by the given rectangle.
	 * After the operation both rectangles are optimally enclosed
	 * by this rectangle.
	 */
	public Rectangle2f extend(Rectangle2f r) {
		float newX1 = Math.min(this.position.x, r.position.x);
		float newX2 = Math.max(this.position.x + this.size.width, r.position.x + r.size.width);
		float newY1 = Math.min(this.position.y, r.position.y);
		float newY2 = Math.max(this.position.y + this.size.height, r.position.y + r.size.height);
		return new Rectangle2f(newX1, newY1, newX2 - newX1, newY2 - newY1);
	}

	/**
	 * Expands this rectangle by the given point.
	 * After the operation the given point is enclosed by this rectangle
	 * (optimally, if the rectangle had to be extended).
	 */
	public Rectangle2f extend(Point2f p) {
		float newX1 = Math.min(this.position.x, p.x);
		float newX2 = Math.max(this.position.x + this.size.width, p.x);
		float newY1 = Math.min(this.position.y, p.y);
		float newY2 = Math.max(this.position.y + this.size.height, p.y);
		return new Rectangle2f(newX1, newY1, newX2 - newX1, newY2 - newY1);
	}

	public Point2f center() {
		return new Point2f(centerX(), centerY());
	}

	public float x1() {
		return position.x;
	}

	public float y1() {
		return position.y;
	}

	public float x2() {
		return position.x + size.width;
	}

	public float y2() {
		return position.y + size.height;
	}

	public float centerX() {
		return position.x + size.width / 2;
	}

	public float centerY() {
		return position.y + size.height / 2;
	}

	public Point2f nw() {
		return p(x1(), y1());
	}

	public Point2f ne() {
		return p(x2(), y1());
	}

	public Point2f sw() {
		return p(x1(), y2());
	}

	public Point2f se() {
		return p(x2(), y2());
	}

	@Override public String toString() {
		return "[" + position + " / " + size + "]";
	}

}
