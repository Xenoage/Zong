package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.zong.core.format.LayoutFormat.defaultLayoutFormat;
import static com.xenoage.zong.musiclayout.settings.LayoutSettings.defaultLayoutSettings;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.async.AsyncProducer;
import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.filter.AllFilter;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.layouter.ScoreLayoutArea;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.layouter.Target;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.symbols.SymbolPool;

import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Creates a {@link ScoreDoc} instance from the document
 * at the given path.
 * 
 * The filepath must be given, when the opened file is an opus document,
 * otherwise null is returned.
 * 
 * If no score is opened, null is returned.
 */
@RequiredArgsConstructor
public class MusicXmlScoreDocFileReader
	implements AsyncProducer<ScoreDoc> {

	private final InputStream stream;
	private final String filePath;
	
	
	@Override public void produce(final AsyncResult<ScoreDoc> result) {
		val reader = new MusicXmlFileReader(stream, filePath, new AllFilter<String>());
		reader.produce(new AsyncResult<List<Score>>() {

			@Override public void onSuccess(List<Score> scores) {
				if (scores.size() == 0) {
					//no score was opened
					result.onSuccess(null);
				}
				else {
					//open first selected score
					Score score = scores.get(0);
					ScoreDoc scoreDoc;
					try {
						scoreDoc = read(score);
						result.onSuccess(scoreDoc);
					} catch (Exception ex) {
						result.onFailure(ex);
					}
				}
			}

			@Override public void onFailure(Exception ex) {
				result.onFailure(ex);
			}
		});
	}
	
	/**
	 * Creates a {@link ScoreDoc} instance from the given score.
	 * TIDY: move elsewhere, e.g. in a ScoreDocFactory class
	 */
	public static ScoreDoc read(Score score)
		throws InvalidFormatException, IOException {

		//page format
		LayoutFormat layoutFormat = defaultLayoutFormat;
		Object oLayoutFormat = score.getMetaData().get("layoutformat");
		if (oLayoutFormat instanceof LayoutFormat) {
			layoutFormat = (LayoutFormat) oLayoutFormat;
		}
		LayoutDefaults layoutDefaults = new LayoutDefaults(layoutFormat);

		//create the document
		ScoreDoc ret = new ScoreDoc(score, layoutDefaults);
		Layout layout = ret.getLayout();

		//layout basics
		PageFormat pageFormat = layoutFormat.getPageFormat(0);
		Size2f frameSize = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());
		Point2f framePos = new Point2f(pageFormat.getMargins().getLeft() + frameSize.width / 2,
			pageFormat.getMargins().getTop() + frameSize.height / 2);

		//layout the score to find out the needed space
		Target target = Target.completeLayoutTarget(new ScoreLayoutArea(frameSize));
		ScoreLayout scoreLayout = new ScoreLayouter(ret, target).createScoreLayout();

		//create and fill at least one page
		if (scoreLayout.frames.size() > 1) {
			//normal layout: one frame per page
			ScoreFrameChain chain = null;
			for (int i = 0; i < scoreLayout.frames.size(); i++) {
				Page page = new Page(pageFormat);
				layout.addPage(page);
				ScoreFrame frame = new ScoreFrame();
				frame.setPosition(framePos);
				frame.setSize(frameSize);
				//TEST frame = frame.withHFill(NoHorizontalSystemFillingStrategy.getInstance());
				page.addFrame(frame);
				if (chain == null) {
					chain = new ScoreFrameChain(score);
					chain.setScoreLayout(scoreLayout);
				}
				chain.add(frame);
			}
		}
		else {
			//no frames: create a single empty page
			Page page = new Page(pageFormat);
			layout.addPage(page);
		}

		//add credit elements - TIDY
		Object o = score.getMetaData().get("mxldoc");
		if (o != null && o instanceof MxlScorePartwise) {
			MxlScorePartwise doc = (MxlScorePartwise) o;
			CreditsReader.read(doc, layout, score.getFormat());
		}

		return ret;
	}
	
}
