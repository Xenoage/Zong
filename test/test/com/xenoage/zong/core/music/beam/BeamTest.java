package com.xenoage.zong.core.music.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChordsUnchecked;
import static com.xenoage.zong.core.music.chord.ChordFactory.graceChord;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.zong.core.music.chord.Chord;

import material.beam.fragments.ChlapikBeamFragments;

/**
 * Tests for {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamTest {
	
	@Test public void getMaxLinesCountTest() {
		ChlapikBeamFragments source = new ChlapikBeamFragments();
		assertEquals(2, source.exampleRow1Col1().getMaxLinesCount());
		assertEquals(2, source.exampleRow1Col2().getMaxLinesCount());
		assertEquals(2, source.exampleRow1Col3().getMaxLinesCount());
		assertEquals(2, source.exampleRow1Col4().getMaxLinesCount());
		assertEquals(2, source.exampleRow2Col1().getMaxLinesCount());
		assertEquals(2, source.exampleRow2Col2().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col3().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col4().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col5().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col6().getMaxLinesCount());
		assertEquals(2, source.exampleRow3Col2().getMaxLinesCount());
		assertEquals(2, source.exampleRow3Col4().getMaxLinesCount());
		assertEquals(3, source.exampleRow3Col6().getMaxLinesCount());
	}
	
	@Test public void getMaxLinesCountTest_Grace() {
		List<Chord> chords = alist();
		chords.add(graceChord(pi(0, 4), Companion.fr(1, 32)));
		chords.add(graceChord(pi(0, 4), Companion.fr(1, 32)));
		Beam beam = beamFromChordsUnchecked(chords);
		assertEquals(3, beam.getMaxLinesCount());
	}

}
