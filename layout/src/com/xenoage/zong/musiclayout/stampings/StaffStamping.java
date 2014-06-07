package com.xenoage.zong.musiclayout.stampings;

import lombok.ToString;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.StaffMarks;
import com.xenoage.zong.musiclayout.stampings.bitmap.StaffStampingBitmapInfo;

/**
 * Class for a staff stamping.
 *
 * @author Andreas Wenger
 */
@Const @ToString
public final class StaffStamping
	extends Stamping {

	/** Top-left (TODO: really?) position (top line) of the staff in mm. */
	public final Point2f position;
	/** Length of the staff in mm. */
	public final float length;
	/** The number of lines in this staff. */
	public final int linesCount;
	/** The interline space, i.e. the space between two staff lines in mm */
	public final float is;

	/** Musical position marks, to convert layout coordinates to musical positions */
	public final StaffMarks staffMarks;

	/** Cached information about the staff for screen display */
	public final StaffStampingBitmapInfo screenInfo;


	public StaffStamping(Point2f position, float length, int linesCount, float is,
		StaffMarks staffMarks) {
		super(null, Stamping.Level.Staff, null, new Rectangle2f(position, new Size2f(length,
			(linesCount - 1) * is /*TODO: line width! */)));
		this.position = position;
		this.length = length;
		this.linesCount = linesCount;
		this.is = is;
		this.staffMarks = staffMarks;
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
	 */
	public float getMeasureStartMm(int measureIndex) {
		return staffMarks.getMeasureMarksAt(measureIndex).getStartMm();
	}

	/**
	 * Gets the end position in mm of the leading spacing of the measure with the given global index,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 */
	public float getMeasureLeadingMm(int measureIndex) {
		return staffMarks.getMeasureMarksAt(measureIndex).getLeadingMm();
	}

	/**
	 * Gets the end position in mm of the measure with the given global index,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 */
	public float getMeasureEndMm(int measureIndex) {
		return staffMarks.getMeasureMarksAt(measureIndex).getEndMm();
	}

	/**
	 * Gets the global index of the first measure in this staff,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 */
	public int getStartMeasureIndex() {
		return staffMarks.getStartMeasureIndex();
	}

	/**
	 * Gets the global index of the last measure in this staff,
	 * or throws an {@link IllegalStateException} if positions are unknown.
	 */
	public int getEndMeasureIndex() {
		return staffMarks.getEndMeasureIndex();
	}

	/**
	 * See {@link StaffMarks#getMPAt(float)}.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 */
	public MP getMPAtX(float positionX) {
		return staffMarks.getMPAt(positionX);
	}

	/**
	 * See {@link StaffMarks#getXMmAt(int, Fraction)}.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 */
	public Float getXMmAt(int measureIndex, Fraction beat) {
		return staffMarks.getXMmAt(measureIndex, beat);
	}

	/**
	 * See {@link StaffMarks#getXMmAt(int, Fraction)}.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 */
	public Float getXMmAt(MP bmp) {
		return staffMarks.getXMmAt(bmp.measure, bmp.beat);
	}

	/**
	 * Gets the system index of this staff element, relative to its parent frame.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 */
	public int getSystemIndex() {
		return staffMarks.getSystemIndex();
	}

	/**
	 * Gets the scorewide staff index of this staff element.
	 * Throws an {@link IllegalStateException} if positions are unknown.
	 */
	public int getStaffIndex() {
		return staffMarks.getStaffIndex();
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.StaffStamping;
	}
	
	public StaffStamping withStaffMarks(StaffMarks staffMarks) {
		return new StaffStamping(position, length, linesCount, is, staffMarks);
	}

}
