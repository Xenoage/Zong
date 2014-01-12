package com.xenoage.zong.desktop.io.ogg.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.wav.out.WavScoreFileOutput;

/**
 * This class writes an Ogg Vorbis file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class OggScoreFileOutput
	implements FileOutput<Score> {

	@Override public void write(Score score, OutputStream stream, String filePath)
		throws IOException {
		writeOgg(score, new JseOutputStream(stream));
	}
	
	/**
	 * Writes the given score as a OGG Vorbis file into the given stream.
	 */
	public static void writeOgg(Score score, java.io.OutputStream stream)
		throws IOException {
		//save temporary WAVE file first
		File tempFile = File.createTempFile(OggScoreFileOutput.class.getName(), ".wav");
		new WavScoreFileOutput().write(score, new JseOutputStream(new FileOutputStream(tempFile)),
			tempFile.getAbsolutePath());
		//convert to ogg
		VorbisEncoder.convert(tempFile.getAbsolutePath(), new JseOutputStream(stream));
		tempFile.delete();
	}

	@Override public boolean isFilePathRequired(Score document) {
		return false;
	}
	
}
