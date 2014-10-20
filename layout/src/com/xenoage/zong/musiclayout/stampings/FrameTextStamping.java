package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.core.text.FormattedText;

/**
 * Class for a text stamping positioned within a frame, e.g. a part name.
 * 
 * If the text belongs to a certain staff, use the
 * {@link StaffTextStamping} class instead.
 *
 * @author Andreas Wenger
 */
@Const public final class FrameTextStamping
	extends TextStamping {

	/** The position, relative to the top left corner of the score frame. */
	public final Point2f position;


	public FrameTextStamping(FormattedText text, Point2f position) {
		super(text, null, null, computeBoundingShape(text, position));
		this.position = position;
	}

	/**
	 * Gets the position within the frame in mm.
	 */
	@Override public Point2f getPositionInFrame() {
		return position;
	}

	/**
	 * Returns the bounding shape of this text.
	 */
	private static Shape computeBoundingShape(FormattedText text, Point2f position) {
		return text.getBoundingRect().move(position);
	}

}
