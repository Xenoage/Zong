package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChordsUnchecked;
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
			gr(Companion.fr(1, 16), false, Companion.pi('D', 0, 5)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
			b1s = gr(Companion.fr(1, 16), false, Companion.pi('E', 0, 5)),
			b1e = gr(Companion.fr(1, 16), false, Companion.pi('D', 0, 5)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
			gr(Companion.fr(1, 16), false, Companion.pi('D', 0, 5)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
			gr(Companion.fr(1, 8), false, Companion.pi('D', 0, 5)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
			//measure 1
			gr(Companion.fr(1, 16), true, Companion.pi('D', 0, 5)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
			b2s = gr(Companion.fr(1, 16), false, Companion.pi('E', 0, 5)),
			b2e = gr(Companion.fr(1, 16), false, Companion.pi('D', 0, 5)),
			ch(Companion.fr(2, 4), Companion.pi('C', 0, 5)),
			gr(Companion.fr(1, 16), true, Companion.pi('D', 0, 5)),
			ch(Companion.fr(1, 8), Companion.pi('C', 0, 5)),
			gr(Companion.fr(1, 16), true, Companion.pi('D', 0, 5)),
			ch(Companion.fr(1, 8), Companion.pi('C', 0, 5)),
			gr(Companion.fr(1, 16), false, Companion.pi('E', 0, 5)),
			//measure 2
			gr(Companion.fr(1, 16), false, Companion.pi('E', 0, 5)),
			ch(Companion.fr(1, 4), Companion.pi('F', 0, 4), Companion.pi('C', 0, 5)),
			gr(Companion.fr(1, 4), false, Companion.pi('D', 1, 5)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
			gr(Companion.fr(1, 4), false, Companion.pi('D', -1, 5)),
			gr(Companion.fr(1, 4), false, Companion.pi('A', -1, 4)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5)),
			ch(Companion.fr(1, 4), Companion.pi('C', 0, 5))
		};
		new BeamAdd(Companion.beamFromChordsUnchecked(alist(b1s, b1e))).execute();
		new BeamAdd(Companion.beamFromChordsUnchecked(alist(b2s, b2e))).execute();
		return expectedChords;
	}

}
