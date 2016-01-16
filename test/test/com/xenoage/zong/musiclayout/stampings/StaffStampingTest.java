package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.math.Delta.df;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.format.ScoreFormat;

/**
 * Tests for {@link StaffStamping}.
 *
 * @author Andreas Wenger
 */
public class StaffStampingTest {

	private ScoreFormat scoreFormat = new ScoreFormat();


	@Test public void createStaffLayoutElement() {
		//create default staff layout element.
		//drawing element must have 5 lines and the default interline space.
		StaffStamping staff1 = new StaffStamping(null, 0, new Point2f(40, 80), 160, 5, 1.6f);
		assertEquals(5, staff1.linesCount);
		assertEquals(scoreFormat.getInterlineSpace(), staff1.is, df);
		assertEquals(40, staff1.positionMm.x, df);
		assertEquals(80, staff1.positionMm.y, df);
		assertEquals(160, staff1.lengthMm, df);
		//create another staff layout element
		//with 3 lines and 4 mm interline space.
		StaffStamping staff2 = new StaffStamping(null, 0, new Point2f(40, 160), 160, 3, 4);
		assertEquals(3, staff2.linesCount);
		assertEquals(4, staff2.is, df);
	}

	@Test public void containsPoint() {
		StaffStamping staff = new StaffStamping(null, 0, new Point2f(40, 80), 160, 5, 1);
		Shape shape = staff.getBoundingShape();
		//don't hit it
		assertFalse(shape.contains(new Point2f(39, 81)));
		assertFalse(shape.contains(new Point2f(42, 79)));
		assertFalse(shape.contains(new Point2f(201, 81)));
		assertFalse(shape.contains(new Point2f(100, 85)));
		//hit it
		assertTrue(shape.contains(new Point2f(40, 81)));
		assertTrue(shape.contains(new Point2f(41, 80)));
		assertTrue(shape.contains(new Point2f(200, 81)));
		assertTrue(shape.contains(new Point2f(100, 84)));
	}

}
