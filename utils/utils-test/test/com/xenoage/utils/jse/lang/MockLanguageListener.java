package com.xenoage.utils.jse.lang;

import com.xenoage.utils.jse.lang.LanguageListener;

/**
 * An simple {@link LanguageListener} for testing.
 * 
 * @author Andreas Wenger
 */
public class MockLanguageListener
	implements LanguageListener {

	public int languageChangedCounter = 0;


	@Override public void languageChanged() {
		languageChangedCounter++;
	}

}
