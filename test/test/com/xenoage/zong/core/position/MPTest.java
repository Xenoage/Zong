package com.xenoage.zong.core.position;

import com.xenoage.zong.core.Score;
import org.junit.Test;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atElement;
import static org.junit.Assert.*;

/**
 * Tests for {@link Score}.
 *
 * @author Andreas Wenger
 */
public class MPTest {

	@Test public void compareTimeToTest() {
		int otherStaff = 1;
		int otherVoice = 1;
		//test: earlier, equal time, later
		for (int test : new int[]{-1, 0, 1}) {
			//equal time (with beats)
			String msg = "First MP should be " + new String[]{"earlier", "equal", "later"}[test+1];
			for (int m : range(0, 2)) { //measure
				for (int b : range(1, 2)) { //beat
					assertEquals(msg, test, atBeat(0, m, 0, Companion.fr(b + test, 4)).compareTimeTo(atBeat(0, m, 0, Companion.fr(b, 4))));
					assertEquals(msg, test, atBeat(otherStaff, m, 0, Companion.fr(b + test, 4)).compareTimeTo(atBeat(0, m, 0, Companion.fr(b, 4))));
					assertEquals(msg, test, atBeat(0, m, 0, Companion.fr(b + test, 4)).compareTimeTo(atBeat(0, m, otherVoice, Companion.fr(b, 4))));
				}
			}
			//equal time (with element index)
			for (int m : range(0, 2)) { //measure
				for (int i : range(1, 2)) { //element index
					assertEquals(msg, test, atElement(0, m, 0, i + test).compareTimeTo(atElement(0, m, 0, i)));
					assertEquals(msg, test, atElement(otherStaff, m, 0, i + test).compareTimeTo(atElement(0, m, 0, i)));
					assertEquals(msg, test, atElement(0, m, 0, i + test).compareTimeTo(atElement(0, m, otherVoice, i)));
				}
			}
		}
	}

}
