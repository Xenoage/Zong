package com.xenoage.utils.math.geom;

import com.xenoage.utils.math.VSide;
import org.junit.Test;

import java.util.ArrayList;

import static com.xenoage.utils.math.geom.Point2f.p;
import static org.junit.Assert.*;

/**
 * Test cases for the {@link ConvexHull} class.
 * 
 * @author Andreas Wenger
 */
public class ConvexHullTest {

	@Test public void computeHullTest() {
		//top hull
		ArrayList<Point2f> points = new ArrayList<>();
		points.add(p(1, 1));
		points.add(p(2, 2));
		points.add(p(3, 4));
		points.add(p(4, 2));
		points.add(p(5, 1));
		ArrayList<Point2f> res = ConvexHull.create(points, VSide.Top).getPoints();
		assertEquals(3, res.size());
		assertEquals(p(1, 1), res.get(0));
		assertEquals(p(3, 4), res.get(1));
		assertEquals(p(5, 1), res.get(2));
		//bottom hull
		points = new ArrayList<>();
		points.add(p(1, 4));
		points.add(p(2, 3));
		points.add(p(3, 1));
		points.add(p(4, 2));
		points.add(p(5, 4));
		res = ConvexHull.create(points, VSide.Bottom).getPoints();
		assertEquals(4, res.size());
		assertEquals(p(1, 4), res.get(0));
		assertEquals(p(3, 1), res.get(1));
		assertEquals(p(4, 2), res.get(2));
		assertEquals(p(5, 4), res.get(3));
	}

}
