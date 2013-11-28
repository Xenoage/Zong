package com.xenoage.zong.musiclayout.continued;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.volta.Volta;

/**
 * Continued {@link Volta}.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ContinuedVolta
	implements ContinuedElement {

	public final Volta volta;
	public final int startMeasureIndex;
	public final int staffIndex;


	@Override public Volta getMusicElement() {
		return volta;
	}

	@Override public int getStaffIndex() {
		return staffIndex;
	}

}
