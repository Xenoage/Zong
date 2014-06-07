package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.ToString;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;

/**
 * Stampings can be visible objects like notes, clefs, texts, but
 * also invisible objects like empty rooms between staves and so on are possible.
 * 
 * Stamps were used in the early days of music notation to paint the symbols.
 * This class is called stamping, because it is the result of placing a stamp,
 * that means, in most cases, a given symbol at a given position. 
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @ToString
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
	public final StaffStamping parentStaff;

	/** The musical level to which this stamping belongs to. */
	public final Level level;

	/**
	 * The musical element for which this stamping was created,
	 * or null, if not availabe (e.g. for staves).
	 * This may be another element than expected, e.g. an accidental layout
	 * element may refer to a chord musical element.
	 */
	public final MusicElement musicElement;

	/**
	 * Bounding geometry, or null if unknown or not needed.
	 */
	public final Shape boundingShape;


	/**
	 * Gets the type of this stamping.
	 */
	public abstract StampingType getType();

}
