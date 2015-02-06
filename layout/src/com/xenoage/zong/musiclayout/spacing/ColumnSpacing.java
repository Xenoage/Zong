package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.kernel.Range.range;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.musiclayout.BeatOffset;

/**
 * The horizontal spacing for one measure column.
 * 
 * It contains a list of the offsets of each multiused
 * beat in the measure, that is beat 0,
 * the beats that are used by more than one voice
 * (or by one voice, if it is the only one), and
 * the beat at the end of the measure.
 * 
 * It also contains the spacings of all elements in
 * the measures and its voices.
 *
 * The units are measured in mm, since the staves may
 * have different heights.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class ColumnSpacing {

	/** The score this spacing belongs to. */
	public Score score;

	/** The list of measure spacings.
	 * Eeach staff of the measure has its own spacing. */
	public List<MeasureSpacing> measureSpacings;

	/** The offsets of the relevant beats (notes and rests) of this measure in mm. */
	public List<BeatOffset> beatOffsets;
	
	/** The barline offsets of this measure in mm.
	 * At least the position of the start barline and the end barline
	 * is available, and mid-measure barlines may also be included. */
	public List<BeatOffset> barlineOffsets;

	
	/**
	 * Gets the width of the measure in mm.
	 * This is the width of the leading spacing plus the width of the voices.
	 */
	public float getWidthMm() {
		return getLeadingWidthMm() + getVoicesWidthMm();
	}
	
	/**
	 * Gets the width of the leading spacing in mm.
	 * If there is no leading spacing, 1 is returned. TODO
	 */
	public float getLeadingWidthMm() {
		float ret = 1; //TODO
		//find the maximum width of the leading spacings
		//of each staff
		for (int iStaff : range(measureSpacings)) {
			MeasureSpacing measureSpacing = measureSpacings.get(iStaff);
			LeadingSpacing leadingSpacing = measureSpacing.getLeadingSpacing();
			if (leadingSpacing != null) {
				float width = leadingSpacing.width * score.getInterlineSpace(iStaff);
				if (width > ret)
					ret = width;
			}
		}
		return ret;
	}
	
	/**
	 * Gets the width of the voices space in mm.
	 */
	public float getVoicesWidthMm() {
		return barlineOffsets.get(barlineOffsets.size() - 1).offsetMm;
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
	public float getOffsetIs(MusicElement element, int staffIndex, int voiceIndex) {
		MeasureSpacing measure = measureSpacings.get(staffIndex);
		for (ElementSpacing se : measure.getVoiceSpacings().get(voiceIndex).spacingElements) {
			if (se.getElement() == element) {
				return se.offsetIs;
			}
		}
		return 0;
	}

	/**
	 * Gets the offset in mm of the barline at the given beat, or 0 if unknown.
	 */
	public float getBarlineOffsetMm(Fraction beat) {
		for (BeatOffset bo : barlineOffsets) {
			if (bo.getBeat().equals(beat))
				return bo.getOffsetMm();
		}
		return 0;
	}

}
