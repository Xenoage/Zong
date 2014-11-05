package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.mp0;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.text.UnformattedText;
import com.xenoage.zong.io.selection.Cursor;


public interface Base42a
	extends Base {

	@Override default String getFileName() {
		return "42a-MultiVoice-TwoVoicesOnStaff-Lyrics.xml";
	}
	
	Staff expectedStaff = getExpectedStaff();
	
	/**
	 * Gets the expected musical data, but without the dynamics, fermatas, articulations and slurs.
	 */
	static Staff getExpectedStaff() {
		Score score = ScoreFactory.create1Staff();
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new Time(TimeType.time_4_4));
		//measure 0, voice 0
		cursor.write(addLyric(chord(pi('E', 0, 5), fr(1, 2)), "This"));
		cursor.write(addLyric(chord(pi('D', 0, 5), fr(1, 4)), "is"));
		cursor.write(addLyric(chord(pi('B', 0, 4), fr(1, 4)), "the"));
		//measure 1, voice 0
		cursor.write(new Rest(fr(1, 4)));
		cursor.write(addLyric(chord(pi('D', 0, 5), fr(1, 4)), "lyrics"));
		cursor.write(addLyric(chord(pi('B', 0, 3), fr(3, 8)), "of"));
		cursor.write(addLyric(chord(pi('C', 0, 5), fr(1, 8)), "Voice1"));
		//measure 0, voice 1
		cursor.setMP(atElement(0, 0, 1, 0));
		cursor.write(addLyric(chord(pi('C', 0, 5), fr(1, 2)), "This"));
		cursor.write(addLyric(chord(pi('B', 0, 4), fr(1, 4)), "is"));
		cursor.write(addLyric(chord(pi('G', 0, 4), fr(1, 4)), "the"));
		//measure 1, voice 1
		cursor.write(new Rest(fr(1, 4)));
		cursor.write(addLyric(chord(pi('B', 0, 4), fr(1, 4)), "lyrics"));
		cursor.write(addLyric(chord(pi('G', 0, 3), fr(3, 8)), "of"));
		cursor.write(addLyric(chord(pi('A', 0, 4), fr(1, 8)), "Voice2"));
		return score.getStaff(0);
	}
	
	static Chord addLyric(Chord chord, String text) {
		chord.setLyrics(alist(new Lyric(new UnformattedText(text), SyllableType.Single, 0)));
		return chord;
	}

}
