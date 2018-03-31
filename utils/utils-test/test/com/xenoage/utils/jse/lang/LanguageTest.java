package com.xenoage.utils.jse.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.xenoage.utils.lang.Language;
import com.xenoage.utils.lang.VocID;

/**
 * Test cases for the {@link Language} class.
 *
 * @author Andreas Wenger
 */
public class LanguageTest {

	public enum TestVocabulary
		implements VocID {
		About,
		Another,
		Anything,
		TestVoc,
		TestVoc2,
		NotExisting;

		@Override public String getDefaultValue() {
			return getID() + "_Val";
		}

		@Override public String getID() {
			return this.toString();
		}

	}


	/**
	 * Tests the get(String)-method.
	 */
	@Test public void get1()
		throws Exception {
		Language l = LanguageReader.read("data/test/lang", "testlang");
		assertEquals("This is a test vocabulary.", l.get(TestVocabulary.TestVoc));
		assertNull(l.getWithNull(TestVocabulary.NotExisting));
	}

	/**
	 * Tests the get(String, String[])-method.
	 */
	@Test public void get2()
		throws Exception {
		Language l = LanguageReader.read("data/test/lang", "testlang");
		String[] tokens = new String[] { "stupid", "- haha", "crazy" };
		assertEquals("This (stupid) text has some crazy tokens in it - haha.",
			l.get(TestVocabulary.TestVoc2, tokens));
	}

}
