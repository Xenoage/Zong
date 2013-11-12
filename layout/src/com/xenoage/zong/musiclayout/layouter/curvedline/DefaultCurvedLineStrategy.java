package com.xenoage.zong.musiclayout.layouter.curvedline;

import java.util.List;

import com.xenoage.utils.math.QuadraticCurve;
import com.xenoage.utils.math.VSide;
import com.xenoage.utils.math.geom.ConvexHull;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.util.math.BezierCurveTools;
import com.xenoage.zong.util.math.CubicBezierCurve;
import com.xenoage.zong.util.math.QuadraticCurvesTools;


/**
 * Default implementation for a {@link CurvedLineStrategy}.
 * 
 * @author Andreas Wenger
 */
public class DefaultCurvedLineStrategy
	implements CurvedLineStrategy
{
	
	public static final float ENDPOINT_TOLERANCE_LP = 6;

	
	@Override public CubicBezierCurve computeCurvedLine(List<Point2f> points, VSide side)
	{
		ConvexHull convexHull = ConvexHull.create(points, side);
    List<QuadraticCurve> quadCurves = QuadraticCurvesTools.computeOverConvexHull(
    	convexHull, ENDPOINT_TOLERANCE_LP, ENDPOINT_TOLERANCE_LP);
    QuadraticCurve bestQuadCurve = QuadraticCurvesTools.getBestCurve(quadCurves, convexHull);
    CubicBezierCurve bestBezierCurve = BezierCurveTools.computeBezierFrom(
    	bestQuadCurve, points.get(0).x, points.get(points.size() - 1).x);
    bestBezierCurve = BezierCurveTools.correctBezier(bestBezierCurve, side);
    return bestBezierCurve;
	}
	

}
