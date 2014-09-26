package com.xenoage.zong.symbols;

import static com.xenoage.utils.jse.async.Sync.sync;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.zong.io.symbols.SymbolPoolReader;

/**
 * Tests for {@link SymbolPool}.
 *
 * @author Andreas Wenger
 */
public class SymbolPoolTest {

	private SymbolPool symbolPool = null;


	@Before public void setUp() {
		//load default symbol pool, clef-g must exist
		try {
			symbolPool = sync(new SymbolPoolReader("default"));
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
