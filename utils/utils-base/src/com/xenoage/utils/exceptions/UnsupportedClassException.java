package com.xenoage.utils.exceptions;


/**
 * This exception is thrown, when an object can not be used because
 * it is an instance of an unsupported class.
 *
 * @author Andreas Wenger
 */
public class UnsupportedClassException
	extends RuntimeException {

	public UnsupportedClassException(Object object) {
		super("Unsupported class: " + object.getClass().getName());
	}

}
