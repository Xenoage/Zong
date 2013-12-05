package com.xenoage.zong.symbols;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.GeneralPath;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.math.Delta;
import com.xenoage.zong.desktop.io.symbols.SymbolPoolReader;

/**
 * Tests for {@link SymbolPool}.
 *
 * @author Andreas Wenger
 */
public class SymbolPoolTest {

	private SymbolPool<GeneralPath> symbolPool = null;


	@Before public void setUp() {
		PlatformUtils.init(JsePlatformUtils.instance);
		DesktopIO.initTest();
		//load default symbol pool, clef-g must exist
		try {
			symbolPool = SymbolPoolReader.readSymbolPool("default");
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
		assertNotNull(symbolPool.getSymbol("clef-g"));
	}

	/**
	 * Computes the width of a number.
	 */
	@Test public void computeNumberWidth() {
		//test two digits
		float width0 = symbolPool.computeNumberWidth(0, 0);
		float width9 = symbolPool.computeNumberWidth(9, 0);
		assertTrue(width0 > 0);
		assertTrue(width9 > 0);
		//test number with 3 digits and 2 gaps
		float gap = 1;
		float width909 = symbolPool.computeNumberWidth(909, gap);
		assertEquals(2 * width9 + width0 + 2 * gap, width909, Delta.DELTA_FLOAT_ROUGH);
	}

}
