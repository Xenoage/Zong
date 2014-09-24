package com.xenoage.zong.desktop.io.musicxml.in;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.jse.async.Sync.sync;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.document.io.FileInput;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.filter.AllFilter;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.MusicXmlFileReader;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * This class reads a MusicXML 2.0 file
 * into a instance of the {@link ScoreDoc} class.
 *
 * @author Andreas Wenger
 */
public class MusicXmlScoreDocFileInput
	implements FileInput<ScoreDoc> {

	/**
	 * Creates a {@link ScoreDoc} instance from the document
	 * at the given path.
	 * 
	 * The filepath must be given, when the opened file is an opus document,
	 * otherwise null is returned.
	 * 
	 * If none is opened, null is returned.
	 */
	@Override public ScoreDoc read(InputStream stream, String filePath)
		throws InvalidFormatException, IOException {

		Score score;

		List<Score> scores;
		try {
			scores = sync(new MusicXmlFileReader(stream, filePath, new AllFilter<String>()));
		} catch (InvalidFormatException ex) {
			throw ex; //forward
		} catch (IOException ex) {
			throw ex; //forward
		} catch (Exception ex) {
			throw new IOException(ex);
		}
		if (scores.size() > 0)
			score = scores.get(0);
		else
			return null;

		return read(score, filePath);
	}

	/**
	 * Creates a {@link ScoreDoc} instance from the given score.
	 */
	public ScoreDoc read(Score score, String filePath)
		throws InvalidFormatException, IOException {

		//page format
		LayoutFormat layoutFormat = null;
		Object oLayoutFormat = score.getMetaData().get("layoutformat");
		if (oLayoutFormat instanceof LayoutFormat) {
			layoutFormat = (LayoutFormat) oLayoutFormat;
		}

		//use default symbol pool
		SymbolPool symbolPool = zongPlatformUtils().getSymbolPool();

		//load layout settings - TIDY: do not reload each time when a score is loaded
		LayoutSettings layoutSettings = LayoutSettingsReader.read(jsePlatformUtils().openFile(
			"data/layout/default.xml"));

		//create layout defaults
		LayoutDefaults layoutDefaults = new LayoutDefaults(layoutFormat, symbolPool, layoutSettings);

		//create the document
		ScoreDoc ret = new ScoreDoc(score, layoutDefaults);
		Layout layout = ret.getLayout();

		//layout basics
		PageFormat pageFormat = layoutFormat.getPageFormat(0);
		Size2f frameSize = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());
		Point2f framePos = new Point2f(pageFormat.getMargins().getLeft() + frameSize.width / 2,
			pageFormat.getMargins().getTop() + frameSize.height / 2);

		//layout the score to find out the needed space
		ScoreLayouter layouter = new ScoreLayouter(score, symbolPool, layoutSettings, true, frameSize);
		ScoreLayout scoreLayout = layouter.createScoreLayout();

		//create and fill at least one page
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

		//add credit elements - TIDY
		Object o = score.getMetaData().get("mxldoc");
		if (o != null && o instanceof MxlScorePartwise) {
			MxlScorePartwise doc = (MxlScorePartwise) o;
			CreditsReader.read(doc, layout, score.getFormat());
		}

		return ret;
	}

}
