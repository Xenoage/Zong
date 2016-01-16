package com.xenoage.zong.musiclayout.settings;

import lombok.AllArgsConstructor;

import static com.xenoage.zong.musiclayout.settings.ChordSpacings.defaultChordSpacingsGrace;
import static com.xenoage.zong.musiclayout.settings.ChordSpacings.defaultChordSpacingsNormal;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.NonNull;

/**
 * Settings for spacings (distances) in IS.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public class Spacings {

	//chord spacings
	@NonNull public final ChordSpacings normalChordSpacings;
	@NonNull public final ChordSpacings graceChordSpacings;

	//distances
	public final float widthSharp, widthFlat, widthClef, widthMeasureEmpty, widthDistanceMin;
	
	public static Spacings defaultSpacings = new Spacings(defaultChordSpacingsNormal,
		defaultChordSpacingsGrace, 1.2f, 1f, 3f, 8f, 1f);

}
