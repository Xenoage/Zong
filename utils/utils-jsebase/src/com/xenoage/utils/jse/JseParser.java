package com.xenoage.utils.jse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Some useful methods for parsing specific to the JRE.
 *
 * @author Andreas Wenger
 */
public class JseParser {

	/**
	 * Returns the Date value of the given String,
	 * parsed with the given pattern, or null
	 * is the conversion is impossible.
	 */
	public static Date parseDate(String value, String pattern) {
		if (value == null)
			return null;
		try {
			DateFormat fmt = new SimpleDateFormat(pattern);
			return fmt.parse(value);
		} catch (ParseException ex) {
			return null;
		}
	}
	
}
