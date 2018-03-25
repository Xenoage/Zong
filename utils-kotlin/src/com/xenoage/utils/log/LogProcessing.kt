package com.xenoage.utils.log

/**
 * Interface for a logging class.
 */
interface LogProcessing {

	/** Writes a message to the log. */
	fun log(report: Report)

	/** Flushes and closes the log file. */
	fun close()

}
