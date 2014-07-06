package com.xenoage.zong.desktop.io.symbols;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.desktop.utils.math.DesktopMathUtils;
import com.xenoage.zong.io.symbols.SvgPathReader;

/**
 * AWT implementation of an {@link SvgPathReader}.
 *
 * @author Andreas Wenger
 */
public class AwtSvgPathReader
	extends SvgPathReader<GeneralPath> {

	private GeneralPath path;


	/**
	 * Gets the resulting path object as a {@link GeneralPath}.
	 */
	@Override public GeneralPath getPath() {
		return path;
	}

	@Override public void init() {
		path = new GeneralPath();
	}

	@Override public Point2f getCurrentPoint() {
		Point2D p = path.getCurrentPoint();
		if (p == null)
			return new Point2f(0, 0);
		return new Point2f((float) p.getX(), (float) p.getY());
	}

	@Override public void closePath() {
		path.closePath();
	}

	@Override public void lineTo(Point2f p) {
		path.lineTo(p.x, p.y);
	}

	@Override public void moveTo(Point2f p) {
		path.moveTo(p.x, p.y);
	}

	@Override public void curveTo(Point2f p1, Point2f p2, Point2f p3) {
		path.curveTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
	}

	@Override public void quadTo(Point2f p1, Point2f p2) {
		path.quadTo(p1.x, p1.y, p2.x, p2.y);
	}

	@Override public void finish() {
		//transform path by -1000/-1000 and scale down to 1%
		AffineTransform transform = new AffineTransform();
		transform.scale(0.01d, 0.01d);
		transform.translate(-1000, -1000);
		path.transform(transform);
	}

	@Override public Rectangle2f getBoundingRect() {
		return DesktopMathUtils.createRectangle2f(path.getBounds2D());
	}

}
