package com.xenoage.utils.jse.lang;

import com.xenoage.utils.lang.Lang;

/**
 * Interface for any component that contains language pack
 * dependent texts or content.
 * 
 * The {@link #languageChanged()} method is called each time when the
 * {@link Lang} class has loaded a new language, but
 * the component must register there before.
 *
 * @author Andreas Wenger
 */
public interface LanguageListener {

	/**
	 * This method must be called when the language was changed.
	 */
	public void languageChanged();

}
