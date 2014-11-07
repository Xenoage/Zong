package musicxmltestsuite.tests.utils;

import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.assertEquals;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.lyric.Lyric;
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
			assertEqualsLyric(expectedChord.getLyrics().get(i), chord.getLyrics().get(i), chordMP, i);
	}
	
	private static void assertEqualsLyric(Lyric expectedLyric, Lyric lyric, MP chordMP, int lyricIndex) {
		String msg = ""+chordMP + ", lyric " + lyricIndex;
		assertEquals(msg, expectedLyric.getSyllableType(), lyric.getSyllableType());
		assertEquals(msg, expectedLyric.getText().toString(), lyric.getText().toString());
		assertEquals(msg, expectedLyric.getVerse(), lyric.getVerse());
	}

}
