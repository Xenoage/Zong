package com.xenoage.utils.jse.lang;

import com.xenoage.utils.jse.lang.LanguageTest.TestVocabulary;
import com.xenoage.utils.lang.Lang;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for {@link LangManager}.
 *
 * @author Andreas Wenger
 */
public class LangManagerTest {

	@Test public void testCreateUnknownLanguage() {
		LangManager.unregisterAllComponents();
		LangManager.loadLanguage(LangManager.defaultLangPath, "thiswillbeunknown", false);
		assertEquals("Unknown language: Must load English!", "en", Lang.getCurrentLanguage().getID());
	}

	/**
	 * Tests if at least one language pack can be found.
	 */
	@Test public void testLanguagePacks()
		throws Exception {
		List<LanguageInfo> langs = LanguageInfo.getAvailableLanguages(LangManager.defaultLangPath);
		assertTrue("There must be at least one language pack!", langs.size() > 0);
		for (LanguageInfo lang : langs) {
			LangManager.loadLanguage(lang.getID());
			assertNotNull("Null result in language " + Lang.getCurrentLanguage().getID(),
					Lang.get(TestVocabulary.Anything));
		}
	}

	/**
	 * Tests the get(String)-method.
	 */
	@Test public void get1() {
		LangManager.loadLanguage("data/test/lang", "testlang", false);
		assertEquals("This is a test vocabulary.", Lang.get(TestVocabulary.TestVoc));
		assertEquals("Über", Lang.get(TestVocabulary.About));
		assertEquals("Noch etwas", Lang.get(TestVocabulary.Another));
		assertNull(Lang.getWithNull(TestVocabulary.NotExisting));
	}

	/**
	 * Tests the get(String, String[])-method.
	 */
	@Test public void get2() {
		LangManager.loadLanguage("data/test/lang", "testlang", false);
		String[] tokens = new String[] { "stupid", "- haha", "crazy" };
		assertEquals("This (stupid) text has some crazy tokens in it - haha.",
			Lang.get(TestVocabulary.TestVoc2, tokens));
	}

	/**
	 * Test the registering and implicit unregistering of {@link LanguageListener}s.
	 */
	@Test public void testLanguageComponents() {
		//create and add components
		LangManager.unregisterAllComponents();
		MockLanguageListener c1 = new MockLanguageListener();
		MockLanguageListener c2 = new MockLanguageListener();
		LangManager.registerComponent(c1);
		LangManager.registerComponent(c2);
		assertEquals(2, LangManager.getLanguageComponentsCount());
		//update 1
		LangManager.updateLanguageComponents();
		assertEquals(1, c1.languageChangedCounter);
		assertEquals(1, c1.languageChangedCounter);
		//update 2
		LangManager.updateLanguageComponents();
		assertEquals(2, c1.languageChangedCounter);
		assertEquals(2, c1.languageChangedCounter);
		//delete component 1
		c1 = null;
		System.gc(); //ok, this no guarantee... can we test it in another way?
		assertEquals(1, LangManager.getLanguageComponentsCount());
		//update 3
		LangManager.updateLanguageComponents();
		assertEquals(3, c2.languageChangedCounter);
		//delete other component
		LangManager.unregisterAllComponents();
		assertEquals(0, LangManager.getLanguageComponentsCount());
	}

}
