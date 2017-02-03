package com.xenoage.zong.io.musicxml.in;

import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.filter.AllFilter;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.promise.Executor;
import com.xenoage.utils.promise.Promise;
import com.xenoage.utils.promise.Return;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.ScoreDocFactory;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.List;

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
public class MusicXmlScoreDocFileReader {

	private final InputStream stream;
	private final String filePath;

	
	public Promise<ScoreDoc> read() {

		return new Promise<ScoreDoc>(new Executor<ScoreDoc>() {
			@Override public void run(final Return<ScoreDoc> ret) {

				val reader = new MusicXmlFileReader(stream, filePath, new AllFilter<String>());
				reader.produce(new AsyncResult<List<Score>>() {

					@Override public void onSuccess(List<Score> scores) {
						if (scores.size() == 0) {
							//no score was opened
							ret.resolve(null);
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

								ret.resolve(scoreDoc);
							} catch (Exception ex) {
								ret.reject(ex);
							}
						}
					}

					@Override public void onFailure(Exception ex) {
						ret.reject(ex);
					}
				});
			}
		});
	}
	
}
