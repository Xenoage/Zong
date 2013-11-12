package com.xenoage.zong.musiclayout.layouter.columnspacing;

import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;


/**
 * A list of voice spacings, which can be accessed by
 * a staff index and a voice index.
 * 
 * @author Andreas Wenger
 */
public class VoiceSpacingsByStaff
{
	
	public final Vector<Vector<VoiceSpacing>> staves;

	
	public VoiceSpacingsByStaff(Vector<Vector<VoiceSpacing>> staves)
	{
		this.staves = staves;
	}
	
	
	public VoiceSpacing get(int staff, int voice)
	{
		return staves.get(staff).get(voice);
	}
	

}
