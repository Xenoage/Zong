package com.xenoage.zong.musiclayout.continued;

import com.xenoage.zong.core.music.direction.Wedge;


/**
 * Continued {@link Wedge}.
 * 
 * @author Andreas Wenger
 */
public final class ContinuedWedge
	implements ContinuedElement
{
	
	public final Wedge wedge;
	public final int staffIndex;
	
	
	public ContinuedWedge(Wedge wedge, int staffIndex)
	{
		this.wedge = wedge;
		this.staffIndex = staffIndex;
	}
	
	
	@Override public Wedge getMusicElement()
	{
		return wedge;
	}
	
	
	@Override public int getStaffIndex()
	{
		return staffIndex;
	}

}
