package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeType;
import com.xenoage.zong.core.music.format.Position;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.continued.ContinuedWedge;
import com.xenoage.zong.musiclayout.continued.OpenWedges;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.WedgeStamping;

import java.util.Iterator;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.format.Position.asPosition;

/**
 * Creates a {@link WedgeStamping} for a {@link Wedge}.
 * 
 * @author Andreas Wenger
 */
public class WedgeStamper {
	
	public static final WedgeStamper wedgeStamper = new WedgeStamper();
	
	
	/**
	 * Creates all wedge stampings in the given system.
	 * @param openWedges  input and output parameter: voltas, which are still open from the
	 *                    last system. After the method returns, it contains the voltas which
	 *                    are still open after this system
	 */
	public List<WedgeStamping> stampSystem(SystemSpacing system,
		Score score, StaffStampings staffStampings, OpenWedges openWedges) {
		List<WedgeStamping> ret = alist();
		//find new wedges beginning in this staff
		for (int iStaff : range(score.getStavesCount())) {
			Staff staff = score.getStaff(iStaff);
			for (int iMeasure : system.getMeasures()) {
				Measure measure = staff.getMeasure(iMeasure);
				for (BeatE<Direction> dir : measure.getDirections()) {
					if (dir.getElement() instanceof Wedge) {
						Wedge wedge = (Wedge) dir.getElement();
						//it should not happen in a consistent score, but it could be possible
						//that a wedge is missing its end element. we only stamp it,
						//if the position of the end element is known
						if (wedge.getWedgeEnd().getMP() != null)
							openWedges.wedges.add(new ContinuedWedge((Wedge) dir.getElement()));
					}
				}
			}
		}
		//draw wedges in the cache, and remove them if closed in this system
		for (Iterator<ContinuedWedge> itW = openWedges.wedges.iterator(); itW.hasNext();) {
			ContinuedWedge wedge = itW.next();
			ret.add(stamp(wedge.element,
				staffStampings.get(system.getSystemIndexInFrame(), wedge.element.getMP().staff)));
			if (MP.getMP(wedge.element.getWedgeEnd()).measure <= system.getEndMeasure()) {
				//wedge is closed
				itW.remove();
			}
		}
		return ret;
	}
	
	/**
	 * Creates a {@link WedgeStamping} for the given {@link Wedge} on the given staff.
	 * The start and end measure of the wedge may be outside the staff, then the
	 * wedge is clipped to the staff.
	 */
	public WedgeStamping stamp(Wedge wedge, StaffStamping staffStamping) {
		SystemSpacing system = staffStamping.system;
		Range systemMeasures = system.getMeasures();
		//musical positions of wedge
		MP p1 = MP.getMP(wedge);
		MP p2 = MP.getMP(wedge.getWedgeEnd());
		//clip start to staff
		float x1Mm;
		if (p1.measure < systemMeasures.getStart()) {
			//begins before staff
			x1Mm = system.getMeasureStartAfterLeadingMm(systemMeasures.getStart());
		}
		else {
			//begins within staff
			x1Mm = system.getXMmAt(p1.getTime());
		}
		//clip end to staff
		float x2Mm;
		if (p2.measure > systemMeasures.getStop()) {
			//ends after staff
			x2Mm = system.getMeasureEndMm(systemMeasures.getStop());
		}
		else {
			//ends within staff
			x2Mm = system.getXMmAt(p2.getTime());
		}
		//spread
		float d1Is = 0;
		float d2Is = 0;
		float defaultSpreadIS = 1.5f;
		if (wedge.getType() == WedgeType.Crescendo) {
			d2Is = (wedge.getSpread() != null ? wedge.getSpread() : defaultSpreadIS);
		}
		else if (wedge.getType() == WedgeType.Diminuendo) {
			d1Is = (wedge.getSpread() != null ? wedge.getSpread() : defaultSpreadIS);
		}

		//custom horizontal position
		Position customPos = asPosition(wedge.getPositioning());
		float length = x2Mm - x1Mm;
		if (customPos != null && customPos.x != null)
			x1Mm = customPos.x;
		x1Mm += Position.getRelativeX(customPos);
		x2Mm = x1Mm + length;

		//vertical position
		float lp;
		if (customPos != null && customPos.y != null)
			lp = customPos.y;
		else
			lp = -6;
		lp += Position.getRelativeY(customPos);

		return new WedgeStamping(lp, x1Mm, x2Mm, d1Is, d2Is, staffStamping);
	}

}
