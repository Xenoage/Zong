package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChordsUnchecked;
import static musicxmltestsuite.tests.utils.Utils.ch;
import static musicxmltestsuite.tests.utils.Utils.gr;

import com.xenoage.zong.commands.core.music.beam.BeamAdd;
import com.xenoage.zong.core.music.chord.Chord;


public interface Base24c
	extends Base {

	@Override default String getFileName() {
		return "24c-GraceNote-MeasureEnd.xml";
	}
	
	default Chord[] getExpectedChords() {
		Chord[] ret = {
			ch(Companion.fr(2, 4), Companion.pi('E', 0, 5)),
			ch(Companion.fr(2, 4), Companion.pi('E', 0, 5)),
			gr(Companion.fr(1, 16), false, Companion.pi('G', 0, 5)),
			gr(Companion.fr(1, 16), false, Companion.pi('A', 0, 5))};
		new BeamAdd(Companion.beamFromChordsUnchecked(alist(ret[2], ret[3]))).execute();
		return ret;
	}

}
