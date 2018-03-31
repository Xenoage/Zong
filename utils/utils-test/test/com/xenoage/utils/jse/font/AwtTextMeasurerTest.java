package com.xenoage.utils.jse.font;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Font;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.jse.OSUtils;
import com.xenoage.utils.jse.OSUtils.OS;

/**
 * Test cases for the {@link AwtTextMeasurer} class.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class AwtTextMeasurerTest {

	private boolean windowsFontsAvailable = true;


	@Before public void setUp() {
		//find out if Verdana and Times New Roman are installed.
		//if not, skip the tests.
		windowsFontsAvailable &= FontUtils.getInstance().isFontFamilySupported("Verdana");
		windowsFontsAvailable &= FontUtils.getInstance().isFontFamilySupported("Times New Roman");
	}
	
	@Test public void checkFontsAvailable() {
		//if we are in Windows, don't accept missing default fonts
		if (OSUtils.getOS() == OS.Windows && false == windowsFontsAvailable)
			fail("Verdana and Times New Roman must be installed");
	}

	@Test public void getWidthTest() {
		if (windowsFontsAvailable) {
			Font font = new Font("Verdana", Font.PLAIN, 72);
			float fw = AwtTextMeasurer.measure(font, "Hallo Welt").getWidth();
			assertEquals(127, fw, 1.5);

			font = new Font("Verdana", Font.PLAIN, 72);
			fw = AwtTextMeasurer.measure(font, "Hallo WeltHallo WeltHallo WeltHallo Welt").getWidth();
			assertEquals(519, fw, 3);

			font = new Font("Times New Roman", Font.PLAIN, 120);
			fw = AwtTextMeasurer.measure(font, "Using Times").getWidth();
			assertEquals(216f, fw, 1f);
		}
	}

	@Test public void getAscentTest() {
		if (windowsFontsAvailable) {
			Font font = new Font("Verdana", Font.PLAIN, 72);
			float f = AwtTextMeasurer.measure(font, "Mein Ascent").getAscent();
			assertEquals(25f, f, 1f);

			font = new Font("Verdana", Font.PLAIN, 12);
			f = AwtTextMeasurer.measure(font, "Mein Ascent").getAscent();
			assertEquals(4.2f, f, 0.1f);

			font = new Font("Times New Roman", Font.PLAIN, 120);
			f = AwtTextMeasurer.measure(font, "Mein Ascent").getAscent();
			assertEquals(38f, f, 1f);
		}
	}

}
