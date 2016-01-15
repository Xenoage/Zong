package com.xenoage.zong.android.renderer.path;

import android.graphics.Canvas;
import android.graphics.Path;

import com.xenoage.zong.symbols.path.CubicCurveTo;
import com.xenoage.zong.symbols.path.LineTo;
import com.xenoage.zong.symbols.path.MoveTo;
import com.xenoage.zong.symbols.path.PathElement;
import com.xenoage.zong.symbols.path.QuadraticCurveTo;

/**
 * Draws a {@link Path} on an Android {@link Canvas}.
 * 
 * @author Andreas Wenger
 */
public class AndroidPath {

	public static Path createPath(com.xenoage.zong.symbols.path.Path path) {
		Path ret = new Path();
		for (PathElement e : path.getElements()) {
			switch (e.getType()) {
				case ClosePath:
					ret.close();
					break;
				case CubicCurveTo:
					CubicCurveTo c = (CubicCurveTo) e;
					ret.cubicTo(c.cp1.x, c.cp1.y, c.cp2.x, c.cp2.y, c.p.x, c.p.y);
					break;
				case LineTo:
					LineTo l = (LineTo) e;
					ret.lineTo(l.p.x, l.p.y);
					break;
				case MoveTo:
					MoveTo m = (MoveTo) e;
					ret.moveTo(m.p.x, m.p.y);
					break;
				case QuadraticCurveTo:
					QuadraticCurveTo q = (QuadraticCurveTo) e;
					ret.quadTo(q.cp.x, q.cp.y, q.p.x, q.p.y);
					break;
			}
		}
		return ret;
	}

}
