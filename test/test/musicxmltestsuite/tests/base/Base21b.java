package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static musicxmltestsuite.tests.utils.Utils.ch;

import com.xenoage.zong.core.music.chord.Chord;


public interface Base21b
	extends Base {

	@Override default String getFileName() {
		return "21b-Chords-TwoNotes.xml";
	}
	
	int expectedChordsCount = 8;
	Chord expectedChord = ch(Companion.fr(1, 4), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4));
	
}
