package material.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notation.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextC;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextEb;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.cw;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.noteOffset;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.susRight;

import java.util.List;

import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;

import lombok.Getter;
import material.ZongSuite;


/**
 * Example chords with {@link OneAccidental}.
 * 
 * @author Andreas Wenger
 */
public class OneAccidental
	implements ZongSuite<Example> {
	
	
	@Getter List<Example> examples = alist(
		//TODO: static import of material.accidentals.Example.example failes. javac bug? 2016-02-15
		material.accidentals.Example.example("1 acc: C#5",
			alist(Companion.pi(0, 1, 5)),
			new NoteDisplacement[] { note(5) }, contextC,
			cw.sharp + cw.accToNoteGap, null),
		material.accidentals.Example.example("1 acc: C##5",
			alist(Companion.pi(0, 2, 5)),
			new NoteDisplacement[] { note(5) }, contextC,
			cw.doubleSharp + cw.accToNoteGap, null),
		material.accidentals.Example.example("1 acc: C4, D4, Gbb4",
			alist(Companion.pi(0, 0, 4), Companion.pi(1, 0, 4), Companion.pi(4, -2, 4)),
			new NoteDisplacement[] { note(-2), note(-1, noteOffset, susRight), note(2) }, contextC,
			cw.doubleFlat + cw.accToNoteGap, null),
		material.accidentals.Example.example("1 acc: Eb4, A4, G##5 with contextEb",
			alist(Companion.pi(2, -1, 4), Companion.pi(5, 0, 4), Companion.pi(4, 2, 5)),
			new NoteDisplacement[] { note(0), note(3), note(9) }, contextEb,
			cw.natural + cw.accToNoteGap, null)
	);

}
