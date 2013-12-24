package com.xenoage.zong.io.ogg.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.ScoreFileOutput;
import com.xenoage.zong.io.wav.out.WAVScoreFileOutput;


/**
 * This class writes an Ogg Vorbis file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class OGGScoreFileOutput
	extends ScoreFileOutput
{
	
	
	@Override public void write(Score score, OutputStream outputStream, String filePath)
    throws IOException
	{
		//save temporary WAVE file first
		File tempFile = File.createTempFile(getClass().getName(),".wav");
		new WAVScoreFileOutput().write(score, new FileOutputStream(tempFile), tempFile.getAbsolutePath());
		//convert to ogg
		VorbisEncoder.convert(tempFile.getAbsolutePath(), outputStream);
		tempFile.delete();
	}
	
}
