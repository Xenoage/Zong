package com.xenoage.zong.desktop.io.mp3.out;

import java.io.IOException;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.FilenameUtils;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This class writes one or more OGG Vorbis files from a given {@link ScoreDoc}.
 * 
 * If there is just one score in the document, a single file is created.
 * If the document has multiple scores, one OGG file for each score is created and
 * named according to {@link FilenameUtils#numberFiles(String, int)}.
 * 
 * The LAME tool must be installed and in the system path.
 * If lame can not be found, an error is reported.
 * 
 * @author Andreas Wenger
 */
public class Mp3ScoreDocFileOutput
	extends FileOutput<ScoreDoc> {

	@Override public void write(ScoreDoc document, int fileIndex, OutputStream stream)
		throws IOException {
		Mp3ScoreFileOutput.writeMp3(document.getScore(), stream);
	}

}
