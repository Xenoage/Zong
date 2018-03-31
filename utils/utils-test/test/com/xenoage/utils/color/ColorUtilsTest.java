package com.xenoage.utils.color;

import org.junit.Test;

import static com.xenoage.utils.color.Color.color;
import static com.xenoage.utils.color.ColorUtils.*;
import static org.junit.Assert.*;

/**
 * Test cases for the {@link ColorUtils} class.
 * 
 * @author Andreas Wenger
 */
public class ColorUtilsTest {

	@Test public void getColorTest() {
		Color c = color(0, 0, 1, 0);
		assertEquals(c, getColor("#00000001"));
		c = color(0, 0, 1, 0xFF);
		assertEquals(c, getColor("#FF000001"));
		assertEquals(c, getColor("#000001"));
		c = color(0x21, 0x16, 0x0B, 0x2C);
		assertEquals(c, getColor("#2C21160B"));
		assertEquals(c, getColor("#2c21160b"));
	}

	@Test public void getHexTest() {
		Color c = color(0, 0, 1, 0);
		assertEquals("#00000001", getHex(c));
		c = color(0, 0, 1, 0xFF);
		assertEquals("#000001", getHex(c));
		c = color(0x21, 0x16, 0x0B, 0x2C);
		assertEquals("#2c21160b", getHex(c));
	}

	@Test public void toHTMLColorTest() {
		assertEquals("#000000", toHTMLColor(Color.black));
		assertEquals("#ffffff", toHTMLColor(Color.white));
		assertEquals("#ffffff", toHTMLColor(color(255, 255, 255, 200)));
		assertEquals("#ff0080", toHTMLColor(color(255, 0, 128, 100)));
		assertEquals("#010203", toHTMLColor(color(1, 2, 3, 100)));
	}

}
