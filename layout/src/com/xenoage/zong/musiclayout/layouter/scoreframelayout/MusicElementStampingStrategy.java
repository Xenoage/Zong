package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.notations.TimeNotation;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.ClefStamping;
import com.xenoage.zong.musiclayout.stampings.CommonTimeStamping;
import com.xenoage.zong.musiclayout.stampings.KeySignatureStamping;
import com.xenoage.zong.musiclayout.stampings.NormalTimeStamping;
import com.xenoage.zong.musiclayout.stampings.RestStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Strategy to create stampings for elements like rests,
 * clefs, keys and time signatures.
 * 
 * @author Andreas Wenger
 */
public class MusicElementStampingStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Creates a stamping for the given rest.
	 */
	public RestStamping createRestStamping(RestNotation rest, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		return new RestStamping(rest.element, DurationInfo.getRestType(rest.element
			.getDuration()), staff, positionX, 1, symbolPool);
	}

	/**
	 * Creates a stamping for the given clef.
	 */
	public ClefStamping createClefStamping(ClefNotation clef, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		return new ClefStamping(clef.element, staff, positionX, clef.scaling, symbolPool);
	}

	/**
	 * Creates a stamping for the given key signature.
	 */
	public KeySignatureStamping createKeyStamping(TraditionalKeyNotation key, float positionX,
		StaffStamping staff, SymbolPool symbolPool, LayoutSettings layoutSettings) {
		return new KeySignatureStamping(key.element, key.c4Lp,
			key.minLp, positionX, staff, symbolPool, layoutSettings);
	}

	/**
	 * Creates a stamping for the given time signature.
	 */
	public Stamping createTimeStamping(TimeNotation time, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		if (time.element.getType() == TimeType.timeCommon) {
			return new CommonTimeStamping(time.element, positionX, staff, symbolPool);
		}
		else {
			return new NormalTimeStamping(time.element, positionX, staff, time.numeratorOffset,
				time.denominatorOffset, time.digitGap);
		}
	}

}
