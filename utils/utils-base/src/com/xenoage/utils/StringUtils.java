package com.xenoage.utils;

import java.util.List;

/**
 * Functions to work with strings.
 *
 * @author Andreas Wenger
 */
public class StringUtils {

	private static final char[] LINEBREAK_CHARS = new char[] { ' ', '-', '\u2013' /* '–' */ };


	/**
	 * Returns a copy of the string, with trailing spaces omitted.
	 */
	public static String trimRight(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (s.charAt(i) != ' ')
				return s.substring(0, i + 1);
		}
		return "";
	}

	/**
	 * Returns true, if the given character is a line break character,
	 * otherwise false.
	 */
	public static boolean isLineBreakCharacter(char c) {
		for (char lbc : LINEBREAK_CHARS)
			if (lbc == c)
				return true;
		return false;
	}

	/**
	 * Returns true, if there is a line break character in the given string,
	 * otherwise false.
	 */
	public static boolean containsLineBreakCharacter(String s) {
		for (int i = 0; i < s.length(); i++)
			if (isLineBreakCharacter(s.charAt(i)))
				return true;
		return false;
	}

	/**
	 * Returns count times the given string concatenated.
	 */
	public static String repeat(String s, int count) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < count; i++)
			ret.append(s);
		return ret.toString();
	}

	/**
	 * Concatenates the Strings in the given list with
	 * the given separator. After the last element no separator
	 * follows.
	 * E.g. ["1", "2", "3"] and ":" results in "1:2:3".
	 */
	public static String concatenate(List<String> words, String separator) {
		int size = words.size();
		StringBuilder ret = new StringBuilder();
		int i = 0;
		for (String word : words) {
			ret.append(word);
			i++;
			if (i < size) {
				ret.append(separator);
			}
		}
		return ret.toString();
	}

	/**
	 * Formats the given integer number so that it has at least the given
	 * number of digits by adding 0 digits to the front.
	 */
	public static String fillIntDigits(int number, int minDigits) {
		String ret = "" + number;
		while (ret.length() < minDigits)
			ret = "0" + ret;
		return ret;
	}

}
