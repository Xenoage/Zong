package com.xenoage.zong.core.music.direction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;



/**
 * Class for a pedal direction. Start or stop.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor public final class Pedal
	extends Direction {

	/** Start and stop marking type. */
	public enum Type {
		/** Depress the pedal. */
		Start,
		/** Release the pedal. */
		Stop;
	}

	/** The start or stop marking type. */
	@Getter private final Type type;

}
