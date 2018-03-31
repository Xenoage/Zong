package com.xenoage.utils.jse.log;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.log.Level.Remark;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;

import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.LogProcessing;
import com.xenoage.utils.log.Report;

/**
 * Logging class for desktop applications.
 * 
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class DesktopLogProcessing
	implements LogProcessing {

	public static final String defaultFileName = "data/app.log";

	private static String logFileName;
	private static PrintStream writer = null;


	/**
	 * Initialize the logging class.
	 */
	public DesktopLogProcessing(String appNameAndVersion) {
		this(defaultFileName, appNameAndVersion);
	}

	/**
	 * Initialize the logging class.
	 */
	public DesktopLogProcessing(String logFileName, String appNameAndVersion) {
		try {
			DesktopLogProcessing.logFileName = logFileName;
			writer = new PrintStream(io().createFile(logFileName));
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
		writer.close();
	}

	/**
	 * Gets the filename of the log file.
	 */
	public String getLogFilename() {
		return logFileName;
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
	 * into the log file and starts a new line. The buffer is immediately
	 * flushed.
	 */
	private void println(Level level, String s) {
		writer.println(time() + " " + level.getFixedString() + " " + s);
		writer.flush();
	}

}
