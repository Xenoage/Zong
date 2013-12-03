package com.xenoage.zong.musiclayout;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * A {@link MeasureMarks} class stores positioning
 * information on the elements of a measure within
 * a {@link StaffStamping}.
 * 
 * The horizontal coordinates of the start and ending of the
 * measure is saved here.
 * 
 * It contains also the relevant {@link BeatOffset}s within the measure
 * in mm (relative to the beginning of the staff).
 * Coordinates can be converted to beats and backwards.
 * The voice of the positions are not relevant.
 * 
 * There is no validation of the given data, so be careful what you do.
 * 
 * @author Andreas Wenger
 */
@Const @Getter public class MeasureMarks {

	public final int measure;
	public final float startMm;
	public final float leadingMm;
	public final float endMm;
	/** All {@link BeatOffset}s in this measure. */
	public IList<BeatOffset> beatOffsets;


	/**
	 * Creates a new {@link MeasureMarks} instance.
	 * @param measure      the index of the measure
	 * @param startMm      start position of the measure in mm relative to the beginning of the staff
	 * @param leadingMm    end position of the leading spacing in mm relative to the beginning of the staff.
	 *                     If there is no leading spacing, this value is equal to <code>startMm</code>
	 * @param endMm        end position of the measure in mm relative to the beginning of the staff
	 * 
	 * @param beatOffsets  offsets for the beats in the measure (at least one)
	 */
	public MeasureMarks(int measure, float startMm, float leadingMm, float endMm,
		IList<BeatOffset> beatOffsets) {
		if (beatOffsets.size() == 0)
			throw new IllegalArgumentException("At least one beat must be given");
		this.measure = measure;
		this.startMm = startMm;
		this.leadingMm = leadingMm;
		this.endMm = endMm;
		this.beatOffsets = beatOffsets;
	}
	
	public boolean contains(float xMm) {
		return MathUtils.between(xMm, startMm, endMm);
	}

	/**
	 * Gets the beat at the given horizontal position in mm.
	 * 
	 * If it is between two marks (which will be true almost ever), the
	 * the right mark is selected (like it is usual e.g. in text
	 * processing applications). If it is behind all known marks,
	 * the last known beat is returned.
	 */
	public Fraction getBeatAt(float xMm) {
		//find beat and return it
		for (int i = 0; i < beatOffsets.size(); i++) {
			if (xMm <= beatOffsets.get(i).getOffsetMm())
				return beatOffsets.get(i).getBeat();
		}
		//return last beat
		return beatOffsets.get(beatOffsets.size() - 1).getBeat();
	}

	/**
	 * Gets the horizontal position in mm, relative to the beginning of the staff,
	 * of the given beat.
	 * If the given beat is not within this list, the next available beat within
	 * this measure is used, or the last beat if the given beat is behind all
	 * known beats.
	 */
	public float getXMmAt(Fraction beat) {
		//find offset and return it
		for (int i = 0; i < beatOffsets.size(); i++) {
			if (beat.compareTo(beatOffsets.get(i).getBeat()) <= 0)
				return beatOffsets.get(i).getOffsetMm();
		}
		//return last offset
		return beatOffsets.get(beatOffsets.size() - 1).getOffsetMm();
	}

}
