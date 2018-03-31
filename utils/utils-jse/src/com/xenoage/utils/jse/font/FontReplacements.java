package com.xenoage.utils.jse.font;

import java.util.HashMap;

import com.xenoage.utils.jse.settings.Settings;


/**
 * This class helps to find a replacement for a font which
 * is not installed on the system.
 * 
 * The font name is looked up in the file
 * "data/test/config/font-replacements.settings".
 * 
 * @author Andreas Wenger
 */
public class FontReplacements
{
	
	private HashMap<String, String> replacements;
	
	private static FontReplacements instance = null;
	
	
	/**
	 * Gets the default {@link FontReplacements}, which is initialized
	 * the first time with the current {@link Settings}.
	 */
	public static FontReplacements getInstance()
	{
		if (instance == null)
			instance = new FontReplacements(Settings.getInstance());
		return instance;
	}
	
	
	public FontReplacements(Settings settings)
	{
		replacements = settings.getAllSettings("font-replacements");
	}
	
	
	/**
	 * Gets the replacement for the given font family.
	 * If unknown, the given font family is returned.
	 */
	public String getReplacement(String fontFamily)
	{
		String ret = replacements.get(fontFamily);
		if (ret == null)
			ret = fontFamily;
		return ret;
	}
	

}
