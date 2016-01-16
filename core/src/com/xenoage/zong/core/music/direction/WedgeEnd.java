package com.xenoage.zong.core.music.direction;

import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.position.MP;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * End marker for a {@link Wedge}.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false, exclude="wedge")
public final class WedgeEnd
	extends Direction {

	/** The wedge that is ended by this marker. */
	private final Wedge wedge;

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.WedgeEnd;
	}
	
	@Override public MP getMP() {
		return MP.getMPFromParent(this);
	}
	
}
