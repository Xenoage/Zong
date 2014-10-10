package com.xenoage.zong.core.music;

import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.zong.core.music.Measure.measure;
import static com.xenoage.zong.core.music.util.MPE.mpE;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.util.MPE;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.utils.exceptions.IllegalMPException;


/**
 * Staff of any size.
 * 
 * A vocal staff that is visible throughout the whole
 * score is an instance of this class as well
 * as a small ossia staff that is only displayed
 * over a single measure.
 * 
 * Staves are divided into measures.
 *
 * @author Andreas Wenger
 */
public final class Staff {

	/** The measures from the beginning to the ending of the score (even the invisible ones). */
	@Getter private List<Measure> measures;
	/** The number of lines in this staff. */
	@Getter @Setter private int linesCount;
	/** Distance between the lines in this staff in mm, or null for default. */
	@Getter @Setter private Float interlineSpace;
	
	/** Back reference: The parent staves list, or null if not part of a score. */
	@Getter @Setter private StavesList parent;


	/**
	 * Creates a new {@link Staff}.
	 */
	public Staff(List<Measure> measures, int linesCount, Float interlineSpace) {
		for (Measure measure : measures)
			measure.setParent(this);
		this.measures = measures;
		this.linesCount = linesCount;
		this.interlineSpace = interlineSpace;
	}


	/**
	 * Creates a new {@link Staff}.
	 */
	public static Staff staff(int linesCount, Float interlineSpace) {
		ArrayList<Measure> measures = new ArrayList<Measure>();
		return new Staff(measures, linesCount, interlineSpace);
	}


	/**
	 * Creates a minimal staff with no content.
	 */
	public static Staff staffMinimal() {
		return staff(5, null);
	}


	/**
	 * Adds the given number of empty measures at the end
	 * of the staff.
	 */
	public void addEmptyMeasures(int measuresCount) {
		for (int i = 0; i < measuresCount; i++) {
			Measure m = measure();
			m.setParent(this);
			measures.add(m);
		}
	}


	/**
	 * Gets the measure with the given index, or throws an
	 * {@link IllegalMPException} if there is none.
	 */
	public Measure getMeasure(int index) {
		if (index >= 0 && index <= measures.size())
			return measures.get(index);
		else
			throw new IllegalMPException(atMeasure(index));
	}


	/**
	 * Gets the measure with the given index, or throws an
	 * {@link IllegalMPException} if there is none.
	 * Only the measure index of the given position is relevant.
	 */
	public Measure getMeasure(MP mp) {
		int index = mp.measure;
		if (index >= 0 && index < measures.size())
			return measures.get(index);
		else
			throw new IllegalMPException(mp);
	}


	/**
	 * Gets the voice within the measure at the given position, or throws an
	 * {@link IllegalMPException} if there is none.
	 * Only the measure index and voice index of the given position are relevant.
	 */
	public Voice getVoice(MP mp) {
		return getMeasure(mp).getVoice(mp);
	}


	/**
	 * Gets the {@link VoiceElement} before the given position (also over measure
	 * boundaries) together with its index in the voice,
	 * or null, if there is none (begin of score or everything is empty)
	 * @param mp         the position after the voice element, with element index
	 * @param onlyChord  if true, rests are ignored, and only a chord (or null) is returned
	 */
	public MPE<VoiceElement> getVoiceElementBefore(MP mp, boolean onlyChord) {
		mp.requireStaffAndMeasureAndVoiceAndElement();
		//find the last voice element ending at or before the current beat
		//in the given measure
		Voice voice = getMeasure(mp.measure).getVoice(mp.voice);
		for (int i : rangeReverse(mp.element - 1, 0)) {
			VoiceElement e = voice.getElement(i);
			if (!onlyChord || (e instanceof Chord))
				return mpE(e, mp.withElement(i));
		}
		//no result in this measure. loop through the preceding measures.
		for (int iMeasure : rangeReverse(mp.measure - 1, 0)) {
			voice = getMeasure(iMeasure).getVoice(mp.voice);
			for (int i : rangeReverse(voice.getElements())) {
				VoiceElement e = voice.getElement(i);
				if (!onlyChord || (e instanceof Chord))
					return mpE(e, MP.atElement(mp.staff, iMeasure, mp.voice, i));
			}
		}
		//nothing found
		return null;
	}


	/**
	 * Sets the measure with the given index.
	 * If out of current range, empty measures up to the given one are created.
	 */
	public void setMeasure(int index, Measure measure) {
		while (index >= measures.size()) {
			Measure m = measure();
			m.setParent(this);
			measures.add(m);
		}
		measure.setParent(this);
		measures.set(index, measure);
	}
	
	/**
	 * Gets the number of voices in this staff.
	 * This is the number of voices in the measure with the most number of voices.
	 */
	public int getVoicesCount() {
		int voiceCount = 0;
		for (Measure measure : measures) {
			int size = measure.getVoices().size();
			voiceCount = max(voiceCount, size);
		}
		return voiceCount;
	}


	/**
	 * Gets the {@link MP} of the given measure, or null if this staff is not
	 * part of a score or if the measure is not part of this staff.
	 */
	public MP getMP(Measure measure) {
		int measureIndex = measures.indexOf(measure);
		if (parent == null || measureIndex == -1)
			return null;
		int staffIndex = parent.getStaves().indexOf(this);
		if (staffIndex == -1)
			return null;
		return MP.atMeasure(staffIndex, measureIndex);
	}

	
	/**
	 * Convenience method. Gets the parent score of this staff,
	 * or null, if this element is not part of a score.
	 */
	public Score getScore() {
		return (parent != null ? parent.getScore() : null);
	}

}
