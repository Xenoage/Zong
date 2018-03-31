package com.xenoage.utils.math.geom;

/**
 * Class for a 2D size.
 *
 * @author Andreas Wenger
 */
public final class Size2i {

	public final int width;
	public final int height;


	public Size2i(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Size2i(Point2i p) {
		this.width = p.x;
		this.height = p.y;
	}

	public int getArea() {
		return width * height;
	}

	public Size2i scale(int scaling) {
		return new Size2i(width * scaling, height * scaling);
	}

	public Size2f scale(float scaling) {
		return new Size2f(width * scaling, height * scaling);
	}

	@Override public boolean equals(Object obj) {
		if (obj instanceof Size2i) {
			Size2i size = (Size2i) obj;
			return (width == size.width && height == size.height);
		}
		else {
			return super.equals(obj);
		}
	}

	@Override public String toString() {
		return width + "x" + height;
	}

}
