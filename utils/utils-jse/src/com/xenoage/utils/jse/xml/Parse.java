package com.xenoage.utils.jse.xml;

import static com.xenoage.utils.jse.xml.InvalidXMLData.invalid;
import static com.xenoage.utils.jse.xml.InvalidXMLData.throwNull;
import static com.xenoage.utils.jse.xml.XMLReader.attribute;
import static com.xenoage.utils.jse.xml.XMLReader.element;
import static com.xenoage.utils.jse.xml.XMLReader.text;

import org.w3c.dom.Element;

/**
 * Useful parsing methods.
 * 
 * @author Andreas Wenger
 */
public final class Parse {

	/**
	 * Gets the text of the child element with the given name
	 * of the given parent element as an float.
	 */
	public static float parseChildFloat(Element parent, String eName) {
		Element e = element(parent, eName);
		if (e != null) {
			return parseFloat(e);
		}
		throw invalid(e);
	}

	/**
	 * Gets the text of the child element with the given name
	 * of the given parent element as an int.
	 */
	public static int parseChildInt(Element parent, String eName) {
		Element e = element(parent, eName);
		if (e != null) {
			return parseInt(e);
		}
		throw invalid(parent);
	}

	/**
	 * Gets the text of the child element with the given name
	 * of the given parent element as an Integer, or null if the
	 * element does not exist.
	 */
	public static Integer parseChildIntNull(Element parent, String eName) {
		Element e = element(parent, eName);
		if (e != null) {
			return parseInt(e);
		}
		return null;
	}

	/**
	 * Gets the text of the child element with the given name
	 * of the given parent element as an Integer, or null if the
	 * element does not exist or the parsing fails.
	 */
	public static Integer parseChildIntNullTry(Element parent, String eName) {
		Element e = element(parent, eName);
		if (e != null) {
			try {
				return Integer.parseInt(text(e));
			} catch (NumberFormatException ex) {
			}
		}
		return null;
	}

	public static Boolean parseAttrBoolean(Element e, String attrName) {
		return throwNull(parseAttrBooleanNull(e, attrName), e);
	}

	public static Boolean parseAttrBooleanDefault(Element e, String attrName, boolean defaultValue) {
		Boolean ret = parseAttrBooleanNull(e, attrName);
		return (ret != null ? ret : defaultValue);
	}

	public static Boolean parseAttrBooleanNull(Element e, String attrName) {
		try {
			String value = attribute(e, attrName);
			return value != null ? Boolean.parseBoolean(value) : null;
		} catch (NumberFormatException ex) {
			throw invalid(e);
		}
	}

	public static Float parseAttrFloat(Element e, String attrName) {
		return throwNull(parseAttrFloatNull(e, attrName), e);
	}

	public static Float parseAttrFloatNull(Element e, String attrName) {
		try {
			String value = attribute(e, attrName);
			return value != null ? Float.parseFloat(value) : null;
		} catch (NumberFormatException ex) {
			throw invalid(e);
		}
	}

	public static int parseAttrInt(Element e, String attrName) {
		return throwNull(parseAttrIntNull(e, attrName), e);
	}

	public static Integer parseAttrIntNull(Element e, String attrName) {
		try {
			String value = attribute(e, attrName);
			return value != null ? parseInt(value) : null;
		} catch (NumberFormatException ex) {
			throw invalid(e);
		}
	}

	/**
	 * Gets the text of the given element as a float.
	 */
	public static float parseFloat(Element e) {
		try {
			return Float.parseFloat(text(e));
		} catch (NumberFormatException ex) {
			throw invalid(e);
		}
	}

	/**
	 * Gets the text of the child element with the given name
	 * of the given parent element as a float.
	 */
	public static float parseFloatInt(Element parent, String eName) {
		Element e = element(parent, eName);
		if (e != null) {
			return parseFloat(e);
		}
		throw invalid(e);
	}

	/**
	 * Gets the text of the child element with the given name
	 * of the given parent element as a Float, or null if the
	 * element does not exist.
	 */
	public static Float parseChildFloatNull(Element parent, String eName) {
		Element e = element(parent, eName);
		if (e != null) {
			return parseFloat(e);
		}
		return null;
	}

	/**
	 * Gets the text of the given element as an int.
	 */
	public static int parseInt(Element e) {
		try {
			return parseInt(text(e));
		} catch (NumberFormatException ex) {
			throw invalid(e);
		}
	}

	/**
	 * Parses an integer from a string. Also values with ".0", ".00" and so
	 * on are allowed.
	 */
	private static int parseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			//also allow .0 values
			float v = Float.parseFloat(value);
			if (v == (int) v)
				return (int) v;
			else
				throw new NumberFormatException("No integer");
		}
	}

}
