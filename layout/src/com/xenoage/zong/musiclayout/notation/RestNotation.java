package com.xenoage.zong.musiclayout.notation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;

/**
 * This class contains layout information about a rest.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class RestNotation
	implements Notation {

	@Getter public final Rest element;
	public final ElementWidth width;
	public final DurationInfo.Type duration;


	@Override public ElementWidth getWidth() {
		return width;
	}

}
