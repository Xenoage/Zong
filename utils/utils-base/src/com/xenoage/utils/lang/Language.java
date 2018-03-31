package com.xenoage.utils.lang;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.xenoage.utils.NullUtils.notNull;

/**
 * Language pack, containing vocabularies.
 * 
 * For non-verbose and quick access to a language pack
 * in the program, use the {@link Lang} class instead of this class.
 *
 * @author Andreas Wenger
 */
public class Language {

	private final String id;

	//maps vocabulary id to translated text
	public HashMap<String, String> entries = new HashMap<>();


	public Language(String id, HashMap<String, String> entries) {
		super();
		this.id = id;
		this.entries = entries;
	}

	/**
	 * Adds the given vocabulary.
	 */
	public void add(String id, String value) {
		entries.put(id, value);
	}

	/**
	 * Gets the ID of this language.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, null is returned.
	 */
	public String getWithNull(VocID id) {
		if (id == null)
			return null;
		return entries.get(id.getID());
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, null is returned.
	 */
	public String getWithNull(String id) {
		if (id == null)
			return null;
		return entries.get(id);
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, the id is given back as a String
	 * (because it's more useful for the user
	 * than an empty string).
	 */
	public String get(VocID id) {
		return notNull(getWithNull(id), id.toString());
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, the id is given back as a String
	 * (because it's more useful for the user
	 * than an empty string).
	 */
	public String get(String id) {
		return notNull(getWithNull(id), id.toString());
	}

	/**
	 * Gets an entry from the language pack.
	 * If no value is found, the id is given back as a String
	 * (because it's more useful for the user
	 * than an empty string).
	 * The tokens {1}, {2}, ... {n} are replaced
	 * by the given Strings.
	 */
	public String get(VocID id, String... replacements) {
		String ret = get(id);
		//search for {n}-tokens and replace them
		for (int i = 0; i < replacements.length; i++) {
			ret = ret.replaceAll("\\{" + (i + 1) + "\\}", replacements[i]);
		}
		return ret;
	}

	/**
	 * Gets all vocabulary keys.
	 */
	public Set<String> getAllKeys() {
		return new HashSet<>(entries.keySet());
	}

}
