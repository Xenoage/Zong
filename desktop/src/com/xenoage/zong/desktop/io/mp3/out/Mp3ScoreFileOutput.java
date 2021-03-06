package com.xenoage.zong.desktop.io.mp3.out;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.warning;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.lang.Lang;
import com.xenoage.zong.Voc;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.wav.out.WavScoreFileOutput;

/**
 * This class writes an MP3 file from a given {@link Score}.
 * 
 * The LAME tool must be installed and in the system path.
 * If lame can not be found, an error is reported.
 * 
 * @author Andreas Wenger
 */
public class Mp3ScoreFileOutput
	extends FileOutput<Score> {
	
	private static boolean debugToConsole = false;
	

	@Override public void write(Score score, int fileIndex, OutputStream stream)
		throws IOException {
		writeMp3(score, stream);
	}

	/**
	 * Writes the given score as a MP3 into the given stream.
	 */
	public static void writeMp3(Score score, OutputStream stream)
		throws IOException {
		//look if LAME is installed
		try {
			Runtime.getRuntime().exec("lame");
		} catch (Exception ex) {
			handle(Companion.warning(Lang.get(Voc.CouldNotFindLAME, Zong.INSTANCE.getWebsite() + "/lame")));
		}
		//save temporary WAVE file first
		File tempWAVFile = File.createTempFile(Mp3ScoreFileOutput.class.getName(), ".wav");
		try (OutputStream tempStream = new JseOutputStream(tempWAVFile)) {
			WavScoreFileOutput.writeWav(score, tempStream);
		}
		//create temporary MP3 file
		File tempMP3File = File.createTempFile(Mp3ScoreFileOutput.class.getName(), ".mp3");
		//convert to MP3
		try {
			Process process = new ProcessBuilder("lame", tempWAVFile.getAbsolutePath(),
				tempMP3File.getAbsolutePath()).start();
			if (debugToConsole) {
				//forward LAME error output to console
				try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
					String line;
					while ((line = br.readLine()) != null) {
					   System.out.println(line);
					}
				}
			}
			process.waitFor();
			int exitValue = process.exitValue();
			if (exitValue != 0)
				throw new IOException("LAME process returned: " + exitValue);
			tempWAVFile.delete();
			//copy MP3 file to output stream
			try (JseOutputStream mp3Stream = new JseOutputStream(stream)) {
				JseFileUtils.copyFileToStream(tempMP3File, mp3Stream);
			}
			tempMP3File.delete();
		} catch (Exception ex) {
			tempWAVFile.delete();
			tempMP3File.delete();
			throw new IOException(ex);
		}
	}

}
