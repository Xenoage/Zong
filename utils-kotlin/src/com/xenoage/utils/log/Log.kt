package com.xenoage.utils.log

/**
 * This class manages a single logging handler for
 * non-verbose and quick access.
 */
object Log {

	internal var instance: LogProcessing = NoLogProcessing

	/** The log level. This will influence the messages which will be logged. */
	var loggingLevel = Level.Remark

	/** Logs the given report, if the logging level is high enough. */
	fun log(report: Report) {
		if (report.level.isIncludedIn(Log.loggingLevel))
			Log.instance.log(report)
	}

	/** Flushes and closes the log file. */
	fun close() {
		instance.close()
	}

}

/** Logs the given report, if the logging level is high enough. */
fun log(report: Report) {
	Log.log(report)
}