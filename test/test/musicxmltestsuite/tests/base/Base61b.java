package musicxmltestsuite.tests.base;

import static com.xenoage.zong.core.text.UnformattedText.ut;

import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;


public interface Base61b
	extends Base {

	@Override default String getFileName() {
		return "61b-MultipleLyrics.xml";
	}
	
	Lyric[][] expectedLyrics = {
		{
			new Lyric(Companion.ut("1.Tra"), SyllableType.Begin, 0),
			new Lyric(Companion.ut("2.tra"), SyllableType.Begin, 1),
			new Lyric(Companion.ut("3.TRA"), SyllableType.Begin, 2),
		},
		{
			new Lyric(Companion.ut("la"), SyllableType.Middle, 0),
			new Lyric(Companion.ut("la"), SyllableType.Middle, 1),
			new Lyric(Companion.ut("LA"), SyllableType.Middle, 2),
		},
		{
			new Lyric(Companion.ut("la,"), SyllableType.End, 0),
			new Lyric(Companion.ut("la,"), SyllableType.End, 1),
			new Lyric(Companion.ut("LA,"), SyllableType.End, 2),
		},
		{
			new Lyric(Companion.ut("ja!"), SyllableType.Single, 0),
			new Lyric(Companion.ut("ja!"), SyllableType.Single, 1),
			new Lyric(Companion.ut("JA!"), SyllableType.Single, 2),
		},
		{	},
		{
			new Lyric(Companion.ut("Tra"), SyllableType.Begin, 0),
			new Lyric(Companion.ut("Tra"), SyllableType.Begin, 1),
			new Lyric(Companion.ut("TRA"), SyllableType.Begin, 2),
		},
		{ },
		{
			new Lyric(Companion.ut("ra..."), SyllableType.End, 0),
			new Lyric(Companion.ut("ra."), SyllableType.End, 1),
			new Lyric(Companion.ut("RA..."), SyllableType.End, 2),
		},
	};

}
