package com.xenoage.utils.document.exceptions;


/**
 * This exception is thrown when a property of an object should be changed, but when
 * this property is already set or when the new and old value are the same.
 *
 * @author Andreas Wenger
 */
public class PropertyAlreadySetException
	extends RuntimeException {

	public PropertyAlreadySetException(String message) {
		super(message);
	}

}
