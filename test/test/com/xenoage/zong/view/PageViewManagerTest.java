package com.xenoage.zong.view;

import static com.xenoage.utils.math.geom.Point2f.p;
import static com.xenoage.zong.layout.LP.lp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.format.PageMargins;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.view.PageViewManager.PageDisplayAlignment;

/**
 * Tests for {@link PageViewManager}.
 *
 * @author Andreas Wenger
 */
public class PageViewManagerTest {

	private PageViewManager view;


	@Before public void setUp() {
		Layout layout = new Layout(null);
		//3 pages:
		// ----          --------
		// |1 | -2------ |3     |
		// |  | -------- |      |
		// ----          --------
		layout.addPage(new Page(new PageFormat(new Size2f(100, 200), new PageMargins(0, 0, 0, 0))));
		layout.addPage(new Page(new PageFormat(new Size2f(200, 100), new PageMargins(0, 0, 0, 0))));
		layout.addPage(new Page(new PageFormat(new Size2f(200, 200), new PageMargins(0, 0, 0, 0))));
		//create view
		view = new PageViewManager(layout);
		view.setPageDisplayAlignment(PageDisplayAlignment.Horizontal);
	}

	@Test public void getPageDisplayOffset() {
		float distance = view.pageDisplayDistance;
		float d = Delta.DELTA_FLOAT;
		//page 1
		Point2f p = view.getPageDisplayOffset(0);
		assertEquals(-100 / 2, p.x, d);
		assertEquals(-200 / 2, p.y, d);
		//page 2
		p = view.getPageDisplayOffset(1);
		assertEquals(100 / 2 + distance, p.x, d);
		assertEquals(-100 / 2, p.y, d);
		//page 3
		p = view.getPageDisplayOffset(2);
		assertEquals(100 / 2 + distance + 200 + distance, p.x, d);
		assertEquals(-200 / 2, p.y, d);
	}

	@Test public void getPageIndex() {
		float distance = view.pageDisplayDistance;
		//point -100/0 belongs to page 0
		assertEquals(0, view.getNearestPageIndex(new Point2f(-100, 0)));
		//point 50/0 still belongs to page 0
		assertEquals(0, view.getNearestPageIndex(new Point2f(50, 0)));
		//point 50+distance+10/0 belongs to page 1
		assertEquals(1, view.getNearestPageIndex(new Point2f(50 + distance + 10, 0)));
		//point 1000/0 belongs to page 2
		assertEquals(2, view.getNearestPageIndex(new Point2f(1000, 0)));
	}

	@Test public void computeLayoutPosition() {
		Size2i viewSize = new Size2i(800, 600);
		float viewScaling = 1;
		Point2f viewScroll = new Point2f(0, 0);
		ViewState viewState = new ViewState(viewSize, viewScaling, viewScroll);
		//default zoom and scroll: point 400/300 must
		//have center position of the page (50/100)
		Point2f pos = view.computeLP(new Point2i(400, 300), viewState).position;
		assertEquals(pos.x, 50, Delta.DELTA_FLOAT);
		assertEquals(pos.y, 100, Delta.DELTA_FLOAT);
		//set zoom to 200%, must still be 50/100
		viewScaling = 2;
		viewState = new ViewState(viewSize, viewScaling, viewScroll);
		pos = view.computeLP(new Point2i(400, 300), viewState).position;
		assertEquals(pos.x, 50, Delta.DELTA_FLOAT);
		assertEquals(pos.y, 100, Delta.DELTA_FLOAT);
		//scroll to -5/10
		//screen point 400/300 must be 45/110
		viewScroll = new Point2f(-5, 10);
		viewState = new ViewState(viewSize, viewScaling, viewScroll);
		pos = view.computeLP(new Point2i(400, 300), viewState).position;
		assertEquals(pos.x, 45, Delta.DELTA_FLOAT);
		assertEquals(pos.y, 110, Delta.DELTA_FLOAT);
		//screen point 0/0 must be
		//50+(-5-400*pxToMm/zoom);100+(10-300*pxToMm/zoom)
		//compute relative to page 0, since coordinates are outside
		pos = view.computeLP(new Point2i(0, 0), 0, viewState).position;
		assertEquals(pos.x, 50 + -5 - Units.pxToMm(400, viewScaling), Delta.DELTA_FLOAT);
		assertEquals(pos.y, 100 + 10 - Units.pxToMm(300, viewScaling), Delta.DELTA_FLOAT);
		//when not computed relative to a given page, it must return null
		assertNull(view.computeLP(new Point2i(0, 0), viewState));
		//reset zoom/scroll, then screen point 800/300 must be on page 1
		viewScaling = 1;
		viewScroll = new Point2f(0, 0);
		viewState = new ViewState(viewSize, viewScaling, viewScroll);
		int pageIndex = view.computeLP(new Point2i(800, 300), viewState).pageIndex;
		assertEquals(1, pageIndex);
	}

	@Test public void computeScreenPosition() {
		Size2i viewSize = new Size2i(800, 600);
		float viewScaling = 1;
		Point2f viewScroll = new Point2f(0, 0);
		ViewState viewState = new ViewState(viewSize, viewScaling, viewScroll);
		//center point of the page must have screen coordinates 400/300
		Point2i pos = view.computePositionPx(lp(null, 0, p(50, 100)), viewState);
		assertEquals(pos.x, 400);
		assertEquals(pos.y, 300);
		//set zoom to 200%, must still be 400/300
		viewScaling = 2;
		viewState = new ViewState(viewSize, viewScaling, viewScroll);
		pos = view.computePositionPx(lp(null, 0, p(50, 100)), viewState);
		assertEquals(pos.x, 400);
		assertEquals(pos.y, 300);
		//scroll to -5/10
		//page point 45/110 must be 400/300
		viewScroll = new Point2f(-5, 10);
		viewState = new ViewState(viewSize, viewScaling, viewScroll);
		pos = view.computePositionPx(lp(null, 0, p(45, 110)), viewState);
		assertEquals(pos.x, 400);
		assertEquals(pos.y, 300);
		//page center point must be
		//400-(-5*MmToPx*zoom); 300-(10*MmToPx*zoom)
		pos = view.computePositionPx(lp(null, 0, p(50, 100)), viewState);
		assertEquals(pos.x, Math.round(400 - Units.mmToPx(-5, viewScaling)));
		assertEquals(pos.y, Math.round(300 - Units.mmToPx(10, viewScaling)));
	}

}
