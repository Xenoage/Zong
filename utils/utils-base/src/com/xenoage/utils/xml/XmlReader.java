package com.xenoage.utils.xml;

import com.xenoage.utils.Parser;

/**
 * Platform independent base class of a XML stream reader,
 * inspired by the StAX Cursor API.
 * 
 * Implementations for different platforms (Java SE, Android, GWT, ...)
 * are provided in the corresponding platform libraries.
 * 
 * @author Andreas Wenger
 */
public abstract class XmlReader {

	/**
	 * Gets the name of this element.
	 */
	public abstract String getElementName();
	
	/**
	 * Gets the complete text content of this element,
	 * or null if there is none.
	 * This method only works correct if called at the start of an element.
	 */
	public abstract String getText();
	
	/**
	 * Gets the complete text content of this element,
	 * or an empty string if the text does not exist.
	 * This method only works correct if called at the start of an element.
	 */
	public String getTextNotNull() {
		String v = getText();
		if (v == null)
			return "";
		return v;
	}
	
	/**
	 * Gets the complete text content of this element as an integer,
	 * or null if there is none.
	 * This method only works correct if called at the start of an element.
	 */
	public Integer getTextInt() {
		String v = getText();
		if (v == null)
			return null;
		return Parser.parseIntegerNull(v);
	}
	
	/**
	 * Gets the complete text content of this element as an integer,
	 * but throws an {@link XmlDataException} if the text does not exist or can not be converted.
	 * This method only works correct if called at the start of an element.
	 */
	public int getTextIntNotNull() {
		String v = getText();
		Integer ret = Parser.parseIntegerNull(v);
		if (ret == null) {
			//TODO: TOLERANT mode
			//try again to read float and cast it to int
			Float floatValue = Parser.parseFloatNull(v);
			if (floatValue == null)
				throw dataException("text \"" + v + "\" in <" + getElementName() +
						"> can not be parsed to int");
			ret = floatValue.intValue();
		}
		return ret;
	}
	
	/**
	 * Gets the complete text content of this element as a float,
	 * or null if there is none.
	 * This method only works correct if called at the start of an element.
	 */
	public Float getTextFloat() {
		String v = getText();
		if (v == null)
			return null;
		return Parser.parseFloatNull(v);
	}
	
	/**
	 * Gets the complete text content of this element as a float,
	 * but throws an {@link XmlDataException} if the text does not exist or can not be converted.
	 * This method only works correct if called at the start of an element.
	 */
	public float getTextFloatNotNull() {
		String v = getText();
		Float ret = Parser.parseFloatNull(v);
		if (ret == null)
			throw dataException("text \"" + v + "\" in <" + getElementName() +
					"> can not be parsed to float");
		return ret;
	}

	/**
	 * Gets the number of attributes at this element.
	 */
	public abstract int getAttributeCount();

	/**
	 * Gets the local name of the attribute at the given index at this element.
	 */
	public abstract String getAttributeName(int index);

	/**
	 * Gets the string value of the attribute at the given index at this element.
	 */
	public abstract String getAttribute(int index);

	/**
	 * Gets all attributes at this element.
	 */
	public XmlAttribute[] getAttributes() {
		XmlAttribute[] ret = new XmlAttribute[getAttributeCount()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new XmlAttribute(getAttributeName(i), getAttribute(i));
		}
		return ret;
	}

	/**
	 * Gets the value of the attribute with the given name at this element, or null if not found.
	 * This method is convenient, but slower than iterating through the attributes on the caller side.
	 * For each call, the whole list of attributes has to be checked.
	 */
	public String getAttribute(String name) {
		for (int i = 0; i < getAttributeCount(); i++) {
			if (getAttributeName(i).equals(name))
				return getAttribute(i);
		}
		return null;
	}
	
	/**
	 * Like {@link #getAttribute(String)}, but throws an {@link XmlDataException} if the
	 * attribute is not set.
	 */
	public String getAttributeNotNull(String name) {
		String ret = getAttribute(name);
		if (ret == null)
			throw dataException(name + " unknown");
		return ret;
	}

	/**
	 * Gets the value of the attribute with the given name at this element
	 * as a int value, or null if not found.
	 * See {@link #getAttribute(String)} for a notice about performance.
	 */
	public Integer getAttributeInt(String name) {
		String v = getAttribute(name);
		if (v == null)
			return null;
		return Parser.parseIntegerNull(v);
	}
	
	/**
	 * Like {@link #getAttributeInt(String)}, but throws an {@link XmlDataException} if the
	 * attribute is not set.
	 */
	public int getAttributeIntNotNull(String name) {
		String v = getAttribute(name);
		if (v == null)
			throw dataException(name + " unknown");
		try {
			return Integer.parseInt(v);
		}
		catch (NumberFormatException ex) {
			throw dataException(name + " in <" + getElementName() +
					"> is no int");
		}
	}

	/**
	 * Gets the value of the attribute with the given name at this element
	 * as a float value, or null if not found.
	 * See {@link #getAttribute(String)} for a notice about performance.
	 */
	public Float getAttributeFloat(String name) {
		String v = getAttribute(name);
		if (v == null)
			return null;
		return Parser.parseFloatNull(v);
	}

	/**
	 * Moves forward to the next start of an child element and returns true.
	 * If there is none (i.e. if the end of the current element is seen before), false is returned.
	 */
	public abstract boolean openNextChildElement();
	
	/**
	 * Moves forward to the next start of an child element with the given local name and returns true.
	 * Child elements with other names are skipped.
	 * If there is none (i.e. if the end of the current element is seen before), false is returned.
	 */
	public boolean openNextChildElement(String name) {
		while (openNextChildElement()) {
			if (getElementName().equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Closes the current element, i.e. moves forward to the end of the current element.
	 * If the current element has child elements, they are skipped.
	 */
	public abstract void closeElement();

	/**
	 * Creates a {@link XmlDataException} at the current position.
	 */
	public abstract XmlDataException dataException();

	/**
	 * Creates a {@link XmlDataException} at the current position,
	 * using the additional detail message.
	 */
	public abstract XmlDataException dataException(String message);
	
	/**
	 * Gets the current line number.
	 */
	public abstract int getLine();
	
	/**
	 * Convenience method to close the underlying input stream.
	 */
	public abstract void close();

}
