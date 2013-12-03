package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.musiclayout.continued.ContinuedSlur;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.util.SlurCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;
import com.xenoage.zong.musiclayout.stampings.SlurStamping;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * This strategy computes the stampings of a {@link Slur}.
 * 
 * @author Andreas Wenger
 */
public class SlurStampingStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Computes the additional distance of the startpoint or endpoint of a slur
	 * to the notehead. This value depends on articulations or stems at the same side.
	 */
	public float computeAdditionalDistance(ChordNotation chord, VSide slurSide) {
		float distance = 0;
		if (chord.getArticulationsAlignment() != null) {
			distance += chord.getArticulationsAlignment().getTotalHeightIS();
		}
		if (chord.getStemDirection().equalsSide(slurSide)) {
			StemAlignment sa = chord.getStemAlignment();
			if (sa != null)
				distance += Math.abs(sa.getEndLinePosition() - sa.getStartLinePosition()) / 2; //LP to IS
		}
		return distance;
	}

	/**
	 * Creates a {@link SlurStamping} for the first part (or only part,
	 * if simple slur without system breaks) of a slur or tie.
	 * 
	 * If the slur continues to another system, the second return value is true,
	 * otherwise false.
	 */
	public Tuple2<SlurStamping, Boolean> createSlurStampingStart(SlurCache slurCache) {
		NoteheadStamping n1 = slurCache.getStartNoteheadStamping();
		NoteheadStamping n2 = slurCache.getStopNoteheadStamping();
		//is one staff enough?
		if (n1 != null && n2 != null && n1.parentStaff == n2.parentStaff) {
			//simple case. just create it.
			SlurStamping cls = createSingle(slurCache);
			return new Tuple2<SlurStamping, Boolean>(cls, false);
		}
		else if (n1 != null) {
			//we need at least two staves.
			//first staff: begin at the notehead, go to the end of the system
			SlurStamping cls = createStart(n1, slurCache.getStartDistanceIS(),
				slurCache.getSlur(), slurCache.getSide());
			//remember this curved line to be continued
			return new Tuple2<SlurStamping, Boolean>(cls, true);
		}
		else {
			throw new IllegalArgumentException(
				"A curved line can only be started if the start notehead is known");
		}
	}

	/**
	 * Creates a {@link SlurStamping} for a middle part of a slur
	 * that spans at least three systems (ties never do that).
	 * 
	 * The appropriate staff stamping must be given.
	 */
	public SlurStamping createSlurStampingMiddle(ContinuedSlur continuedSlur,
		StaffStamping staffStamping) {
		return createMiddle(staffStamping, continuedSlur.slur, continuedSlur.side);
	}

	/**
	 * Creates a {@link SlurStamping} for a last part of a slur or tie
	 * that spans at least two systems.
	 */
	public SlurStamping createSlurStampingStop(SlurCache slurCache) {
		NoteheadStamping n = slurCache.getStopNoteheadStamping();
		if (n != null) {
			return createStop(n, slurCache.getStopDistanceIS(), slurCache.getSlur(),
				slurCache.getSide());
		}
		else {
			throw new IllegalArgumentException(
				"A curved line can only be stopped if the stop notehead is known");
		}
	}

	/**
	 * Creates a {@link SlurStamping} for a curved line that
	 * uses only a single staff.
	 */
	SlurStamping createSingle(SlurCache tiedChords) {
		StaffStamping staff = tiedChords.getStartNoteheadStamping().parentStaff;
		Slur slur = tiedChords.getSlur();
		SlurWaypoint wp1 = slur.getStart();
		SlurWaypoint wp2 = slur.getStop();

		//end points of the bezier curve
		VSide side = tiedChords.getSide();
		SP p1 = computeEndPoint(slur, tiedChords.getStartNoteheadStamping(), wp1.getBezierPoint(), side,
			tiedChords.getStartDistanceIS());
		SP p2 = computeEndPoint(slur, tiedChords.getStopNoteheadStamping(), wp2.getBezierPoint(), side,
			tiedChords.getStopDistanceIS());

		//control points of the bezier curve
		BezierPoint b1 = wp1.getBezierPoint();
		BezierPoint b2 = wp2.getBezierPoint();
		SP c1 = (b1 != null && b1.getControl() != null ? b1.getControl() : //custom formatting
			computeLeftControlPoint(slur, p1, p2, side, staff)); //default formatting
		SP c2 = (b2 != null && b2.getControl() != null ? b2.getControl() : //custom formatting
			computeRightControlPoint(slur, p1, p2, side, staff)); //default formatting

		return new SlurStamping(staff, slur, p1, p2, c1, c2);
	}

	/**
	 * Creates a {@link SlurStamping} for a curved line that
	 * starts at this staff but spans at least one other staff.
	 */
	SlurStamping createStart(NoteheadStamping startNotehead, float startAdditionalDistanceIS,
		Slur slur, VSide side) {
		StaffStamping staff = startNotehead.parentStaff;
		SlurWaypoint wp1 = slur.getStart();

		//end points of the bezier curve
		SP p1 = computeEndPoint(slur, startNotehead, wp1.getBezierPoint(), side,
			startAdditionalDistanceIS);
		SP p2 = sp(staff.position.x + staff.length, p1.lp);

		//control points of the bezier curve
		BezierPoint b1 = wp1.getBezierPoint();
		SP c1 = (b1 != null && b1.getControl() != null ? b1.getControl() : //custom formatting
			computeLeftControlPoint(slur, p1, p2, side, staff)); //default formatting
		SP c2 = computeRightControlPoint(slur, p1, p2, side, staff); //default formatting

		return new SlurStamping(staff, slur, p1, p2, c1, c2);
	}

	/**
	 * Creates a {@link SlurStamping} for a curved line that
	 * starts at an earlier staff and ends at a later staff, but
	 * spans also the given staff.
	 */
	SlurStamping createMiddle(StaffStamping staff, Slur slur, VSide side) {
		if (slur.getType() == SlurType.Tie) {
			//ties can not have middle staves
			return null;
		}

		//end points of the bezier curve
		float p1x = staff.position.x + staff.getMeasureLeadingMm(staff.getStartMeasureIndex()) - 5; //TODO
		float p2x = staff.position.x + staff.length;
		float yLp;
		if (side == VSide.Top) {
			yLp = (staff.linesCount - 1) * 2 + 2; //1 IS over the top staff line
		}
		else {
			yLp = -2; //1 IS below the bottom staff line
		}
		SP p1 = sp(p1x, yLp);
		SP p2 = sp(p2x, yLp);

		//control points of the bezier curve
		SP c1 = computeLeftControlPoint(slur, p1, p2, side, staff); //default formatting
		SP c2 = computeRightControlPoint(slur, p1, p2, side, staff); //default formatting

		return new SlurStamping(staff, slur, p1, p2, c1, c2);
	}

	/**
	 * Creates a {@link SlurStamping} for a last part of a slur or tie
	 * that spans at least two systems.
	 */
	SlurStamping createStop(NoteheadStamping stopNotehead, float stopAdditionalDistanceIS,
		Slur slur, VSide side) {
		StaffStamping staff = stopNotehead.parentStaff;
		SlurWaypoint wp2 = slur.getStop();

		//end points of the bezier curve
		SP p2 = computeEndPoint(slur, stopNotehead, wp2.getBezierPoint(), side, stopAdditionalDistanceIS);
		SP p1 = sp(staff.position.x + staff.getMeasureLeadingMm(staff.getStartMeasureIndex()) - 5,
			p2.lp); //TODO

		//control points of the bezier curve
		BezierPoint b2 = wp2.getBezierPoint();
		SP c1 = computeLeftControlPoint(slur, p1, p2, side, staff); //default formatting
		SP c2 = (b2 != null && b2.getControl() != null ? b2.getControl() : //custom formatting
			computeRightControlPoint(slur, p1, p2, side, staff)); //default formatting

		return new SlurStamping(staff, slur, p1, p2, c1, c2);
	}

	/**
	 * Computes the end position of a slur or tie, dependent on its corresponding note
	 * and the bezier information (may be null for default formatting)
	 * and the vertical side of placement and the given additional distance in IS.
	 */
	SP computeEndPoint(Slur slur, NoteheadStamping note, BezierPoint bezierPoint, VSide side,
		float additionalDistanceIS) {
		int dir = side.getDir();
		if (bezierPoint == null || bezierPoint.getPoint() == null) {
			//default formatting
			float distanceLP = (slur.getType() == SlurType.Slur ? 2 : 1.5f); //slur is 2 LP away from note center, tie 1.5
			float yLp = note.position.lp + dir * distanceLP + dir * 2 * additionalDistanceIS;
			return sp(note.position.xMm, yLp);
		}
		else {
			//custom formatting
			float yLp = note.position.lp + bezierPoint.getPoint().lp;
			return sp(note.position.xMm + bezierPoint.getPoint().xMm, yLp);
		}
	}

	/**
	 * Computes the position of the left bezier control point,
	 * relative to the left end point.
	 * @param slur   the slur or tie
	 * @param p1     the position of the left end point of the slur
	 * @param p2     the position of the right end point of the slur
	 * @param side   the vertical side where to create the slur (above or below)
	 * @param staff  the staff stamping
	 */
	SP computeLeftControlPoint(Slur slur, SP p1, SP p2, VSide side, StaffStamping staff) {
		return (slur.getType() == SlurType.Slur ? computeLeftSlurControlPoint(p1, p2, side, staff)
			: computeLeftTieControlPoint(p1, p2, side, staff));
	}

	/**
	 * Computes the position of the right bezier control point,
	 * relative to the left end point.
	 * @param slur   the slur or tie
	 * @param p1     the position of the left end point of the slur
	 * @param p2     the position of the right end point of the slur
	 * @param side   the vertical side where to create the slur (above or below)
	 * @param staff  the staff stamping
	 */
	SP computeRightControlPoint(Slur slur, SP p1, SP p2, VSide side, StaffStamping staff) {
		return (slur.getType() == SlurType.Slur ? computeRightSlurControlPoint(p1, p2, side, staff)
			: computeRightTieControlPoint(p1, p2, side, staff));
	}

	/**
	 * Computes the position of the left bezier control point of a slur,
	 * relative to the left end point.
	 * @param p1     the position of the left end point of the slur
	 * @param p2     the position of the right end point of the slur
	 * @param side   the vertical side where to create the slur (above or below)
	 * @param staff  the staff stamping
	 */
	SP computeLeftSlurControlPoint(SP p1, SP p2, VSide side, StaffStamping staff) {
		//slur: longer and higher curve than tie
		float distanceX = Math.abs(p2.xMm - p1.xMm);
		float retX = distanceX / 4;
		float retY = MathUtils.clamp(0.3f * distanceX / staff.is, 0, 8) * side.getDir();
		return sp(retX, retY);
	}

	/**
	 * Computes the position of the right bezier control point of a slur,
	 * relative to the right end point.
	 * @param p1     the position of the left end point of the slur
	 * @param p2     the position of the right end point of the slur
	 * @param side   the vertical side where to create the slur (above or below)
	 * @param staff  the staff stamping
	 */
	SP computeRightSlurControlPoint(SP p1, SP p2, VSide side, StaffStamping staff) {
		SP sp = computeLeftSlurControlPoint(p1, p2, side, staff);
		return sp(-1 * sp.xMm, sp.lp);
	}

	/**
	 * Computes the position of the left bezier control point of a tie,
	 * relative to the left end point.
	 * @param p1     the position of the left end point of the tie
	 * @param p2     the position of the right end point of the tie
	 * @param side   the vertical side where to create the tie (above or below)
	 * @param staff  the staff stamping
	 */
	SP computeLeftTieControlPoint(SP p1, SP p2, VSide side, StaffStamping staff) {
		//slur: longer and higher curve than tie
		float distanceX = Math.abs(p2.xMm - p1.xMm);
		float retX = 1;
		float retY = MathUtils.clamp(0.3f * distanceX / staff.is, 0, 8) * side.getDir();
		return sp(retX, retY);
	}

	/**
	 * Computes the position of the right bezier control point of a tie,
	 * relative to the right end point.
	 * @param p1     the position of the left end point of the tie
	 * @param p2     the position of the right end point of the tie
	 * @param side   the vertical side where to create the tie (above or below)
	 * @param staff  the staff stamping
	 */
	SP computeRightTieControlPoint(SP p1, SP p2, VSide side, StaffStamping staff) {
		SP sp = computeLeftTieControlPoint(p1, p2, side, staff);
		return sp(-1 * sp.xMm, sp.lp);
	}

}
