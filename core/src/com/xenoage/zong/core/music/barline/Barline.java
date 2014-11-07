package com.xenoage.zong.core.music.barline;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Measure;

/**
 * Class for a barline.
 * 
 * Though barlines can be placed in the middle of a measure, usually they are used
 * as the explicit start or end barline of a measure.
 * 
 * They are never placed within a {@link Measure}, but
 * always within a {@link ColumnHeader}.
 *
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(exclude = "parent")
public final class Barline
	implements ColumnElement {

	/** The style of the line(s). */
	@NonNull private BarlineStyle style;
	/** The repeat style. */
	@NonNull private BarlineRepeat repeat;
	/** The number of repeats. Only used for backward repeats, otherwise 0. */
	private int repeatTimes;

	/** Back reference: the parent column header, or null if not part of a score. */
	private ColumnHeader parent = null;


	/**
	 * Creates a barline without repeat.
	 * @param style  the style of the line(s)
	 */
	public static Barline barline(BarlineStyle style) {
		return new Barline(style, BarlineRepeat.None, 0);
	}

	/**
	 * Creates a regular barline.
	 */
	public static Barline barlineRegular() {
		return new Barline(BarlineStyle.Regular, BarlineRepeat.None, 0);
	}

	/**
	 * Creates a barline with forward repeat.
	 * @param style        the style of the line(s)
	 */
	public static Barline barlineForwardRepeat(BarlineStyle style) {
		return new Barline(style, BarlineRepeat.Forward, 0);
	}

	/**
	 * Creates a barline with backward repeat.
	 * @param style        the style of the line(s)
	 * @param repeatTimes  the number of repeats
	 */
	public static Barline barlineBackwardRepeat(BarlineStyle style, int repeatTimes) {
		return new Barline(style, BarlineRepeat.Backward, repeatTimes);
	}

	/**
	 * Creates a barline with repeat at both sides. This barline is only
	 * supported as a mid-measure barline!
	 * @param style        the style of the line(s)
	 * @param repeatTimes  the number of repeats
	 */
	public static Barline barlineMiddleBothRepeat(BarlineStyle style, int repeatTimes) {
		return new Barline(style, BarlineRepeat.Both, repeatTimes);
	}

	private Barline(BarlineStyle style, BarlineRepeat repeat, int repeatTimes) {
		checkArgsNotNull(style, repeat);
		this.style = style;
		this.repeat = repeat;
		this.repeatTimes = repeatTimes;
	}

	@Override public String toString() {
		return "Barline [style=" + style + ", repeat=" + repeat + ", repeatTimes=" + repeatTimes + "]";
	}

}
