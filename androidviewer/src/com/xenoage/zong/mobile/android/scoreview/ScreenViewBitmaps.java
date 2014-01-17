package com.xenoage.zong.mobile.android.scoreview;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.renderer.AndroidPageLayoutRenderer.androidPageLayoutRenderer;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.format.PageMargins;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.format.SystemLayout;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.mobile.android.App;
import com.xenoage.zong.mobile.android.R;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;

/**
 * This class manages the bitmaps of a given {@link ScoreDoc}
 * and scaling shown on a {@link ScoreScreenView}.
 * 
 * When the document or the scaling is changed, a new instance
 * of this class is required.
 * 
 * @author Andreas Wenger
 */
public class ScreenViewBitmaps {

	//constants
	private final static int marginPx = 32;

	//layout
	private final String title;
	private final Layout layout;

	//view
	private final Size2i screenSize;
	private final float scaling;
	private final float titleTextHeightPx;

	//bitmaps and resources
	private final Bitmap[] pageBitmaps;
	private final Resources resources;


	public ScreenViewBitmaps(ScoreDoc doc, Size2i screenSize, float scaling, Resources res) {
		this.screenSize = screenSize;
		this.scaling = scaling;
		this.resources = res;
		this.title = notNull(doc.getScore().getInfo().getTitle(), "Untitled Score");

		//recompute the layout, so that each page fits into the available screen space
		int pageWidthPx = screenSize.width;
		int pageHeightPx = screenSize.height;
		float pageWidthMm = Units.pxToMm(pageWidthPx, scaling);
		float pageHeightMm = Units.pxToMm(pageHeightPx, scaling);
		Size2f pageSizeMm = new Size2f(pageWidthMm, pageHeightMm);
		float marginMm = Units.pxToMm(marginPx, scaling);
		PageFormat pageFormat = new PageFormat(pageSizeMm, new PageMargins(marginMm, marginMm,
			marginMm, marginMm));
		Size2f frameSizeMm = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());
		//first page needs space for title text
		titleTextHeightPx = screenSize.height / 20;
		float firstFrameOffsetY = Units.pxToMm(titleTextHeightPx, scaling);
		Size2f firstFrameSizeMm = new Size2f(frameSizeMm.width, frameSizeMm.height - firstFrameOffsetY);

		//delete unnecessary layout information, like system distances
		//or system breaks
		Score score = doc.getScore();
		score.setFormat(ScoreFormat.defaultValue);
		ScoreHeader header = score.getHeader();
		for (int i : range(header.getSystemLayouts())) {
			SystemLayout sl = header.getSystemLayout(i);
			if (sl != null) {
				sl.setDistance(ScoreFormat.defaultValue.getSystemLayout().getDistance());
			}
		}
		for (int i : range(header.getColumnHeaders())) {
			ColumnHeader ch = header.getColumnHeader(i);
			if (ch.getMeasureBreak() != null) {
				ch.setBreak(null);
			}
		}

		//layout the score to find out the needed space
		ScoreLayouter layouter = new ScoreLayouter(score, App.getSymbolPool(),
			doc.getLayout().getDefaults().getLayoutSettings(), true, frameSizeMm);
		ScoreLayout scoreLayout = layouter.createScoreLayout();

		//create and fill at least one page
		Layout layout = new Layout(doc.getLayout().getDefaults());
		ScoreFrameChain chain = null;
		for (int i = 0; i < scoreLayout.frames.size(); i++) {
			Page page = new Page(pageFormat);
			Point2f position;
			Size2f size;
			if (i == 0) {
				//first page
				position = new Point2f(pageSizeMm.width / 2, pageSizeMm.height / 2 + firstFrameOffsetY);
				size = firstFrameSizeMm;
			}
			else {
				//other pages
				position = new Point2f(pageSizeMm.width / 2, pageSizeMm.height / 2);
				size = frameSizeMm;
			}
			ScoreFrame frame = new ScoreFrame();
			frame.setPosition(position);
			frame.setSize(size);
			page.addFrame(frame);
			layout.addPage(page);
			if (chain == null) {
				chain = new ScoreFrameChain(score);
				chain.setScoreLayout(scoreLayout);
			}
			chain.add(frame);
		}

		//create a bitmap array with one element for each page
		pageBitmaps = new Bitmap[layout.getPages().size()];

		this.layout = layout;
	}

	/**
	 * Gets the number of pages.
	 */
	public int getPagesCount() {
		return layout.getPages().size();
	}

	/**
	 * Gets the bitmap for the page with the given index.
	 * If not loaded, it is created.
	 */
	public Bitmap getPageBitmap(int pageIndex) {
		if (pageBitmaps[pageIndex] == null)
			pageBitmaps[pageIndex] = renderPage(pageIndex);
		return pageBitmaps[pageIndex];
	}

	/**
	 * Sets the active page index. This ensures that the active, the previous and
	 * the next page are loaded. The other pages are removed from memory.
	 */
	public void setActivePageIndex(int activePageIndex) {
		for (int iPage : range(layout.getPages())) {
			if (iPage < activePageIndex - 2 || iPage > activePageIndex + 1) {
				pageBitmaps[iPage] = null;
			}
			else if (pageBitmaps[iPage] == null) {
				pageBitmaps[iPage] = renderPage(iPage);
			}
		}
	}

	private Bitmap renderPage(int page) {
		Bitmap ret = Bitmap.createBitmap(screenSize.width, screenSize.height, Config.ARGB_8888);
		Canvas canvas = new Canvas(ret);
		int background = R.drawable.paper_middle;
		int pagesCount = layout.getPages().size();
		if (pagesCount == 1)
			background = R.drawable.paper_single;
		else if (page == 0)
			background = R.drawable.paper_first;
		else if (page == pagesCount - 1)
			background = R.drawable.paper_last;
		Bitmap bmp = BitmapFactory.decodeResource(resources, background);
		canvas.drawBitmap(bmp, null, canvas.getClipBounds(), null);
		AndroidCanvas context = new AndroidCanvas(canvas, layout.getPages().get(page).getFormat().getSize(),
			CanvasFormat.Raster, CanvasDecoration.None, CanvasIntegrity.Perfect);
		androidPageLayoutRenderer.paint(layout, page, context, scaling);
		//first page: draw title
		if (page == 0) {
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			paint.setTypeface(Typeface.SERIF);
			paint.setTextSize(titleTextHeightPx * 0.6f);
			float width = paint.measureText(title);
			canvas.drawText(title, screenSize.width / 2 - width / 2, marginPx + titleTextHeightPx, paint);
		}
		return ret;
	}

}
