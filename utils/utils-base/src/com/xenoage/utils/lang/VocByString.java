package com.xenoage.utils.lang;

/**
 * Vocabulary IDs in String format.
 * 
 * The default value is "".
 * 
 * @author Andreas Wenger
 */
public class VocByString
	implements VocID {

	private final String id;


	/**
	 * Creates a new {@link VocID} using the given vocabulary ID (like "UnknownError").
	 */
	public static VocByString voc(String vocID) {
		return new VocByString(vocID);
	}

	private VocByString(String vocID) {
		this.id = vocID;
	}

	/**
	 * Gets the ID of the vocabulary as a String.
	 */
	@Override public String getID() {
		return id;
	}

	@Override public String toString() {
		return id;
	}

	@Override public String getDefaultValue() {
		return "";
	}

}
