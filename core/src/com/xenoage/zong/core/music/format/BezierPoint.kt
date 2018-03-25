package com.xenoage.zong.core.music.format


/**
 * Formatting of an endpoint of a bezier curve.
 */
class BezierPoint(
	/** The position of the end point relative to a reference point.  */
	var point: SP,
	/** The position of the control point relative to the end point.  */
	var control: SP)
