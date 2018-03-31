package com.xenoage.utils.io;

import java.io.IOException;

/**
 * Exception, when a file could not be found.
 * 
 * This class is a replacement for the same exception from the JRE,
 * but is needed for compatibility with other frameworks like GWT.
 * 
 * @author Andreas Wenger
 */
public class FileNotFoundException
	extends IOException {
	
	public FileNotFoundException(String message) {
		super(message);
	}

}
