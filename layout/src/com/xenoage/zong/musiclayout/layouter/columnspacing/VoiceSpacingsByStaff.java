package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.zong.musiclayout.spacing.measure.VoiceSpacing;

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
	
	public List<VoiceSpacing> getAll() {
		ArrayList<VoiceSpacing> ret = alist();
		for (List<VoiceSpacing> staff : staves)
			ret.addAll(staff);
		return ret;
	}
	
	public List<VoiceSpacing> getStaff(int staff) {
		return staves.get(staff);
	}

	public VoiceSpacing get(int staff, int voice) {
		return staves.get(staff).get(voice);
	}

}
