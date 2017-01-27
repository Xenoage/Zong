package com.xenoage.zong.io.midi.out;

import com.xenoage.zong.core.music.Pitch;
import org.junit.Test;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.Pitch.pi;
import static org.junit.Assert.*;


/**
 * Test cases for a Pitch class.
 *
 * @author Andreas Wenger
 */
public class MidiToolsTest {

	private int[] midi;
	private Pitch[] pitch;

	public MidiToolsTest() {
		midi = new int[5];
		pitch = new Pitch[5];
		//C8
		midi[0] = 108;
		pitch[0] = pi(0, 0, 8);
		//F#6
		midi[1] = 90;
		pitch[1] = pi(3, 1, 6);
		//C4
		midi[2] = 60;
		pitch[2] = pi(0, 0, 4);
		//Eb2
		midi[3] = 39;
		pitch[3] = pi(1, 1, 2);
		//A0
		midi[4] = 21;
		pitch[4] = pi(5, 0, 0);
	}


	@Test public void getPitchFromNoteNumberTest() {
		for (int i : range(midi)) {
			Pitch p = MidiTools.getPitchFromNoteNumber(midi[i]);
			assertEquals(pitch[i], p);
		}
	}

	@Test public void getNoteNumberFromPitchTest() {
		for (int i : range(midi)) {
			assertEquals(midi[i], MidiTools.getNoteNumber(pitch[i]));
		}
	}

}
