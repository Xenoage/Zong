package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;
import lombok.ToString;

import com.xenoage.utils.annotations.Const;
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
		super(null, new Rectangle2f(position, new Size2f(length,
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
	 * See {@link SystemSpacing#getMpAt(float, int)}.
	 */
	public MP getMpAtX(float positionX) {
		return system.getMpAt(positionX, staffIndex);
	}

	@Override public StampingType getType() {
		return StampingType.StaffStamping;
	}

	@Override public Level getLevel() {
		return Level.Staff;
	}
	
}
