package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.kernel.Range.range;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.spacing.horizontal.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementSpacing;

/**
 * The horizontal spacing for one measure column.
 * 
 * This is a list of the offsets of each multiused
 * beat in the measure, that is beat 0,
 * the beats that are used by more than one voice
 * (or by one voice, if it is the only one), and
 * the beat at the end of the measure.
 *
 * The units are measured in mm, since the staves may
 * have different heights.
 *
 * @author Andreas Wenger
 */
@Const @Getter public final class ColumnSpacing {

	/** The score this spacing belongs to. */
	private final Score score;

	/** The list of measure spacings.
	 * Eeach staff of the measure has its own spacing. */
	private final MeasureSpacing[] measureSpacings;

	/** The offsets of the relevant beats (notes and rests) of this measure in mm. */
	private final BeatOffset[] beatOffsets;
	
	/** The barline offsets of this measure in mm.
	 * At least the position of the start barline and the end barline
	 * is available, and mid-measure barlines may also be included. */
	private final BeatOffset[] barlineOffsets;

	/** The width of the leading spacing in mm. */
	private final float leadingWidth;


	/**
	 * Creates a new {@link ColumnSpacing}.
	 * @param measureSpacings  for every staff one measure spacing
	 * @param beatOffsets      the offsets of the relevant beats (notes) in mm
	 * @param barlineOffsets   the offsets of the relevant beats (barlines) in mm, where
	 *                         at least the position of the start barline and the end barline
	 *                         must be given (even if they are invisible).
	 */
	public ColumnSpacing(Score score, MeasureSpacing[] measureSpacings,
		BeatOffset[] beatOffsets, BeatOffset[] barlineOffsets) {
		this.score = score;
		this.measureSpacings = measureSpacings;
		this.beatOffsets = beatOffsets;
		if (barlineOffsets.length < 2)
			throw new IllegalArgumentException(
				"At least two barline offsets (start and end) must be given");
		this.barlineOffsets = barlineOffsets;
		//compute width of the leading and voice space
		this.leadingWidth = computeLeadingSpacingWidth(score);
	}

	/**
	 * Gets the width of the measure in mm.
	 * This is the width of the leading spacing plus the width of the voices.
	 */
	public float getWidth() {
		return getLeadingWidth() + getVoicesWidth();
	}

	/**
	 * Gets the offset for the given beat, or null if unknown.
	 */
	public BeatOffset getBeatOffset(Fraction beat) {
		for (BeatOffset beatOffset : beatOffsets) {
			if (beatOffset.getBeat().equals(beat))
				return beatOffset;
		}
		return null;
	}

	/**
	 * Returns the offset of the given {@link MusicElement} in IS, or
	 * 0 if not found. The index of the staff and voice must also be given.
	 */
	public float getOffset(MusicElement element, int staffIndex, int voiceIndex) {
		MeasureSpacing measure = measureSpacings[staffIndex];
		for (ElementSpacing se : measure.getVoiceSpacings().get(voiceIndex).spacingElements) {
			if (se.element == element) {
				return se.offsetIs;
			}
		}
		return 0;
	}

	/**
	 * Gets the width of the leading spacing in mm.
	 * If there is no leading spacing, 1 is returned. TODO
	 */
	private float computeLeadingSpacingWidth(Score score) {
		float ret = 1; //TODO
		//find the maximum width of the leading spacings
		//of each staff
		for (int i : range(measureSpacings)) {
			MeasureSpacing measureSpacing = measureSpacings[i];
			LeadingSpacing leadingSpacing = measureSpacing.getLeadingSpacing();
			if (leadingSpacing != null) {
				float width = leadingSpacing.width * score.getInterlineSpace(MP.atStaff(i));
				if (width > ret)
					ret = width;
			}
		}
		return ret;
	}

	/**
	 * Gets the width of the voices space in mm.
	 */
	public float getVoicesWidth() {
		return barlineOffsets[barlineOffsets.length - 1].getOffsetMm();
	}

	/**
	 * Gets the offset of barline at the given beat, or 0 if unknown.
	 */
	public float getBarlineOffset(Fraction beat) {
		for (BeatOffset bo : barlineOffsets) {
			if (bo.getBeat().equals(beat))
				return bo.getOffsetMm();
		}
		return 0;
	}

}
