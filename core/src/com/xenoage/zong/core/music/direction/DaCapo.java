package com.xenoage.zong.core.music.direction;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.position.MP;


/**
 * Class for a DaCapo.
 * 
 * It must always be written to the {@link ColumnHeader}.
 * 
 * @Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class DaCapo
	extends Direction
	implements NavigationSign {

	/** True, iff repeats should be played after jumping back. */
	private boolean conRepetizione = true;

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.DaCapo;
	}
	
	@Override public MP getMP() {
		return MP.getMPFromParent(this);
	}
	
}
