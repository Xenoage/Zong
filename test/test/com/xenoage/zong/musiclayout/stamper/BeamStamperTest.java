package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChords;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.Waypoint.HookLeft;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.Waypoint.HookRight;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.Waypoint.None;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.Waypoint.Start;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.Waypoint.Stop;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.musiclayout.stamper.BeamStamper;
import com.xenoage.zong.musiclayout.stamper.BeamStamper.Waypoint;

/**
 * Tests for {@link BeamStamper}.
 * 
 * The test examples are from Chlapik, page 45, rule 6.
 * 
 * @author Andreas Wenger
 */
public class BeamStamperTest {

	BeamStamper s = new BeamStamper();


	@Test public void getLevelsTest() {
		assertEquals(2, s.getLevels(exampleRow1Col1()));
		assertEquals(2, s.getLevels(exampleRow1Col2()));
		assertEquals(2, s.getLevels(exampleRow1Col3()));
		assertEquals(2, s.getLevels(exampleRow1Col4()));
		assertEquals(2, s.getLevels(exampleRow2Col1()));
		assertEquals(2, s.getLevels(exampleRow2Col2()));
		assertEquals(3, s.getLevels(exampleRow2Col3()));
		assertEquals(3, s.getLevels(exampleRow2Col4()));
		assertEquals(3, s.getLevels(exampleRow2Col5()));
		assertEquals(3, s.getLevels(exampleRow2Col6()));
		assertEquals(2, s.getLevels(exampleRow3Col2()));
		assertEquals(2, s.getLevels(exampleRow3Col4()));
		assertEquals(3, s.getLevels(exampleRow3Col6()));
	}

	@Test public void computeWaypointsTest() {
		Beam b;

		//example of row 1, column 1
		b = exampleRow1Col1();
		try {
			s.computeWaypoints(b, 0, null); //not working for 8th lines
			fail();
		} catch (IllegalArgumentException ex) {
			//ok
		}
		List<Waypoint> wp = s.computeWaypoints(b, 2, null);
		assertEqualsList(wp, None, None, None); //32th
		assertEqualsList(s.computeWaypoints(b, 1, wp), HookRight, None, HookLeft); //16th

		//example of row 1, column 2
		b = exampleRow1Col2();
		assertEqualsList(s.computeWaypoints(b, 1, null), HookRight, None, HookLeft); //16th

		//example of row 1, column 3
		b = exampleRow1Col3();
		assertEqualsList(s.computeWaypoints(b, 1, null), HookRight, None, None, HookLeft); //16th

		//example of row 1, column 4
		b = exampleRow1Col4();
		assertEqualsList(s.computeWaypoints(b, 1, null), HookRight, None, None, HookLeft); //16th

		//example of row 2, column 1
		b = exampleRow2Col1();
		assertEqualsList(s.computeWaypoints(b, 1, null), None, HookLeft); //16th

		//example of row 2, column 2
		b = exampleRow2Col2();
		assertEqualsList(s.computeWaypoints(b, 1, null), None, HookLeft); //16th

		//example of row 2, column 3
		b = exampleRow2Col3();
		wp = s.computeWaypoints(b, 2, null);
		assertEqualsList(wp, None, HookLeft, None, HookLeft); //32th
		assertEqualsList(s.computeWaypoints(b, 1, wp), Start, None, None, Stop); //16th

		//example of row 2, column 4
		b = exampleRow2Col4();
		wp = s.computeWaypoints(b, 2, null);
		assertEqualsList(wp, None, HookLeft, None, HookLeft); //32th
		assertEqualsList(s.computeWaypoints(b, 1, wp), Start, None, None, Stop); //16th

		//example of row 3, column 2
		b = exampleRow3Col2();
		assertEqualsList(s.computeWaypoints(b, 1, null), HookRight, None, HookLeft, None); //16th

		//example of row 3, column 4
		b = exampleRow3Col4();
		assertEqualsList(s.computeWaypoints(b, 1, null), None, HookRight, None); //16th

		//example of row 3, column 6
		b = exampleRow3Col6();
		wp = s.computeWaypoints(b, 2, null);
		assertEqualsList(wp, None, HookLeft, None, HookLeft); //32th
		assertEqualsList(s.computeWaypoints(b, 1, wp), None, HookLeft, Start, Stop); //16th

	}

	private Beam exampleRow1Col1() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(1, 16)));
		chords.add(chordC(fr(1, 8)));
		chords.add(chordC(fr(1, 16)));
		return beamFromChords(chords);
	}

	private Beam exampleRow1Col2() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordF(fr(1, 16)));
		chords.add(chordF(fr(1, 8)));
		chords.add(chordF(fr(1, 16)));
		return beamFromChords(chords);
	}

	private Beam exampleRow1Col3() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(1, 16)));
		chords.add(chordC(fr(1, 8)));
		chords.add(chordC(fr(1, 8)));
		chords.add(chordC(fr(1, 16)));
		return beamFromChords(chords);
	}

	private Beam exampleRow1Col4() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordF(fr(1, 16)));
		chords.add(chordF(fr(1, 8)));
		chords.add(chordF(fr(1, 8)));
		chords.add(chordF(fr(1, 16)));
		return beamFromChords(chords);
	}

	private Beam exampleRow2Col1() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(3, 16)));
		chords.add(chordC(fr(1, 16)));
		return beamFromChords(chords);
	}

	private Beam exampleRow2Col2() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordF(fr(3, 16)));
		chords.add(chordF(fr(1, 16)));
		return beamFromChords(chords);
	}

	private Beam exampleRow2Col3() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(3, 32)));
		chords.add(chordC(fr(1, 32)));
		chords.add(chordC(fr(3, 32)));
		chords.add(chordC(fr(1, 32)));
		return beamFromChords(chords);
	}

	private Beam exampleRow2Col4() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordF(fr(3, 32)));
		chords.add(chordF(fr(1, 32)));
		chords.add(chordF(fr(3, 32)));
		chords.add(chordF(fr(1, 32)));
		return beamFromChords(chords);
	}

	private Beam exampleRow2Col5() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(1, 8)));
		chords.add(chordC(fr(1, 32)));
		chords.add(chordC(fr(3, 32)));
		return beamFromChords(chords);
	}

	private Beam exampleRow2Col6() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordF(fr(1, 8)));
		chords.add(chordF(fr(1, 32)));
		chords.add(chordF(fr(3, 32)));
		return beamFromChords(chords);
	}

	private Beam exampleRow3Col2() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(1, 16)));
		chords.add(chordC(fr(1, 8)));
		chords.add(chordC(fr(1, 16)));
		chords.add(chordC(fr(1, 8)));
		return beamFromChords(chords);
	}

	private Beam exampleRow3Col4() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(1, 8)));
		chords.add(chordC(fr(1, 16)));
		chords.add(chordC(fr(3, 16)));
		return beamFromChords(chords);
	}

	private Beam exampleRow3Col6() {
		LinkedList<Chord> chords = new LinkedList<Chord>();
		chords.add(chordC(fr(7, 32)));
		chords.add(chordC(fr(1, 32)));
		chords.add(chordC(fr(3, 32)));
		chords.add(chordC(fr(1, 32)));
		return beamFromChords(chords);
	}

	private Chord chordC(Fraction duration) {
		return new Chord(new Note(pi(0, 0, 5)), duration);
	}

	private Chord chordF(Fraction duration) {
		return new Chord(new Note(pi(3, 0, 4)), duration);
	}

	private void assertEqualsList(List<Waypoint> list, Waypoint... expected) {
		for (int i = 0; i < expected.length; i++) {
			assertEquals("Fail at position " + i, expected[i], list.get(i));
		}
	}

}
