package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.alistFromLists;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._1$4;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.position.MP.atElement;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.core.text.FormattedTextStyle.defaultStyle;
import static com.xenoage.zong.core.text.FormattedTextUtils.styleText;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.OneAccidental.oneAccidental;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.Strategy.getParams;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TestData.contextC;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.ThreeAccidentals.threeAccidentals;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TwoAccidentals.twoAccidentals;
import static com.xenoage.zong.musiclayout.settings.ChordWidths.defaultChordWidthsNormal;
import static material.ExampleResult.perfect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.FontStyle;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.ChordFactory;
import com.xenoage.zong.core.music.direction.Words;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.io.selection.Cursor;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.test.VisualTest;
import com.xenoage.zong.test.VisualTester;

import material.ExampleResult;
import material.ExampleResult.Result;
import material.accidentals.Example;
import material.accidentals.OneAccidental;
import material.accidentals.RossThreeAccidentals;
import material.accidentals.RossTwoAccidentals;


/**
 * Tests for {@link Strategy}.
 * 
 * @author Andreas Wenger
 */
public class StrategyTest
	implements VisualTest {
	
	private Strategy[] testees = { null, oneAccidental, twoAccidentals, threeAccidentals };
	
	private final ChordWidths cw = defaultChordWidthsNormal;
	
	
	private List<Example> getAllExamples() {
		return alistFromLists(new OneAccidental().getExamples(),
			new RossTwoAccidentals().getExamples(), new RossThreeAccidentals().getExamples());
	}
	
	
	@Test public void testStrategies() {
		//collect test material
		List<Example> examples = getAllExamples();
		//run tests
		List<ExampleResult> failedExamples = alist();
		for (Example example : examples) {
			ExampleResult result = testExample(example);
			if (result.getResult() == Result.Failed)
				failedExamples.add(result);
		}
		//list failed examples
		if (failedExamples.size() > 0) {
			for (ExampleResult example : failedExamples)
				System.out.println(example.getExample().getName() + " failed: " + example.getComment());
			fail(failedExamples.size() + " of " + examples.size() + " examples failed. " +
				"See console for details.");
		}
	}
	
	private ExampleResult testExample(Example example) {
		int accsCount = example.getAccsCount();
		AccidentalsNotation accs = testees[accsCount].compute(getParams(example.getPitches(),
			example.getNotes(), example.getAccsCount(), cw, example.getContext()));
		ExampleResult result = perfect(example);
		result.checkEquals("number of accidentals",
			accsCount, accs.accidentals.length);
		result.checkEquals("total width",
			example.getExpectedAccsWidthIs(), accs.widthIs);
		float[] accsXIs = example.getExpectedAccsXIs();
		if (accsXIs != null) {
			for (int i : range(accsCount)) {
				result.checkEquals("offset of accidental " + i,
					accsXIs[i], accs.accidentals[i].xIs);
			}
		}
		int[] accsLp = example.getExpectedAccsLp();
		for (int i : range(accsCount)) {
			result.checkEquals("LP of accidental " + i,
				accsLp[i], accs.accidentals[i].yLp);
		}
		return result;
	}
	
	@Test public void getAccNoteIndexTest() {
		//C#5
		//must have an accidental
		List<Pitch> pitches = alist(Companion.pi(0, 1, 5));
		assertEquals(0, Strategy.getAccNoteIndex(pitches, 0, contextC));
		//E4, G#4, C#5 (no accidental at bottom note)
		//must have accidentals on note 1 and 2
		pitches = alist(Companion.pi(2, 0, 4), Companion.pi(4, 1, 4), Companion.pi(0, 1, 5));
		assertEquals(1, Strategy.getAccNoteIndex(pitches, 0, contextC));
		assertEquals(2, Strategy.getAccNoteIndex(pitches, 2, contextC));
	}

	@Override public Score getScore() {
		//collect test material
		List<Example> examples = getAllExamples();
		//text style
		FormattedTextStyle style = defaultStyle.withFont(new FontInfo("Arial", 6f, FontStyle.normal));
		//one chord in each measure
		Score score = ScoreFactory.create1Staff();
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new TimeSignature(TimeType.time_3_4));
		for (int i : range(examples)) {
			Example example = examples.get(i);
			cursor.setMp(atElement(0, i, 0, 0));
			//write key
			int fifths = ((TraditionalKey) example.getContext().getKey()).getFifths();
			cursor.write((ColumnElement) new TraditionalKey(fifths));
			//write example name (each 2nd example one line down for better reading)
			String text = (i % 2 == 1 ? "\n" : "") + example.getName();
			cursor.write((MeasureElement) new Words(styleText(text, style)));
			//write chord with all accidentals from context (or a rest)
			Map<Pitch, Integer> accs = example.getContext().getAccidentals();
			if (accs.size() > 0) {
				Pitch[] pitches = new Pitch[accs.size()];
				int accIndex = 0;
				for (Pitch acc : accs.keySet()) {
					pitches[accIndex] = Companion.pi(acc.getStep(), accs.get(acc), acc.getOctave());
					accIndex++;
				}
				Chord accsChords = ChordFactory.chord(pitches, Companion.get_1$4());
				cursor.write(accsChords);
			}
			else {
				cursor.write(new Rest(Companion.get_1$4()));
			}
			//write a rest
			cursor.write(new Rest(Companion.get_1$4()));
			//write the tested chord
			Chord testedChord = ChordFactory.chord(example.getPitches().toArray(new Pitch[0]), Companion.get_1$4());
			cursor.write(testedChord);
		}
		return score;
	}
	
	public static void main(String... args) {
		VisualTester.start(new StrategyTest());
	}

}
