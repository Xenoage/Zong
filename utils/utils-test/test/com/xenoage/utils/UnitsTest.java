package com.xenoage.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Units;

/**
 * Test cases for a Units class.
 *
 * @author Andreas Wenger
 */
public class UnitsTest {

	@Test public void pxToMm() {
		//no scaling
		float scaling = 1;
		//72 dpi are used, so 72 px must be 25.4 mm
		//and 1 px must be about 0.35277778 mm
		assertEquals(Units.pxToMm(72, scaling), 25.4f, Delta.DELTA_FLOAT);
		assertEquals(Units.pxToMm(1, scaling), 0.35277778f, Delta.DELTA_FLOAT);
		//double scaling
		scaling = 2;
		//144 dpi, so 72 px must be 12.7 mm
		assertEquals(Units.pxToMm(72, scaling), 12.7f, Delta.DELTA_FLOAT);
	}

	@Test public void mmToPxAndMmToPxFloat() {
		//no scaling
		float scaling = 1;
		//72 dpi are used, so 25.4 mm must be 72 px
		//and 0.35277778 mm must be about 1 px 
		assertEquals(Units.mmToPx(25.4f, scaling), 72, Delta.DELTA_FLOAT);
		assertEquals(Units.mmToPx(25.4f, scaling), 72, Delta.DELTA_FLOAT);
		assertEquals(Units.mmToPx(0.35277778f, scaling), 1, Delta.DELTA_FLOAT);
		assertEquals(Units.mmToPx(0.35277778f, scaling), 1, Delta.DELTA_FLOAT);
		//double scaling
		scaling = 2;
		//144 dpi, so 12.7 mm must be 72 px
		assertEquals(Units.mmToPx(12.7f, scaling), 72, Delta.DELTA_FLOAT);
		assertEquals(Units.mmToPx(12.7f, scaling), 72, Delta.DELTA_FLOAT);
	}

}
