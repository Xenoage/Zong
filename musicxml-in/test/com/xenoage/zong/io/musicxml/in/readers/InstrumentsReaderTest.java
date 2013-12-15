package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.BMP.atMeasure;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;


/**
 * Test cases for the {@link InstrumentsReader}.
 * 
 * @author Andreas Wenger
 */
public class InstrumentsReaderTest
{
	
	/**
	 * Read the file "InstrumentChanges.xml".
	 * It must contain 3 instruments, namely a Clarinet in B, an Alto Sax in Eb
	 * and a Trombone in C. Check also the transpositons and see if the instrument
	 * changes happen at the right positions.
	 */
	@Test public void testInstrumentChanges()
	{
		Score score = MusicXMLScoreFileInputTest.loadXMLTestScore("InstrumentChanges.xml");
		Part part = score.stavesList.parts.getFirst();
		assertEquals(3, part.getInstruments().size());
		
		//clarinet
		PitchedInstrument instr0 = (PitchedInstrument) part.getInstruments().get(0);
		assertEquals("Clarinet in Bb", instr0.base.name);
		assertEquals(new Integer(-1), instr0.transpose.diatonic);
		assertEquals(-2, instr0.transpose.chromatic);
		//altosax
		PitchedInstrument instr1 = (PitchedInstrument) part.getInstruments().get(1);
		assertEquals("Alto Saxophone", instr1.base.name);
		assertEquals(new Integer(-5), instr1.transpose.diatonic);
		assertEquals(-9, instr1.transpose.chromatic);
		//trombone
		PitchedInstrument instr2 = (PitchedInstrument) part.getInstruments().get(2);
		assertEquals("Trombone", instr2.base.name);
		assertEquals(new Integer(0), instr2.transpose.diatonic);
		assertEquals(0, instr2.transpose.chromatic);
		
		//instrument changes in measures 1, 2 and 3
		Measure measure = score.getMeasure(atMeasure(0, 1));
		assertEquals(instr1, getInstrumentChangeAtBeat0(measure).getInstrument());
		measure = score.getMeasure(atMeasure(0, 2));
		assertEquals(instr2, getInstrumentChangeAtBeat0(measure).getInstrument());
		measure = score.getMeasure(atMeasure(0, 3));
		assertEquals(instr0, getInstrumentChangeAtBeat0(measure).getInstrument());
	}
	
	
	private static InstrumentChange getInstrumentChangeAtBeat0(Measure measure)
	{
		for (BeatE<InstrumentChange> ic : measure.instrumentChanges)
		{
			if (ic.beat.equals(_0))
				return ic.element;
		}
		fail("Instrument change not found");
		return null;
	}
	

}
