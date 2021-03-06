package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChordsUnchecked;
import static musicxmltestsuite.tests.utils.Utils.ch;
import static musicxmltestsuite.tests.utils.Utils.gr;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.beam.BeamAdd;
import com.xenoage.zong.core.music.chord.Chord;


public interface Base24e
	extends Base {

	@Override default String getFileName() {
		return "24e-GraceNote-StaffChange.xml";
	}
	
	Chord[][] expectedStavesChords = getExpectedStavesChords();
	
	static Chord[][] getExpectedStavesChords() {
		Chord[][] ret = {
			{ //staff 0
				ch(Companion.fr(2, 4), Companion.pi('E', 0, 5)),
				ch(Companion.fr(2, 4), Companion.pi('E', 0, 5))
			},
			{ //staff 1
				gr(Companion.fr(1, 16), false, Companion.pi('G', 0, 5)),
				gr(Companion.fr(1, 16), false, Companion.pi('A', 0, 5))
			}};
		new BeamAdd(Companion.beamFromChordsUnchecked(alist(ret[0][0], ret[0][1]))).execute();
		return ret;
	}
	
	Fraction[][] expectedStavesBeats = {
		{Companion.get_0(), Companion.fr(2, 4) }, //staff 0
		{ Companion.fr(2, 4), Companion.fr(2, 4) }, //staff 1
	};

}
