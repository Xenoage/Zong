package com.xenoage.utils.error;

import com.xenoage.utils.log.Report;

/**
 * This error handler wraps all messages it receives into a
 * {@link RuntimeException} and throws it.
 * 
 * @author Andreas Wenger
 */
public class NoErrorProcessing
	implements ErrorProcessing {

	@Override public void report(Report report) {
		throw new RuntimeException("Received a report: " + report.toString());
	}

}
