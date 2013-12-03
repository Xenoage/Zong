package com.xenoage.zong.core.music.format;

import lombok.Data;


/**
 * Custom positioning of an object.
 * 
 * @author Andreas Wenger
 */
@Data public final class Position
	implements Positioning {

	/** The x coordinate in mm, or null for default. */
	public final Float x;
	/** The y coordinate in LP, or null for default. */
	public final Float y;
	/** An additional horizontal offset in mm, or null for 0. */
	public final Float relativeX;
	/** An additional vertical offset in LP, or null for 0. */
	public final Float relativeY;
	

	/**
	 * Returns the additional horizontal offset of the given {@link Position},
	 * or 0 if the position is null or the offset is null.
	 */
	public static float getRelativeX(Position p) {
		return (p == null || p.relativeX == null ? 0 : p.relativeX);
	}


	/**
	 * Returns the additional vertical offset of the given {@link Position},
	 * or 0 if the position is null or the offset is null.
	 */
	public static float getRelativeY(Position p) {
		return (p == null || p.relativeY == null ? 0 : p.relativeY);
	}


	/**
	 * Gets the {@link Position} in the given {@link Positioning}, or null
	 * if it is no {@link Position}.
	 */
	public static Position asPosition(Positioning positioning) {
		if (positioning instanceof Position)
			return (Position) positioning;
		else
			return null;
	}


}
