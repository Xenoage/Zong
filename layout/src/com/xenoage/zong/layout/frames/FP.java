package com.xenoage.zong.layout.frames;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Frame position.
 * 
 * A frame position is a reference to a frame
 * and a position in mm in frame space.
 * 
 * @author Andreas Wenger
 */
@Const @Data public class FP {

	/** The frame */
	public final Frame frame;
	/** The position on this frame in mm. */
	public final Point2f position;


	public static FP fp(Frame frame, Point2f position) {
		return new FP(frame, position);
	}

	public FP(Frame frame, Point2f position) {
		this.frame = frame;
		this.position = position;
	}

}
