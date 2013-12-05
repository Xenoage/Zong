package com.xenoage.zong.desktop.io.symbols;

import org.junit.Test;

/**
 * Tests for {@link AwtSvgPathReader}.
 *
 * @author Andreas Wenger
 */
public class AwtSvgPathReaderTest {

	@Test public void readTest() {
		AwtSvgPathReader p = new AwtSvgPathReader();
		String validPath = "M 100 100 L 300 100 L 200 300 z";
		p.read(validPath);
		validPath = "M200,300 L400,50 L600,300 L800,550 L1000,300";
		p.read(validPath);
	}

}
