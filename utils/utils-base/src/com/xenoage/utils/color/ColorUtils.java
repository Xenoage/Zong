package com.xenoage.utils.color;

import static com.xenoage.utils.color.Color.color;

/**
 * Useful methods to work with colors.
 * 
 * @author Andreas Wenger
 */
public class ColorUtils {

	/**
	 * Reads a hexadecimal color in either the form
	 * "#aarrggbb" or "#rrggbb".
	 * a, r, g and b may be lowercase or uppercase.
	 */
	public static Color getColor(String s)
		throws NumberFormatException {
		try {
			boolean withAlpha = (s.length() == 9);
			if (s.length() != 7 && !withAlpha) {
				throw new IllegalArgumentException("Illegal length");
			}
			int a = (withAlpha ? Integer.parseInt(s.substring(1, 3), 16) : 0xFF);
			int offset = (withAlpha ? 2 : 0);
			int r = Integer.parseInt(s.substring(offset + 1, offset + 3), 16);
			int g = Integer.parseInt(s.substring(offset + 3, offset + 5), 16);
			int b = Integer.parseInt(s.substring(offset + 5, offset + 7), 16);
			return color(r, g, b, a);
		} catch (RuntimeException ex) {
			throw new NumberFormatException();
		}
	}

	/**
	 * Gets the given color as a hex string in either the form
	 * "#aarrggbb" if alpha {@literal <} 0xFF, or "#rrggbb" otherwise.
	 * a, r, g and b are lowercase.
	 */
	public static String getHex(Color color) {
		StringBuilder s = new StringBuilder("#");
		if (color.a < 0xFF) {
			s.append(toHex2Digits(color.a));
		}
		s.append(toHex2Digits(color.r));
		s.append(toHex2Digits(color.g));
		s.append(toHex2Digits(color.b));
		return s.toString();
	}

	/**
	 * Returns the hexadecimal HTML-style value of a color
	 * with a leading # (format: #rrggbb).
	 */
	public static String toHTMLColor(Color color) {
		String rgb = Integer.toHexString(color.getRGB());
		rgb = "#" + rgb.substring(2, rgb.length());
		return rgb;
	}

	private static String toHex2Digits(int i) {
		String ret = Integer.toHexString(i);
		return (ret.length() == 1 ? "0" + ret : ret);
	}

}
