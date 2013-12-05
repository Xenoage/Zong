package com.xenoage.zong.view;

import static com.xenoage.utils.math.geom.Point2f.p;
import static com.xenoage.zong.layout.LP.lp;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Rectangle2i;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.layout.LP;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;

/**
 * This class manages a page view of a {@link Layout}.
 * It can be used by different applications that display
 * page layouts.
 * 
 * @author Andreas Wenger
 */
public class PageViewManager {

	//the layout this view is showing
	private Layout layout;


	//special zooms
	public enum SpecialZoom {
		FitPage,
		FitWidth,
		FitHeight
	};


	//the display distance between two pages in mm
	public final float pageDisplayDistance = 30;


	//the alignment of the pages in the view
	public enum PageDisplayAlignment {
		Horizontal,
		Vertical
	};


	private PageDisplayAlignment pageDisplayAlignment = PageDisplayAlignment.Horizontal;

	//bounding rectangles of the pages in mm
	protected ArrayList<Rectangle2f> pageRects;


	/**
	 * Creates a {@link PageViewManager} with the given initial layout.
	 */
	public PageViewManager(Layout layout) {
		setLayout(layout);
	}

	/**
	 * Gets the managed layout this view shows.
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * Sets the managed layout.
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
		//compute the page bounding rectangles
		this.pageRects = computePageRects();
	}

	/**
	 * Returns the zooming factor for the given special zoom.
	 * @param specialZoom  the special kind of zoom
	 */
	public float getSpecialZoomScaling(SpecialZoom specialZoom) {
		//TODO
		return 1;
	}

	/**
	 * Gets the alignment of the pages in the view.
	 */
	public PageDisplayAlignment getPageDisplayAlignment() {
		return pageDisplayAlignment;
	}

	/**
	 * Sets the alignment of the pages in the view.
	 */
	public void setPageDisplayAlignment(PageDisplayAlignment pageDisplayAlignment) {
		this.pageDisplayAlignment = pageDisplayAlignment;
		this.pageRects = computePageRects();
	}

	/**
	 * Computes the offsets of all pages in mm, relative to the upper left
	 * corner of the first page.
	 */
	private ArrayList<Rectangle2f> computePageRects() {
		float offsetX = 0; // horizontal center of the current page
		float offsetY = 0; // vertical center of the current page
		List<Page> pages = layout.getPages();
		ArrayList<Rectangle2f> ret = new ArrayList<Rectangle2f>(pages.size());
		if (pages.size() > 0) {
			// compute offsets for all pages
			float firstPageOffsetX = -pages.get(0).getFormat().getSize().width / 2;
			float firstPageOffsetY = -pages.get(0).getFormat().getSize().height / 2;
			for (int i = 0; i < pages.size(); i++) {
				Size2f pageSize = pages.get(i).getFormat().getSize();
				Point2f offset = null;
				switch (pageDisplayAlignment) {
					case Horizontal:
						offset = new Point2f(offsetX + firstPageOffsetX, -pageSize.height / 2);
						offsetX += pageSize.width + pageDisplayDistance;
						break;
					case Vertical:
						offset = new Point2f(-pageSize.width / 2, offsetY + firstPageOffsetY);
						offsetY += pageSize.height + pageDisplayDistance;
						break;
				}
				ret.add(new Rectangle2f(offset, new Size2f(pageSize)));
			}
		}
		return ret;
	}

	/**
	 * Computes the page index and the page coordinates at the given position in
	 * px. If there is no page at the given position, null is returned.
	 */
	public LP computeLP(Point2i pPx, ViewState viewState) {
		Point2f pMm = computePositionMm(pPx, viewState);
		//TODO: performance? binary search?
		for (int page = pageRects.size() - 1; page >= 0; page--) {
			if (pageRects.get(page).contains(pMm)) {
				pMm = pMm.sub(getPageDisplayOffset(page));
				return lp(layout, page, p(pMm.x, pMm.y));
			}
		}
		return null;
	}

	/**
	 * Returns the display position in mm of the given position in px,
	 * relative to the center point of the first page.
	 */
	public Point2f computePositionMm(Point2i pPx, ViewState viewState) {
		Point2f pMm = new Point2f(Units.pxToMm(pPx.x - viewState.sizePx.width / 2, viewState.scaling),
			Units.pxToMm(pPx.y - viewState.sizePx.height / 2, viewState.scaling));
		pMm = pMm.add(viewState.scrollMm);
		return pMm;
	}

	/**
	 * Computes the page coordinates at the given position in px, relative to
	 * the upper left corner of the given page.
	 */
	public LP computeLP(Point2i px, Page page, ViewState viewState) {
		int pageIndex = layout.getPages().indexOf(page);
		return computeLP(px, pageIndex, viewState);
	}

	/**
	 * Computes the page coordinates at the given position in px, relative to
	 * the upper left corner of the given page.
	 */
	public LP computeLP(Point2i pPx, int page, ViewState viewState) {
		Point2f pMm = computePositionMm(pPx, viewState);
		Point2f pageOffset = pageRects.get(page).position;
		pMm = pMm.sub(pageOffset);
		return lp(layout, page, p(pMm.x, pMm.y));
	}

	/**
	 * Gets the display position in mm of the page with the given index,
	 * relative to the center of the first page.
	 * @param pageIndex the index of the page, beginning at 0
	 */
	public Point2f getPageDisplayOffset(int pageIndex) {
		return pageRects.get(pageIndex).position;
	}

	/**
	 * Transforms the given layout coordinates to screen coordinates in px. If
	 * not possible, null is returned.
	 */
	public Point2i computePositionPx(LP p, ViewState viewState) {
		Point2f pos = getPageDisplayOffset(p.pageIndex);
		pos = pos.add(p.position);
		pos = pos.sub(viewState.scrollMm);
		Point2i ret = new Point2i(Units.mmToPxInt(pos.x, viewState.scaling), Units.mmToPxInt(pos.y,
			viewState.scaling));
		ret = ret.add(viewState.sizePx.width / 2, viewState.sizePx.height / 2);
		return ret;
	}

	/**
	 * Transforms the given layout coordinates to screen coordinates in px. If
	 * not possible, null is returned.
	 */
	public Rectangle2i computeRectangleMm(int page, Rectangle2f rect, ViewState viewState) {
		Point2f nw = rect.position;
		Point2f se = nw.add(rect.size);
		Point2i nwPx = computePositionPx(lp(layout, page, nw), viewState);
		Point2i sePx = computePositionPx(lp(layout, page, se), viewState);
		return new Rectangle2i(nwPx, new Size2i(sePx.sub(nwPx)));
	}

	/**
	 * Computes the index of the page that is behind the given coordinates in
	 * px. If it is before the first page, the first page is returned. If it is
	 * between two pages, the left/top one is returned.
	 */
	public int getNearestPageIndex(Point2f pMm) {
		// relevant axis: x when using horizontal page alignment, else y
		float p = (pageDisplayAlignment == PageDisplayAlignment.Horizontal ? pMm.x : pMm.y);
		// TODO (when closures are available): use binary search instead to find
		// page very fast
		if (pageRects.size() == 0) {
			return 0;
		}
		else {
			int ret = 0;
			for (int i = 1; i < pageRects.size(); i++) {
				Point2f po = getPageDisplayOffset(i);
				float poc = (pageDisplayAlignment == PageDisplayAlignment.Horizontal ? po.x : po.y);
				if (p > poc)
					ret++;
				else
					break;
			}
			return ret;
		}
	}

	/**
	 * Returns the coordinates in px of the given page.
	 */
	public Rectangle2i computePageRect(int pageIndex, ViewState viewState) {
		//get page coordinates in px
		Point2i pagePositionUpperLeftPx = computePositionPx(lp(layout, pageIndex, p(0, 0)), viewState);
		Size2f size = layout.getPages().get(pageIndex).getFormat().getSize();
		Point2i pagePositionLowerRightPx = computePositionPx(
			lp(layout, pageIndex, p(size.width, size.height)), viewState);
		//create rectangle
		Rectangle2i ret = new Rectangle2i(pagePositionUpperLeftPx, new Size2i(
			pagePositionLowerRightPx.x - pagePositionUpperLeftPx.x, pagePositionLowerRightPx.y -
				pagePositionUpperLeftPx.y));
		return ret;
	}

}
