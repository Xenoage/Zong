package com.xenoage.zong.musiclayout.spacer.beam.stem;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Countable;
import com.xenoage.zong.musiclayout.spacing.ChordSpacing;
import lombok.val;

import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;
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

	/* The horizontal offset in IS of the stem of the first chord. */
	public final float leftXIs;
	/** The horizontal offset in IS of the stem of the last chord. */
	public final float rightXIs;
	/** The LP of the outermost stem-side note of the first chord. */
	public final int leftNoteLp;
	/** The LP of the outermost stem-side note of the last chord. */
	public final int rightNoteLp;


	public static BeamedStems fromBeam(List<ChordSpacing> beamChords) {
		val stems = new CList<BeamedStem>(beamChords.size());
		for (val chord : beamChords) {
			//we need a stem for each beamed chord
			float stemsLengthIs;
			if (chord.notation.stem != null)
				stemsLengthIs = abs(chord.notation.stem.endLp - chord.notation.getStemSideNoteLp()) / 2;
			else
				stemsLengthIs = 3; //if there is no stem (should not happen very often)
			stems.add(new BeamedStem(chord.getStemXIs(), chord.notation.stemDirection,
					chord.getNotation().getStemSideNoteLp(), stemsLengthIs));
		}
		return new BeamedStems(stems.close());
	}

	public BeamedStems(IList<BeamedStem> stems) {
		this.stems = stems;
		this.leftXIs = getFirst().xIs;
		this.rightXIs = getLast().xIs;
		this.leftNoteLp = getFirst().noteLp;
		this.rightNoteLp = getLast().noteLp;
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

	public int getMaxNoteLp() {
		int max = Integer.MIN_VALUE;
		for (val stem : stems)
			max = max(max, stem.noteLp);
		return max;
	}

	public int getMinNoteLp() {
		int min = Integer.MAX_VALUE;
		for (val stem : stems)
			min = min(min, stem.noteLp);
		return min;
	}

	@Override public Iterator<BeamedStem> iterator() {
		return stems.iterator();
	}
}
