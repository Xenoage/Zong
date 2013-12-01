package com.xenoage.zong.musiclayout.layouter.cache.util;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * This class is used by the layouter
 * to collect the stems connected by one beam.
 * 
 * The middle stems are stamped last, so only
 * their horizontal and both possible vertical
 * start positions are saved.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter public class BeamedStemStampings {

	/** The {@link Beam} instance this list of beamed stems belongs to. */
	private Beam beam;
	/** The first stem of the beam. */
	@Setter private StemStamping firstStem;
	/** The middle stems of the beam (all but the first and the last one). */
	private ArrayList<OpenBeamMiddleStem> middleStems;
	/** The last stem of the beam. */
	@Setter private StemStamping lastStem;


	/**
	 * Creates an empty list of beamed stems
	 * for the given {@link Beam} instance.
	 */
	public BeamedStemStampings(Beam beam) {
		this(beam, null, new ArrayList<OpenBeamMiddleStem>(), null);
	}

	/**
	 * Gets the number of stems.
	 */
	public int getStemsCount() {
		return this.middleStems.size() + 2;
	}

	/**
	 * Adds a stem to the middle part of beam.
	 */
	public void addMiddleStem(OpenBeamMiddleStem middleStem) {
		middleStems.add(middleStem);
	}

	/**
	 * Gets the horizontal position of the stem with the given index
	 * (it may be the first, last or any middle stem).
	 */
	public float getStemX(int index) {
		if (index == 0)
			return this.firstStem.xMm;
		else if (index == middleStems.size() + 1)
			return this.lastStem.xMm;
		else
			return this.middleStems.get(index - 1).positionX;
	}

}
