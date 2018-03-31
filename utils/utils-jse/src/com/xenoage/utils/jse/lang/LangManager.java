package com.xenoage.utils.jse.lang;

import com.xenoage.utils.jse.collections.WeakList;
import com.xenoage.utils.lang.Lang;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.utils.log.Report.warning;

/**
 * This class manages and watches the current {@link Lang}
 * and notifies its registered listeners.
 * 
 * Listeners do not need to be unregistered.
 * This is done automatically when there are no more references.
 * 
 * @author Andreas Wenger
 */
public class LangManager {

	public static final String defaultLangPath = "data/lang";

	private static WeakList<LanguageListener> languageComponents = new WeakList<>();


	/**
	 * Loads the given language pack from the default directory.
	 * If this fails, it is tried to load the English language pack.
	 * If this fails too, a fatal error is thrown.
	 * @param id    id of the language pack
	 */
	public static void loadLanguage(String id) {
		loadLanguage(defaultLangPath, id, true);
	}

	/**
	 * Loads the given language pack from the given directory.
	 * If this fails, it is tried to load the English language pack.
	 * If this fails too, a fatal error is thrown.
	 * @param path     path to the language pack directory (without trailing slash)
	 * @param id       id of the language pack
	 * @param warning  report warning if requested language pack could not be loaded
	 *                  and the English pack is loaded instead
	 */
	public static void loadLanguage(String path, String id, boolean warning) {
		try {
			//load requested language
			Lang.setCurrentLanguage(LanguageReader.read(path, id));
		} catch (Exception ex) {
			//loading language pack failed
			if (warning)
				handle(warning("The language \"" + id +
					"\" could not be loaded. Loading English pack instead...", ex));
			//try to load English one
			try {
				Lang.setCurrentLanguage(LanguageReader.read(path, "en"));
			} catch (Exception ex2) {
				handle(fatal("The language \"" + id + "\" could not be loaded.", ex2));
			}
		}
		updateLanguageComponents();
	}

	/**
	 * Register the given {@link LanguageListener}.
	 * Every time the language is changed, the <code>languageChanged()</code>
	 * method of all registered components is called.
	 * 
	 * Unregistering is not necessary. This class stores only weak
	 * references of the components, so they can be removed by the
	 * garbage collector when they are not used any more.
	 */
	public static void registerComponent(LanguageListener component) {
		languageComponents.add(component);
	}

	/**
	 * Unregister the all LanguageComponents.
	 */
	static void unregisterAllComponents() {
		languageComponents.clear();
	}

	/**
	 * Update all registered language components.
	 */
	static void updateLanguageComponents() {
		for (LanguageListener component : languageComponents.getAll()) {
			component.languageChanged();
		}
	}

	/**
	 * Gets the number of registered language components.
	 */
	static int getLanguageComponentsCount() {
		return languageComponents.getAll().size();
	}

}
