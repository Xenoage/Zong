package com.xenoage.zong.musiclayout.stampings.bitmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Units;

/**
 * Test cases for a {@link BitmapLine}.
 *
 * @author Andreas Wenger
 */
public class BitmapLineTest {

	@Test public void test1Pixel() {
		//1px line. Should result in 1 px, opaque.
		for (float scaling = 0.1f; scaling < 5; scaling *= 1.5f) {
			Color color = Color.black;
			BitmapLine sl = new BitmapLine(Units.pxToMm(1, scaling), color, scaling);
			assertEquals(Units.pxToMm(1, scaling), sl.widthMm, Delta.DELTA_FLOAT);
			assertEquals(255, sl.color.a);
		}
	}

	@Test public void testHalfPixel() {
		//0.5px line. Should result in 1 px with transparency.
		for (float scaling = 0.1f; scaling < 5; scaling *= 1.5f) {
			Color color = Color.black;
			BitmapLine sl = new BitmapLine(Units.pxToMm(0.5f, scaling), color, scaling);
			assertEquals(Units.pxToMm(1, scaling), sl.widthMm, Delta.DELTA_FLOAT);
			int alpha = sl.color.a;
			assertTrue(Math.abs(alpha - 128) < 5);
		}
	}

	@Test public void test3Pixel() {
		//3px line. Should result in 3 px, opaque.
		for (float scaling = 0.1f; scaling < 5; scaling *= 1.5f) {
			Color color = Color.black;
			BitmapLine sl = new BitmapLine(Units.pxToMm(3, scaling), color, scaling);
			assertEquals(Units.pxToMm(3, scaling), sl.widthMm, Delta.DELTA_FLOAT);
			assertEquals(255, sl.color.a);
		}
	}

}
