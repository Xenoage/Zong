package com.xenoage.utils.lang;

/**
 * This interface must be implemented by all enums that contain
 * vocabulary IDs.
 * 
 * There must be a method {@link #getDefaultValue()} to which
 * contains the text in the default language of the application
 * (used for the default language and for fallback)
 * and a method {@link #getID()} that returns the ID
 * as a String needed to index the vocabulary in XML files
 * (like "Error_UnknownValue").
 * 
 * @author Andreas Wenger
 */
public interface VocID {

	/**
	 * Gets the text in the default language.
	 */
	public String getDefaultValue();

	/**
	 * Gets the ID of the vocabulary as a String.
	 */
	public String getID();

}
