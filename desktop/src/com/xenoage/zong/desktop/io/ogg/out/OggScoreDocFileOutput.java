package com.xenoage.zong.desktop.io.ogg.out;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.io.FilenameUtils.numberFiles;
import static com.xenoage.utils.kernel.Range.range;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.FilenameUtils;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This class writes one or more OGG Vorbis files from a given {@link ScoreDoc}.
 * 
 * If there is just one score in the document, a single file is created.
 * If the document has multiple scores, one OGG file for each score is created and
 * named according to {@link FilenameUtils#numberFiles(String, int)}.
 * 
 * @author Andreas Wenger
 */
public class OggScoreDocFileOutput
	implements FileOutput<ScoreDoc> {

	//TIDY: share code with Mp3ScoreDocFileOutput and WavScoreDocFileOutput, since same logic
	@Override public void write(ScoreDoc document, OutputStream stream,
		String filePath)
		throws IOException {
		List<Score> scores = alist();
		scores.add(document.getScore()); //TODO: currently there is only one score per document
		if (scores.size() == 1 || filePath == null) {
			//simple case: just one score
			OggScoreFileOutput.writeOgg(scores.get(0), new JseOutputStream(stream));
		}
		else {
			//more scores: one OGG file for each score
			List<String> filenames = numberFiles(filePath, scores.size());
			for (int i : range(scores)) {
				OggScoreFileOutput.writeOgg(scores.get(i), new FileOutputStream(filenames.get(i)));
			}
		}
	}

	/**
	 * Returns true, since multiple files may be required.
	 */
	@Override public boolean isFilePathRequired(ScoreDoc document) {
		return true;
	}

}
