package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.text.UnformattedText.ut;

import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;


public interface Base61f
	extends Base {

	@Override default String getFileName() {
		return "61f-Lyrics-GracedNotes.xml";
	}
	
	Lyric[][] expectedLyrics = {
		{ new Lyric(ut("Ly"), SyllableType.Begin, 0) },
		{ },
		{ new Lyric(ut("rics"), SyllableType.End, 0) },
		{ new Lyric(ut("on"), SyllableType.Single, 0) },
		{ new Lyric(ut("notes"), SyllableType.Single, 0) },
		{ },
		{ new Lyric(ut("with"), SyllableType.Single, 0) },
		{ new Lyric(ut("graces"), SyllableType.Single, 0) },
	};

}
