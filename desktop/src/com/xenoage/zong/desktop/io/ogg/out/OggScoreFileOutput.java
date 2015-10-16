package com.xenoage.zong.desktop.io.ogg.out;

import java.io.File;
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
	extends FileOutput<Score> {

	@Override public void write(Score score, int fileIndex, OutputStream stream)
		throws IOException {
		writeOgg(score, stream);
	}
	
	/**
	 * Writes the given score as a OGG Vorbis file into the given stream.
	 */
	public static void writeOgg(Score score, OutputStream stream)
		throws IOException {
		//save temporary WAVE file first
		File tempFile = File.createTempFile(OggScoreFileOutput.class.getName(), ".wav");
		try (OutputStream tempStream = new JseOutputStream(tempFile)) {
			WavScoreFileOutput.writeWav(score, tempStream);
		}
		//convert to ogg
		VorbisEncoder.convert(tempFile.getAbsolutePath(), new JseOutputStream(stream));
		tempFile.delete();
	}
	
}
