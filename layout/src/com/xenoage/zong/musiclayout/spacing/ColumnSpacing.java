package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.core.position.MP.unknown;

import java.util.List;

import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.notations.ChordNotation;

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
@Getter
public class ColumnSpacing {

	/** The global measure index. */
	public int measureIndex;
	/** The list of measure spacings.
	 * Eeach staff of the measure has its own spacing. */
	public List<MeasureSpacing> measures;
	/** The offsets of the relevant beats (notes and rests) of this measure in mm. */
	public List<BeatOffset> beatOffsets;
	/** The barline offsets of this measure in mm.
	 * At least the position of the start barline and the end barline
	 * is available, and mid-measure barlines may also be included. */
	public List<BeatOffset> barlineOffsets;
	
	/** Backward reference to the system. */
	public SystemSpacing parentSystem = null;

	
	public ColumnSpacing(int measureIndex, List<MeasureSpacing> measureSpacings,
		List<BeatOffset> beatOffsets, List<BeatOffset> barlineOffsets) {
		this.measureIndex = measureIndex;
		this.measures = measureSpacings;
		this.beatOffsets = beatOffsets;
		this.barlineOffsets = barlineOffsets;
	}
	
	/**
	 * Gets the width of the measure in mm.
	 * This is the width of the leading spacing plus the width of the voices.
	 */
	public float getWidthMm() {
		return getLeadingWidthMm() + getVoicesWidthMm();
	}
	
	/**
	 * Gets the width of the leading spacing in mm.
	 * If there is no leading spacing, 0 is returned. TODO
	 */
	public float getLeadingWidthMm() {
		float ret = 0; //TODO
		//find the maximum width of the leading spacings
		//of each staff
		for (int iStaff : range(measures)) {
			MeasureSpacing measureSpacing = measures.get(iStaff);
			LeadingSpacing leadingSpacing = measureSpacing.getLeadingSpacing();
			if (leadingSpacing != null) {
				float width = leadingSpacing.widthIs * measureSpacing.getInterlineSpace();
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
	 * @deprecated use {@link #getChord(Chord)}
	 */
	public float getOffsetIs(MusicElement element, int staffIndex, int voiceIndex) {
		MeasureSpacing measure = measures.get(staffIndex);
		for (ElementSpacing se : measure.getVoiceSpacings().get(voiceIndex).elements) {
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
	
	/**
	 * Gets the VoiceSpacing at the given staff and voice.
	 */
	public VoiceSpacing getVoice(int staff, int voice) {
		return measures.get(staff).getVoiceSpacings().get(voice);
	}
	
	/**
	 * Convenience method to get the {@link ElementSpacing} of the given
	 * {@link VoiceElement} in this column.
	 */
	public ElementSpacing getElement(VoiceElement element) {
		MP mp = getMP(element);
		VoiceSpacing voice = getVoice(mp.staff, mp.voice);
		return voice.getElement(element);
	}
	
	/**
	 * Convenience method to get the {@link ChordNotation} of the given
	 * {@link Chord} in this column.
	 */
	public ChordNotation getNotation(Chord chord) {
		return (ChordNotation) getElement(chord).notation;
	}
	
	/**
	 * Gets the beat at the given horizontal position in mm,
	 * relative to the beginning of the measure.
	 * 
	 * If it is between two beats (which will be true almost ever), the
	 * the beat nearest to the position is selected (like it is usual e.g. in text
	 * processing applications). If it is behind all known beats,
	 * the last known beat is returned.
	 */
	public Fraction getBeatAt(float xMm) {
		//find beat and return it
		BeatOffset last = null;
		for (BeatOffset bo : beatOffsets) {
			if (xMm <= bo.offsetMm)
				return getBeatAt(xMm, last, bo);
			last = bo;
		}
		//return last beat
		return getLast(beatOffsets).beat;
	}
	
	/**
	 * Like {@link #getBeatAt(float)}, but only for a single staff,
	 * i.e. unused beats in this staff are ignored.
	 * 
	 * When the staff is {@link MP#unknown}, this method works like
	 * {@link #getBeatAt(float)}. 
	 */
	public Fraction getBeatAt(float xMm, int staff) {
		if (staff == unknown)
			return getBeatAt(xMm);
		//find beat and return it
		MeasureSpacing measure = measures.get(staff);
		BeatOffset last = null;
		for (Fraction beat : measure.getUsedBeats()) {
			BeatOffset bo = getBeatOffset(beat);
			if (xMm <= bo.offsetMm)
				return getBeatAt(xMm, last, bo);
			last = bo;
		}
		//return last beat
		return getLast(beatOffsets).beat;
	}
	
	/**
	 * Returns the beat at the given position from the candidate,
	 * which position is nearer to the given position.
	 */
	private Fraction getBeatAt(float xMm, @MaybeNull BeatOffset leftCandidate, BeatOffset rightCandidate) {
		if (leftCandidate == null)
			return rightCandidate.beat;
		//check previous or given candidate
		boolean right = (xMm >= (leftCandidate.offsetMm + rightCandidate.offsetMm) / 2);
		return (right ? rightCandidate : leftCandidate).beat;
	}

	/**
	 * Gets the horizontal position in mm, relative to the beginning of the measure,
	 * of the given beat.
	 * If the given beat is after the last beat, the offset of the last beat is returned.
	 */
	public float getXMmAt(Fraction beat) {
		//find offset and return it
		for (BeatOffset bo : beatOffsets) {
			if (beat.compareTo(bo.beat) <= 0)
				return bo.offsetMm;
		}
		//return last offset
		return getLast(beatOffsets).offsetMm;
	}

}
