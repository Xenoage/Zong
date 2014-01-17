package com.xenoage.zong.mobile.android.scoreview;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.kernel.Range.range;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.NinePatchDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2i;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.mobile.android.R;
import com.xenoage.zong.renderer.AndroidBitmapPageRenderer;
import com.xenoage.zong.view.PageViewManager;
import com.xenoage.zong.view.ViewState;

/**
 * This class provides a scrollable (by dragging) and zoomable (by using
 * the zoom buttons) view on a {@link ScoreDoc}.
 * 
 * @author Andreas Wenger
 */
public class ScorePageView
	extends View {

	private Layout layout;

	//view parameters
	private float scaling;
	private Point2f scrollMm = new Point2f(0, 0);
	private PageViewManager pageViewManager;

	private Bitmap desktopBitmap;
	private NinePatchDrawable page9Patch;
	private int page9PatchOffsetLeft = 1, page9PatchOffsetTop = 1, page9PatchOffsetRight = 19,
		page9PatchOffsetBottom = 19;

	private Point2f scrollLastPx = new Point2f(0, 0);

	//currently loaded tiles
	private ArrayList<Bitmap> pagesBitmaps = new ArrayList<Bitmap>();
	private int pagePreloadDistance = 200;
	private int pageUnloadDistance = 400;


	public ScorePageView(Context context, float scaling) {
		super(context);
		this.scaling = scaling;
	}

	public ScorePageView(Context context) {
		this(context, 1);
	}

	public void setScoreDoc(ScoreDoc doc) {
		this.layout = doc.getLayout();
		this.pageViewManager = new PageViewManager(layout);
		//load desktop and page image
		try {
			desktopBitmap = BitmapFactory.decodeStream(new JseInputStream(
				platformUtils().openFile("data/img/desktop/desktop.png")));
			page9Patch = (NinePatchDrawable) getContext().getResources().getDrawable(R.drawable.page);
		} catch (IOException ex) {
		}
		//register touch listener for scrolling
		setOnTouchListener(new View.OnTouchListener() {

			@Override public boolean onTouch(View view, MotionEvent event) {
				Point2f currentPx = new Point2f(event.getX(), event.getY());
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						scrollLastPx = new Point2f(event.getX(), event.getY());
						break;
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
						Point2f distancePx = scrollLastPx.sub(currentPx);
						Point2f distanceMm = new Point2f(Units.pxToMm(distancePx.x, scaling), Units.pxToMm(
							distancePx.y, scaling));
						scrollMm = scrollMm.add(distanceMm);
						scrollLastPx = currentPx;
						view.postInvalidate();
						break;
				}
				return true;
			}
		});
	}

	public void setScaling(float scaling) {
		this.scaling = scaling;
		pagesBitmaps.clear();
		postInvalidate();
	}

	private ViewState getViewState() {
		return new ViewState(new Size2i(getWidth(), getHeight()), scaling, scrollMm);
	}

	@Override protected void onDraw(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		Rect viewRect = new Rect(0, 0, w, h);
		Rectangle2i viewRectangle2i = new Rectangle2i(0, 0, w, h);

		//cleanup tiles
		cleanupPages();

		//paint desktop
		canvas.drawBitmap(desktopBitmap, null, viewRect, null);

		//paint pages
		for (int iPage : range(layout.getPages())) {
			Rectangle2i pageRect = pageViewManager.computePageRect(iPage, getViewState());

			//if page is invisible, ingore page
			if (!isRectVisible(viewRectangle2i, pageRect, 30)) //30: for shadow...
				continue;

			canvas.save();
			canvas.translate(pageRect.x1(), pageRect.y1());

			//page background
			page9Patch.setBounds(-page9PatchOffsetLeft, -page9PatchOffsetTop, pageRect.width() +
				page9PatchOffsetRight, pageRect.height() + page9PatchOffsetBottom);
			page9Patch.draw(canvas);

			//frames
			if (isPageVisible(iPage, pagePreloadDistance)) {
				Bitmap pageBitmap = getPageBitmap(iPage);
				canvas.drawBitmap(pageBitmap, null,
					new RectF(0, 0, pageBitmap.getWidth(), pageBitmap.getHeight()), null);
			}

			canvas.restore();
		}

		//TEST
		//Rectangle2i pageRect = pageViewManager.computePageRect(0, getViewState());
		//canvas.drawBitmap(firstPageBitmap, null, new RectF(pageRect.x1(), pageRect.y1(),
		//	pageRect.x2(), pageRect.y2()), null);

		/*
		//TEST: paint a line from the top left corner to the bottom right corner
		Paint paintLine = new Paint();
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setColor(Color.WHITE);
		canvas.drawLine(0, 0, getWidth(), getHeight(), paintLine); */
	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//maximize in parent (if width and height are fill_parent)
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
	}

	/**
	 * Removes all pages, that are currently not visible.
	 */
	private void cleanupPages() {
		for (int iPage : range(layout.getPages())) {
			if (!isPageVisible(iPage, pageUnloadDistance))
				pagesBitmaps.set(iPage, null);
		}
	}

	/**
	 * Gets the {@link Bitmap} of the given page.
	 * If it is not loaded yet, it is generated and then returned.
	 * The method blocks until the bitmap is ready.
	 */
	private Bitmap getPageBitmap(int pageIndex) {
		Bitmap ret = pagesBitmaps.get(pageIndex);
		if (ret == null) {
			ret = AndroidBitmapPageRenderer.paint(layout, pageIndex, scaling);
			pagesBitmaps.set(pageIndex, ret);
		}
		return ret;
	}

	/**
	 * Returns true, if the page with the given index
	 * is currently visible (with respect to the given additional distance).
	 */
	private boolean isPageVisible(int pageIndex, int additionalDistance) {
		Rectangle2i viewRect = new Rectangle2i(-additionalDistance, -additionalDistance, getWidth() +
			2 * additionalDistance, getHeight() + 2 * additionalDistance);
		Rectangle2i pageRect = pageViewManager.computePageRect(pageIndex, getViewState());
		return (isRectVisible(viewRect, pageRect, 0));
	}

	/**
	 * Returns true, if the given rectangle is partially
	 * or fully visible on the view rect, otherwise false.
	 */
	private boolean isRectVisible(Rectangle2i viewRect, Rectangle2i rect, int additionalDistance) {
		int ad = additionalDistance;
		return (rect.x2() + ad >= viewRect.x1() && rect.x1() - ad <= viewRect.x2() &&
			rect.y2() + ad >= viewRect.y1() && rect.y1() - ad <= viewRect.y2());
	}

}
