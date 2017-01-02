package com.xenoage.zong.musicxml.util.error.handler;

/**
 * Handles errors in MusicXML files.
 *
 * @author Andreas Wenger
 */
public interface ErrorHandler {

	/**
	 * Call this method to report an error, which can not be fixed,
	 * and will stop the process.
	 */
	void throwException(RuntimeException exception) throws RuntimeException;

	/**
	 * Call this method to report an error, which could be fixed by the caller.
	 * The implementing class of this interface may decide if the fix is allowed
	 * (by returning true). If not (false), the given exception is thrown.
	 */
	boolean tryFix(RuntimeException exception) throws RuntimeException;

}
