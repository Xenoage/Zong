package com.xenoage.zong.musiclayout.notations.beam;

import com.xenoage.zong.core.music.chord.StemDirection;


/**
 * Contains the directions of the stems
 * of a single {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public final class BeamStemDirections
{
	
	private final StemDirection[] stemDirections;

	
	public BeamStemDirections(StemDirection[] stemDirections)
	{
		this.stemDirections = stemDirections;
	}
	
	
	public StemDirection[] getStemDirections()
	{
		return stemDirections;
	}
	

}
