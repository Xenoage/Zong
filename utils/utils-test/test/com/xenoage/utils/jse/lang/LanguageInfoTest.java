package com.xenoage.utils.jse.lang;

import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for a {@link LanguageInfo}.
 *
 * @author Andreas Wenger
 */
public class LanguageInfoTest {

	private List<LanguageInfo> langs;


	@Before public void setUp()
		throws Exception {
		langs = LanguageInfo.getAvailableLanguages(LangManager.defaultLangPath);
		assertTrue("There must be at least one language pack!", langs.size() > 0);
	}

	@Test public void getAvailableLanguages()
		throws IOException {
		//all folders from language pack folder must be returned
		List<String> folders = io().listDirectories(LangManager.defaultLangPath);
		if (folders.size() == 0)
			fail("Error in test setup: No language found");
		assertEquals(folders.size(), langs.size());
		for (String folder : folders) {
			boolean found = false;
			for (LanguageInfo lang : langs) {
				if (lang.getID().equals(folder)) {
					found = true;
					break;
				}
			}
			assertTrue(found);
		}
	}

	@Test public void getLangInformation() {
		String errorMsg = "international name must be set";
		for (LanguageInfo lang : langs) {
			assertNotNull(errorMsg, lang.getInternationalName());
		}
	}

	@Test public void getFlag16() {
		String errorMsg = "flag16 must be set, because there is a flag16.png";
		for (LanguageInfo lang : langs) {
			if (new File(LangManager.defaultLangPath + "/" + lang.getID() + "/flag16.png").exists())
				assertNotNull(errorMsg, lang.getFlag16Path());
		}
	}

}
