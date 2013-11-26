package com.xenoage.zong.util.math;

import com.xenoage.utils.math.geom.Point2f;

/**
 * Class for a cubic bezier curve.
 * All points have absolute coordinates.
 * 
 * @author Andreas Wenger
 */
public class CubicBezierCurve {

	private final Point2f p1, c1, c2, p2;


	public CubicBezierCurve(Point2f p1, Point2f c1, Point2f c2, Point2f p2) {
		this.p1 = p1;
		this.c1 = c1;
		this.c2 = c2;
		this.p2 = p2;
	}

	public Point2f getP1() {
		return p1;
	}

	public Point2f getC1() {
		return c1;
	}

	public Point2f getC2() {
		return c2;
	}

	public Point2f getP2() {
		return p2;
	}

}
