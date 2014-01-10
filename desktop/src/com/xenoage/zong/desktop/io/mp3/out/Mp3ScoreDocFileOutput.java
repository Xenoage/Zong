package com.xenoage.zong.desktop.io.mp3.out;

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
 * This class writes one or more MP3 files from a given {@link ScoreDoc}.
 * 
 * If there is just one score in the document, a single file is created.
 * If the document has multiple scores, one MP3 file for each score is created and
 * named according to {@link FilenameUtils#numberFiles(String, int)}.
 * 
 * The LAME tool must be installed and in the system path.
 * If lame can not be found, an error is reported.
 * 
 * @author Andreas Wenger
 */
public class Mp3ScoreDocFileOutput
	implements FileOutput<ScoreDoc> {

	@Override public void write(ScoreDoc document, OutputStream stream,
		String filePath)
		throws IOException {
		List<Score> scores = alist();
		scores.add(document.getScore()); //TODO: currently there is only one score per document
		if (scores.size() == 1 || filePath == null) {
			//simple case: just one score
			Mp3ScoreFileOutput.writeMP3(scores.get(0), new JseOutputStream(stream));
		}
		else {
			//more scores: one MP3 file for each score
			List<String> filenames = numberFiles(filePath, scores.size());
			for (int i : range(scores)) {
				Mp3ScoreFileOutput.writeMP3(scores.get(i), new FileOutputStream(filenames.get(i)));
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
