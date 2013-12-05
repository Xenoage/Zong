package com.xenoage.zong.layout;

import static com.xenoage.utils.math.geom.Point2f.p;
import static com.xenoage.utils.math.geom.Size2f.s;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.format.PageMargins;
import com.xenoage.zong.layout.frames.FP;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.GroupFrame;
import com.xenoage.zong.layout.frames.ScoreFrame;

/**
 * Tests for {@link Page}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class PageTest {

	private Frame frm1, frm2a, frm2b, frm3;
	private GroupFrame frm2;


	/**
	 * Tests the computeFramePosition-method
	 * without rotation.
	 */
	@Test public void getFPTestUnrotated() {
		Layout layout = createPageWithUnrotatedFrames();
		Page page = layout.getPages().get(0);

		//Test 1
		Point2f p = new Point2f(113, 122);
		FP fp = page.getFP(p);
		assertNotNull(fp);
		assertEquals(frm2, fp.getFrame());
		assertEquals(23, fp.getPosition().x, Delta.DELTA_FLOAT);
		assertEquals(12, fp.getPosition().y, Delta.DELTA_FLOAT);

		//Test 2
		p = new Point2f(122, 108);
		fp = page.getFP(p);
		assertNotNull(fp);
		assertEquals(frm2a, fp.getFrame());
		assertEquals(2, fp.getPosition().x, Delta.DELTA_FLOAT);
		assertEquals(-2, fp.getPosition().y, Delta.DELTA_FLOAT);

		//Test 3
		p = new Point2f(86, 88);
		fp = page.getFP(p);
		assertNull(fp);
	}

	/**
	 * Tests the computeFramePosition-method with rotation.
	 * See PageTest.odg in this folder.
	 */
	@Test public void getFPTestRotated() {
		Layout layout = createPageWithRotatedFrames();
		Page page = layout.getPages().get(0);
		FP fp;
		float delta = 1;

		//Test 1 (yellow)
		fp = page.getFP(p(162.1f, 96.1f));
		assertNull(fp);
		fp = page.getFP(p(163f, 107.3f));
		assertNull(fp);
		fp = page.getFP(p(163f, 100.4f));
		assertNotNull(fp);
		assertEquals(frm1, fp.getFrame());
		Point2f pExpected = p(27, -38);
		assertEquals(pExpected.x, fp.getPosition().x, delta);
		assertEquals(pExpected.y, fp.getPosition().y, delta);

		//Test 2 (green)
		fp = page.getFP(p(137, 145));
		assertEquals(frm1, fp.getFrame());
		fp = page.getFP(p(130, 160));
		assertEquals(frm1, fp.getFrame());
		fp = page.getFP(p(132, 152.1f));
		assertEquals(frm3, fp.getFrame());
		fp = page.getFP(p(137, 152.1f));
		assertEquals(frm3, fp.getFrame());

		//Test 3 (orange)
		fp = page.getFP(p(100, 85));
		assertEquals(frm2, fp.getFrame());
		fp = page.getFP(p(102, 90));
		assertEquals(frm2, fp.getFrame());
		fp = page.getFP(p(102, 95));
		assertEquals(frm2a, fp.getFrame());
		pExpected = p(-2, -4);
		assertEquals(pExpected.x, fp.getPosition().x, delta);
		assertEquals(pExpected.y, fp.getPosition().y, delta);
		fp = page.getFP(p(109, 84));
		assertEquals(frm1, fp.getFrame());
		fp = page.getFP(p(105, 82.1f));
		assertNull(fp);

		//Test 4 (red)
		fp = page.getFP(p(82, 130));
		assertEquals(frm2b, fp.getFrame());
		pExpected = p(-8, -9);
		assertEquals(pExpected.x, fp.getPosition().x, delta);
		assertEquals(pExpected.y, fp.getPosition().y, delta);
		fp = page.getFP(p(76, 127.1f));
		assertEquals(frm2, fp.getFrame());
		fp = page.getFP(p(75, 135));
		assertEquals(frm2, fp.getFrame());
		fp = page.getFP(p(75, 139));
		assertEquals(frm1, fp.getFrame());
		fp = page.getFP(p(75, 144.1f));
		assertEquals(frm3, fp.getFrame());
		fp = page.getFP(p(70, 137.1f));
		assertNull(fp);
	}

	/**
	 * Creates a layout with a page with some unrotated frames for testing.
	 */
	private Layout createPageWithUnrotatedFrames() {
		Layout layout = new Layout(null);

		PageFormat pf = new PageFormat(new Size2f(200, 200), new PageMargins(10, 10, 10, 10));
		Page page = new Page(pf);
		layout.addPage(page);

		//Frame 1
		frm1 = new ScoreFrame();
		frm1.setPosition(p(120, 120));
		frm1.setSize(s(60, 80));
		page.addFrame(frm1);

		//Frame 2
		frm2 = new GroupFrame();
		frm2.setPosition(p(90, 110));
		frm2.setSize(s(60, 40));
		page.addFrame(frm2);

		//Childframe 2a
		frm2a = new ScoreFrame();
		frm2a.setPosition(p(30, 0));
		frm2a.setSize(s(10, 10));
		frm2.addChildFrame(frm2a);

		//Childframe 2b
		frm2b = new ScoreFrame();
		frm2b.setPosition(p(-10, -20));
		frm2b.setSize(s(10, 10));
		frm2.addChildFrame(frm2b);

		//Frame 3
		frm3 = new ScoreFrame();
		frm3.setPosition(p(30, 80));
		frm3.setSize(s(30, 60));
		page.addFrame(frm3);

		return layout;
	}

	/**
	 * Creates a layout with a page with some rotated frames for testing.
	 * See PageTest.odg for a preview of the page.
	 */
	private Layout createPageWithRotatedFrames() {
		Layout layout = new Layout(null);

		PageFormat pf = new PageFormat(new Size2f(200, 200), new PageMargins(10, 10, 10, 10));
		Page page = new Page(pf);
		layout.addPage(page);

		//Frame 1
		frm1 = new ScoreFrame();
		frm1.setPosition(p(120, 120));
		frm1.setSize(s(60, 80));
		frm1.setRotation(-30);
		page.addFrame(frm1);

		//Frame 2
		frm2 = new GroupFrame();
		frm2.setPosition(p(90, 110));
		frm2.setSize(s(60, 40));
		frm2.setRotation(70);
		page.addFrame(frm2);

		//Childframe 2a
		frm2a = new ScoreFrame();
		frm2a.setPosition(p(20, 10));
		frm2a.setSize(s(10, 10));
		frm2a.setRotation(0);
		frm2.addChildFrame(frm2a);

		//Childframe 2b
		frm2b = new ScoreFrame();
		frm2b.setPosition(p(-10, -5));
		frm2b.setSize(s(20, 20));
		frm2b.setRotation(70);
		frm2.addChildFrame(frm2b);

		//Frame 3
		frm3 = new ScoreFrame();
		frm3.setPosition(p(95, 155));
		frm3.setSize(s(80, 40));
		frm3.setRotation(30);
		page.addFrame(frm3);

		return layout;
	}

}
