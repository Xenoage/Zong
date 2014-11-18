package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.text.UnformattedText.ut;

import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;


public interface Base61i
	extends Base {

	@Override default String getFileName() {
		return "61i-Lyrics-Chords.xml";
	}
	
	int[] expectedChordNotesCount = { 3 };
	
	Lyric[][] expectedLyrics = {
		{
			new Lyric(ut("Lyrics 1"), SyllableType.Single, 0),
			new Lyric(ut("Lyrics 2"), SyllableType.Single, 1),
			new Lyric(ut("Lyrics 3"), SyllableType.Single, 2),
		},
	};

}
