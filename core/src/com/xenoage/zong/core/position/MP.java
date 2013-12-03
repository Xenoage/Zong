package com.xenoage.zong.core.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Voice;


/**
 * Musical Position within a score.
 * 
 * It can contain a staff, measure, voice index, beat and element index.
 * 
 * All values may be {@link #unknown} (or {@link #unknownBeat} for the beat).
 * The element index is only used for elements within a voice, otherwise its value is {@link #unknown}.
 * 
 * @author Andreas Wenger
 */
@Data @AllArgsConstructor public class MP
	implements Comparable<MP> {

	/** The staff index, or {@link #unknown}. */
	@Wither public final int staff;
	/** The measure index, or {@link #unknown}. */
	@Wither public final int measure;
	/** The voice index, or {@link #unknown}. */
	@Wither public final int voice;
	/** The beat, or {@link #unknownBeat}. */
	@Wither public final Fraction beat;
	/** The element index within the voice, or {@link #unknown}. */
	@Wither public final int element;

	/** Special value for an unknown beat. */
	public static final Fraction unknownBeat = null;
	/** Special value for an unknown index. */
	public static final int unknown = -1;
	

	/** Musical position with all values set to 0, including beat and element index. */
	public static final MP mp0 = new MP(0, 0, 0, Fraction._0, 0);
	/** Musical position with all values set to 0, including beat ("b" in name), but element index set to {@link #unknown}. */
	public static final MP mpb0 = new MP(0, 0, 0, Fraction._0, unknown);
	/** Musical position with all values set to 0, including element ("e" in name), but beat set to {@link #unknownBeat}. */
	public static final MP mpe0 = new MP(0, 0, 0, unknownBeat, 0);


	/**
	 * Creates a new musical position at the given staff, measure, voice, element index and beat.
	 */
	public static MP mp(int staff, int measure, int voice, Fraction beat, int element) {
		return new MP(staff, measure, voice, beat, element);
	}


	/**
	 * Creates a new musical position with the given measure and the
	 * other values set to unknown.
	 */
	public static MP atMeasure(int measure) {
		return new MP(unknown, measure, unknown, unknownBeat, unknown);
	}


	/**
	 * Creates a new musical position with the given staff and the
	 * other values set to unknown.
	 */
	public static MP atStaff(int staff) {
		return new MP(staff, unknown, unknown, unknownBeat, unknown);
	}


	/**
	 * Creates a new musical position with the given staff and measure and the
	 * voice set to unknown.
	 */
	public static MP atMeasure(int staff, int measure) {
		return new MP(staff, measure, unknown, unknownBeat, unknown);
	}


	/**
	 * Creates a new musical position with the given voice and the
	 * other values set to unknown.
	 */
	public static MP atVoice(int voice) {
		return new MP(unknown, unknown, voice, unknownBeat, unknown);
	}


	/**
	 * Creates a new musical position at the given staff, measure and voice.
	 */
	public static MP atVoice(int staff, int measure, int voice) {
		return new MP(staff, measure, voice, unknownBeat, unknown);
	}
	
	
	/**
	 * Creates a new musical position at the given beat.
	 */
	public static MP atBeat(Fraction beat) {
		return new MP(unknown, unknown, unknown, beat, unknown);
	}
	
	
	/**
	 * Creates a new musical position at the given staff, measure, voice and beat.
	 */
	public static MP atBeat(int staff, int measure, int voice, Fraction beat) {
		return new MP(staff, measure, voice, beat, unknown);
	}
	
	
	/**
	 * Creates a new musical position at the given element index.
	 */
	public static MP atElement(int element) {
		return new MP(unknown, unknown, unknown, unknownBeat, element);
	}
	
	
	/**
	 * Creates a new musical position at the given staff, measure, voice and element index.
	 */
	public static MP atElement(int staff, int measure, int voice, int element) {
		return new MP(staff, measure, voice, unknownBeat, element);
	}
	
	
	/**
	 * Creates a new musical position with the given measure and beat
	 * and the other values set to unknown.
	 */
	public static MP atColumnBeat(int measure, Fraction beat)
	{
		return new MP(unknown, measure, unknown, beat, unknown);
	}


	public boolean isMeasureUnknown() {
		return measure == unknown;
	}


	public boolean isStaffOrMeasureUnknown() {
		return staff == unknown || measure == unknown;
	}


	public boolean isStaffOrMeasureOrVoiceUnknown() {
		return staff == unknown || measure == unknown || voice == unknown;
	}


	public boolean isStaffOrMeasureOrVoiceOrBeatUnknown() {
		return staff == unknown || measure == unknown || voice == unknown || beat == unknownBeat;
	}


	public boolean isStaffOrMeasureOrVoiceOrElementUnknown() {
		return staff == unknown || measure == unknown || voice == unknown || element == unknown;
	}
	
	
	public void requireStaffAndMeasureAndVoiceAndElement() {
		if (isStaffOrMeasureOrVoiceOrElementUnknown())
			throw new IllegalStateException("Missing MP data. Required: staff, measure, voice, element. In: " + toString());
	}


	/**
	 * Gets the {@link MP} of the given element, or null if it or its parent is null
	 * or if the parent is not part of a score.
	 */
	public static MP getMP(MPElement element) {
		if (element == null || element.getParent() == null)
			return null;
		return element.getParent().getMP(element);
	}
	
	
	/**
	 * Gets the {@link MP} of the given element, or null if it is no MPElement, or
	 * if its parent is null or if the parent is not part of a score.
	 */
	public static MP getMP(MusicElement element) {
		if (element instanceof MPElement)
			return getMP((MPElement) element);
		else
			return null;
	}


	@Override public String toString() {
		return "[" + (staff != unknown ? "Staff = " + staff + ", " : "") +
			(measure != unknown ? "Measure = " + measure + ", " : "") + (voice != unknown ? "Voice = " + voice + ", ": "") +
			(beat != unknownBeat ? "Beat = " + beat.getNumerator() + "/" + beat.getDenominator() + ", " : "") +
			(element != unknown ? "Element = " + element : "") + "]";
	}


	/**
	 * Compares this {@link MP} with the given one.
	 * It is compared by staff, measure, voice, beat or element index.
	 * If the beats are known, only those are compared, and if they are unknown,
	 * only the element indices are compared.
	 * None of the vother alues should be unknown, otherwise the result is undefined.
	 */
	@Override public int compareTo(MP mp) {
		//staff
		if (staff < mp.staff)
			return -1;
		else if (staff > mp.staff)
			return 1;
		else {
			//measure
			if (measure < mp.measure)
				return -1;
			else if (measure > mp.measure)
				return 1;
			else {
				//voice
				if (voice < mp.voice)
					return -1;
				else if (voice > mp.voice)
					return 1;
				else if (beat != null && mp.beat != null) {
					//beat
					return beat.compareTo(mp.beat);
				}
				else {
					//element index
					if (element < mp.element)
						return -1;
					else if (element > mp.element)
						return 1;
					else
						return 0;
				}
			}
		}
	}
	
	
	/**
	 * Returns this {@link MP} but also with the {@link #beat} field.
	 * For this, the {@link #element} index must be known and the score must be given.
	 */
	public MP getWithBeat(Score score) {
		Voice voice = score.getVoice(this);
		Fraction beat = voice.getBeat(element);
		return withBeat(beat);
	}


}
