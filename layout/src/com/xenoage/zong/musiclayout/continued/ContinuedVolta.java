package com.xenoage.zong.musiclayout.continued;

import com.xenoage.zong.core.music.volta.Volta;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Continued {@link Volta}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public final class ContinuedVolta
	implements ContinuedElement {

	@Getter public Volta element;

}
