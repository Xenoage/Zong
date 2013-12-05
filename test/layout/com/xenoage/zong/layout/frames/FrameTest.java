package com.xenoage.zong.layout.frames;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;

/**
 * Tests for a {@link Frame}.
 *
 * @author Andreas Wenger
 */
public class FrameTest {

	/**
	 * Tests the computation of the absolute position.
	 */
	@Test public void getAbsolutePosition() {
		double u = Math.PI / 180d;
		Layout layout = new Layout(null);
		Page page = new Page(PageFormat.defaultValue);
		layout.addPage(page);
		GroupFrame frame = createFrameWithChildren();
		page.addFrame(frame);
		//frame
		Point2f p = frame.getAbsolutePosition();
		assertEquals(50, p.x, Delta.DELTA_FLOAT);
		assertEquals(75, p.y, Delta.DELTA_FLOAT);
		//child frame
		GroupFrame child = (GroupFrame) frame.children.get(0);
		p = child.getAbsolutePosition();
		float r = 30;
		double childX = 50 + 20 * Math.cos(r * u) + 10 * Math.sin(r * u);
		double childY = 75 - 20 * Math.sin(r * u) + 10 * Math.cos(r * u);
		assertEquals(childX, p.x, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(childY, p.y, Delta.DELTA_FLOAT_ROUGH);
		//child frame of child frame
		Frame child2 = child.children.get(0);
		p = child2.getAbsolutePosition();
		r += 30;
		assertEquals(childX + -15 * Math.cos(r * u) + -10 * Math.sin(r * u), p.x,
			Delta.DELTA_FLOAT_ROUGH);
		assertEquals(childY - -15 * Math.sin(r * u) + -10 * Math.cos(r * u), p.y,
			Delta.DELTA_FLOAT_ROUGH);
	}

	public static GroupFrame createFrameWithChildren() {
		//frame
		GroupFrame frame = new GroupFrame();
		frame.setPosition(new Point2f(50, 75));
		frame.setSize(new Size2f(80, 50));
		frame.setRotation(30);
		//child frame
		GroupFrame child = new GroupFrame();
		child.setPosition(new Point2f(20, 10));
		child.setSize(new Size2f(30, 20));
		child.setRotation(30);
		//child frame of child frame
		TestFrame child2 = new TestFrame();
		child2.setPosition(new Point2f(-15, -10));
		child2.setSize(new Size2f(5, 5));
		child2.setRotation(45);
		//hierarchy
		child.addChildFrame(child2);
		frame.addChildFrame(child);
		return frame;
	}

}
