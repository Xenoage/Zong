package com.xenoage.zong.musicxml.testsuite;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musicxml.testsuite.Utils.ch;
import static com.xenoage.zong.musicxml.testsuite.Utils.gr;
import lombok.Getter;

import com.xenoage.zong.core.music.chord.Chord;

/**
 * Chords as grace notes.
 * 
 * @author Andreas Wenger
 */
public class T24b
	extends TestSuiteTest {
	
	@Getter private final String file = "24b-ChordAsGraceNote.xml";
	
	public Chord[] getExpectedChords() {
		return new Chord[] {
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 16), true, pi('D', 0, 5), pi('F', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 16), true, pi('B', 0, 4), pi('D', 0, 5)),
			ch(fr(1, 4), pi('A', 0, 4), pi('C', 0, 5))};
	}

}
