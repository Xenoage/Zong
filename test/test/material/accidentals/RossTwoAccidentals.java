package material.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.alistFromLists;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notation.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextAccC5;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextAccD5;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextAccG4;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextC;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.cw;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.susLeft;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.susRight;


import java.util.List;

import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notator.chord.accidentals.TwoAccidentals;

import lombok.Getter;
import material.RossSuite;



/**
 * Example chords with {@link TwoAccidentals}, from
 * Ross p. 131 and 132.
 * 
 * @author Andreas Wenger
 */
public class RossTwoAccidentals
	implements RossSuite<Example> {
		
	//TODO: static import of material.accidentals.Example.example failes. javac bug? 2016-02-15
	
	
	/**
	 * Some chords with two notes and two accidentals. Ross, p. 131 f.
	 */
	List<Example> chords2Notes = alist(
		material.accidentals.Example.example("2 accs: F#4, F#5 (p. 131, F# instead of F(nat))",
			alist(pi(3, 1, 4), pi(3, 1, 5)),
			new NoteDisplacement[] { note(1), note(8) }, contextC,
			cw.sharp + cw.accToNoteGap, null),
		material.accidentals.Example.example("2 accs: G#4, B#5 (p. 131)",
			alist(pi(4, 1, 4), pi(6, 1, 5)),
			new NoteDisplacement[] { note(2), note(11) }, contextC,
			cw.sharp + cw.accToNoteGap, null),
		material.accidentals.Example.example("2 accs: Bb4, Ab5 (p. 131)",
			alist(pi(6, -1, 4), pi(5, -1, 5)),
			new NoteDisplacement[] { note(4), note(10) }, contextC,
			cw.flat + cw.accToNoteGap, null),
		material.accidentals.Example.example("2 accs: Ab4, Bb4 (p. 132)",
			alist(pi(5, -1, 4), pi(6, -1, 4)),
			new NoteDisplacement[] { note(3), note(4, cw.quarter, susRight) }, contextC,
			cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap,
			new float[]{0f, cw.flat + cw.accToAccGap}),
		material.accidentals.Example.example("2 accs: A#4, F#5 (p. 132)",
			alist(pi(5, 1, 4), pi(3, 1, 5)),
			new NoteDisplacement[] { note(3), note(8) }, contextC,
			cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap,
			new float[]{0f, cw.sharp + cw.accToAccGap}),
		material.accidentals.Example.example("2 accs: Db5, Eb5 (p. 132)",
			alist(pi(1, -1, 5), pi(2, -1, 5)),
			new NoteDisplacement[] { note(6, 0, susLeft), note(7, cw.quarter) }, contextC,
			cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap,
			new float[]{0f, cw.flat + cw.accToAccGap})
	);

	/**
	 * Some chords with three notes and two accidentals. Ross, p. 132.
	 */
	List<Example> chords3Notes = alist(
		material.accidentals.Example.example("2 accs: E4, G#4, C#5 (no accidental at bottom note)",
			alist(pi(2, 0, 4), pi(4, 1, 4), pi(0, 1, 5)),
			new NoteDisplacement[] { note(0), note(2), note(5) }, contextC,
			cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap,
			new float[]{0f, cw.sharp + cw.accToAccGap}),
		material.accidentals.Example.example("2 accs: Eb4, G(nat)4, C5 with contextAccG4 (no accidental at top note)",
			alist(pi(2, -1, 4), pi(4, 0, 4), pi(0, 0, 5)),
			new NoteDisplacement[] { note(0), note(2), note(5) }, contextAccG4,
			cw.flat + cw.accToAccGap + cw.natural + cw.accToNoteGap,
			new float[]{0f, cw.flat + cw.accToAccGap}),

		//TODO: Eb4-accidental can be placed nearer to the chord
		material.accidentals.Example.example("2 accs: Eb4, G4, C(nat)5 with contextAccC5 (no accidental at middle note)",
			alist(pi(2, -1, 4), pi(4, 0, 4), pi(0, 0, 5)),
			new NoteDisplacement[] { note(0), note(2), note(5) }, contextAccC5,
			cw.flat + cw.accToAccGap + cw.natural + cw.accToNoteGap,
			new float[]{0f, cw.flat + cw.accToAccGap}),

		//(no accidental at bottom note, middle note suspended)
		material.accidentals.Example.example("2 accs: F4, G#4, D(nat)5 with contextAccD5",
			alist(pi(3, 0, 4), pi(4, 1, 4), pi(1, 0, 5)),
			new NoteDisplacement[] { note(1), note(2, cw.quarter, susRight), note(6) }, contextAccD5,
			cw.natural + cw.accToAccGap + cw.sharp + cw.accToNoteGap,
			new float[]{ cw.natural + cw.accToAccGap, 0f}),
		//(no accidental at middle note, top note suspended)
		material.accidentals.Example.example("2 accs: F#4, C5, D#5",
			alist(pi(3, 1, 4), pi(0, 0, 5), pi(1, 1, 5)),
			new NoteDisplacement[] { note(1), note(5), note(6, cw.quarter, susRight) }, contextC,
			cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap,
			new float[]{0f, cw.sharp + cw.accToAccGap}),

		//(no accidental at top note, middle note suspended)
		//TODO: accidentals nearer to chord (Ab4-accidental
		//has enough room under Eb5)
		material.accidentals.Example.example("2 accs: Ab4, Eb5, F5",
			alist(pi(5, -1, 4), pi(2, -1, 5), pi(3, 0, 5)),
			new NoteDisplacement[] { note(3, cw.quarter), note(7, 0, susLeft),
				note(8, cw.quarter) }, contextC,
			cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap,
			new float[]{cw.flat + cw.accToAccGap, 0f})
	);
	
	@Getter List<Example> examples = alistFromLists(chords2Notes, chords3Notes);
		
}
