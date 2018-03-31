package com.xenoage.utils.error;

import com.xenoage.utils.log.Report;

/**
 * Interface for error handlers.
 * 
 * @author Andreas Wenger
 */
public interface ErrorProcessing {

	/**
	 * Reports a problem.
	 */
	public void report(Report report);

}
