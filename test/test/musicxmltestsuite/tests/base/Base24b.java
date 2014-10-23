package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static musicxmltestsuite.tests.utils.Utils.ch;
import static musicxmltestsuite.tests.utils.Utils.gr;

import com.xenoage.zong.core.music.chord.Chord;

/**
 * Chords as grace notes. 
 * 
 * @author Andreas Wenger
 */
public interface Base24b
	extends Base {

	@Override default String getFileName() {
		return "24b-ChordAsGraceNote.xml";
	}
	
	Chord[] expectedChords = {
		ch(fr(1, 4), pi('C', 0, 5)),
		gr(fr(1, 8), true, pi('D', 0, 5), pi('F', 0, 5)),
		ch(fr(1, 4), pi('C', 0, 5)),
		gr(fr(1, 8), true, pi('B', 0, 4), pi('D', 0, 5)),
		ch(fr(1, 4), pi('A', 0, 4), pi('C', 0, 5))
	};

}
