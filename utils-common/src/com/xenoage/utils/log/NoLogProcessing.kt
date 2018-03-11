package com.xenoage.utils.log

/**
 * No logging (quiet).
 */
object NoLogProcessing : LogProcessing {

	override fun log(report: Report) {}

	override fun close() {}

}
