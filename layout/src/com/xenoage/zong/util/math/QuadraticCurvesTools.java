package com.xenoage.zong.util.math;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.xenoage.utils.math.Gauss;
import com.xenoage.utils.math.QuadraticCurve;
import com.xenoage.utils.math.VSide;
import com.xenoage.utils.math.geom.ConvexHull;
import com.xenoage.utils.math.geom.Point2f;

/**
 * This class computes quadratic curves using the given
 * parameters.
 * 
 * @author Andreas Wenger
 */
public class QuadraticCurvesTools {

	/**
	 * Computes a list of possible quadratic curves over the given {@link ConvexHull},
	 * that start at the first point of the hull and end at the last point of the hull,
	 * but may also start and end further away according to the given parameters. The curve
	 * will not cross the area of the convex hull.
	 * @param convexHull  the convex hull
	 * @param leftArea    the tolerance of the distance of the start point
	 *                    (between 0 and this value). always positive or 0.
	 * @param rightArea   the tolerance of the distance of the end point
	 *                    (between 0 and this value). always positive or 0.
	 * @return  a list of possible quadratic curves
	 */
	public static List<QuadraticCurve> computeOverConvexHull(ConvexHull convexHull, float leftArea,
		float rightArea) {
		LinkedList<QuadraticCurve> ret = new LinkedList<QuadraticCurve>();
		ArrayList<Point2f> points = convexHull.getPoints();
		VSide side = convexHull.getSide();
		int sideDir = side.getDir();
		int n = points.size();
		//compute the possible start and endpoints
		Point2f[] startPoints = new Point2f[] { points.get(0), points.get(0).add(0, sideDir * leftArea) };
		Point2f[] endPoints = new Point2f[] { points.get(n - 1),
			points.get(n - 1).add(0, sideDir * rightArea) };
		//the quadratic expression {a, b, c} for ax² + bx + c = 0
		//equations:
		// - (2): must start at startPoints[0] or startPoints[1] (between is never optimal!)
		// - (2): must end at endPoints[0] or endPoints[1] (between is never optimal!)
		//inequations:
		// - (0): a must be <=/>= 0 (parabola is open on the bottom/top side, dependent
		//        on the side of the convex hull) - not used in SLE, checked later
		// - (m): curve must be above/below each of the m = n-2 middle points (dependent on the side)
		int m = n - 2;
		Point2f[] eq = new Point2f[2 + 2 + m];
		eq[0] = startPoints[0];
		eq[1] = startPoints[1];
		eq[2] = endPoints[0];
		eq[3] = endPoints[1];
		for (int i = 0; i < m; i++) {
			eq[4 + i] = points.get(1 + i);
		}
		//strategy, based on the simplex algorithm for linear optimization:
		//for each possible combination of 3 equations (optimum is always at the corner of the simplex,
		//so we can use the inequations like equations), solve the SLE, test, if the curve is
		//valid for all inequations, and if so, compute the area between the curve and the convex hull.
		//take the curve which has the smallest area.
		//there are ((m+4) choose 3) possible SLEs, but we have to ignore those where eq[0] AND eq[1]
		//are used and those where eq[2] AND eq[3] are used.
		int[][] subsets = getAllCombinationsOf3(m + 4);
		for (int i = 0; i < subsets.length; i++) {
			int[] eqIndices = subsets[i];
			//not useable: {0,1,?}, {2,3,?} and {?,2,3}
			if (eqIndices[0] == 0 && eqIndices[1] == 1 || eqIndices[0] == 2 && eqIndices[1] == 3 ||
				eqIndices[1] == 2 && eqIndices[2] == 3) {
				//ignore
			}
			else {
				//usable. solve SLE
				double[][] A = new double[3][3];
				double b[] = new double[3];
				for (int iy = 0; iy < 3; iy++) {
					Point2f p = eq[eqIndices[iy]];
					A[iy][0] = p.x * p.x;
					A[iy][1] = p.x;
					A[iy][2] = 1;
					b[iy] = p.y;
				}
				double[] params = Gauss.solve(A, b);
				//parameters ok for all equations?
				boolean ok = true;
				ok &= sideDir * getY(startPoints[0].x, params) >= sideDir * startPoints[0].y;
				ok &= sideDir * getY(startPoints[1].x, params) <= sideDir * startPoints[1].y;
				ok &= sideDir * getY(endPoints[0].x, params) >= sideDir * endPoints[0].y;
				ok &= sideDir * getY(endPoints[1].x, params) <= sideDir * endPoints[1].y;
				ok &= sideDir * params[0] <= 0; //parabole is open on the bottom/top side
				for (int im = 0; ok && im < m; im++) {
					ok &= sideDir * getY(points.get(1 + im).x, params) >= sideDir * points.get(1 + im).y;
				}
				if (ok) {
					//remember this equation
					ret.add(new QuadraticCurve((float) params[0], (float) params[1], (float) params[2]));
				}
			}
		}
		if (ret.size() == 0) {
			//no curve found. use direct line between first and last point.
			double[][] A = new double[][] { { points.get(0).x, 1 }, { points.get(n - 1).x, 1 } };
			double b[] = new double[] { points.get(0).y, points.get(n - 1).y };
			double[] params = Gauss.solve(A, b);
			ret.add(new QuadraticCurve(0f, (float) params[0], (float) params[1]));
		}
		//return result
		return ret;
	}

	public static QuadraticCurve getBestCurve(List<QuadraticCurve> curves, ConvexHull hull) {
		if (curves.size() > 1) {
			//find the curve with the least area and return it
			int minIndex = 0;
			float minArea = Float.POSITIVE_INFINITY;
			for (int i = 0; i < curves.size(); i++) {
				float area = getAreaBetween(curves.get(i), hull);
				if (area < minArea) {
					minArea = area;
					minIndex = i;
				}
			}
			return curves.get(minIndex);
		}
		else if (curves.size() == 1) {
			return curves.get(0);
		}
		else {
			return null;
		}
	}

	//TODO: move into a math class
	private static int[][] getAllCombinationsOf3(int slotsCount) {
		//(dirty)
		//use array with 0 or 1 for each slot. count from 00...0 to 11...1
		//for all "numbers" with a sum of 3, remember the corresponding subset.
		ArrayList<int[]> ret = new ArrayList<int[]>();
		int[] subsetIndices = new int[slotsCount];
		int max = pow(2, slotsCount);
		for (int i = 0; i < max; i++) {
			if (sum(subsetIndices) == 3) {
				ret.add(getIndicesWith1(subsetIndices));
			}
			increment(subsetIndices);
		}
		return ret.toArray(new int[0][]);
	}

	private static int pow(int a, int b) //TODO
	{
		int ret = 1;
		for (int i = 0; i < b; i++) {
			ret *= a;
		}
		return ret;
	}

	private static void increment(int[] binaryNumber) {
		int digit = binaryNumber.length - 1;
		do {
			binaryNumber[digit] = 1 - binaryNumber[digit];
			digit--;
		}
		//repeat, if digit changed to 0, but stop at 11...1
		while (binaryNumber[digit + 1] == 0 && digit >= 0);
	}

	private static int sum(int[] number) {
		int ret = 0;
		for (int i = 0; i < number.length; i++) {
			ret += number[i];
		}
		return ret;
	}

	private static int[] getIndicesWith1(int[] binaryNumber) {
		int[] ret = new int[3];
		int index = 0;
		for (int i = 0; i < binaryNumber.length && index < 3; i++) {
			if (binaryNumber[i] == 1) {
				ret[index] = i;
				index++;
			}
		}
		return ret;
	}

	/**
	 * Returns ax² + bx + c.
	 * @param x       x
	 * @param params  {a, b, c}
	 */
	private static float getY(float x, double[] params) {
		return (float) (params[0] * x * x + params[1] * x + params[2]);
	}

	/**
	 * Computes the area between the given quadratic curve and the
	 * convex hull below it.
	 */
	private static float getAreaBetween(QuadraticCurve curve, ConvexHull convexHull) {
		float sumArea = 0;
		ArrayList<Point2f> polygon = convexHull.getPoints();
		for (int i = 0; i < polygon.size() - 1; i++) {
			float x1 = polygon.get(i).x;
			float x2 = polygon.get(i + 1).x;
			float curveArea = curve.getArea(x1, x2);
			float polyArea = (x2 - x1) * (polygon.get(i).y + polygon.get(i + 1).y) / 2;
			sumArea += convexHull.getSide().getDir() * (curveArea - polyArea);
		}
		return sumArea;
	}

}
