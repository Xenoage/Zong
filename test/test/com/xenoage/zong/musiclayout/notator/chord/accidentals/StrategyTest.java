package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.alistFromLists;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.Pitch.pi;
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

import org.junit.Test;

import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

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
public class StrategyTest {
	
	private Strategy[] testees = { null, oneAccidental, twoAccidentals, threeAccidentals };
	
	private final ChordWidths cw = defaultChordWidthsNormal;
	
	
	@Test public void testStrategies() {
		//collect test material
		List<Example> examples = alistFromLists(new OneAccidental().getExamples(),
			new RossTwoAccidentals().getExamples(), new RossThreeAccidentals().getExamples());
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
		List<Pitch> pitches = alist(pi(0, 1, 5));
		assertEquals(0, Strategy.getAccNoteIndex(pitches, 0, contextC));
		//E4, G#4, C#5 (no accidental at bottom note)
		//must have accidentals on note 1 and 2
		pitches = alist(pi(2, 0, 4), pi(4, 1, 4), pi(0, 1, 5));
		assertEquals(1, Strategy.getAccNoteIndex(pitches, 0, contextC));
		assertEquals(2, Strategy.getAccNoteIndex(pitches, 2, contextC));
	}

}
