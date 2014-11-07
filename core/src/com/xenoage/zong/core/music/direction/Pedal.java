package com.xenoage.zong.core.music.direction;

import lombok.Data;
import lombok.EqualsAndHashCode;



/**
 * Class for a pedal direction. Start or stop.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper=false)
public final class Pedal
	extends Direction {

	/** Start and stop marking type. */
	public enum Type {
		/** Depress the pedal. */
		Start,
		/** Release the pedal. */
		Stop;
	}

	/** The start or stop marking type. */
	private final Type type;

}
