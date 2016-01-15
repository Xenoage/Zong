package com.xenoage.utils.android.log;

import static com.xenoage.utils.log.Level.Remark;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;

import android.util.Log;

import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.LogProcessing;
import com.xenoage.utils.log.Report;

/**
 * Logging class for Android applications.
 * It is using the {@link Log} class.
 * 
 * @author Andreas Wenger
 */
public class AndroidLogProcessing
	implements LogProcessing {

	/**
	 * Initialize the logging class.
	 */
	public AndroidLogProcessing(String appNameAndVersion) {
		try {
			//start message
			println(Remark, "Logging started for: " + appNameAndVersion);
			//os
			println(
				Remark,
				"Operating system: " + System.getProperty("os.name") + ", " +
					System.getProperty("os.version"));
			//java version
			println(Remark, "Java version: " + System.getProperty("java.version"));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override public void log(Report report) {
		println(report.level, report.toString());
	}

	@Override public void close() {
		println(Remark, "Logging closed");
	}

	/**
	 * Returns the current time in the format HH:MM:SS.
	 */
	private static String time() {
		DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
		return df.format(new Date());
	}

	/**
	 * Prints the current time, the given log level and message
	 * into the log.
	 */
	private void println(Level level, String s) {
		Log.d("ZONG", time() + " " + level.getFixedString() + " " + s);
	}

}
