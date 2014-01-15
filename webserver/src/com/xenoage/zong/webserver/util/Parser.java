package com.xenoage.zong.webserver.util;

import java.util.Map;

import org.w3c.dom.Element;

import com.xenoage.utils.jse.xml.XMLReader;

/**
 * Some parser helpers.
 * 
 * @author Andreas Wenger
 */
public class Parser {

	public static String getString(Element parent, String key) {
		String ret = XMLReader.elementText(parent, key);
		if (ret == null || ret.length() == 0) {
			throw new RuntimeException(key + " must be given");
		}
		return ret;
	}

	public static int getInt(Element parent, String key) {
		String value = getString(parent, key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			throw new RuntimeException("Invalid integer in " + key);
		}
	}

	public static int getIntAttr(Element element, String attr) {
		String value = XMLReader.attribute(element, attr);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			throw new RuntimeException("Invalid integer in attribute " + attr + " of element " +
				element.getNodeName());
		}
	}

	public static String getString(Map<String, String[]> params, String key) {
		String[] rets = params.get(key);
		if (rets == null || rets.length == 0) {
			throw new RuntimeException(key + " must be given");
		}
		return rets[0];
	}

	public static int getInt(Map<String, String[]> params, String key) {
		String value = getString(params, key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			throw new RuntimeException("Invalid integer in " + key);
		}
	}

}
