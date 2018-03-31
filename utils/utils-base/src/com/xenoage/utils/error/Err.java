package com.xenoage.utils.error;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.log.Report;

/**
 * This class manages a single error handler for
 * non-verbose and quick access.
 * 
 * By default, the {@link BasicErrorProcessing} strategy is used.
 *
 * @author Andreas Wenger
 */
public class Err {

	private static ErrorProcessing instance = new BasicErrorProcessing();


	/**
	 * Sets the current global {@link ErrorProcessing} instance.
	 */
	public static void init(@NonNull ErrorProcessing errorProcessing) {
		checkArgsNotNull(errorProcessing);
		instance = errorProcessing;
	}

	/**
	 * Gets the current global {@link ErrorProcessing} instance.
	 */
	public static ErrorProcessing getErrorProcessing() {
		return instance;
	}

	/**
	 * Handles the given error report, dependent on the registered {@link ErrorProcessing}.
	 */
	public static void handle(Report report) {
		instance.report(report);
	}

}
