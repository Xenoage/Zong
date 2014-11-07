package musicxmltestsuite.tests.base;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.core.position.MP.unknown;

import java.util.List;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.selection.Cursor;


public interface Base42b
	extends Base {

	@Override default String getFileName() {
		return "42b-MultiVoice-MidMeasureClefChange.xml";
	}
	
	Staff[] expectedStaves = getExpectedStaves();
	List<Tuple2<MP, Clef>> expectedClefs = getExpectedClefs();
	
	/**
	 * Gets the expected musical data, but without the dynamics, articulations and slurs
	 * and also without the clef changes.
	 */
	static Staff[] getExpectedStaves() {
		Score score = ScoreFactory.create1Staff();
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new Time(TimeType.time_6_8));
		//staff 1, measure 0
		cursor.write(chord(pi('F', 0, 4), fr(1, 8)));
		cursor.write(chord(pi('D', 0, 4), fr(1, 8)));
		cursor.write(chord(pi('B', 0, 3), fr(1, 8)));
		cursor.write(chord(pi('G', 0, 3), fr(1, 8)));
		cursor.write(chord(pi('F', 0, 3), fr(1, 4)));
		//staff 1, measure 1
		cursor.write(chord(pi('E', 0, 5), fr(1, 8)));
		cursor.write(chord(pi('C', 0, 5), fr(1, 8)));
		cursor.write(chord(pi('G', 0, 4), fr(1, 8)));
		cursor.write(chord(pi('G', 0, 4), fr(1, 8)));
		cursor.write(chord(pi('F', 0, 4), fr(1, 4)));
		//staff 1, measure 0
		cursor.setMP(atElement(1, 0, 0, 0));
		cursor.write(new Rest(fr(1, 8)));
		cursor.write(chord(pi('G', 0, 2), fr(1, 8)));
		cursor.write(chord(pi('G', 0, 2), fr(1, 8)));
		cursor.write(chord(pi('G', 0, 2), fr(1, 8)));
		cursor.write(chord(pi('A', 0, 2), fr(1, 16)));
		cursor.write(chord(pi('G', 0, 2), fr(1, 16)));
		cursor.write(chord(pi('F', 1, 2), fr(1, 16)));
		cursor.write(chord(pi('G', 0, 2), fr(1, 16)));
		//staff 1, measure 1
		cursor.write(chord(new Pitch[]{pi('C', 0, 3), pi('E', 0, 3), pi('G', 0, 3), pi('C', 0, 4)}, fr(1, 4)));
		cursor.write(new Rest(fr(1, 8)));
		cursor.write(new Rest(fr(1, 4)));
		cursor.write(chord(pi('G', 0, 3), fr(1, 8)));
		return new Staff[]{score.getStaff(0), score.getStaff(1)};
	}
	
	static List<Tuple2<MP, Clef>> getExpectedClefs() {
		List<Tuple2<MP, Clef>> clefs = alist();
		clefs.add(t(atBeat(0, 0, unknown, fr(0, 8)), new Clef(ClefType.clefTreble)));
		clefs.add(t(atBeat(0, 0, unknown, fr(3, 8)), new Clef(ClefType.clefBass)));
		clefs.add(t(atBeat(0, 1, unknown, fr(0, 8)), new Clef(ClefType.clefTreble)));
		clefs.add(t(atBeat(1, 0, unknown, fr(0, 8)), new Clef(ClefType.clefBass)));
		return clefs;
	}

}
