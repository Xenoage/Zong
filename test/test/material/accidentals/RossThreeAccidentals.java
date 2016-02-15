package material.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.alistFromLists;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notation.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextAccB4;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextAccD4;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextAccsD4B4;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextC;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.cw;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.susRight;

import java.util.List;

import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notator.chord.accidentals.ThreeAccidentals;

import lombok.Getter;
import material.RossSuite;

/**
 * Example chords with {@link ThreeAccidentals}, from
 * Ross p. 132 and 133.
 * 
 * @author Andreas Wenger
 */
public class RossThreeAccidentals
	implements RossSuite<Example> {
		
	//TODO: static import of material.accidentals.Example.example failes. javac bug? 2016-02-15
	
	
	/**
	 * Ross, p. 132/133, rule 1.
	 */
	List<Example> chordsRule1 = alist(
		material.accidentals.Example.example("3 accs: D#4, F#4, C#5",
			alist(pi(1, 1, 4), pi(3, 1, 4), pi(0, 1, 5)),
			new NoteDisplacement[] { note(-1), note(1), note(5) }, contextC,
			cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap,
			new float[]{cw.sharp + cw.accToAccGap, 0f, cw.sharp + cw.accToAccGap})
	);

	/**
	 * Ross, p. 132/133, rule 2.
	 */
	List<Example> chordsRule2 = alist(
		material.accidentals.Example.example("3 accs: D#4, F#4, B4 with contextAccB4",
			alist(pi(1, 1, 4), pi(3, 1, 4), pi(6, 0, 4)),
			new NoteDisplacement[] { note(-1), note(1), note(4) }, contextAccB4,
			2 * (cw.sharp + cw.accToAccGap) + cw.natural + cw.accToNoteGap,
			new float[]{cw.sharp + cw.accToAccGap, 0f, 2 * (cw.sharp + cw.accToAccGap)})
	);

	/**
	 * Ross, p. 132/133, rule 3.
	 */
	List<Example> chordsRule3 = alist(
		material.accidentals.Example.example("3 accs: D#4, E#4, C#5",
			alist(pi(1, 1, 4), pi(2, 1, 4), pi(0, 1, 5)),
			new NoteDisplacement[] { note(-1), note(0, cw.quarter, susRight), note(5) }, contextC,
			2 * (cw.sharp + cw.accToAccGap) + cw.sharp + cw.accToNoteGap,
			new float[]{0f, 2 * (cw.sharp + cw.accToAccGap), cw.sharp + cw.accToAccGap})
	);

	/**
	 * Ross, p. 133, rule 4.
	 */
	List<Example> chordsRule4 = alist(
		material.accidentals.Example.example("3 accs: D#4, C#5, D#5",
			alist(pi(1, 1, 4), pi(0, 1, 5), pi(1, 1, 5)),
			new NoteDisplacement[] { note(-1), note(5), note(6, cw.quarter, susRight) }, contextC,
			cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap,
			new float[]{cw.sharp + cw.accToAccGap, 0f, cw.sharp + cw.accToAccGap})
	);

	/**
	 * Ross, p. 133, rule 5.
	 */
	List<Example> chordsRule5 = alist(
		//TODO: natural can be indented nearer to the chord
		material.accidentals.Example.example("3 accs: D4, Ab4, Bb4 with contextAccD4",
			alist(pi(1, 0, 4), pi(5, -1, 4), pi(6, -1, 4)),
			new NoteDisplacement[] { note(-1), note(3), note(4, cw.quarter, susRight) }, contextAccD4,
			cw.flat + cw.accToAccGap + cw.natural + cw.accToAccGap + cw.flat + cw.accToNoteGap,
			new float[]{cw.flat + cw.accToAccGap, 0f,
				cw.flat + cw.accToAccGap + cw.natural + cw.accToAccGap})
	);

	/**
	 * Ross, p. 133, rule 6.
	 */
	List<Example> chordsRule6 = alist(
		//TODO: natural can be indented nearer to the chord
		material.accidentals.Example.example("3 accs: D4, E#4, B4 with contextAccsD4B4",
			alist(pi(1, 0, 4), pi(2, 1, 4), pi(6, 0, 4)),
			new NoteDisplacement[] { note(-1), note(0, cw.quarter, susRight), note(4) }, contextAccsD4B4,
			cw.natural + cw.accToAccGap + cw.natural + cw.accToAccGap + cw.sharp + cw.accToNoteGap,
			new float[]{0f, 2 * (cw.natural + cw.accToAccGap), cw.natural + cw.accToAccGap})
	);

	/**
	 * Ross, p. 133.
	 */
	List<Example> chordsSuspendedLeft = alist( /*
		//TODO (ZONG-90): can not be solved with the current algorithm
		example("3 accs: Db5, Eb5, A#5",
			alist(pi(1, -1, 5), pi(2, -1, 5), pi(6, 1, 5)),
		  new NoteDisplacement[]{note(6, 0, susLeft), note(7, cw.quarter), note(10, cw.quarter)}, contextC,
		  2 * (cw.flat + cw.accToAccGap) + cw.sharp + cw.accToNoteGap,
		  new float[]{0f, cw.flat + cw.accToAccGap, 2 * (cw.flat + cw.accToAccGap)}),

		example("3 accs: A#4, B#4, A#5",
			alist(pi(5, 1, 4), pi(6, 1, 4), pi(6, 1, 5)),
		  new NoteDisplacement[]{note(3, 0, susLeft), note(4, cw.quarter), note(10, cw.quarter)}, contextC,
		  2 * (cw.sharp + cw.accToAccGap) + cw.sharp + cw.accToNoteGap,
		  new float[]{0f, cw.sharp + cw.accToAccGap, 2 * (cw.sharp + cw.accToAccGap)}),
		example("3 accs: C#5, G5, A5 with contextAccsG5A5",
			alist(pi(0, 1, 5), pi(4, 0, 5), pi(5, 0, 5)),
		  new NoteDisplacement[]{note(5, cw.quarter), note(9, 0, susLeft), note(10, cw.quarter)},
		  contextAccsG5A5,
		  2 * (cw.natural + cw.accToAccGap) + cw.sharp + cw.accToNoteGap,
		  new float[]{2 * (cw.natural + cw.accToAccGap), 0f, cw.natural + cw.accToAccGap})
	*/);
	
	@Getter List<Example> examples = alistFromLists(chordsRule1, chordsRule2, chordsRule3,
		chordsRule4, chordsRule5, chordsRule6, chordsSuspendedLeft);

}
