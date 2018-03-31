package com.xenoage.utils.log;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;

import com.xenoage.utils.annotations.NonNull;

/**
 * This class manages a single logging handler for
 * non-verbose and quick access.
 * 
 * @author Andreas Wenger
 */
public class Log {

	private static LogProcessing instance = new NoLogProcessing();
	private static Level minLogLevel = Level.Remark;


	/**
	 * Sets the current global {@link LogProcessing} instance.
	 */
	public static void init(@NonNull LogProcessing logProcessing) {
		checkArgsNotNull(logProcessing);
		Log.instance = logProcessing;
	}

	/**
	 * Disables logging.
	 */
	public static void initNoLog() {
		Log.instance = new NoLogProcessing();
	}

	/**
	 * Sets the log level. This will influence the messages which
	 * will be logged afterwards.
	 */
	public static void setLoggingLevel(Level minLogLevel) {
		Log.minLogLevel = minLogLevel;
	}

	public static Level getLoggingLevel() {
		return minLogLevel;
	}

	public static void log(Report report) {
		if (report.level.isIncludedIn(minLogLevel))
			instance.log(report);
	}

	/**
	 * Flushes and closes the log file.
	 */
	public static void close() {
		instance.close();
	}

}
