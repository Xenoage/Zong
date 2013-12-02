package com.xenoage.zong.musiclayout.layouter.columnspacing;

import com.xenoage.utils.collections.IList;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * A list of {@link VoiceSpacing}s, which can be accessed by
 * a staff index and a voice index.
 * 
 * @author Andreas Wenger
 */
public class VoiceSpacingsByStaff {

	public IList<IList<VoiceSpacing>> staves;


	public VoiceSpacingsByStaff(IList<IList<VoiceSpacing>> staves) {
		this.staves = staves;
	}

	public VoiceSpacing get(int staff, int voice) {
		return staves.get(staff).get(voice);
	}

}
