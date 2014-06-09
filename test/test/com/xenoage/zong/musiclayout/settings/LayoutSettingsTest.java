package com.xenoage.zong.musiclayout.settings;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;

public class LayoutSettingsTest {

	@Test public void testGetWidth()
		throws IOException {
		LayoutSettings ls = loadTestSettings();
		ChordSpacings sp = ls.spacings.normalChordSpacings;
		//duration 1/4 = width 3
		assertEquals(3, sp.getWidth(fr(1, 4)), Delta.DELTA_FLOAT);
		//duration 2/4 (=1/2) = width 4
		assertEquals(4, sp.getWidth(fr(2, 4)), Delta.DELTA_FLOAT);
		//duration 1/1 = width 6
		assertEquals(6, sp.getWidth(fr(1, 1)), Delta.DELTA_FLOAT);
		//duration 1/3 = width 3.1111...
		assertEquals((3 + 4) * ((1f / 3) / (3f / 4)), sp.getWidth(fr(1, 3)), Delta.DELTA_FLOAT_ROUGH);
		//duration 1/16 = width 1 (half of duration 1/8)
		assertEquals(1, sp.getWidth(fr(1, 16)), Delta.DELTA_FLOAT_ROUGH);
		//duration 2/1 = width 12 (double of duration 1/1)
		assertEquals(12, sp.getWidth(fr(2, 1)), Delta.DELTA_FLOAT_ROUGH);
		//same value again (to ensure the cache has no malfunction)
		assertEquals(12, sp.getWidth(fr(2, 1)), Delta.DELTA_FLOAT_ROUGH);
	}

	public static LayoutSettings loadTestSettings()
		throws IOException {
		return LayoutSettingsReader.read(
			jsePlatformUtils().openFile("data/test/layout/LayoutSettingsTest.xml"));
	}

}
