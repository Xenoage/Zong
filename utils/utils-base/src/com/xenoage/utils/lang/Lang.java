package com.xenoage.utils.lang;

import com.xenoage.utils.iterators.It;
import com.xenoage.utils.kernel.Tuple2;

import java.util.ArrayList;

import static com.xenoage.utils.kernel.Tuple2.t;

/**
 * This class manages a single language pack for
 * non-verbose and quick access.
 *
 * @author Andreas Wenger
 */
public class Lang {

	private static Language currentLanguage = null;

	private static ArrayList<Tuple2<String, String>> tokens = new ArrayList<>();


	/**
	 * Sets the current language to the given language.
	 */
	public static void setCurrentLanguage(Language language) {
		currentLanguage = language;
	}

	/**
	 * Gets the current language.
	 */
	public static Language getCurrentLanguage() {
		return currentLanguage;
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, null is returned.
	 */
	public static String getWithNull(VocID id) {
		if (currentLanguage == null)
			return null;
		return currentLanguage.getWithNull(id);
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, the id is given back as a String
	 * (because it's more useful for the user than an empty string).
	 */
	public static String get(VocID id) {
		if (id == null)
			return "";
		String s = getWithNull(id);
		if (s == null)
			return id.getDefaultValue();
		for (Tuple2<String, String> token : tokens)
			s = s.replace(token.get1(), token.get2());
		return s;
	}

	/**
	 * Gets an entry from the language pack as a label text.
	 * By default this is the text with a trailing colon (":").
	 */
	public static String getLabel(VocID id) {
		return get(id) + ":";
	}

	/**
	 * Gets an entry from the language pack with a trailing "...".
	 */
	public static String getWithEllipsis(VocID id) {
		return get(id) + "...";
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, the id is given back as a String
	 * (because it's more useful for the user
	 * than an empty string).
	 * The tokens {1}, {2}, ... {n} are replaced
	 * by the given Strings.
	 */
	public static String get(VocID id, String... replacements) {
		String ret = get(id);
		//search for {n}-tokens and replace them
		for (int i = 0; i < replacements.length; i++) {
			ret = ret.replace("{" + (i + 1) + "}", replacements[i]);
		}
		return ret;
	}

	/**
	 * Register a token for replacement, e.g. "{app.name}" by "Xenoage WhatEver".
	 */
	public static void registerToken(String symbol, String value) {
		tokens.add(t(symbol, value));
	}

	/**
	 * Gets an iterator over the tokens for replacement.
	 */
	public static It<Tuple2<String, String>> getTokens() {
		return new It<>(tokens);
	}

}
