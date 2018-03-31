package com.xenoage.utils.jse.io;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Some useful URL functions.
 *
 * @author Andreas Wenger
 */
public class URLUtils {

	/**
	 * Returns true, if the given String contains an absolute URL.
	 */
	public static boolean isAbsoluteURL(String path) {
		try {
			new URL(path);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

}
