package com.xenoage.zong.android.view.provider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.math.geom.Size2i;
import com.xenoage.zong.android.App;
import com.xenoage.zong.android.R;
import com.xenoage.zong.android.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.android.view.PageView;
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
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.layouter.Target;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.canvas.CanvasFormat;
import com.xenoage.zong.renderer.canvas.CanvasIntegrity;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Units.pxToMm;
import static com.xenoage.zong.android.renderer.AndroidLayoutRenderer.androidLayoutRenderer;

/**
 * {@link ScoreViewProvider} for {@link ScoreDoc} documents.
 *
 * @author Andreas Wenger
 */
public class ScoreDocScoreViewProvider
	extends ScoreViewProvider {

	private final static int marginPx = 32;

	private ScoreDoc doc;
	private Size2i screenSizePx;
	private float scaling;
	private String title;
	private Layout layout;

	//GOON
	private float titleTextHeightPx;


	/**
	 * Creates a provider for page views for the given document, using
	 * the given screen size in px and the given scaling factor.
	 */
	public ScoreDocScoreViewProvider(ScoreDoc doc, Size2i screenSizePx, float scaling) {
		this.doc = doc;
		this.screenSizePx = screenSizePx;
		this.scaling = scaling;
		this.title = notNull(doc.getScore().getInfo().getTitle(), "Untitled Score");

		//recompute the layout, so that each page fits into the available screen space
		Size2f pageSizeMm = pxToMm(screenSizePx, scaling);
		float marginMm = pxToMm(marginPx, scaling);
		PageFormat pageFormat = new PageFormat(pageSizeMm,
			new PageMargins(marginMm, marginMm, marginMm, marginMm));
		Size2f frameSizeMm = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());

		//first page needs space for title text
		titleTextHeightPx = screenSizePx.height / 20f;
		float firstFrameOffsetY = pxToMm(titleTextHeightPx, scaling);
		Size2f firstFrameSizeMm = new Size2f(frameSizeMm.width, frameSizeMm.height - firstFrameOffsetY);

		//delete unnecessary layout information, like system distances or system breaks
		Score score = doc.getScore();
		score.setFormat(new ScoreFormat());
		ScoreHeader header = score.getHeader();
		for (int i : range(header.getSystemLayouts())) {
			SystemLayout sl = header.getSystemLayout(i);
			if (sl != null)
				sl.setDistance(SystemLayout.defaultDistance);
		}
		for (int i : range(header.getColumnHeaders())) {
			ColumnHeader ch = header.getColumnHeader(i);
			if (ch.getMeasureBreak() != null)
				ch.setBreak(null);
		}

		//layout the score to find out the needed space
		Context context = new Context(score, App.getSymbolPool(),
			doc.getLayout().getDefaults().getLayoutSettings());
		Target target = Target.completeLayoutTarget(new ScoreLayoutArea(frameSizeMm));
		ScoreLayouter layouter = new ScoreLayouter(context, target);
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
		this.layout = layout;
	}

	@Override public int getPagesCount() {
		return layout.getPages().size();
	}

	@Override public PageView createPageView(int pageIndex) {
		return new PageView(renderPage(pageIndex));
	}

	private Bitmap renderPage(int pageIndex) {

		//prepare bitmap
		Bitmap ret = Bitmap.createBitmap(screenSizePx.width, screenSizePx.height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(ret);
		AndroidCanvas androidCanvas = new AndroidCanvas(canvas, layout.getPages().get(pageIndex).getFormat().getSize(),
			CanvasFormat.Raster, CanvasDecoration.None, CanvasIntegrity.Perfect);

		//background
		canvas.drawBitmap(getBackgroundImage(pageIndex), null, canvas.getClipBounds(), null);

		//paint score
		androidLayoutRenderer.paint(layout, pageIndex, androidCanvas, scaling);

		//first page: draw title
		if (pageIndex == 0) {
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			paint.setTypeface(Typeface.SERIF);
			paint.setTextSize(titleTextHeightPx * 0.6f);
			float width = paint.measureText(title);
			canvas.drawText(title, screenSizePx.width / 2 - width / 2, marginPx + titleTextHeightPx, paint);
		}
		return ret;
	}



}
