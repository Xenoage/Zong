package com.xenoage.zong.musiclayout.layouter.curvedline;

import java.util.List;

import com.xenoage.utils.math.VSide;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.util.math.CubicBezierCurve;


/**
 * Implementations of this strategy compute bezier curves
 * for curved lines, given the absolute coordinates of
 * the noteheads of the chords connected by the curved line.
 * 
 * All coordinates must be given in line positions, even
 * the horizontal ones. If the curved line spans over more
 * than a single staff, the line positions of the other staves
 * must be expressed as line positions of the staff the first
 * chord belongs to.
 * 
 * @author Andreas Wenger
 */
public interface CurvedLineStrategy
{
	
	
	/**
	 * Returns a cubic bezier curve that forms a curved line
	 * over/under the given list of points expressed in LPs, dependend
	 * on the given side.
	 */
	public CubicBezierCurve computeCurvedLine(List<Point2f> points, VSide side); 

}
