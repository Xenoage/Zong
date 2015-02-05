package com.xenoage.zong.musiclayout.layouter.columnspacing;

import java.util.List;

import com.xenoage.utils.collections.IList;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * A list of {@link VoiceSpacing}s, which can be accessed by
 * a staff index and a voice index.
 * 
 * @author Andreas Wenger
 */
public class VoiceSpacingsByStaff {

	public List<List<VoiceSpacing>> staves;


	public VoiceSpacingsByStaff(List<List<VoiceSpacing>> staves) {
		this.staves = staves;
	}
	
	public List<VoiceSpacing> getStaff(int staff) {
		return staves.get(staff);
	}

	public VoiceSpacing get(int staff, int voice) {
		return staves.get(staff).get(voice);
	}

}
