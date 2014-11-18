package com.xenoage.zong.core.music;

import static com.xenoage.utils.collections.ArrayUtils.indexOf;
import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.util.FirstOrLast.First;
import static com.xenoage.zong.core.music.util.FirstOrLast.Last;
import static com.xenoage.zong.core.music.util.Interval.After;
import static com.xenoage.zong.core.music.util.Interval.At;
import static com.xenoage.zong.core.music.util.Interval.AtOrAfter;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.music.util.StartOrStop.Start;
import static com.xenoage.zong.core.music.util.StartOrStop.Stop;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.FirstOrLast;
import com.xenoage.zong.core.music.util.IndexE;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.music.util.StartOrStop;


/**
 * Test cases for {@link Voice}.
 * 
 * @author Andreas Wenger
 */
public class VoiceTest {


	@Test public void getElementTest() {
		//our test example: (g: grace note)
		//Beats:       0        1     2           3     4
		//Elements     |g1|-----a-----|g2|g3|--b--|--c--|g4|
		//Checked:     x  x           x  x  x     x     x  x
		Rest n = null, a = new Rest(fr(2)), b = new Rest(fr(1)), c = new Rest(fr(1));
		Chord g1 = grace(1), g2 = grace(2), g3 = grace(3), g4 = grace(4);
		VoiceElement[] elementsPool = { a, b, c, g1, g2, g3, g4 };
		Voice voice = new Voice(ilist(g1, a, g2, g3, b, c, g4));
		//expected solutions
		int sideCount = FirstOrLast.values().length;
		int borderCount = StartOrStop.values().length;
		int inCount = Interval.values().length;
		VoiceElement[][][][] expected = new VoiceElement[sideCount][borderCount][inCount][];
		//first/start
		expected[o(First)][o(Start)][o(Before)] = new VoiceElement[] { n, g1, g1, g1, g1, g1, g1, g1 };
		expected[o(First)][o(Start)][o(BeforeOrAt)] = new VoiceElement[] { g1, g1, g1, g1, g1, g1, g1, g1 };
		expected[o(First)][o(Start)][o(At)] = new VoiceElement[] { g1, a, g2, g3, b, c, g4, n };
		expected[o(First)][o(Start)][o(AtOrAfter)] = new VoiceElement[] { g1, a, g2, g3, b, c, g4, n };
		expected[o(First)][o(Start)][o(After)] = new VoiceElement[] { a, g2, g3, b, c, g4, n, n };
		//first/stop
		expected[o(First)][o(Stop)][o(Before)] = new VoiceElement[] { n, n, g1, g1, g1, g1, g1, g1 };
		expected[o(First)][o(Stop)][o(BeforeOrAt)] = new VoiceElement[] { n, g1, g1, g1, g1, g1, g1, g1 };
		expected[o(First)][o(Stop)][o(At)] = new VoiceElement[] { n, g1, a, g2, g3, b, c, g4 };
		expected[o(First)][o(Stop)][o(AtOrAfter)] = new VoiceElement[] { g1, g1, a, g2, g3, b, c, g4 };
		expected[o(First)][o(Stop)][o(After)] = new VoiceElement[] { g1, a, g2, g3, b, c, g4, n };
		//last/start
		expected[o(Last)][o(Start)][o(Before)] = new VoiceElement[] { n, g1, a, g2, g3, b, c, g4 };
		expected[o(Last)][o(Start)][o(BeforeOrAt)] = new VoiceElement[] { g1, a, g2, g3, b, c, g4, g4 };
		expected[o(Last)][o(Start)][o(At)] = new VoiceElement[] { g1, a, g2, g3, b, c, g4, n };
		expected[o(Last)][o(Start)][o(AtOrAfter)] = new VoiceElement[] { g4, g4, g4, g4, g4, g4, g4, n };
		expected[o(Last)][o(Start)][o(After)] = new VoiceElement[] { g4, g4, g4, g4, g4, g4, n, n };
		//last/stop
		expected[o(Last)][o(Stop)][o(Before)] = new VoiceElement[] { n, n, g1, a, g2, g3, b, c };
		expected[o(Last)][o(Stop)][o(BeforeOrAt)] = new VoiceElement[] { n, g1, a, g2, g3, b, c, g4 };
		expected[o(Last)][o(Stop)][o(At)] = new VoiceElement[] { n, g1, a, g2, g3, b, c, g4 };
		expected[o(Last)][o(Stop)][o(AtOrAfter)] = new VoiceElement[] { g4, g4, g4, g4, g4, g4, g4, g4 };
		expected[o(Last)][o(Stop)][o(After)] = new VoiceElement[] { g4, g4, g4, g4, g4, g4, g4, n };
		//test
		for (int iSide = 0; iSide < sideCount; iSide++) {
			for (int iBorder = 0; iBorder < borderCount; iBorder++) {
				for (int iIn = 0; iIn < inCount; iIn++) {
					assertGetElement(voice, elementsPool, FirstOrLast.values()[iSide], StartOrStop.values()[iBorder],
						Interval.values()[iIn], expected[iSide][iBorder][iIn]);
				}
			}
		}
	}


	private void assertGetElement(Voice voice, VoiceElement[] elementsPool, FirstOrLast side, StartOrStop border, Interval in,
		VoiceElement[] expected) {
		for (int i : range(voice.getElements().size() + 1)) {
			//DEBUG
			/*
			side = First;
			border = Start;
			in = Before;
			//*/

			IndexE<VoiceElement> p = voice.getElement(side, border, in, i);
			VoiceElement pe = (p != null ? p.element : null);
			Assert.assertEquals("Expected: " + getElementName(indexOf(elementsPool, expected[i])) + ", but was " +
				getElementName(indexOf(elementsPool, pe)) + " for " + side + "/" + border + "/" + in + " at index " + i + ".",
				expected[i], pe);
		}
	}


	@Test public void getElementIndexTest() {
		//our test example: (g: grace note)
		//Beats:       0        1     2           3     4    5
		//Elements     |g1|-----a-----|g2|g3|--b--|--c--|g4|
		//Indices      0  1           2  3  4     5     6  7
		//Checked:     x        x     x           x     x    x
		Rest a = new Rest(fr(2)), b = new Rest(fr(1)), c = new Rest(fr(1));
		Chord g1 = grace(1), g2 = grace(2), g3 = grace(3), g4 = grace(4);
		Voice voice = new Voice(ilist(g1, a, g2, g3, b, c, g4));
		assertEquals(1, voice.getElementIndex(fr(0)));
		assertEquals(1, voice.getElementIndex(fr(1)));
		assertEquals(4, voice.getElementIndex(fr(2)));
		assertEquals(5, voice.getElementIndex(fr(3)));
		assertEquals(7, voice.getElementIndex(fr(4)));
		assertEquals(7, voice.getElementIndex(fr(5)));
	}


	private int o(FirstOrLast e) {
		return e.ordinal();
	}


	private int o(StartOrStop e) {
		return e.ordinal();
	}


	private int o(Interval e) {
		return e.ordinal();
	}


	private String getElementName(int index) {
		switch (index) {
			case 0:
				return "a";
			case 1:
				return "b";
			case 2:
				return "c";
			case 3:
				return "g1";
			case 4:
				return "g2";
			case 5:
				return "g3";
			case 6:
				return "g4";
		}
		return "n";
	}
	
	@Test public void getElementAtTest() {
		//our test example: (g: grace note)
		//Beats:       0        1     2           3     4
		//Elements     |g1|-----a-----|g2|g3|--b--|--c--|g4|
		//Checked:     x  x           x  x  x     x     x  x
		Rest a = new Rest(fr(2)), b = new Rest(fr(1)), c = new Rest(fr(1));
		Chord g1 = grace(1), g2 = grace(2), g3 = grace(3), g4 = grace(4);
		Voice voice = new Voice(ilist(g1, a, g2, g3, b, c, g4));
		//test
		assertEquals(a, voice.getElementAt(fr(0)));
		assertEquals(null, voice.getElementAt(fr(1)));
		assertEquals(b, voice.getElementAt(fr(2)));
		assertEquals(c, voice.getElementAt(fr(3)));
		assertEquals(g4, voice.getElementAt(fr(4)));
	}

	public static Chord grace(int step) {
		Chord chord = new Chord(new Note(pi(step, 0)), fr(0, 4));
		chord.setGrace(new Grace(true, fr(1, 16)));
		return chord;
	}


}
