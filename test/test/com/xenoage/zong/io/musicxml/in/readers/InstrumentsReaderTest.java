package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInputTest;

/**
 * Test cases for the {@link InstrumentsReader}.
 * 
 * @author Andreas Wenger
 */
public class InstrumentsReaderTest {

	/**
	 * Read the file "InstrumentChanges.xml".
	 * It must contain 3 instruments, namely a Clarinet in B, an Alto Sax in Eb
	 * and a Trombone in C. Check also the transpositons and see if the instrument
	 * changes happen at the right positions.
	 */
	@Test public void testInstrumentChanges() {
		Score score = MusicXmlScoreFileInputTest.loadXMLTestScore("InstrumentChanges.xml");
		Part part = score.getStavesList().getParts().get(0);
		assertEquals(3, part.getInstruments().size());

		//clarinet
		PitchedInstrument instr0 = (PitchedInstrument) part.getInstruments().get(0);
		assertEquals("Clarinet in Bb", instr0.getName());
		assertEquals(new Integer(-1), instr0.getTranspose().getDiatonic());
		assertEquals(-2, instr0.getTranspose().getChromatic());
		//altosax
		PitchedInstrument instr1 = (PitchedInstrument) part.getInstruments().get(1);
		assertEquals("Alto Saxophone", instr1.getName());
		assertEquals(new Integer(-5), instr1.getTranspose().getDiatonic());
		assertEquals(-9, instr1.getTranspose().getChromatic());
		//trombone
		PitchedInstrument instr2 = (PitchedInstrument) part.getInstruments().get(2);
		assertEquals("Trombone", instr2.getName());
		assertEquals(new Integer(0), instr2.getTranspose().getDiatonic());
		assertEquals(0, instr2.getTranspose().getChromatic());

		//instrument changes in measures 1, 2 and 3
		Measure measure = score.getMeasure(atMeasure(0, 1));
		assertEquals(instr1, getInstrumentChangeAtBeat0(measure).getInstrument());
		measure = score.getMeasure(atMeasure(0, 2));
		assertEquals(instr2, getInstrumentChangeAtBeat0(measure).getInstrument());
		measure = score.getMeasure(atMeasure(0, 3));
		assertEquals(instr0, getInstrumentChangeAtBeat0(measure).getInstrument());
	}

	private static InstrumentChange getInstrumentChangeAtBeat0(Measure measure) {
		for (BeatE<InstrumentChange> ic : measure.getInstrumentChanges()) {
			if (ic.beat.equals(Companion.get_0()))
				return ic.element;
		}
		fail("Instrument change not found");
		return null;
	}

}
