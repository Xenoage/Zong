package com.xenoage.utils.log;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.lang.VocID;

/**
 * A logging or error report.
 * 
 * @author Andreas Wenger
 */
public class Report {

	/** The severity of the report. */
	@NonNull public final Level level;
	/** The {@link VocID} of the message. */
	@MaybeNull public final VocID messageID;
	/** The raw text message. */
	@MaybeNull public final String message;
	/** The stack trace element where the report was made. */
	@MaybeNull public final StackTraceElement caller;
	/** The error object. */
	@MaybeNull public final Throwable error;
	/** The paths of the files which belong to this report. */
	@MaybeNull public final List<String> filePaths;


	public Report(Level level, VocID messageID, String message, StackTraceElement caller,
		Throwable error, List<String> filePaths) {
		checkArgsNotNull(level);
		this.level = level;
		this.messageID = messageID;
		this.message = message;
		this.caller = caller;
		this.error = error;
		this.filePaths = filePaths;
	}

	public static Report createReport(Level level, boolean findCaller, VocID messageID,
		String message, Throwable error, List<String> filePaths) {
		//only find caller, if log level is high enough
		if (level.isIncludedIn(Log.getLoggingLevel()) == false)
			findCaller = false;
		//get stack trace, if requested and if possible
		StackTraceElement caller = null;
		if (findCaller) {
			caller = platformUtils().getCaller(1);
		}
		return new Report(level, messageID, message, caller, error, filePaths);
	}

	@Override public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("[");
		ret.append("level:" + level + "; ");
		if (messageID != null)
			ret.append("messageID: <" + messageID + ">; ");
		if (message != null)
			ret.append("message: <" + message + ">; ");
		if (filePaths != null)
			ret.append("filePaths: " + filePaths + "; ");
		if (caller != null)
			ret.append("caller: <" + caller + ">; ");
		if (error != null)
			ret.append("error: <" + error + ">, stack trace: <" +
				platformUtils().getStackTraceString(error) + ">; ");
		return ret.toString();
	}

	public static Report fatal(VocID messageID) {
		return createReport(Level.Fatal, true, messageID, null, null, null);
	}

	public static Report fatal(String message) {
		return createReport(Level.Fatal, true, null, message, null, null);
	}

	public static Report fatal(Throwable error) {
		return createReport(Level.Fatal, true, null, null, error, null);
	}

	public static Report fatal(VocID messageID, Throwable error) {
		return createReport(Level.Fatal, true, messageID, null, error, null);
	}

	public static Report fatal(String message, Throwable error) {
		return createReport(Level.Fatal, true, null, message, error, null);
	}

	public static Report fatal(VocID messageID, Throwable error, String filePath) {
		return createReport(Level.Fatal, true, messageID, null, error, alist(filePath));
	}

	public static Report fatal(String message, Throwable error, String filePath) {
		return createReport(Level.Fatal, true, null, message, error, alist(filePath));
	}

	public static Report error(VocID messageID) {
		return createReport(Level.Error, true, messageID, null, null, null);
	}

	public static Report error(String message) {
		return createReport(Level.Error, true, null, message, null, null);
	}

	public static Report error(Throwable error) {
		return createReport(Level.Error, true, null, null, error, null);
	}

	public static Report error(VocID messageID, Throwable error) {
		return createReport(Level.Error, true, messageID, null, error, null);
	}

	public static Report error(String message, Throwable error) {
		return createReport(Level.Error, true, null, message, error, null);
	}

	public static Report error(VocID messageID, Throwable error, String filePath) {
		return createReport(Level.Error, true, messageID, null, error, alist(filePath));
	}

	public static Report error(String message, Throwable error, String filePath) {
		return createReport(Level.Error, true, null, message, error, alist(filePath));
	}

	public static Report warning(VocID messageID) {
		return createReport(Level.Warning, true, messageID, null, null, null);
	}

	public static Report warning(String message) {
		return createReport(Level.Warning, true, null, message, null, null);
	}

	public static Report warning(VocID messageID, String filePath) {
		return createReport(Level.Warning, false, messageID, null, null, alist(filePath));
	}

	public static Report warning(String message, String filePath) {
		return createReport(Level.Warning, false, null, message, null, alist(filePath));
	}

	public static Report warning(Throwable error) {
		return createReport(Level.Warning, true, null, null, error, null);
	}

	public static Report warning(VocID messageID, Throwable error) {
		return createReport(Level.Warning, true, messageID, null, error, null);
	}

	public static Report warning(String message, Throwable error) {
		return createReport(Level.Warning, true, null, message, error, null);
	}

	public static Report warning(VocID messageID, Throwable error, String filePath) {
		return createReport(Level.Warning, true, messageID, null, error, alist(filePath));
	}

	public static Report warning(String message, Throwable error, String filePath) {
		return createReport(Level.Warning, true, null, message, error, alist(filePath));
	}

	public static Report remark(VocID messageID) {
		return createReport(Level.Remark, false, messageID, null, null, null);
	}

	public static Report remark(String message) {
		return createReport(Level.Remark, false, null, message, null, null);
	}

	public static Report remark(VocID messageID, String filePath) {
		return createReport(Level.Remark, false, messageID, null, null, alist(filePath));
	}

	public static Report remark(String message, String filePath) {
		return createReport(Level.Remark, false, null, message, null, alist(filePath));
	}

}
