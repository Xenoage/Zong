package com.xenoage.utils.math.geom;

/**
 * Class for a 2D point.
 *
 * @author Andreas Wenger
 */
public final class Point2i {
	
	/** Point at the origin (0, 0). */
	public static final Point2i origin = new Point2i(0, 0);


	public final int x;
	public final int y;


	public Point2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point2i(Point2f p) {
		this.x = Math.round(p.x);
		this.y = Math.round(p.y);
	}

	public Point2i add(int x, int y) {
		return new Point2i(this.x + x, this.y + y);
	}

	public Point2i add(Point2i p) {
		return new Point2i(this.x + p.x, this.y + p.y);
	}

	public Point2i sub(Point2i p) {
		return new Point2i(this.x - p.x, this.y - p.y);
	}
	
	@Override public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point2i other = (Point2i) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override public String toString() {
		return x + ";" + y;
	}

}
