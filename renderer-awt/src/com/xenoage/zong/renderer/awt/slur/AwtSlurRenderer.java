package com.xenoage.zong.renderer.awt.slur;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.renderer.slur.SimpleSlurShape;

/**
 * AWT renderer for a slur.
 * 
 * @author Andreas Wenger
 */
public class AwtSlurRenderer {

	/**
	 * Gets the {@link Shape} of the given slur, needed for printing.
	 */
	public static Shape getShape(SimpleSlurShape slurShape) {
		SimpleSlurShape s = slurShape;
		float cap = s.interlineSpace / 4;
		GeneralPath shape = new GeneralPath();
		shape.moveTo(s.p1top.x, s.p1top.y);
		//bezier curve from p1top to p2top
		shape.curveTo(s.c1top.x, s.c1top.y, s.c2top.x, s.c2top.y, s.p2top.x, s.p2top.y);
		//cap at p2
		Point2f capDir = new Point2f(s.p2top.x - s.c2top.x, s.p2top.y - s.c2top.y).normalize().scale(
			cap);
		shape.curveTo(s.p2top.x + capDir.x, s.p2top.y + capDir.y, s.p2bottom.x + capDir.x,
			s.p2bottom.y + capDir.y, s.p2bottom.x, s.p2bottom.y);
		//bezier curve back from p2bottom to p1bottom
		shape.curveTo(s.c2bottom.x, s.c2bottom.y, s.c1bottom.x, s.c1bottom.y, s.p1bottom.x,
			s.p1bottom.y);
		//cap at p1
		capDir = new Point2f(s.p1top.x - s.c1top.x, s.p1top.y - s.c1top.y).normalize().scale(cap);
		shape.curveTo(s.p1bottom.x + capDir.x, s.p1bottom.y + capDir.y, s.p1top.x + capDir.x,
			s.p1top.y + capDir.y, s.p1top.x, s.p1top.y);
		shape.closePath();
		return shape;
	}

}
