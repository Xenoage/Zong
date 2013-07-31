package com.xenoage.zong.core.instrument;

import lombok.Data;


/**
 * Instrument group, like woodwind, brass or
 * keyboard.
 * 
 * @author Andreas Wenger
 */
@Data public final class InstrumentGroup
{
	
	/** The unique ID of the instrument group. */
	private final String id;
	/** International name. */
	private String name;
	
	
	/**
	 * Returns true, if the given object is an {@link InstrumentGroup} with the
	 * same ID as this one, otherwise false.
	 */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		else if (o instanceof InstrumentGroup) {
			return id.equals(((InstrumentGroup) o).id);
		}
		else {
			return false;
		}
	}


	/**
	 * Gets the hash code of this instrument group.
	 * This is the hash code of its ID.
	 */
	@Override public int hashCode() {
		return id.hashCode();
	}
}
