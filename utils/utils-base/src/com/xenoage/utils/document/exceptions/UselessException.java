package com.xenoage.utils.document.exceptions;


/**
 * This exception is thrown when any operation was cancelled because it is useless.
 * 
 * For example, setting a green area to green or adding 0 to a number may be useless operations.
 *
 * @author Andreas Wenger
 */
public class UselessException
	extends RuntimeException {
}
