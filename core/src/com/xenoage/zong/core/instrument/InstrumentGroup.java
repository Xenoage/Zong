package com.xenoage.zong.core.instrument;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Instrument group, like woodwind, brass or keyboard.
 * 
 * Two instances are equal, if their ID is equal.
 * 
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(exclude="name")
public final class InstrumentGroup {

	/** The unique ID of the instrument group. */
	private final String id;
	/** International name. */
	private String name;

}
