package com.xenoage.utils.math;

/**
 * Vertical side: top or bottom.
 * 
 * @author Andreas Wenger
 */
public enum VSide {
	Top,
	Bottom;

	public int getDir() {
		if (this == Bottom)
			return -1;
		else
			return 1;
	}

}
