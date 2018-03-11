package com.xenoage.utils.log

import com.xenoage.utils.lang.VocID
import com.xenoage.utils.log.Report.Companion.createReport

/**
 * A logging or error report.
 */
class Report(
		/** The severity of the report  */
		val level: Level,
		/** The [VocID] of the message, or null  */
		val messageID: VocID? = null,
		/** The raw text message, or null  */
		val message: String? = null,
		/** The stack trace element where the report was made, or null  */
		val caller: String? = null, //TODO: StrackTraceElement
		/** The error object, or null  */
		val error: Throwable? = null,
		/** The paths of the files which belong to this report, or null  */
		val filePaths: List<String>? = null) {

	override fun toString(): String {
		val ret = StringBuilder()
		ret.append("[")
		ret.append("level: $level; ")
		if (messageID != null)
			ret.append("messageID: <$messageID>; ")
		if (message != null)
			ret.append("message: <$message>; ")
		if (filePaths != null)
			ret.append("filePaths: $filePaths; ")
		if (caller != null)
			ret.append("caller: <$caller>; ")
		if (error != null)
			ret.append("error: <" + error + ">") /* TODO , stack trace: <" +
					platformUtils().getStackTraceString(error) + ">; ") */
		return ret.toString()
	}

	companion object {

		fun createReport(level: Level, findCaller: Boolean, messageID: VocID? = null,
		                         message: String? = null, error: Throwable? = null, filePaths: List<String>? = null): Report {
			//only find caller, if log level is high enough
			/* TODO
			if (level.isIncludedIn(Log.getLoggingLevel()) == false)
				findCaller = false
			//get stack trace, if requested and if possible
			var caller: String? = null //TODO: StrackTraceElement
			if (findCaller) {
				caller = platformUtils().getCaller(1)
			} */
			return Report(level, messageID, message, null /* caller */, error, filePaths)
		}

	}

}

fun fatal(messageID: VocID? = null, error: Throwable? = null,
          filePaths: List<String>? = null): Report {
	return createReport(Level.Fatal, true, messageID, null, error, filePaths)
}

fun fatal(message: String?, error: Throwable? = null,
          filePaths: List<String>? = null): Report {
	return createReport(Level.Fatal, true, null, message, error, filePaths)
}

fun error(messageID: VocID? = null, error: Throwable? = null,
          filePaths: List<String>? = null): Report {
	return createReport(Level.Error, true, messageID, null, error, filePaths)
}

fun error(message: String? = null, error: Throwable? = null,
          filePaths: List<String>? = null): Report {
	return createReport(Level.Error, true, null, message, error, filePaths)
}

fun warning(messageID: VocID? = null, error: Throwable? = null,
            filePaths: List<String>? = null): Report {
	return createReport(Level.Warning, true, messageID, null, error, filePaths)
}

fun warning(message: String? = null, error: Throwable? = null,
            filePaths: List<String>? = null): Report {
	return createReport(Level.Warning, true, null, message, error, filePaths)
}

fun remark(messageID: VocID? = null, error: Throwable? = null,
           filePaths: List<String>? = null): Report {
	return createReport(Level.Remark, false, messageID, null, error, filePaths)
}

fun remark(message: String? = null, error: Throwable? = null,
           filePaths: List<String>? = null): Report {
	return createReport(Level.Remark, false, null, message, error, filePaths)
}