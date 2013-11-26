package com.xenoage.zong.layout.frames.background;

import lombok.Data;

import com.xenoage.utils.color.Color;

/**
 * A single color background for a frame.
 * 
 * @author Andreas Wenger
 */
@Data public final class ColorBackground
	implements Background {

	private Color color;


	/**
	 * Creates a background with the given color.
	 */
	public ColorBackground(Color color) {
		this.color = color;
	}

	/**
	 * Gets the type of this background.
	 */
	@Override public BackgroundType getType() {
		return BackgroundType.Color;
	}

}
