package com.xenoage.zong.core.music.volta;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.position.MP;

/**
 * Class for a volta (also called "ending" in MusicMXL, and informally called "Haus" in German).
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
 * The rules for ordering consecutive voltas (called a volta group) are as follows:
 * The voltas do not have to be sorted (even this makes sense in most cases), e.g.
 * the "2nd time" volta may appear before the "1st time" volta. If there are
 * voltas without a number, called default voltas, the first one of it is played
 * each time when a volta with the explicit repeat time number is missing. When
 * the last volta of a group is a default volta, it is always played (the very last time).
 * When there is a gap (e.g. during the 2nd playback when no "2nd time" and no default
 * volta exists), the whole repeat is skipped.
 *
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
@Data @EqualsAndHashCode(exclude="parent") @ToString(exclude="parent")
public final class Volta
	implements ColumnElement {
	
	public static final String dash = "–";

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
		else if (numbers == null)
			return "";
		else if (numbers.getCount() == 1)
			return numbers.getStart() + ".";
		else
			return numbers.getStart() + "." + dash + numbers.getStop() + ".";
	}

	/**
	 * Gets the caption of this volta, or null if unset.
	 */
	public String getCaptionOrNull() {
		return caption;
	}

	/**
	 * Returns true, if this volta is the default case,
	 * i.e. not for an explicit repeat time like 1st or 2nd time.
	 */
	public boolean isDefault() {
		return numbers == null;
	}

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.Volta;
	}
	
	@Override public MP getMP() {
		return MP.getMPFromParent(this);
	}
	
	/**
	 * Gets the index of the measure, where this volta ends (inclusive).
	 */
	public int getEndMeasureIndex() {
		return getMP().measure + length - 1;
	}
	
}
