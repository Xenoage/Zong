package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.text.UnformattedText.ut;

import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;


public interface Base61c
	extends Base {

	@Override default String getFileName() {
		return "61c-Lyrics-Pianostaff.xml";
	}
	
	Lyric[][][] expectedLyrics = {
		{
			{ new Lyric(ut("tra"), SyllableType.Begin, 0) },
			{ new Lyric(ut("la"), SyllableType.Middle, 0) },
			{ new Lyric(ut("li"), SyllableType.End, 0) },
			{ new Lyric(ut("ja!"), SyllableType.Single, 0) },
		},
		{
			{ new Lyric(ut("TRA"), SyllableType.Begin, 0) },
			{ new Lyric(ut("LA"), SyllableType.Middle, 0) },
			{ new Lyric(ut("LI"), SyllableType.End, 0) },
			{ new Lyric(ut("JA!"), SyllableType.Single, 0) },
		}};

}
