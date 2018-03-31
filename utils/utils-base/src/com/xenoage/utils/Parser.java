package com.xenoage.utils;


/**
 * Some useful methods for parsing.
 *
 * @author Andreas Wenger
 */
public class Parser {

	private Parser() {
	}

	/**
	 * Returns the int value of the given String,
	 * or 0 if the conversion impossible.
	 */
	public static int parseInt(String value) {
		return parseInt(value, 0);
	}

	/**
	 * Returns the int value of the given String,
	 * or the default value if the conversion is impossible.
	 */
	public static int parseInt(String value, int defaultValue) {
		if (value == null)
			return defaultValue;
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Returns the long value of the given String,
	 * or 0 if the conversion impossible.
	 */
	public static long parseLong(String value) {
		if (value == null)
			return 0;
		try {
			return Long.parseLong(value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * Returns the float value of the given String,
	 * or 0 if the conversion impossible.
	 */
	public static float parseFloat(String value) {
		if (value == null)
			return 0;
		try {
			return Float.parseFloat(value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * Returns the boolean value of the given String,
	 * or false if the conversion impossible.
	 */
	public static boolean parseBoolean(String value) {
		if (value == null)
			return false;
		try {
			return Boolean.parseBoolean(value);
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Returns the byte value of the given String,
	 * or 0 if the conversion impossible.
	 */
	public static byte parseByte(String value) {
		if (value == null)
			return 0;
		try {
			return Byte.parseByte(value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * Returns the Integer value of the given String,
	 * or null if the conversion impossible.
	 */
	public static Integer parseIntegerNull(String value) {
		if (value == null)
			return null;
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Returns the Float value of the given String,
	 * or null if the conversion impossible.
	 */
	public static Float parseFloatNull(String value) {
		if (value == null)
			return null;
		try {
			return Float.parseFloat(value);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Returns the Boolean value of the given String,
	 * or null if the conversion impossible.
	 */
	public static Boolean parseBooleanNull(String value) {
		if (value == null)
			return null;
		try {
			return Boolean.parseBoolean(value);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Returns the Boolean value of the given String,
	 * or null if the conversion impossible. "yes" and "no"
	 * are also allowed.
	 */
	public static Boolean parseBooleanNullYesNo(String value) {
		if (value == null)
			return null;
		else if (value.equals("yes"))
			return true;
		else if (value.equals("no"))
			return false;
		else
			return parseBooleanNull(value);
	}

	/**
	 * Returns the Integer value of the given hexadecimal String,
	 * or null if the conversion impossible.
	 */
	public static Integer parseInteger16Null(String value) {
		if (value == null)
			return null;
		try {
			return Integer.parseInt(value, 16);
		} catch (Exception ex) {
			return null;
		}
	}

}
