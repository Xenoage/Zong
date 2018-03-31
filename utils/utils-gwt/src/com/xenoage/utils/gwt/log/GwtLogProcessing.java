package com.xenoage.utils.gwt.log;

import static com.xenoage.utils.log.Level.Remark;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.xenoage.utils.log.Level;
import com.xenoage.utils.log.LogProcessing;
import com.xenoage.utils.log.Report;

/**
 * Logging class for GWT applications.
 * 
 * This class uses the {@link GWT#log(String)} method.
 * 
 * @author Andreas Wenger
 */
public class GwtLogProcessing
	implements LogProcessing {

	/**
	 * Initialize the logging class.
	 */
	public GwtLogProcessing(String appNameAndVersion) {
		//start message
		println(Remark, "Logging started for: " + appNameAndVersion);
		//system information
		println(Remark, "GWT version: " + GWT.getVersion());
		println(Remark, "Host URL: " + GWT.getHostPageBaseURL());
	}

	@Override public void log(Report report) {
		println(report.level, report.toString());
	}

	@Override public void close() {
	}

	/**
	 * Returns the current time in the format "23:59:59".
	 */
	private static String time() {
		DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss");
		return df.format(new Date());
	}

	/**
	 * Prints the current time, the given log level and message
	 * and starts a new line.
	 */
	private void println(Level level, String s) {
		println(time() + " " + level.getFixedString() + " " + s);
	}
	
	/**
	 * Prints the given message and starts a new line.
	 */
	private void println(String s) {
		GWT.log(s);
	}

}
