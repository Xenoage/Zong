package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.utils.math.geom.Point2f.p;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.text.FormattedText;

/**
 * Class for a text stamping belonging to a staff, e.g. for lyric
 * and directions.
 *
 * @author Andreas Wenger
 */
@Const public final class StaffTextStamping
	extends TextStamping {

	/** The position, relative to the left border of the staff. */
	public final SP position;


	public StaffTextStamping(StaffStamping parentStaff, MusicElement musicElement,
		FormattedText text, SP position) {
		super(text, parentStaff, musicElement, computeBoundingShape(text, parentStaff, position));
		this.position = position;
	}

	/**
	 * Gets the position within the frame in mm.
	 */
	@Override public Point2f getPositionInFrame() {
		return p(position.xMm, parentStaff.computeYMm(position.lp));
	}

	/**
	 * Returns the bounding shape of this text.
	 */
	private static Shape computeBoundingShape(FormattedText text, StaffStamping parentStaff,
		SP position) {
		float x = position.xMm;
		float y = parentStaff.computeYMm(position.lp);
		return text.getBoundingRect().move(x, y);
	}

}
