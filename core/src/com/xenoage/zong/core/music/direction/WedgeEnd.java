package com.xenoage.zong.core.music.direction;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * End marker for a {@link Wedge}.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor public final class WedgeEnd
	extends Direction {

	/** The wedge that is ended by this marker. */
	@Getter private final Wedge wedge;

}
