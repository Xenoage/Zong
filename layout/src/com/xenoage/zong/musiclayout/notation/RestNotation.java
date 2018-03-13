package com.xenoage.zong.musiclayout.notation;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.Duration;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class contains layout information about a rest.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class RestNotation
	implements Notation {

	@Getter public final Rest element;
	public final ElementWidth width;
	public final Duration.Type duration;

	@Override public MP getMp() {
		return element.getMP();
	}

	@Override public ElementWidth getWidth() {
		return width;
	}

}
