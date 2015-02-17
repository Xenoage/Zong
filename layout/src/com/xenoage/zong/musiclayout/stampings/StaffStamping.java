package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;
import lombok.ToString;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.bitmap.StaffStampingBitmapInfo;

/**
 * Class for a staff stamping.
 *
 * @author Andreas Wenger
 */
@Const @ToString
public class StaffStamping
	extends Stamping {

	/** Top-left (TODO: really?) position (top line) of the staff in mm. */
	public final Point2f position;
	/** Length of the staff in mm. */
	public final float length;
	/** The number of lines in this staff. */
	public final int linesCount;
	/** The interline space, i.e. the space between two staff lines, in mm */
	public final float is;

	/** The system this staff belongs to. */
	public final SystemSpacing system;
	/** The staff index. */
	@Getter public final int staffIndex;

	/** Cached information about the staff for screen display */
	public final StaffStampingBitmapInfo screenInfo;


	public StaffStamping(SystemSpacing system, int staffIndex,
		Point2f position, float length, int linesCount, float is) {
		super(null, Stamping.Level.Staff, null, new Rectangle2f(position, new Size2f(length,
			(linesCount - 1) * is /*TODO: line width! */)));
		this.system = system;
		this.staffIndex = staffIndex;
		this.position = position;
		this.length = length;
		this.linesCount = linesCount;
		this.is = is;
		this.screenInfo = new StaffStampingBitmapInfo(this);
	}

	/**
	 * Gets the width of the line in mm.
	 */
	public float getLineWidth() //TODO: everything in interline spaces
	{
		return is / 8f; //TODO: allow custom values
	}

	/**
	 * Computes and returns the y-coordinate of an object
	 * on the given line position in mm.
	 * Also non-integer values (fractions of interline spaces)
	 * are allowed.
	 */
	public float computeYMm(float lp) {
		return position.y + (linesCount - 1) * is - lp * is / 2 + getLineWidth() / 2;
	}

	/**
	 * Computes and returns the y-coordinate of an object
	 * at the given vertical position in mm as a line position.
	 * Also non-integer values are allowed.
	 */
	public float computeYLP(float mm) {
		return (position.y + (linesCount - 1) * is + getLineWidth() / 2 - mm) * 2 / is;
	}

	/**
	 * Gets the start position in mm of the measure with the given global index,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public float getMeasureStartMm(int measureIndex) {
		return system.getMeasureStartMm(measureIndex);
	}

	/**
	 * Gets the end position in mm of the leading spacing of the measure with the given global index,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public float getMeasureLeadingMm(int measureIndex) {
		return system.getMeasureStartAfterLeadingMm(measureIndex);
	}

	/**
	 * Gets the end position in mm of the measure with the given global index,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public float getMeasureEndMm(int measureIndex) {
		return system.getMeasureEndMm(measureIndex);
	}

	/**
	 * Gets the global index of the first measure in this staff,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public int getStartMeasureIndex() {
		return system.getStartMeasureIndex();
	}

	/**
	 * Gets the global index of the last measure in this staff,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public int getEndMeasureIndex() {
		return system.getEndMeasureIndex();
	}

	/**
	 * See {@link StaffMarks#getMPAt(float)}.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public MP getMPAtX(float positionX) {
		return system.getMpAt(positionX, staffIndex);
	}

	/**
	 * See {@link StaffMarks#getXMmAt(int, Fraction)}.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public float getXMmAt(int measureIndex, Fraction beat) {
		return system.getXMmAt(measureIndex, beat);
	}

	/**
	 * See {@link StaffMarks#getXMmAt(int, Fraction)}.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public float getXMmAt(MP bmp) {
		return system.getXMmAt(bmp.measure, bmp.beat);
	}

	/**
	 * Gets the system index of this staff element, relative to its parent frame.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 * @deprecated call method from {@link SystemSpacing} directly
	 */
	public int getSystemIndex() {
		return system.getSystemIndexInFrame();
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.StaffStamping;
	}

}
