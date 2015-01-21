package com.xenoage.zong.webapp.renderer.gwt.path;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Window;
import com.xenoage.zong.symbols.path.CubicCurveTo;
import com.xenoage.zong.symbols.path.LineTo;
import com.xenoage.zong.symbols.path.MoveTo;
import com.xenoage.zong.symbols.path.Path;
import com.xenoage.zong.symbols.path.PathElement;
import com.xenoage.zong.symbols.path.QuadraticCurveTo;

/**
 * Draws a {@link Path} on a GWT {@link Context2d}.
 * 
 * @author Andreas Wenger
 */
public class GwtPath {
	
	public static void drawPath(Path path, Context2d context) {
		context.beginPath();
		for (PathElement e : path.getElements()) {
			switch (e.getType()) {
				case ClosePath:
					context.closePath();
					break;
				case CubicCurveTo:
					CubicCurveTo c = (CubicCurveTo) e;
					context.bezierCurveTo(c.cp1.x, c.cp1.y, c.cp2.x, c.cp2.y, c.p.x, c.p.y);
					break;
				case LineTo:
					LineTo l = (LineTo) e;
					context.lineTo(l.p.x, l.p.y);
					break;
				case MoveTo:
					MoveTo m = (MoveTo) e;
					context.moveTo(m.p.x, m.p.y);
					break;
				case QuadraticCurveTo:
					QuadraticCurveTo q = (QuadraticCurveTo) e;
					context.quadraticCurveTo(q.cp.x, q.cp.y, q.p.x, q.p.y);
					break;
			}
		}
	}

}
