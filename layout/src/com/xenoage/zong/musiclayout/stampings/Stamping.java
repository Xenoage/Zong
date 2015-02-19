package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import lombok.AllArgsConstructor;
import lombok.ToString;

import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.math.geom.Shape;

/**
 * Stampings can be visible objects like notes, clefs, texts, but
 * also invisible objects like empty rooms between staves.
 * 
 * Stamps were used in the early days of music notation to paint the symbols.
 * This class is called stamping, because it is the result of placing a stamp,
 * that means, in most cases, a given symbol at a given position. 
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @ToString
public abstract class Stamping {

	/**
	 * The musical level to which a stamping belongs to.
	 */
	public enum Level {
		/** Empty space. */
		EmptySpace,
		/** Staff. */
		Staff,
		/** Notes, barlines, ... */
		Music,
		/** Text, dynamic symbols, ... */
		Text;
	}


	/**
	 * The parent staff stamping of this staff or null.
	 * This is important for the renderer, when it needs some
	 * information from the parent staff of this element.
	 * But the stamping may also belong to more than only this staff.
	 */
	public StaffStamping parentStaff;

	/**
	 * Bounding geometry, or null if unknown or not needed.
	 */
	public final Shape boundingShape;

	/**
	 * Gets the type of this stamping.
	 */
	@Optimized(Performance)
	public abstract StampingType getType();
	
	/**
	 * Gets the musical level to which this stamping belongs to.
	 */
	public abstract Level getLevel();

}
