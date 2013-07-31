package com.xenoage.zong.core.music.time;


/**
 * Symbols for time signatures.
 * 
 * @author Andreas Wenger
 */
public enum TimeSymbol {
	/** No symbol (for senza misura). */
	None,
	/** Common time symbol ("C") for a 4/4 time signature. */
	Common,
	/** Alla breve symbol (also known as "cut" symbol) for a 2/2 time signature. */
	AllaBreve,
	/** Time signature shown by numbers, e.g. 3/4 or 7/8. */
	Fractional;
}
