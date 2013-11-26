package com.xenoage.zong.util.math;

import static com.xenoage.utils.math.geom.Point2f.p;

import com.xenoage.utils.math.QuadraticCurve;
import com.xenoage.utils.math.VSide;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Computes a cubic bezier curve from a given quadratic curve
 * and a start and end point.
 * 
 * @author Andreas Wenger
 */
public class BezierCurveTools {

	/**
	 * Computes a curbic bezier curve from the given quadratic curve.
	 * @param curve   the quadratic curve
	 * @param startX  the horizontal start coordinate
	 * @param endX    the horizontal end coordinate
	 */
	public static CubicBezierCurve computeBezierFrom(QuadraticCurve curve, float startX, float endX) {
		//compute p1 and p2
		Point2f p1 = p(startX, curve.getY(startX));
		Point2f p2 = p(endX, curve.getY(endX));
		//compute a helper point about in the middle of the curve
		float hx = (startX + endX) / 2;
		Point2f h = p(hx, curve.getY(hx));
		//t ist simply 0.5 :-)
		float t = 0.5f;
		//using the quadratic bezier formula, compute c (the control point of a quadratic bezier curve):
		//c = (h - (1-t)²p1 - t²p2) / (2t(1-t))
		Point2f c = h.sub(p1.scale((1 - t) * (1 - t))).sub(p2.scale(t * t))
			.scale(1f / (2 * t * (1 - t)));
		//since we want to have a cubic bezier curve, compute c1 and c2 out of p1, p2, and c.
		//this is easy, because the direction of the control points does not change and
		//the length of the distance is multiplied by 2/3
		Point2f c1 = p1.scale(1f / 3).add(c.scale(2f / 3));
		Point2f c2 = p2.scale(1f / 3).add(c.scale(2f / 3));
		return new CubicBezierCurve(p1, c1, c2, p2);
	}

	/**
	 * Corrects the given bezier curve, if it is too plain,
	 * so it looks more like a part of a circle (which looks good for
	 * a slur).
	 */
	public static CubicBezierCurve correctBezier(CubicBezierCurve curve, VSide side) {
		//compute left angle
		Point2f p1c1 = curve.getC1().sub(curve.getP1());
		Point2f p1p2 = curve.getP2().sub(curve.getP1());
		float angleLeft = (float) Math.acos(p1c1.dotProduct(p1p2) / (p1c1.length() * p1p2.length()));
		if (angleLeft < 0.3 || Float.isNaN(angleLeft)) {
			//correct curve
			float slurDown = -1 * side.getDir() * 1;
			Point2f p1 = curve.getP1().add(0, slurDown);
			Point2f c1 = curve.getC1().add(0, slurDown).rotate(p1, 1 * side.getDir() * 0.5f);
			Point2f p2 = curve.getP2().add(0, slurDown);
			Point2f c2 = curve.getC2().add(0, slurDown).rotate(p2, -1 * side.getDir() * 0.5f);
			return new CubicBezierCurve(p1, c1, c2, p2);
		}
		else {
			//curve is ok
			return curve;
		}
	}

}
