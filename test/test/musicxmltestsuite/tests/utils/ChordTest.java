package musicxmltestsuite.tests.utils;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;


public class ChordTest {
	
	public static void assertEqualsChord(Chord expectedChord, Chord chord, MP chordMP) {
		assertEqualsChordNotes(expectedChord, chord, chordMP);
		assertEqualsChordLyrics(expectedChord, chord, chordMP);
	}
	
	private static void assertEqualsChordNotes(Chord expectedChord, Chord chord, MP chordMP) {
		assertEquals(""+chordMP, expectedChord.getNotes().size(), chord.getNotes().size());
		for (int i : range(expectedChord.getNotes()))
			assertEquals(""+chordMP + ", note " + i,
				expectedChord.getNotes().get(i), chord.getNotes().get(i));
	}
	
	private static void assertEqualsChordLyrics(Chord expectedChord, Chord chord, MP chordMP) {
		assertEquals(""+chordMP, expectedChord.getLyrics().size(), chord.getLyrics().size());
		for (int i : range(expectedChord.getLyrics()))
			assertEquals(""+chordMP + ", lyric " + i,
				expectedChord.getLyrics().get(i), chord.getLyrics().get(i));
	}
	
	public static Chord getChordAtBeat(Score score, MP mp) {
		VoiceElement e = score.getVoice(mp).getElementAt(mp.beat);
		if (false == e instanceof Chord)
			fail("No chord at " + mp);
		return (Chord) e;
	}

}
