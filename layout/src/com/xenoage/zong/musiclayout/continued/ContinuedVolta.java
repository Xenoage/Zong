package com.xenoage.zong.musiclayout.continued;

import com.xenoage.zong.core.music.volta.Volta;


/**
 * Continued volta.
 * 
 * @author Andreas Wenger
 */
public final class ContinuedVolta
	implements ContinuedElement
{
	
	public final Volta volta;
	public final int startMeasureIndex;
	public final int staffIndex;
	
	
	public ContinuedVolta(Volta volta, int startMeasureIndex, int staffIndex)
	{
		this.volta = volta;
		this.startMeasureIndex = startMeasureIndex;
		this.staffIndex = staffIndex;
	}
	
	
	@Override public Volta getMusicElement()
	{
		return volta;
	}
	
	
	@Override public int getStaffIndex()
	{
		return staffIndex;
	}

}
