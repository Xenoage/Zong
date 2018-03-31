package com.xenoage.utils;

import com.xenoage.utils.xml.EnumWithXmlNames;

/**
 * Useful methods for working with enumerations.
 * 
 * @author Andreas Wenger
 */
public class EnumUtils {

	/**
	 * Gets the enum value from the given array, that
	 * matches (lower case) the given text, or null if not found.
	 * If the text begins with a digit, the prefix "_" is added.
	 */
	public static <T> T getEnumValue(String text, T... values) {
		if (text != null && text.length() > 0) {
			text = text.toLowerCase();
			if (Character.isDigit(text.charAt(0)))
				text = "_" + text;
			for (T value : values) {
				if (text.equals(value.toString().toLowerCase())) {
					return value;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the enum value from the given array, that
	 * matches (lower case) the string representation of the given object,
	 * or null if not found.
	 * If the text begins with a digit, the prefix "_" is added.
	 */
	public static <T> T getEnumValue(Object text, T... values) {
		if (text != null)
			return getEnumValue("" + text, values);
		return null;
	}
	
	/**
	 * Gets the {@link EnumWithXmlNames}
	 *  enum value from the given array, that
	 * matches (lower case) the given text, or null if not found.
	 */
	public static <T extends EnumWithXmlNames> T getEnumValueNamed(String text, T... values) {
		if (text != null) {
			for (T value : values) {
				if (text.equals(value.getXmlName())) {
					return value;
				}
			}
		}
		return null;
	}

}
