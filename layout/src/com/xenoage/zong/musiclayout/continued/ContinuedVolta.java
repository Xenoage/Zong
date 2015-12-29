package com.xenoage.zong.musiclayout.continued;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.volta.Volta;

/**
 * Continued {@link Volta}.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class ContinuedVolta
	implements ContinuedElement {

	@Getter public final Volta element;

}
