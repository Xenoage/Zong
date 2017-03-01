package com.xenoage.zong.musiclayout.spacer.beam.stem;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Countable;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.SLP;
import com.xenoage.zong.musiclayout.spacing.ChordSpacing;
import lombok.val;

import java.util.Iterator;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Positions of beamed stems.
 *
 * @author Andreas Wenger
 */
@Const
public class BeamedStems
	implements Countable, Iterable<BeamedStem> {

	private final IList<BeamedStem> stems;

	//cached:

	/** The primary direction of the stems. This is the direction of the first stem. */
	public final StemDirection primaryStemDir;
	/* The horizontal offset in IS of the stem of the first chord. */
	public final float leftXIs;
	/** The horizontal offset in IS of the stem of the last chord. */
	public final float rightXIs;
	/** The LP of the outermost stem-side note of the first chord. */
	public final float leftNoteLp;
	/** The LP of the outermost stem-side note of the last chord. */
	public final float rightNoteLp;


	public static BeamedStems fromBeam(List<ChordSpacing> beamChords) {
		val stems = new CList<BeamedStem>(beamChords.size());
		for (val chord : beamChords) {
			//we need a stem for each beamed chord
			if (chord.notation.stem == null)
				throw new IllegalStateException("TODO! we need a stem for each beamed chord");
			SLP stemSideNoteSlp = chord.getNotation().getStemSideNoteSlp();
			SLP stemEndSlp = chord.getNotation().getStemEndSlp();
			stems.add(new BeamedStem(chord.getStemXIs(), chord.notation.stemDirection,
					stemSideNoteSlp, stemEndSlp));
		}
		return new BeamedStems(stems.close());
	}

	public BeamedStems(IList<BeamedStem> stems) {
		this.stems = stems;
		this.primaryStemDir = getFirst().dir;
		this.leftXIs = getFirst().xIs;
		this.rightXIs = getLast().xIs;
		this.leftNoteLp = getFirst().noteSlp.lp;
		this.rightNoteLp = getLast().noteSlp.lp;
	}

	public int getCount() {
		return stems.size();
	}

	public BeamedStem get(int index) {
		return stems.get(index);
	}

	public BeamedStem getFirst() {
		return CollectionUtils.getFirst(stems);
	}

	public BeamedStem getLast() {
		return CollectionUtils.getLast(stems);
	}

	public float getMaxNoteLp() {
		float max = -1 * Float.MAX_VALUE;
		for (val stem : stems)
			max = max(max, stem.noteSlp.lp);
		return max;
	}

	public float getMinNoteLp() {
		float min = Float.MAX_VALUE;
		for (val stem : stems)
			min = min(min, stem.noteSlp.lp);
		return min;
	}

	@Override public Iterator<BeamedStem> iterator() {
		return stems.iterator();
	}
}
