package material.beam.slant;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.range.OneMeasureOneStaff.oneMeasureOneStaff;

import java.util.List;

import lombok.Getter;
import material.ExampleBase;
import material.Suite;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.chord.ChordLps;
import com.xenoage.zong.musiclayout.spacer.beam.Placement;

/**
 * Beam slant example.
 * 
 * @author Andreas Wenger
 */
public class Example
	implements ExampleBase {
	
	public static List<Suite<Example>> all = alist(new ChlapikBeamSlant(), new RossBeamSlant());
	
	@Getter public String name;
	public int leftNoteLp;
	public int rightNoteLp;
	public float leftStemLengthIs;
	public float rightStemLengthIs;
	public float widthIs = Float.NaN;
	public int[] middleNotesLps = new int[0];
	private StemDirection stemDir = StemDirection.Default;
	public final int staffLines = 5;
	
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
	
	public Placement getPlacement() {
		StemDirection stemDir = getStemDir();
		return new Placement(
			leftNoteLp + stemDir.getSign() * leftStemLengthIs * 2,
			rightNoteLp + stemDir.getSign() * rightStemLengthIs * 2);
	}
	
	/**
	 * Returns an array of the preferred stem lengths.
	 * The stem length of the left and right chord is known, the other
	 * ones are simply set to 2.5.
	 */
	@Deprecated
	public float[] getStemsLengthIs() {
		int notesCount = getNotesLp().length;
		float[] ret = ArrayUtils.arrayFloat(notesCount, 2.5f);
		ret[0] = leftStemLengthIs;
		ret[notesCount - 1] = rightStemLengthIs;
		return ret;
	}

}
