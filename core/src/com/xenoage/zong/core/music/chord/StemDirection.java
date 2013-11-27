package com.xenoage.zong.core.music.chord;

import com.xenoage.utils.math.VSide;

/**
 * Direction of a chord stem.
 *
 * @author Andreas Wenger
 */
public enum StemDirection {
	/** No stem. */
	None,
	/** Upward stem. */
	Up,
	/** Downward stem. */
	Down;

	/**
	 * Gets the direction of the stem as its signum:
	 * 1 for up, -1 for down, 0 for none.
	 */
	public int getSign() {
		switch (this) {
			case Up:
				return 1;
			case Down:
				return -1;
			default:
				return 0;
		}
	}

	/**
	 * Returns if the given {@link VSide} has the same side as this stem.
	 */
	public boolean equalsSide(VSide side) {
		return ((this == Up && side == VSide.Top) || (this == Down && side == VSide.Bottom));
	}

}
