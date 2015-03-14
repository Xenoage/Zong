package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.range.OneMeasureOneStaff.oneMeasureOneStaff;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamOffsetter;
import static java.lang.Math.abs;
import static org.junit.Assert.fail;

import java.util.List;

import material.beam.slant.RossBeamSlant;
import material.beam.slant.RossBeamSlant.Example;

import org.junit.Test;

import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;

/**
 * Tests for {@link BeamPlacer}.
 * 
 * @author Andreas Wenger
 */
public class BeamOffsetterTest {
	
	private BeamPlacer testee = beamOffsetter;
	private final int staffLines = 5;
	
	
	/**
	 * Tests with examples from Ross.
	 */
	@Test public void computeForOneStaffTestRoss() {
		List<String> failed = alist();
		List<Example> examples = new RossBeamSlant().examples;
		for (Example example : examples) {
			//collect data
			int notesLp[] = getNotesLp(example);
			StemDirection stemDir = getStemDir(example.stemDir, notesLp);
			float[] stemsXIs = getStemsXIs(example, notesLp.length);
			//run test
			Placement offset = testee.computeForOneStaff(notesLp, stemDir, stemsXIs, 1, staffLines);
			//check result
			String failMessage = check(offset, example, stemDir);
			if (failMessage != null)
				failed.add(failMessage);
		}
		//success, when >95% of the examples are correct
		if (1.0 * failed.size() / examples.size() > 0.05)
			fail("Beam slanting incorrect for " + failed.size() + " of " + examples.size() +
				" examples: \n" + failed);
	}
	
	private float[] getStemsXIs(Example example, int chordsCount) {
		float[] stemsXIs = new float[chordsCount];
		float distance = example.getStemsDistanceIs();
		for (int i : range(chordsCount))
			stemsXIs[i] = i * distance;
		return stemsXIs;
	}
	
	private String check(Placement offset, Example example, StemDirection stemDir) {
		float expectedLeftEndLp = example.leftNoteLp +
			stemDir.getSign() * example.leftStemLengthIs * 2;
		float expectedRightEndLp = example.rightNoteLp +
			stemDir.getSign() * example.rightStemLengthIs * 2;
		if (abs(offset.leftEndLp - expectedLeftEndLp) < df &&
			abs(offset.rightEndLp - expectedRightEndLp) < df) {
			return null; //ok
		}
		else {
			return example.name + ": expected [" + expectedLeftEndLp + "," + expectedRightEndLp +
				"] but was [" + offset.leftEndLp + "," + offset.rightEndLp + "]";
		}
	}

}
