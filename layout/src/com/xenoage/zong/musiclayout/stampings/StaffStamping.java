package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.bitmap.StaffStampingBitmapInfo;

/**
 * Class for a staff stamping.
 *
 * @author Andreas Wenger
 */
@Const @RequiredArgsConstructor @Getter @ToString
public class StaffStamping
	extends Stamping {

	/** The system this staff belongs to. */
	public final SystemSpacing system;
	/** The staff index. */
	@Getter public final int staffIndex;
	
	/** Top-left (TODO: really?) position (top line) of the staff in mm. */
	public final Point2f positionMm;
	/** Length of the staff in mm. */
	public final float lengthMm;
	/** The number of lines in this staff. */
	public final int linesCount;
	/** The interline space, i.e. the space between two staff lines, in mm */
	public final float is;

	

	/** Cached information about the staff for screen display */
	@Optimized(Performance)
	private StaffStampingBitmapInfo cachedBitmapInfo;


	/**
	 * Gets the width of the line in mm.
	 */
	public float getLineWidthMm() {
		return is / 8f; //TODO: allow custom values
	}

	/**
	 * Computes and returns the y-coordinate of an object
	 * on the given line position in mm in frame space.
	 * Also non-integer values (fractions of interline spaces)
	 * are allowed.
	 */
	public float computeYMm(float lp) {
		return positionMm.y + (linesCount - 1) * is - lp * is / 2;
	}

	/**
	 * Computes and returns the y-coordinate of an object
	 * at the given vertical position in mm in frame space as a line position.
	 * Also non-integer values are allowed.
	 */
	public float computeYLp(float mm) {
		return (positionMm.y + (linesCount - 1) * is - mm) * 2 / is;
	}

	/**
	 * See {@link SystemSpacing#getMpAt(float, int)}.
	 */
	public MP getMpAtX(float positionX) {
		return system.getMpAt(positionX, staffIndex);
	}
	
	public StaffStampingBitmapInfo getBitmapInfo() {
		if (cachedBitmapInfo == null)
			cachedBitmapInfo = new StaffStampingBitmapInfo(this);
		return cachedBitmapInfo;
	}
	
	@Override public Shape getBoundingShape() {
		return new Rectangle2f(positionMm, new Size2f(lengthMm, (linesCount - 1) * is /*TODO: line width! */));
	}

	@Override public StampingType getType() {
		return StampingType.StaffStamping;
	}

	@Override public Level getLevel() {
		return Level.Staff;
	}

}
