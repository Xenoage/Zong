package com.xenoage.zong.core.music.direction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * Class for crescendos and diminuendos.
 * 
 * To create a wedge, create an instance of this class,
 * retrieve its end marker by calling {@link #getWedgeEnd()} and
 * place it anywhere after the instance of this class. The wedge ends
 * at the element at which the end marker is placed.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class Wedge
	extends Direction {

	/** Crescendo or diminuendo. */
	@Getter private final WedgeType type;
	/** End marker of this wedge. */
	@Getter private final WedgeEnd wedgeEnd = new WedgeEnd(this);
	/** Height of the opening in IS, or null for default. */
	@Getter @Setter private Float spread;

}
