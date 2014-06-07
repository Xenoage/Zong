package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.kernel.Range.range;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.ToString;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.selection.ScoreSelection;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * A {@link ScoreLayout} stores the layout of a score,
 * distributed across a list of {@link ScoreFrameLayout}s.
 *
 * @author Andreas Wenger
 */
@ToString
public class ScoreLayout {

	/** The score */
	public final Score score;

	/** The musical layouts of the frames */
	public final List<ScoreFrameLayout> frames;

	/** The used pool of symbols */
	public final SymbolPool symbolPool;

	/** The used layout settings */
	public final LayoutSettings layoutSettings;


	/**
	 * Creates a {@link ScoreLayout} with the given frame layouts.
	 * @param score           the score which is shown
	 * @param frames          a list of frame layouts (may be empty)
	 * @param symbolPool      the used pool of symbols
	 * @param layoutSettings  the used layout settings
	 */
	public ScoreLayout(Score score, @MaybeEmpty List<ScoreFrameLayout> frames,
		SymbolPool symbolPool, LayoutSettings layoutSettings) {
		this.score = score;
		this.frames = frames;
		this.symbolPool = symbolPool;
		this.layoutSettings = layoutSettings;
	}

	/**
	 * Computes and returns the appropriate musical position
	 * to the given metric position, or null, if unknown.
	 */
	public MP getMP(ScoreLP coordinates) {
		if (coordinates == null)
			return null;
		//first test, if there is a staff element.
		ScoreFrameLayout frame = frames.get(coordinates.frameIndex);
		StaffStamping staff = frame.getStaffStampingAt(coordinates.pMm);
		//if there is no staff, return null
		if (staff == null) {
			return null;
		}
		//otherwise, compute the beat at this position and return it
		else {
			float posX = coordinates.pMm.x - staff.position.x;
			return staff.getMPAtX(posX);
		}
	}

	/**
	 * Computes and returns the {@link StaffStampingPosition}
	 * at the given musical position.
	 */
	public StaffStampingPosition getStaffStampingPosition(MP mp) {
		//go through all staff stampings and look for the given staff index
		//and measure index
		//TODO: find a much nicer way to do this, e.g. by storing an allocation
		//table for [staff index, measure index] -> [staff stamping]
		//within the layout
		for (int iFrame = 0; iFrame < frames.size(); iFrame++) {
			ScoreFrameLayout frame = frames.get(iFrame);
			for (StaffStamping s : frame.getStaffStampings()) {
				if (s.getStaffIndex() == mp.staff) {
					//this staff stamping is part of the correct scorewide staff.
					//look if it contains the given musical score position
					Float posX = s.getXMmAt(mp);
					if (posX != null) {
						//we found it. return staff and position
						return new StaffStampingPosition(s, iFrame, posX);
					}
				}
			}
		}
		//we found nothing. return null
		return null;
	}

	/**
	 * Computes the index of the frame containing the measure with the
	 * given global index. If not found, -1 is returned.
	 */
	public int getFrameIndexOf(int measure) {
		//go through all frames
		for (int iFrame : range(frames)) {
			FrameArrangement frameArr = frames.get(iFrame).getFrameArrangement();
			if (frameArr.getStartMeasureIndex() <= measure && frameArr.getEndMeasureIndex() >= measure)
				return iFrame;
		}
		//we found nothing
		return -1;
	}

	/**
	 * Computes the local index of the system (relative to the frame,
	 * thus beginning at 0 for each frame) within the frame with the given index,
	 * containing the measure with the given global index. If not found, -1 is returned.
	 */
	public int getSystemIndexOf(int frame, int measure) {
		FrameArrangement frameArr = frames.get(frame).getFrameArrangement();
		//go through all systems of this frame
		for (int iSystem : range(frameArr.systems)) {
			SystemArrangement system = frameArr.systems.get(iSystem);
			if (system.startMeasureIndex <= measure && system.endMeasureIndex >= measure)
				return iSystem;
		}
		//we found nothing
		return -1;
	}

	/**
	 * Updates the selection stampings of this layout, according to the
	 * given {@link ScoreSelection}.
	 */
	public void updateSelections(ScoreSelection selection) {
		//selections
		ArrayList<LinkedList<Stamping>> selections = new ArrayList<LinkedList<Stamping>>(
			this.frames.size());
		for (int i = 0; i < this.frames.size(); i++) {
			selections.add(new LinkedList<Stamping>());
		}
		/* TODO if (selection != null && selection instanceof Cursor)
		{
		  Cursor cursor = (Cursor) selection;
		  StaffStampingPosition ssp =
		    computeStaffStampingPosition(cursor.getMP());
		  if (ssp != null)
		  {
		    StaffCursorStamping scs = new StaffCursorStamping(ssp.getStaff(), ssp.getPositionX(), -0.5f);
		    selections.get(ssp.getFrameIndex()).add(scs);
		  }
		} */
		//stampings
		for (int i = 0; i < this.frames.size(); i++) {
			//TODO
			//this.frames.get(i).setSelectionStampings(selections.get(i));
		}
	}

	/**
	 * Creates an layout for showing an error.
	 * This can be used if the layouter could not finish its work successfully.
	 * Currently, the error layout consists of a single frame containing nothing.
	 * TODO: show warning text in this frame!
	 */
	public static ScoreLayout createErrorLayout(Score score, SymbolPool symbolPool) {
		return new ScoreLayout(score, new ArrayList<ScoreFrameLayout>(), symbolPool, null);
	}

	/**
	 * Gets the {@link ScoreFrameLayout} that contains the given measure,
	 * or null, if not found.
	 */
	public ScoreFrameLayout getScoreFrameLayout(int measure) {
		for (ScoreFrameLayout frame : frames) {
			if (measure >= frame.getFrameArrangement().getStartMeasureIndex() &&
				measure <= frame.getFrameArrangement().getEndMeasureIndex()) {
				return frame;
			}
		}
		return null;
	}

	/**
	 * Computes the {@link ScoreLP} of the given {@link MP} at the given line position.
	 * If not found, null is returned.
	 */
	public ScoreLP getScoreLP(MP bmp, float lp) {
		int iFrame = getFrameIndexOf(bmp.measure);
		if (iFrame > -1) {
			ScoreFrameLayout sfl = frames.get(iFrame);
			StaffStamping ss;
			if (bmp.staff != MP.unknown)
				ss = sfl.getStaffStamping(bmp.staff, bmp.measure);
			else
				ss = sfl.getStaffStamping(0, bmp.measure);
			if (ss != null) {
				float x = ss.position.x + ss.getXMmAt(bmp);
				float y = ss.computeYMm(lp);
				return new ScoreLP(iFrame, new Point2f(x, y));
			}
		}
		return null;
	}

}
