package com.xenoage.zong.core.music.direction;

import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.position.MP;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Class for a dynamic sign like forte, piano, sforzando and so on.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class Dynamic
	extends Direction {

	private final DynamicValue value;

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.Dynamic;
	}
	
	@Override public MP getMP() {
		return MP.getMPFromParent(this);
	}
	
}
