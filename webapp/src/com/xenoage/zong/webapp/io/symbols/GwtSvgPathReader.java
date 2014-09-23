package com.xenoage.zong.webapp.io.symbols;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.io.symbols.SvgPathReader;
import com.xenoage.zong.webapp.symbols.GwtPathSymbol;
import com.xenoage.zong.webapp.symbols.path.BezierCurveTo;
import com.xenoage.zong.webapp.symbols.path.ClosePath;
import com.xenoage.zong.webapp.symbols.path.LineTo;
import com.xenoage.zong.webapp.symbols.path.MoveTo;
import com.xenoage.zong.webapp.symbols.path.QuadraticCurveTo;

/**
 * GWT implementation of an {@link SvgPathReader}.
 *
 * @author Andreas Wenger
 */
public class GwtSvgPathReader
	extends SvgPathReader<GwtPathSymbol> {

	private GwtPathSymbol path;
	private Point2f lastStartPoint = Point2f.origin;
	private Point2f currentPoint = Point2f.origin;
	private float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
	private float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;


	/**
	 * Gets the resulting path object as a {@link GwtPathSymbol}.
	 */
	@Override public GwtPathSymbol getPath() {
		return path;
	}

	@Override public void init() {
		path = new GwtPathSymbol();
	}

	@Override public Point2f getCurrentPoint() {
		return currentPoint;
	}

	@Override public void closePath() {
		path.add(new ClosePath());
		currentPoint = lastStartPoint;
	}

	@Override public void lineTo(Point2f p) {
		path.add(new LineTo(transform(p)));
		currentPoint = p;
		updateBounds(p);
	}

	@Override public void moveTo(Point2f p) {
		path.add(new MoveTo(transform(p)));
		lastStartPoint = currentPoint = p;
		updateBounds(p);
	}

	@Override public void curveTo(Point2f p1, Point2f p2, Point2f p3) {
		path.add(new BezierCurveTo(transform(p1), transform(p2), transform(p3)));
		currentPoint = p3;
		updateBounds(p3);
	}

	@Override public void quadTo(Point2f p1, Point2f p2) {
		path.add(new QuadraticCurveTo(transform(p1), transform(p2)));
		currentPoint = p2;
		updateBounds(p2);
	}

	private Point2f transform(Point2f p) {
		//transform point by -1000/-1000 and scale down to 1%
		p = p.add(-1000, -1000);
		p = p.scale(0.01f);
		return p;
	}
	
	private void updateBounds(Point2f p) {
		p = transform(p);
		minX = Math.min(minX, p.x);
		minY = Math.min(minY, p.y);
		maxX = Math.max(maxX, p.x);
		maxY = Math.max(maxY, p.y);
	}

	@Override public Rectangle2f getBoundingRect() {
		//just an estimate (curve lines not included, just the points), but ok for now
		return new Rectangle2f(minX, minY, maxX - minX, maxY - minY);
	}

	@Override public void finish() {
	}

}
