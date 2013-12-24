package com.xenoage.zong.io.ogg.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.wav.out.WAVScoreFileOutput;

/**
 * This class writes an Ogg Vorbis file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class OGGScoreFileOutput
	implements FileOutput<Score> {

	@Override public void write(Score score, OutputStream stream, String filePath)
		throws IOException {
		//save temporary WAVE file first
		File tempFile = File.createTempFile(getClass().getName(), ".wav");
		new WAVScoreFileOutput().write(score, new JseOutputStream(new FileOutputStream(tempFile)),
			tempFile.getAbsolutePath());
		//convert to ogg
		VorbisEncoder.convert(tempFile.getAbsolutePath(), new JseOutputStream(stream));
		tempFile.delete();
	}

	@Override public boolean isFilePathRequired(Score document) {
		return true;
	}

}
