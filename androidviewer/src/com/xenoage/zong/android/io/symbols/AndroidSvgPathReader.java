package com.xenoage.zong.android.io.symbols;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.io.symbols.SvgPathReader;

/**
 * This class creates a path from a given SVG path.
 * It must be subclassed for the implementation of the geometry functions.
 *
 * @author Andreas Wenger
 */
public class AndroidSvgPathReader
	extends SvgPathReader<Path> {

	private Path path;
	private Point2f currentPoint = new Point2f(0, 0);
	private Point2f lastMoveToPoint = new Point2f(0, 0);


	/**
	 * Gets the resulting path object as a {@link Path}.
	 */
	@Override public Path getPath() {
		return path;
	}

	@Override public void init() {
		path = new Path();
	}

	@Override public Point2f getCurrentPoint() {
		return currentPoint;
	}

	@Override public void closePath() {
		path.close();
		currentPoint = lastMoveToPoint;
	}

	@Override public void lineTo(Point2f p) {
		path.lineTo(p.x, p.y);
		currentPoint = p;
	}

	@Override public void moveTo(Point2f p) {
		path.moveTo(p.x, p.y);
		currentPoint = lastMoveToPoint = p;
	}

	@Override public void curveTo(Point2f p1, Point2f p2, Point2f p3) {
		path.cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
		currentPoint = p3;
	}

	@Override public void quadTo(Point2f p1, Point2f p2) {
		path.quadTo(p1.x, p1.y, p2.x, p2.y);
		currentPoint = p2;
	}

	@Override public void finish() {
		//transform path by -1000/-1000 and scale down to 1%
		Matrix matrix = new Matrix();
		matrix.postTranslate(-1000, -1000);
		matrix.postScale(0.01f, 0.01f);
		path.transform(matrix);
	}

	@Override public Rectangle2f getBoundingRect() {
		RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		return new Rectangle2f(bounds.left, bounds.top, bounds.width(), bounds.height());
	}

}
