package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChordsUnchecked;
import static musicxmltestsuite.tests.utils.Utils.ch;
import static musicxmltestsuite.tests.utils.Utils.gr;
import musicxmltestsuite.tests.utils.ToDo;

import com.xenoage.zong.commands.core.music.beam.BeamAdd;
import com.xenoage.zong.core.music.chord.Chord;


@ToDo("concept of steal-time-previous and steal-time-following is not implemented yet")
public interface Base24d
	extends Base {

	@Override default String getFileName() {
		return "24d-AfterGrace.xml";
	}
	
	default Chord[] getExpectedChords() {
		Chord[] ret = {
			ch(Companion.fr(2, 4), pi('E', 0, 5)),
			gr(Companion.fr(1, 16), false, pi('G', 0, 5)),
			gr(Companion.fr(1, 16), false, pi('A', 0, 5)),
			gr(Companion.fr(1, 16), false, pi('A', 0, 5)),
			ch(Companion.fr(2, 4), pi('E', 0, 5)),
			gr(Companion.fr(1, 16), false, pi('G', 0, 5)),
			gr(Companion.fr(1, 16), false, pi('A', 0, 5))};
		new BeamAdd(Companion.beamFromChordsUnchecked(alist(ret[4], ret[5]))).execute();
		return ret;
	}

}
