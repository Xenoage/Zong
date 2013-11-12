package com.xenoage.zong.musiclayout.continued;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.curvedline.CurvedLine;


/**
 * Continued slur or tie.
 * 
 * The {@link MusicElement} is known, as well as the placement
 * (above or below staff), the global index of the staff
 * and the level of the slur (0: first slur, 1: another slur, ...
 * used for the vertical position, to avoid collisions).
 * 
 * @author Andreas Wenger
 */
public final class ContinuedCurvedLine
	implements ContinuedElement
{
	
	public final CurvedLine curvedLine;
	public final VSide side;
	public final int staffIndex;
	public final int level;
	
	
	public ContinuedCurvedLine(CurvedLine curvedLine, VSide side, int staffIndex, int level)
	{
		this.curvedLine = curvedLine;
		this.side = side;
		this.staffIndex = staffIndex;
		this.level = level;
	}


	@Override public CurvedLine getMusicElement()
	{
		return curvedLine;
	}


	@Override public int getStaffIndex()
	{
		return staffIndex;
	}

}
