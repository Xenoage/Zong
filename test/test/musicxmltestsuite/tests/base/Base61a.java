package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.text.UnformattedText.ut;

import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;


public interface Base61a
	extends Base {

	@Override default String getFileName() {
		return "61a-Lyrics.xml";
	}
	
	Lyric[][] expectedLyrics = {
		{ new Lyric(ut("Tra"), SyllableType.Begin, 0) },
		{ new Lyric(ut("la"), SyllableType.Middle, 0) },
		{ new Lyric(ut("li"), SyllableType.End, 0) },
		{ new Lyric(ut("Ja!"), SyllableType.Single, 0) },
		{ },
		{ new Lyric(ut("Tra"), SyllableType.Begin, 0) },
		{ },
		{ new Lyric(ut("ra!"), SyllableType.End, 0) },
		{ },
		{ new Lyric(ut("Bah!"), SyllableType.Single, 0) },
		{ },
	};

}
