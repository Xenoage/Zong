package com.xenoage.utils.math.geom;

/**
 * Class for a 2D size.
 *
 * @author Andreas Wenger
 */
public final class Size2d {

	public final double width;
	public final double height;


	public Size2d(Size2d size) {
		this.width = size.width;
		this.height = size.height;
	}

	public Size2d(Size2i size) {
		this.width = size.width;
		this.height = size.height;
	}

	public Size2d(Size2f size) {
		this.width = size.width;
		this.height = size.height;
	}

	public Size2d(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public static Size2d s(double width, double height) {
		return new Size2d(width, height);
	}

	public double getArea() {
		return width * height;
	}

	public Size2d add(Size2i size) {
		return new Size2d(this.width + size.width, this.height + size.height);
	}

	public Size2d scale(double f) {
		return new Size2d(width * f, height * f);
	}

	public Size2d withWidth(double width) {
		return new Size2d(width, height);
	}

	public Size2d withHeight(double height) {
		return new Size2d(width, height);
	}

	@Override public String toString() {
		return width + ", " + height;
	}

	@Override public boolean equals(Object obj) {
		if (obj instanceof Size2d) {
			Size2d size = (Size2d) obj;
			return (width == size.width && height == size.height);
		}
		else {
			return super.equals(obj);
		}
	}

}
