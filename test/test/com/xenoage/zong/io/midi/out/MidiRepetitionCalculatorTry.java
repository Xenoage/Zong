package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.music.time.TimeType.timeType;
import static com.xenoage.zong.core.position.MP.mp0;

import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.io.selection.Cursor;

/**
 * TODO: missing test?
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiRepetitionCalculatorTry {

	public static Score createRepetitionDemoScore1() {
		Score score = new Score();
		Part pianoPart = new Part("", null, 1, null);
		new PartAdd(score, pianoPart, 0, null).execute();

		//measure 0
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new Clef(ClefType.clefTreble));
		cursor.write((ColumnElement) new TraditionalKey(-3));
		cursor.write(new Time(timeType(3, 4)));

		cursor.write(chord(pi('C', 0, 4), fr(1, 4)));
		cursor.write(barlineForwardRepeat(BarlineStyle.HeavyLight));
		cursor.write(chord(pi('D', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('E', 0, 4), fr(1, 4)));

		//measure 1
		cursor.write(chord(pi('D', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('E', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('F', 0, 4), fr(1, 4)));
		cursor.getScore().getColumnHeader(1).setEndBarline(barlineBackwardRepeat(BarlineStyle.LightHeavy, 2));

		//measure 2
		cursor.getScore().getColumnHeader(2).setStartBarline(barlineForwardRepeat(BarlineStyle.HeavyLight));
		cursor.write(chord(pi('E', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('F', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('G', 0, 4), fr(1, 4)));

		//measure 3
		cursor.write(chord(pi('F', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('G', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('A', 0, 4), fr(1, 4)));

		//measure 4
		cursor.write(new Volta(1, range(1, 2), null, true));
		cursor.write(chord(pi('B', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('A', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('F', 0, 4), fr(1, 4)));
		cursor.getScore().getColumnHeader(4).setEndBarline(barlineBackwardRepeat(BarlineStyle.LightHeavy, 2));

		//measure 5
		cursor.write(new Volta(1, range(3, 3), null, true));
		cursor.write(chord(pi('E', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('D', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('C', 0, 4), fr(1, 4)));

		//measure 6
		cursor.write(new Volta(2, null, null, false));
		cursor.write(chord(pi('E', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('F', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('G', 0, 4), fr(1, 4)));

		//measure 7
		cursor.write(chord(pi('E', 0, 5), fr(1, 4)));
		cursor.write(chord(pi('F', 0, 5), fr(1, 4)));
		cursor.write(chord(pi('G', 0, 5), fr(1, 4)));

		//measure 8
		cursor.write(chord(pi('E', 0, 4), fr(1, 4)));
		cursor.write(chord(pi('F', 0, 5), fr(1, 4)));
		cursor.write(chord(pi('G', 0, 5), fr(1, 4)));

		return cursor.getScore();
	}

}
