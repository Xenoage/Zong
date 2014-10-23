package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static musicxmltestsuite.tests.utils.Utils.ch;

import com.xenoage.zong.core.music.chord.Chord;

/**
 * One simple chord consisting of two notes.
 * 
 * @author Andreas Wenger
 */
public interface Base21a
	extends Base {

	@Override default String getFileName() {
		return "21a-Chord-Basic.xml";
	}
	
	Chord expectedChord = ch(fr(1, 4), pi('F', 0, 4), pi('A', 0, 4));
	
}
