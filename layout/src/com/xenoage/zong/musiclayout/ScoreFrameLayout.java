package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.pdlib.IVector.ivec;

import java.util.List;

import com.xenoage.utils.base.iterators.MultiIt;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.BMP;
import com.xenoage.zong.musiclayout.continued.ContinuedElement;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.TextStamping;


/**
 * A score frame layout contains the musical layout of a
 * score for one score frame.
 * 
 * This includes the musical stampings as well as the current
 * selection and playback stampings. A list of continued elements
 * (e.g. slurs or voltas) to the following frame is also saved.
 * 
 * While the musical stampings are final in this class, the
 * selection and playback stampings are mutable.
 * 
 * @author Andreas Wenger
 */
public final class ScoreFrameLayout
{

	//information about the systems in this frame
	private final FrameArrangement frameArrangement;

	//for quicker lookup: divided into staff stampings and other stampings
	private final Vector<StaffStamping> staffStampings;
	private final Vector<Stamping> otherStampings;

	//continued elements to the following frame
	private final Vector<ContinuedElement> continuedElements;

	//mutable stampings
	private List<? extends Stamping> selectionStampings = ivec();
	private List<? extends Stamping> playbackStampings = ivec();


	/**
	 * Creates a new layout for a score frame with no selections.
	 * @param frameArrangement   information about the systems in this frame
	 * @param staffStampings     the list of staff stampings
	 * @param otherStampings     the list of all other stampings
	 * @param continuedElements  unclosed elements of this frame that have to be continued
	 *                           on the next frame
	 */
	public ScoreFrameLayout(FrameArrangement frameArrangement, Vector<StaffStamping> staffStampings,
		Vector<Stamping> otherStampings, Vector<ContinuedElement> continuedElements)
	{
		this.frameArrangement = frameArrangement;
		this.staffStampings = staffStampings;
		this.otherStampings = otherStampings;
		this.continuedElements = continuedElements;
	}


	/**
	 * Gets information about the systems in this frame.
	 */
	public FrameArrangement getFrameArrangement()
	{
		return frameArrangement;
	}


	/**
	 * Gets a list of all staff stampings of this frame.
	 */
	public Vector<StaffStamping> getStaffStampings()
	{
		return staffStampings;
	}


	/**
	 * Gets a list of all other stampings of this frame.
	 */
	public Vector<Stamping> getOtherStampings()
	{
		return otherStampings;
	}


	/**
	 * Gets the list of continued elements, that means the unclosed elements
	 * that must also be painted on the next frame.
	 */
	public Vector<ContinuedElement> getContinuedElements()
	{
		return continuedElements;
	}


	/**
	 * Gets all musical stampings (staves, notes, ...) of this frame,
	 * but not selections or playback stampings.
	 */
	@SuppressWarnings("unchecked") public Iterable<Stamping> getMusicalStampings()
	{
		return new MultiIt<Stamping>(staffStampings, otherStampings);
	}


	/**
	 * Gets all stampings of this frame, including both the musical
	 * and the status stampings (selection, playback).
	 */
	@SuppressWarnings("unchecked") public Iterable<Stamping> getAllStampings()
	{
		return new MultiIt<Stamping>(staffStampings, otherStampings, selectionStampings, playbackStampings);
	}


	/**
	 * Returns the {@link Stamping} under the given position
	 * in score layout coordinates (mm relative to the
	 * upper left corner) or null, if there is none.
	 */
	public Stamping getStampingAt(Point2f point)
	{
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
	public StaffStamping getStaffStampingAt(Point2f point)
	{
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
	public Stamping getOtherStampingAt(Point2f point, Class<?> stampingClass)
	{
		for (Stamping s : otherStampings) {
			if (s.boundingShape != null && s.boundingShape.contains(point) && stampingClass.isInstance(s))
				return s;
		}
		return null;
	}


	/**
	 * Returns the first {@link Stamping} which belongs to the given {@link MusicElement},
	 * or null if there is none.
	 */
	public Stamping getStampingFor(MusicElement element)
	{
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
	public BMP computeMP(Point2f point)
	{
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
	public TextStamping computeTextStamping(Point2f point)
	{
		if (point == null)
			return null;
		return (TextStamping) getOtherStampingAt(point, TextStamping.class);
	}


	/**
	 * Gets the staff stamping containing the given measure, or null if not found.
	 */
	public StaffStamping getStaffStamping(int staff, int measure)
	{
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
	public Rectangle2f getSystemBoundaries(int systemIndex)
	{
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
	 * Gets the horizontal position in mm of the given {@link BMP} within the given frame,
	 * or 0 if not found (should not happen).
	 * Only the measure and the beat of the given {@link BMP} are used.
	 */
	public float getPositionX(BMP bmp)
	{
		float minX = Float.MAX_VALUE;
		//search all staves for the given musical position, beginning at the top staff
		for (StaffStamping staff : staffStampings) {
			Float x = staff.getXMmAt(bmp);
			if (x != null && x < minX)
				minX = x;
		}
		if (minX == Float.MAX_VALUE)
			return 0;
		else
			return minX;
	}
	
	
	/**
	 * Gets a list of the selection stampings of this frame.
	 */
	public List<? extends Stamping> getSelectionStampings()
	{
		return selectionStampings;
	}


	/**
	 * Sets the list of the selection stampings of this frame.
	 */
	public void setSelectionStampings(List<? extends Stamping> selectionStampings)
	{
		this.selectionStampings = selectionStampings;
	}


	/**
	 * Gets a list of the playback stampings of this frame.
	 */
	public List<? extends Stamping> getPlaybackStampings()
	{
		return playbackStampings;
	}


	/**
	 * Sets the list of the playback stampings of this frame.
	 */
	public void setPlaybackStampings(List<? extends Stamping> playbackStampings)
	{
		this.playbackStampings = playbackStampings;
	}


}
