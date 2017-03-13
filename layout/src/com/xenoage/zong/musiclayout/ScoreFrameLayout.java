package com.xenoage.zong.musiclayout;

import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.annotations.Optimized.Reason;
import com.xenoage.utils.iterators.MultiListIt;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StampingType;
import com.xenoage.zong.musiclayout.stampings.TextStamping;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

/**
 * A score frame layout contains the musical layout of a
 * score for one score frame.
 * 
 * This includes the musical stampings as well as the current
 * selection and playback stampings. A list of continued elements
 * (e.g. slurs or voltas) to the following frame is also saved.
 * 
 * @author Andreas Wenger
 */
@ToString
public final class ScoreFrameLayout {

	/** Information about the systems in this frame. */
	@Getter private FrameSpacing frameSpacing;

	/** The list of all staff stampings of this frame. Staff stampings and
	 * other stampings are divided for performance reasons. */
	@Optimized(Reason.Performance)
	@Getter private ArrayList<StaffStamping> staffStampings;
	/** The list of all other stampings of this frame. Staff stampings and
	 * other stampings are divided for performance reasons.*/
	@Optimized(Reason.Performance)
	@Getter private ArrayList<Stamping> otherStampings;

	/** The list of continued elements, that means the unclosed elements
	 * that must also be painted on the next frame. */
	@Getter private ArrayList<ContinuedElement> continuedElements;

	//TODO: move into other class
	/** The list of the selection stampings of this frame. */
	@Getter @Setter private List<? extends Stamping> selectionStampings = alist();
	/** The list of the playback stampings of this frame. */
	@Getter @Setter private List<? extends Stamping> playbackStampings = alist();

	
	public ScoreFrameLayout(FrameSpacing frameArrangement, ArrayList<StaffStamping> staffStampings,
		ArrayList<Stamping> otherStampings, ArrayList<ContinuedElement> continuedElements)
	{
		this.frameSpacing = frameArrangement;
		this.staffStampings = staffStampings;
		this.otherStampings = otherStampings;
		this.continuedElements = continuedElements;
	}


	/**
	 * Gets all musical stampings (staves, notes, ...) of this frame,
	 * but not selections or playback stampings.
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Stamping> getMusicalStampings() {
		return new MultiListIt<>(staffStampings, otherStampings);
	}

	/**
	 * Gets all stampings of this frame, including both the musical
	 * and the status stampings (selection, playback).
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Stamping> getAllStampings() {
		return new MultiListIt<>(staffStampings, otherStampings, selectionStampings,
				playbackStampings);
	}

	/**
	 * Returns the {@link Stamping} under the given position
	 * in score layout coordinates (mm relative to the
	 * upper left corner) or null, if there is none.
	 */
	public Stamping getStampingAt(Point2f point) {
		Stamping ret = null;
		int highestLevel = -1;
		for (Stamping s : getMusicalStampings()) {
			if (s.getLevel().ordinal() > highestLevel && s.getBoundingShape().contains(point)) {
				highestLevel = s.getLevel().ordinal();
				ret = s;
			}
		}
		return ret;
	}

	/**
	 * Returns the {@link StaffStamping} under the given position
	 * in score layout coordinates, or null, if there is none.
	 */
	public StaffStamping getStaffStampingAt(Point2f point) {
		for (StaffStamping s : staffStampings) {
			if (s.getBoundingShape().contains(point))
				return (StaffStamping) s;
		}
		return null;
	}

	/**
	 * Returns the {@link Stamping} (not a {@link StaffStamping}) which is instance
	 * of the given class under the given position in score layout coordinates,
	 * or null, if there is none.
	 */
	public Stamping getOtherStampingAt(Point2f point, StampingType type) {
		for (Stamping s : otherStampings) {
			if (s.getType() == type && s.getBoundingShape().contains(point))
				return s;
		}
		return null;
	}

	/**
	 * Computes and returns the appropriate musical position
	 * to the given position in score layout coordinates, or null, if unknown.
	 */
	public MP computeMP(Point2f point) {
		if (point == null)
			return null;
		//first test, if there is a staff element.
		StaffStamping staff = getStaffStampingAt(point);
		//if there is no staff, return null
		if (staff == null) {
			return null;
		}
		//otherwise, compute the beat at this position and return it
		else {
			float posX = point.x - staff.positionMm.x;
			return staff.getMpAtX(posX);
		}
	}

	/**
	 * Computes and returns the text stamping to the given
	 * position in score layout coordinates, or null, if unknown.
	 */
	public TextStamping computeTextStamping(Point2f point) {
		if (point == null)
			return null;
		return (TextStamping) getOtherStampingAt(point, StampingType.TextStamping);
	}

	/**
	 * Gets the staff stamping containing the given measure, or null if not found.
	 */
	public StaffStamping getStaffStamping(int staff, int measure) {
		for (StaffStamping s : staffStampings)
			if (s.getStaffIndex() == staff && s.system.containsMeasure(measure))
				return s;
		return null;
	}

	/**
	 * Computes and returns the bounding rectangle of the system with the given index
	 * (relative to the frame). Only the staves are regarded, not text around them
	 * or notes over the top staff or notes below the bottom staff.
	 * If there are no staves in this system, null is returned.
	 */
	public Rectangle2f getSystemBoundaries(int systemIndex) {
		boolean found = false;
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		for (StaffStamping staff : staffStampings) {
			if (staff.system.getSystemIndexInFrame() == systemIndex) {
				found = true;
				minX = Math.min(minX, staff.positionMm.x);
				minY = Math.min(minY, staff.positionMm.y);
				maxX = Math.max(maxX, staff.positionMm.x + staff.lengthMm);
				maxY = Math.max(maxY, staff.positionMm.y + (staff.linesCount - 1) * staff.is);
			}
		}
		if (found)
			return new Rectangle2f(minX, minY, maxX - minX, maxY - minY);
		else
			return null;
	}

	/**
	 * Gets the horizontal position in mm of the given {@link Time} within the given frame,
	 * or 0 if not found (should not happen).
	 */
	public float getPositionX(Time time) {
		//search all systems for the given musical position, beginning at the top staff
		for (SystemSpacing system : frameSpacing.systems)
			if (system.containsMeasure(time.measure))
				return system.getXMmAt(time);
		return 0;
	}
	
}
