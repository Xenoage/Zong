package com.xenoage.utils.jse.font;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.jse.settings.Settings;

/**
 * Test cases for a {@link FontReplacements} class.
 *
 * @author Andreas Wenger
 */
public class FontReplacementsTest {

	private Settings settings;


	@Before public void setUp() {
		settings = new Settings("data/test/config/");
	}

	@Test public void testGetReplacement() {
		FontReplacements fr = new FontReplacements(settings);
		//"Times New Roman" is replaced by "Linux Libertine"
		assertEquals("Linux Libertine", fr.getReplacement("Times New Roman"));
		//"No Way, This is No Font" is not replaced
		assertEquals("No Way, This is No Font", fr.getReplacement("No Way, This is No Font"));
	}

}
