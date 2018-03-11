package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static musicxmltestsuite.tests.utils.Utils.ch;

import com.xenoage.zong.core.music.chord.Chord;


public interface Base21c
	extends Base {

	@Override default String getFileName() {
		return "21c-Chords-ThreeNotesDuration.xml";
	}
	
	Chord[] expectedChords = {
		ch(Companion.fr(3, 8), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
		ch(Companion.fr(1, 8), pi('A', 0, 4), pi('G', 0, 5)),
		ch(Companion.fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
		ch(Companion.fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('C', 0, 5)),
		ch(Companion.fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('E', 0, 5)),
		ch(Companion.fr(1, 4), pi('F', 0, 4), pi('A', 0, 4), pi('F', 0, 5)),
		ch(Companion.fr(1, 2), pi('F', 0, 4), pi('A', 0, 4), pi('D', 0, 5))
	};
}
