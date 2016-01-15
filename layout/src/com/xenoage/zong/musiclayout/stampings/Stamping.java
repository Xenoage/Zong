package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import static com.xenoage.utils.math.geom.NoShape.noShape;

import java.util.Collections;
import java.util.List;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.math.geom.NoShape;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.musiclayout.notation.Notation;

import lombok.ToString;

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
@ToString
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
	 * An empty immutable list of {@link Stamping}s.
	 */
	public static final List<Stamping> emptyList = Collections.emptyList();
	

	/**
	 * Gets the bounding geometry, or a {@link NoShape} if unknown or not needed.
	 */
	@MaybeNull public Shape getBoundingShape() {
		return noShape;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Optimized(Performance)
	public abstract StampingType getType();
	
	/**
	 * Gets the musical level to which this stamping belongs to.
	 */
	public abstract Level getLevel();
	
	/**
	 * Gets the {@link MusicElement} or {@link Notation} behind the stamping, or null.
	 * TIDY
	 */
	public Object getElement() {
		return null;
	}
	
	/**
	 * Gets the {@link MusicElement} behind the stamping, or null.
	 */
	public MusicElement getMusicElement() {
		Object element = getElement();
		if (element instanceof Notation)
			return ((Notation) element).getElement();
		else if (element instanceof MusicElement)
			return (MusicElement) element;
		return null;
	}

}
