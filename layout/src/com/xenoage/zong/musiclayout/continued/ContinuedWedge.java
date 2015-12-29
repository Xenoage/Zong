package com.xenoage.zong.musiclayout.continued;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.Wedge;

/**
 * Continued {@link Wedge}.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class ContinuedWedge
	implements ContinuedElement {

	@Getter public final Wedge element;
	@Getter public final int staffIndex;

}
