package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;

/**
 * This class contains layout information about a rest.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class RestNotation
	implements Notation {

	public final Rest element;
	public final ElementWidth width;


	@Override public ElementWidth getWidth() {
		return width;
	}

	@Override public Rest getMusicElement() {
		return element;
	}

}
