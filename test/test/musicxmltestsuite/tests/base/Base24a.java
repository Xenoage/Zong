package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChords;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChordsNoCheck;
import static musicxmltestsuite.tests.utils.Utils.ch;
import static musicxmltestsuite.tests.utils.Utils.gr;

import com.xenoage.zong.commands.core.music.beam.BeamAdd;
import com.xenoage.zong.core.music.chord.Chord;


public interface Base24a
	extends Base {

	@Override default String getFileName() {
		return "24a-GraceNotes.xml";
	}
	
	Chord[] expectedChords = getExpectedChords();
	
	static Chord[] getExpectedChords() {
		//[b]eamed chords [s]tart and [e]nd
		Chord b1s, b1e, b2s, b2e;
		Chord[] expectedChords = new Chord[] {
			//measure 0
			gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			b1s = gr(fr(1, 16), false, pi('E', 0, 5)),
			b1e = gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 8), false, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			//measure 1
			gr(fr(1, 16), true, pi('D', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			b2s = gr(fr(1, 16), false, pi('E', 0, 5)),
			b2e = gr(fr(1, 16), false, pi('D', 0, 5)),
			ch(fr(2, 4), pi('C', 0, 5)),
			gr(fr(1, 16), true, pi('D', 0, 5)),
			ch(fr(1, 8), pi('C', 0, 5)),
			gr(fr(1, 16), true, pi('D', 0, 5)),
			ch(fr(1, 8), pi('C', 0, 5)),
			gr(fr(1, 16), false, pi('E', 0, 5)),
			//measure 2
			gr(fr(1, 16), false, pi('E', 0, 5)),
			ch(fr(1, 4), pi('F', 0, 4), pi('C', 0, 5)),
			gr(fr(1, 4), false, pi('D', 1, 5)),
			ch(fr(1, 4), pi('C', 0, 5)),
			gr(fr(1, 4), false, pi('D', -1, 5)),
			gr(fr(1, 4), false, pi('A', -1, 4)),
			ch(fr(1, 4), pi('C', 0, 5)),
			ch(fr(1, 4), pi('C', 0, 5))
		};
		new BeamAdd(beamFromChordsNoCheck(alist(b1s, b1e))).execute();
		new BeamAdd(beamFromChordsNoCheck(alist(b2s, b2e))).execute();
		return expectedChords;
	}

}
