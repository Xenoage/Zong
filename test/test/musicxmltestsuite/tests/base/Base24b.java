package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static musicxmltestsuite.tests.utils.Utils.ch;
import static musicxmltestsuite.tests.utils.Utils.gr;

import com.xenoage.zong.core.music.chord.Chord;


public interface Base24b
	extends Base {

	@Override default String getFileName() {
		return "24b-ChordAsGraceNote.xml";
	}
	
	Chord[] expectedChords = {
		ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
		gr(Companion.fr(1, 8), true, Companion.pi('D', 0, 5), Companion.pi('F', 0, 5)),
		ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
		gr(Companion.fr(1, 8), true, Companion.pi('B', 0, 4), Companion.pi('D', 0, 5)),
		ch(Companion.fr(1, 4), Companion.pi('A', 0, 4), Companion.pi('C', 0, 5))
	};

}
