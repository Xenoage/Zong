package com.xenoage.zong.core.music.volta;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;

/**
 * Class for a volta (also informally called "Haus" in German).
 * 
 * A volta is never used within a voice, but only in the {@link ColumnHeader}.
 * Voltas span over whole measures, at least one. The number of spanned
 * measures is saved in this class.
 * 
 * Each volta has a numbers attribute, which is a range which
 * tells in which repetitions it should be entered,
 * and optionally an arbitrary caption.
 * A downward hook on the right side is optional.
 *
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
@Data @EqualsAndHashCode(exclude="parent")
public final class Volta
	implements ColumnElement {

	/** The number of measures this volta spans, at least one. */
	private int length;
	/** The repetitions (beginning with 1) where this volta is entered, or null for the default case.
	 * E.g. [1,1] for the 1st time, ..., null for "else". */
	private Range numbers;
	/** The caption, or null to use a default caption, generated from the numbers:
	 * [x,x] results to "x.", [x,y] to "x.–y.", null to "". */
	private String caption;
	/** True, iff there is a downward hook on the right side. */
	private boolean rightHook;

	/** The parent measure column, or null if not part of a score. */
	private ColumnHeader parent;


	public Volta(int length, Range numbers, String caption, boolean rightHook) {
		if (length < 1)
			throw new IllegalArgumentException("Volta must span at least 1 measure");
		this.length = length;
		this.numbers = numbers;
		this.caption = caption;
		this.rightHook = rightHook;
	}

	/**
	 * Gets the caption of this volta.
	 * This is never null, but may be the empty string.
	 */
	public String getCaption() {
		if (caption != null)
			return caption;
		else if (numbers.getCount() == 1)
			return numbers.getStart() + ".";
		else
			return numbers.getStart() + ".–" + numbers.getStop() + ".";
	}

	/**
	 * Gets the caption of this volta, or null if unset.
	 */
	public String getCaptionOrNull() {
		return caption;
	}

}
