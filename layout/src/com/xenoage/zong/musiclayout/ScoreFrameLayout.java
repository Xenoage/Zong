package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.xenoage.utils.iterators.MultiIt;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StampingType;
import com.xenoage.zong.musiclayout.stampings.TextStamping;

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
	@Getter private FrameArrangement frameArrangement;

	/** The list of all staff stampings of this frame. Staff stampings and
	 * other stampings are divided for performance reasons. */
	@Getter private ArrayList<StaffStamping> staffStampings;
	/** The list of all other stampings of this frame. Staff stampings and
	 * other stampings are divided for performance reasons.*/
	@Getter private ArrayList<Stamping> otherStampings;

	/** The list of continued elements, that means the unclosed elements
	 * that must also be painted on the next frame. */
	@Getter private ArrayList<ContinuedElement> continuedElements;

	//TODO: move into other class
	/** The list of the selection stampings of this frame. */
	@Getter @Setter private List<? extends Stamping> selectionStampings = alist();
	/** The list of the playback stampings of this frame. */
	@Getter @Setter private List<? extends Stamping> playbackStampings = alist();

	
	public ScoreFrameLayout(FrameArrangement frameArrangement, ArrayList<StaffStamping> staffStampings,
		ArrayList<Stamping> otherStampings, ArrayList<ContinuedElement> continuedElements)
	{
		this.frameArrangement = frameArrangement;
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
		return new MultiIt<Stamping>(staffStampings, otherStampings);
	}

	/**
	 * Gets all stampings of this frame, including both the musical
	 * and the status stampings (selection, playback).
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Stamping> getAllStampings() {
		return new MultiIt<Stamping>(staffStampings, otherStampings, selectionStampings,
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
			if (s.level.ordinal() > highestLevel && s.boundingShape.contains(point)) {
				highestLevel = s.level.ordinal();
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
			if (s.boundingShape.contains(point))
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
			if (s.boundingShape != null && s.boundingShape.contains(point) && s.getType() == type)
				return s;
		}
		return null;
	}

	/**
	 * Returns the first {@link Stamping} which belongs to the given {@link MusicElement},
	 * or null if there is none.
	 */
	public Stamping getStampingFor(MusicElement element) {
		for (Stamping s : otherStampings) {
			if (s.musicElement == element)
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
			float posX = point.x - staff.position.x;
			return staff.getMPAtX(posX);
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
		for (StaffStamping s : staffStampings) {
			if (s.getStaffIndex() == staff && measure >= s.getStartMeasureIndex() &&
				measure <= s.getEndMeasureIndex()) {
				return s;
			}
		}
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
			if (staff.getSystemIndex() == systemIndex) {
				found = true;
				minX = Math.min(minX, staff.position.x);
				minY = Math.min(minY, staff.position.y);
				maxX = Math.max(maxX, staff.position.x + staff.length);
				maxY = Math.max(maxY, staff.position.y + (staff.linesCount - 1) * staff.is);
			}
		}
		if (found)
			return new Rectangle2f(minX, minY, maxX - minX, maxY - minY);
		else
			return null;
	}

	/**
	 * Gets the horizontal position in mm of the given {@link MP} within the given frame,
	 * or 0 if not found (should not happen).
	 * Only the measure and the beat of the given {@link MP} are used.
	 */
	public float getPositionX(MP mp) {
		float minX = Float.MAX_VALUE;
		//search all staves for the given musical position, beginning at the top staff
		for (StaffStamping staff : staffStampings) {
			Float x = staff.getXMmAt(mp);
			if (x != null && x < minX)
				minX = x;
		}
		if (minX == Float.MAX_VALUE)
			return 0;
		else
			return minX;
	}
	
}
