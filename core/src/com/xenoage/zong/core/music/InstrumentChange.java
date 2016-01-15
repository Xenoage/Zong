package com.xenoage.zong.core.music;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.position.MP;


/**
 * This element indicates the change of an instrument
 * within a staff.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class InstrumentChange
	implements MeasureElement {

	/** The instrument to switch to. */
	@Getter @Setter @NonNull private Instrument instrument;

	/** Back reference: The parent measure, or null if not part of a measure. */
	@Getter @Setter private Measure parent;

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.InstrumentChange;
	}
	
	@Override public MP getMP() {
		return MP.getMPFromParent(this);
	}
	
}
