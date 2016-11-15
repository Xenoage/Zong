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
import com.xenoage.zong.io.ScoreDocFactory;
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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
						scoreDoc = new ScoreDocFactory().read(score);

						//add credit elements - TIDY
						Object o = score.getMetaData().get("mxldoc");
						if (o != null && o instanceof MxlScorePartwise) {
							MxlScorePartwise doc = (MxlScorePartwise) o;
							CreditsReader.read(doc, scoreDoc.getLayout(), score.getFormat());
						}

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
	
}
