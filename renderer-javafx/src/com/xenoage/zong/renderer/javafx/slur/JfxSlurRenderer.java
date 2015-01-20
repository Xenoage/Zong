package com.xenoage.zong.renderer.javafx.slur;

import javafx.scene.canvas.GraphicsContext;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.renderer.slur.SimpleSlurShape;

/**
 * JavaFX renderer for a slur.
 * 
 * TIDY: merge into a platform-independent graphics context!
 * This is the same code as for the AWT Slur renderer and the GWT
 * slur renderer will also look the same
 * 
 * @author Andreas Wenger
 */
public class JfxSlurRenderer {

	public void drawSlur(SimpleSlurShape slurShape, GraphicsContext context) {
		SimpleSlurShape s = slurShape;
		float cap = s.interlineSpace / 4;
		context.beginPath();
		context.moveTo(s.p1top.x, s.p1top.y);
		//bezier curve from p1top to p2top
		context.bezierCurveTo(s.c1top.x, s.c1top.y, s.c2top.x, s.c2top.y, s.p2top.x, s.p2top.y);
		//cap at p2
		Point2f capDir = new Point2f(s.p2top.x - s.c2top.x, s.p2top.y - s.c2top.y).normalize().scale(
			cap);
		context.bezierCurveTo(s.p2top.x + capDir.x, s.p2top.y + capDir.y, s.p2bottom.x + capDir.x,
			s.p2bottom.y + capDir.y, s.p2bottom.x, s.p2bottom.y);
		//bezier curve back from p2bottom to p1bottom
		context.bezierCurveTo(s.c2bottom.x, s.c2bottom.y, s.c1bottom.x, s.c1bottom.y, s.p1bottom.x,
			s.p1bottom.y);
		//cap at p1
		capDir = new Point2f(s.p1top.x - s.c1top.x, s.p1top.y - s.c1top.y).normalize().scale(cap);
		context.bezierCurveTo(s.p1bottom.x + capDir.x, s.p1bottom.y + capDir.y, s.p1top.x + capDir.x,
			s.p1top.y + capDir.y, s.p1top.x, s.p1top.y);
	}

}
