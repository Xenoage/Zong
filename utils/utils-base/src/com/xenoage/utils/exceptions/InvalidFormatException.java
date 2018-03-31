package com.xenoage.utils.exceptions;

import java.io.IOException;

/**
 * This exception is thrown, when some data has a wrong format.
 * 
 * It can be used for example within a file reader that expected another format,
 * or when the content of the file is incorrect.
 *
 * @author Andreas Wenger
 */
public class InvalidFormatException
	extends IOException {

	public InvalidFormatException(String message) {
		super(message);
	}

	public InvalidFormatException(Throwable cause) {
		super(cause);
	}

	public InvalidFormatException(String message, Throwable cause) {
		super(message, cause);
	}

}
