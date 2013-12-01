package com.xenoage.zong.musiclayout.layouter.cache.util;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * Middle stems of a still open beam.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public class OpenBeamMiddleStem {

	public final StaffStamping staff;
	public final Chord chord;
	public final StemDirection stemDirection;
	public final float positionX;
	public final float bottomNoteLP;
	public final float topNoteLP;

}
