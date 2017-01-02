package com.xenoage.zong.musicxml.util.error.handler;

/**
 * A tolerant error handler, which try to use the provided fixes
 * instead of stopping the process.
 *
 * @author Andreas Wenger
 */
public class TolerantErrorHandler implements ErrorHandler {

	@Override public void throwException(RuntimeException exception)
			throws RuntimeException {;
		throw exception;
	}

	@Override public boolean tryFix(RuntimeException exception) {
		return true;
	}

}
