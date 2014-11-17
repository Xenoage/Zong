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
			new Lyric(ut("1.Tra"), SyllableType.Begin, 0),
			new Lyric(ut("2.tra"), SyllableType.Begin, 1),
			new Lyric(ut("3.TRA"), SyllableType.Begin, 2),
		},
		{
			new Lyric(ut("la"), SyllableType.Middle, 0),
			new Lyric(ut("la"), SyllableType.Middle, 1),
			new Lyric(ut("LA"), SyllableType.Middle, 2),
		},
		{
			new Lyric(ut("la,"), SyllableType.End, 0),
			new Lyric(ut("la,"), SyllableType.End, 1),
			new Lyric(ut("LA,"), SyllableType.End, 2),
		},
		{
			new Lyric(ut("ja!"), SyllableType.Single, 0),
			new Lyric(ut("ja!"), SyllableType.Single, 1),
			new Lyric(ut("JA!"), SyllableType.Single, 2),
		},
		{	},
		{
			new Lyric(ut("Tra"), SyllableType.Begin, 0),
			new Lyric(ut("Tra"), SyllableType.Begin, 1),
			new Lyric(ut("TRA"), SyllableType.Begin, 2),
		},
		{ },
		{
			new Lyric(ut("ra..."), SyllableType.End, 0),
			new Lyric(ut("ra."), SyllableType.End, 1),
			new Lyric(ut("RA..."), SyllableType.End, 2),
		},
	};

}
