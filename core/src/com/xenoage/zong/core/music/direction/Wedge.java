package com.xenoage.zong.core.music.direction;

import lombok.Data;
import lombok.EqualsAndHashCode;


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
@Data @EqualsAndHashCode(callSuper=false)
public final class Wedge
	extends Direction {

	/** Crescendo or diminuendo. */
	private final WedgeType type;
	/** End marker of this wedge. */
	private final WedgeEnd wedgeEnd = new WedgeEnd(this);
	/** Height of the opening in IS, or null for default. */
	private Float spread;

}
