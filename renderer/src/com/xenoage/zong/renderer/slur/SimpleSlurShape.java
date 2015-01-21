package com.xenoage.zong.renderer.slur;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.Getter;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.symbols.path.CubicCurveTo;
import com.xenoage.zong.symbols.path.MoveTo;
import com.xenoage.zong.symbols.path.Path;
import com.xenoage.zong.symbols.path.PathElement;

/**
 * Shape of a slur, drawn in the Default style.
 * 
 * This is a slur which is thicker in the middle
 * and has sharp ends.
 * 
 * @author Andreas Wenger
 */
public class SimpleSlurShape {

	//TODO (for more complicated slurs like S-slurs): different algorithms for different symbol pools.
	//for example printed style bounding quads look like this (because vertical lines are thinner than horizontal ones)
	//        ______
	//   __--|      |--___
	//  |    |______|  |   |
	//  |__--        --|___|
	//(this is the DefaultShapeStrategy)
	//
	//while handwritten style bounding quads look more than this (because they have equal line width everywhere).
	//        _______
	//  ___--\       /--____
	// \   \  |_____|   /   /
	//  \___\--     -- /___/

	public final float interlineSpace;
	public final Point2f p1top, p2top, c1top, c2top, p1bottom, p2bottom, c1bottom, c2bottom;

	@Getter private Path path;
	

	/**
	 * Creates the shape of a slur, using the given Bézier curve.
	 * @param p1      the starting point in mm
	 * @param p2      the ending point in mm
	 * @param c1      the first control point in mm
	 * @param c2      the second control point in mm
	 * @param interlineSpace  the interline space in mm
	 */
	public SimpleSlurShape(Point2f p1, Point2f p2, Point2f c1, Point2f c2, float interlineSpace) {
		//TODO: the following is only a rough estimate, not an exact formula!!
		//we want the slur to have a height of only about 30% to 40% in the middle,
		//but the following is no formula to compute that in a correct way.

		this.interlineSpace = interlineSpace;
		//height at the end points
		float startHeight = 0.1f * interlineSpace;
		//maximum width in the middle: 0.3 interline spaces
		float maxHeight = 0.3f * interlineSpace;

		float s = startHeight / 2;
		float m = maxHeight / 2;

		this.p1top = p1.add(0, s);
		this.p2top = p2.add(0, s);
		this.c1top = c1.add(0, s + m);
		this.c2top = c2.add(0, s + m);

		this.p1bottom = p1.add(0, -s);
		this.p2bottom = p2.add(0, -s);
		this.c1bottom = c1.add(0, -(s + m));
		this.c2bottom = c2.add(0, -(s + m));
		
		this.path = createPath();
	}
	
	private Path createPath() {
		List<PathElement> elements = alist(5);
		float cap = interlineSpace / 4;
		elements.add(new MoveTo(p1top));
		//bezier curve from p1top to p2top
		elements.add(new CubicCurveTo(c1top, c2top, p2top));
		//cap at p2
		Point2f capDir = p2top.sub(c2top).normalize().scale(cap);
		elements.add(new CubicCurveTo(p2top.add(capDir), p2bottom.add(capDir), p2bottom));
		//bezier curve back from p2bottom to p1bottom
		elements.add(new CubicCurveTo(c2bottom, c1bottom, p1bottom));
		//cap at p1
		capDir = p1top.sub(c1top).normalize().scale(cap);
		elements.add(new CubicCurveTo(p1bottom.add(capDir), p1top.add(capDir), p1top));
		Path path = new Path(elements);
		return path;
	}

}
