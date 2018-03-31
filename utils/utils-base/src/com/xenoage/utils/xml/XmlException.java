package com.xenoage.utils.xml;


/**
 * Runtime exception for any error when reading or writing an XML document.
 * 
 * It is a {@link RuntimeException} to avoid lots of "throws" boilerplate.
 * 
 * @author Andreas Wenger
 */
public class XmlException
	extends RuntimeException {

	public XmlException(String message) {
		super(message);
	}
	
	public XmlException(Throwable throwable) {
		super(throwable);
	}

}
