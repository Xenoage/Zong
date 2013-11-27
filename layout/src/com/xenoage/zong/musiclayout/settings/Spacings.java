package com.xenoage.zong.musiclayout.settings;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;

/**
 * Settings for spacings (distances) in IS.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public class Spacings {

	//chord spacings
	public final ChordSpacings normalChordSpacings;
	public final ChordSpacings graceChordSpacings;

	//distances
	public final float widthSharp, widthFlat, widthClef, widthMeasureEmpty, widthDistanceMin;

}
