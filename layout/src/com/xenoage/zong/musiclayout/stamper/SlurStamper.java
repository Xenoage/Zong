package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.musiclayout.continued.ContinuedSlur;
import com.xenoage.zong.musiclayout.layouter.cache.util.SlurCache;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.stampings.SlurStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

import static com.xenoage.zong.core.music.format.SP.sp;

/**
 * Creates the {@link SlurStamping}s for {@link Slur}s.
 * 
 * @author Andreas Wenger
 */
public class SlurStamper {
	
	public static final SlurStamper slurStamper = new SlurStamper();
	

	/**
	 * Computes the additional distance of the startpoint or endpoint of a slur
	 * to the notehead. This value depends on articulations or stems at the same side.
	 */
	public float getAdditionalDistanceIs(ChordNotation chord, VSide slurSide) {
		return chord.articulations.heightIs;
	}

	/**
	 * Gets the explicit or default {@link VSide} of the given slur.
	 * TODO : move into other class use and better algorithm
	 */
	public VSide getSide(Slur slur /*, Notations notations */) {
		if (slur.getSide() != null)
			return slur.getSide();
		//val chord = notations.getChord(slur.getStart().getChord());
		//return chord.stemDirection == StemDirection.Up ? VSide.Bottom : VSide.Top;
		return VSide.Top;
	}

	/**
	 * Creates a {@link SlurStamping} for the first part (or only part,
	 * if simple slur without system breaks) of a slur or tie.
	 * 
	 * If the slur continues to another system, the second return value is true,
	 * otherwise false.
	 */
	public Tuple2<SlurStamping, Boolean> createSlurStampingStart(SlurCache slurCache) {
		//is one staff enough?
		if (slurCache.getStartSystem() == slurCache.getStopSystem()) {
			//simple case. just create it.
			SlurStamping cls = createForSingleSystem(slurCache);
			return new Tuple2<SlurStamping, Boolean>(cls, false);
		}
		else {
			//we need at least two staves.
			//first staff: begin at the notehead, go to the end of the system
			SlurStamping cls = createStartForFirstSystem(slurCache.getStartStaff(), slurCache.getDefaultStartSp(),
					slurCache.getSlur());
			//remember this curved line to be continued
			return new Tuple2<SlurStamping, Boolean>(cls, true);
		}
	}

	/**
	 * Creates a {@link SlurStamping} for a middle part of a slur
	 * that spans at least three systems (ties never do that).
	 * 
	 * The appropriate staff stamping must be given.
	 */
	public SlurStamping createSlurStampingMiddle(ContinuedSlur continuedSlur) {
		return createForMiddleSystem(continuedSlur.staff, continuedSlur.element);
	}

	/**
	 * Creates a {@link SlurStamping} for a last part of a slur or tie
	 * that spans at least two systems.
	 */
	public SlurStamping createStopForLastSystem(SlurCache slurCache) {
		return createStopForLastSystem(slurCache.getStopStaff(), slurCache.getDefaultStopSp(),
				slurCache.getSlur());
	}

	/**
	 * Creates a {@link SlurStamping} for a curved line that
	 * uses only a single system. The slur may span over multiple staves.
	 */
	SlurStamping createForSingleSystem(SlurCache slurCache) {
		Slur slur = slurCache.getSlur();
		SlurWaypoint wp1 = slur.getStart();
		SlurWaypoint wp2 = slur.getStop();

		//end points of the bezier curve
		VSide side = slurCache.getSide();
		SP p1 = computeEndPoint(slur, slurCache.getDefaultStartSp(), wp1.getBezierPoint());
		SP p2 = computeEndPoint(slur, slurCache.getDefaultStopSp(), wp2.getBezierPoint());

		//control points of the bezier curve
		BezierPoint b1 = wp1.getBezierPoint();
		BezierPoint b2 = wp2.getBezierPoint();
		SP c1 = (b1 != null && b1.getControl() != null ? b1.getControl() : //custom formatting
			computeLeftControlPoint(slur, p1, p2, slurCache.getStartStaff())); //default formatting
		SP c2 = (b2 != null && b2.getControl() != null ? b2.getControl() : //custom formatting
			computeRightControlPoint(slur, p1, p2, slurCache.getStopStaff())); //default formatting

		return new SlurStamping(slur, p1, p2, c1, c2, slurCache.getStartStaff(),
				slurCache.getStopStaff());
	}

	/**
	 * Creates a {@link SlurStamping} for a curved line that
	 * starts at this system but spans at least one other system.
	 */
	SlurStamping createStartForFirstSystem(StaffStamping staff, SP defaultSp, Slur slur) {
		SlurWaypoint wp1 = slur.getStart();

		//end points of the bezier curve
		SP p1 = computeEndPoint(slur, defaultSp, wp1.getBezierPoint());
		SP p2 = sp(staff.positionMm.x + staff.lengthMm, p1.lp);

		//control points of the bezier curve
		BezierPoint b1 = wp1.getBezierPoint();
		SP c1 = (b1 != null && b1.getControl() != null ? b1.getControl() : //custom formatting
			computeLeftControlPoint(slur, p1, p2, staff)); //default formatting
		SP c2 = computeRightControlPoint(slur, p1, p2, staff); //default formatting

		return new SlurStamping(slur, p1, p2, c1, c2, staff, staff);
	}

	/**
	 * Creates a {@link SlurStamping} for a curved line that
	 * starts at an earlier system and ends at a later system, but
	 * spans also the given system.
	 */
	SlurStamping createForMiddleSystem(StaffStamping staff, Slur slur) {
		if (slur.getType() == SlurType.Tie) {
			//ties can not have middle staves
			return null;
		}

		//end points of the bezier curve
		float p1x = staff.positionMm.x + staff.system.getMeasureStartAfterLeadingMm(
			staff.system.getStartMeasure()) - 5; //TODO
		float p2x = staff.positionMm.x + staff.lengthMm;
		float lp;
		if (slur.getSide() == VSide.Top)
			lp = (staff.linesCount - 1) * 2 + 2; //1 IS over the top staff line
		else
			lp = -2; //1 IS below the bottom staff line
		SP p1 = sp(p1x, lp);
		SP p2 = sp(p2x, lp);

		//control points of the bezier curve
		SP c1 = computeLeftControlPoint(slur, p1, p2, staff); //default formatting
		SP c2 = computeRightControlPoint(slur, p1, p2, staff); //default formatting

		return new SlurStamping(slur, p1, p2, c1, c2, staff, staff);
	}

	/**
	 * Creates a {@link SlurStamping} for a last part of a slur or tie
	 * that spans at least two systems.
	 */
	SlurStamping createStopForLastSystem(StaffStamping staff, SP defaultSp, Slur slur) {
		SlurWaypoint wp2 = slur.getStop();

		//end points of the bezier curve
		SP p2 = computeEndPoint(slur, defaultSp, wp2.getBezierPoint());
		SP p1 = sp(staff.positionMm.x + staff.system.getMeasureStartAfterLeadingMm(
			staff.system.getStartMeasure()) - 5, p2.lp); //TODO

		//control points of the bezier curve
		BezierPoint b2 = wp2.getBezierPoint();
		SP c1 = computeLeftControlPoint(slur, p1, p2, staff); //default formatting
		SP c2 = (b2 != null && b2.getControl() != null ? b2.getControl() : //custom formatting
			computeRightControlPoint(slur, p1, p2, staff)); //default formatting

		return new SlurStamping(slur, p1, p2, c1, c2, staff, staff);
	}

	/**
	 * Computes the end position of a slur or tie, dependent on its corresponding default position
	 * and the bezier information (may be null for default formatting).
	 */
	SP computeEndPoint(Slur slur, SP defaultSp, BezierPoint bezierPoint) {
		int dir = getSide(slur).getDir();
		if (bezierPoint == null || bezierPoint.getPoint() == null) {
			//default formatting
			float distanceLP = (slur.getType() == SlurType.Slur ? 2 : 1.5f); //slur is 2 LP away from note center, tie 1.5
			float lp = defaultSp.lp + dir * distanceLP;
			return defaultSp.withLp(lp);
		}
		else {
			//custom formatting
			return defaultSp.add(bezierPoint.point);
		}
	}

	/**
	 * Computes the position of the left bezier control point,
	 * relative to the left end point.
	 * @param slur   the slur or tie
	 * @param p1     the position of the left end point of the slur
	 * @param p2     the position of the right end point of the slur
	 * @param staff  the staff stamping
	 */
	SP computeLeftControlPoint(Slur slur, SP p1, SP p2, StaffStamping staff) {
		return (slur.getType() == SlurType.Slur ? computeLeftSlurControlPoint(p1, p2, getSide(slur), staff)
			: computeLeftTieControlPoint(p1, p2, getSide(slur), staff));
	}

	/**
	 * Computes the position of the right bezier control point,
	 * relative to the left end point.
	 * @param slur   the slur or tie
	 * @param p1     the position of the left end point of the slur
	 * @param p2     the position of the right end point of the slur
	 * @param staff  the staff stamping
	 */
	SP computeRightControlPoint(Slur slur, SP p1, SP p2, StaffStamping staff) {
		return (slur.getType() == SlurType.Slur ? computeRightSlurControlPoint(p1, p2, getSide(slur), staff)
			: computeRightTieControlPoint(p1, p2, getSide(slur), staff));
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
