package com.xenoage.zong.musiclayout.layouter.cache.util;

import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;


/**
 * Middle stems of a still open beam.
 * 
 * @author Andreas Wenger
 */
public class OpenBeamMiddleStem
{
	
	public final StaffStamping staff;
	public final Chord chord;
	public final StemDirection stemDirection;
	public final float positionX;
	public final float bottomNoteLP;
	public final float topNoteLP;
	
	
	public OpenBeamMiddleStem(StaffStamping staff, Chord chord,
		StemDirection stemDirection, float positionX, float bottomNoteLP, float topNoteLP)
	{
		this.staff = staff;
		this.chord = chord;
		this.stemDirection = stemDirection;
		this.positionX = positionX;
		this.bottomNoteLP = bottomNoteLP;
		this.topNoteLP = topNoteLP;
	}
	

}
