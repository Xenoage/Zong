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
		ch(Companion.fr(3, 8), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4), Companion.pi('C', 0, 5)),
		ch(Companion.fr(1, 8), Companion.pi('A', 0, 4), Companion.pi('G', 0, 5)),
		ch(Companion.fr(1, 4), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4), Companion.pi('C', 0, 5)),
		ch(Companion.fr(1, 4), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4), Companion.pi('C', 0, 5)),
		ch(Companion.fr(1, 4), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4), Companion.pi('E', 0, 5)),
		ch(Companion.fr(1, 4), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4), Companion.pi('F', 0, 5)),
		ch(Companion.fr(1, 2), Companion.pi('F', 0, 4), Companion.pi('A', 0, 4), Companion.pi('D', 0, 5))
	};
}
