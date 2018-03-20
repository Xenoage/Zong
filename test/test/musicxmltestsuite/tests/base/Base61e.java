package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.text.UnformattedText.ut;

import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;


public interface Base61e
	extends Base {

	@Override default String getFileName() {
		return "61e-Lyrics-Chords.xml";
	}
	
	int[] expectedChordNotesCount = { 2, 2, 2, 2 };
	
	Lyric[][] expectedLyrics = {
		{ new Lyric(Companion.ut("Ly"), SyllableType.Begin, 0) },
		{ new Lyric(Companion.ut("rics"), SyllableType.End, 0) },
		{ new Lyric(Companion.ut("on"), SyllableType.Single, 0) },
		{ new Lyric(Companion.ut("chords"), SyllableType.Single, 0) },
	};

}
