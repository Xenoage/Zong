package com.xenoage.utils.log;

/**
 * No logging (quiet).
 * 
 * @author Andreas Wenger
 */
public class NoLogProcessing
	implements LogProcessing {

	@Override public void log(Report report) {
	}

	@Override public void close() {
	}

}
