package com.xenoage.zong.desktop.io.mp3.out;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.warning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
	implements FileOutput<Score> {
	
	private static boolean debugToConsole = false;
	

	@Override public void write(Score score, OutputStream stream, String filePath)
		throws IOException {
		writeMp3(score, new JseOutputStream(stream));
	}

	/**
	 * Writes the given score as a MP3 into the given stream.
	 */
	public static void writeMp3(Score score, java.io.OutputStream stream)
		throws IOException {
		//look if LAME is installed
		try {
			Runtime.getRuntime().exec("lame");
		} catch (Exception ex) {
			handle(warning(Lang.get(Voc.CouldNotFindLAME, Zong.website + "/lame")));
		}
		//save temporary WAVE file first
		File tempWAVFile = File.createTempFile(Mp3ScoreFileOutput.class.getName(), ".wav");
		new WavScoreFileOutput().write(score, new JseOutputStream(new FileOutputStream(tempWAVFile)),
			tempWAVFile.getAbsolutePath());
		//create temporary MP3 file
		File tempMP3File = File.createTempFile(Mp3ScoreFileOutput.class.getName(), ".mp3");
		//convert to MP3
		try {
			Process process = new ProcessBuilder("lame", tempWAVFile.getAbsolutePath(),
				tempMP3File.getAbsolutePath()).start();
			if (debugToConsole) {
				//forward LAME error output to console
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String line;
				while ((line = br.readLine()) != null) {
				   System.out.println(line);
				}
			}
			process.waitFor();
			int exitValue = process.exitValue();
			if (exitValue != 0)
				throw new IOException("LAME process returned: " + exitValue);
			tempWAVFile.delete();
			//copy MP3 file to output stream
			JseFileUtils.copyFile(tempMP3File.getAbsolutePath(), new JseOutputStream(stream));
			tempMP3File.delete();
		} catch (Exception ex) {
			tempWAVFile.delete();
			tempMP3File.delete();
			throw new IOException(ex);
		}
	}

	@Override public boolean isFilePathRequired(Score document) {
		return true;
	}

}
