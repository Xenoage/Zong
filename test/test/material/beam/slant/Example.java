package material.beam.slant;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.range.OneMeasureOneStaff.oneMeasureOneStaff;
import static org.junit.Assert.fail;

import java.util.List;

import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;

/**
 * Beam slant example.
 * 
 * @author Andreas Wenger
 */
public class Example {
	
	public final int staffLines = 5;
	
	public String name;
	public int leftNoteLp;
	public int rightNoteLp;
	public float leftStemLengthIs;
	public float rightStemLengthIs;
	public float widthIs = Float.NaN;
	public int[] middleNotesLps = new int[0];
	private StemDirection stemDir = StemDirection.Default;
	
	public static Example example(String name) {
		Example ret = new Example();
		ret.name = name;
		return ret;
	}

	/**
	 * Simple example with normal spacing, like on Ross, page 104.
	 */
	public static Example example(String name, int leftNoteLp, double leftStemLengthIs,
		int rightNoteLp, double rightStemLengthIs) {
		return example(name).left(leftNoteLp, leftStemLengthIs).width(4.5).right(
			rightNoteLp, rightStemLengthIs);
	}
	
	public Example left(int noteLp, double stemLengthIs) {
		leftNoteLp = noteLp;
		leftStemLengthIs = (float) stemLengthIs;
		return this;
	}
	
	public Example width(double widthIs) {
		this.widthIs = (float) widthIs;
		return this;
	}
	
	public Example middleNotes(int... noteLps) {
		middleNotesLps = noteLps;
		return this;
	}
	
	public Example right(int noteLp, double stemLengthIs) {
		rightNoteLp = noteLp;
		rightStemLengthIs = (float) stemLengthIs;
		return this;
	}
	
	public Example stemDir(StemDirection stemDir) {
		this.stemDir = stemDir;
		return this;
	}
	
	public float[] getStemsXIs() {
		//compute positions. use same distance for each note
		float distanceIs = getStemsDistanceIs();
		float[] stemsXIs = new float[2 + middleNotesLps.length];
		for (int i : range(stemsXIs)) {
			stemsXIs[i] = i * distanceIs;
		}
		return stemsXIs;
	}
	
	public float getStemsDistanceIs() {
		if (Double.isNaN(widthIs))
			return 5; //use normal distance of 5 spaces as default
		else
			return widthIs / (middleNotesLps.length + 1); //equally split width for all stems
	}
	
	public float getSlantIs() {
		int stemDir = getStemDir().getSign();
		float leftStemEndLp = leftNoteLp + stemDir * leftStemLengthIs * 2;
		float rightStemEndLp = rightNoteLp + stemDir * rightStemLengthIs * 2;
		return (rightStemEndLp - leftStemEndLp) / 2;
	}
	
	public int[] getNotesLp() {
		int chordsCount = 2 + middleNotesLps.length;
		int[] notesLp = new int[chordsCount];
		notesLp[0] = leftNoteLp;
		for (int i : range(middleNotesLps))
			notesLp[1 + i] = middleNotesLps[i];
		notesLp[chordsCount - 1] = rightNoteLp;
		return notesLp;
	}
	
	public StemDirection getStemDir() {
		if (stemDir != StemDirection.Default)
			return stemDir;
		//compute stem direction
		int[] notesLp = getNotesLp();
		ChordLps[] chordLps = new ChordLps[notesLp.length];
		for (int i : range(notesLp))
			chordLps[i] = new ChordLps(notesLp[i]);
		return oneMeasureOneStaff.compute(chordLps, staffLines)[0];
	}
	
	/**
	 * Filters the given list of examples by the given part of a name.
	 * @param examples               the examples to filter
	 * @param namePart               an example qualifies, if its name contains this string
	 * @param minExpectedTestsCount  minimum number of expected results, otherwise fail
	 */
	public static List<Example> filter(List<Example> examples,
		String namePart, int minExpectedTestsCount) {
		List<Example> ret = alist();
		int found = 0;
		for (Example example : examples) {
			if (example.name.contains(namePart)) {
				found++;
				ret.add(example);
			}
		}
		if (found < minExpectedTestsCount) {
			fail("only " + found + " test found");
		}
		return ret;
	}
}
