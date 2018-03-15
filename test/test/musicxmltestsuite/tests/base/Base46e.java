package musicxmltestsuite.tests.base;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.mp0;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.io.selection.Cursor;


public interface Base46e
	extends Base {

	@Override default String getFileName() {
		return "46e-PickupMeasure-SecondVoiceStartsLater.xml";
	}
	
	Staff expectedStaff = getExpectedStaff();
	Fraction[] expectedMeasureFilledBeats = { Companion.fr(1,4), Companion.fr(4,4) };

	static Staff getExpectedStaff() {
		Score score = ScoreFactory.create1Staff();
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new TimeSignature(TimeType.timeCommon));
		//measure 0, voice 0
		cursor.write(chord(Companion.pi('C', 0, 5), Companion.fr(1, 4)));
		//measure 1, voice 0
		cursor.setMp(atElement(0, 1, 0, 0));
		cursor.write(chord(Companion.pi('C', 0, 5), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi('A', 0, 4), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi('F', 0, 4), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi('C', 0, 5), Companion.fr(1, 4)));
		//measure 1, voice 1
		cursor.setMp(atElement(0, 1, 1, 0));
		cursor.write(new Rest(Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi('C', 0, 4), Companion.fr(1, 4)));
		return score.getStaff(0);
	}

}
