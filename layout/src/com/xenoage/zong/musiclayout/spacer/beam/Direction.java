package com.xenoage.zong.musiclayout.spacer.beam;

/**
 * Beam direction.
 * 
 * @author Andreas Wenger
 */
public enum Direction {
	Horizontal,
	Ascending,
	Descending;
	
	public int getSign() {
		switch (this) {
			case Horizontal: return 0;
			case Ascending: return 1;
			case Descending: return -1;
		}
		return 0;
	}
}
