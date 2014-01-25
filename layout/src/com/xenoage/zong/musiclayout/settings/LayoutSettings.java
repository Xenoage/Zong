package com.xenoage.zong.musiclayout.settings;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.NonNull;

/**
 * Settings for the musical layout.
 * 
 * TODO: move to converter project: One instance of this class is used throughout the
 * whole application, and is loaded on demand from
 * "data/layout/default.xml".
 * 
 * All values are in interline spaces, unless otherwise stated.
 * 
 * Some of the default values are adapted from
 * "Ross: The Art of Music Engraving", page 77.
 * 
 * @author Andreas Wenger
 */
@Const @Data public final class LayoutSettings {

	//chord settings
	@NonNull public final ChordWidths chordWidths;
	@NonNull public final ChordWidths graceChordWidths;

	//spacings
	@NonNull public final Spacings spacings;

	//scalings
	public final float scalingClefInner; //clef in the middle of a staff
	public final float scalingGrace;

	//offsets
	public final float offsetMeasureStart; //offset of the first element in a measure
	public final float offsetBeatsMinimal; //minimal offset between to different beats

}
