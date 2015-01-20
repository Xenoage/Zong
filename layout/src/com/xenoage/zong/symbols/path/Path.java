package com.xenoage.zong.symbols.path;

import java.util.List;

import lombok.Getter;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Platform independent path representation.
 * 
 * @author Andreas Wenger
 */
public class Path {
	
	@Getter private final List<PathElement> elements;
	@Getter private final Rectangle2f bounds;
	
	
	public Path(List<PathElement> elements) {
		this.elements = elements;
		this.bounds = computeBounds();
	}
	
	private Rectangle2f computeBounds() {
		//just an estimate (curve lines not included, just the points), but ok for now
		float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;
		for (PathElement e : elements) {
			Point2f p = e.getTarget();
			if (p != null) {
				minX = Math.min(minX, p.x);
				maxX = Math.max(maxX, p.x);
				minY = Math.min(minY, p.y);
				maxY = Math.max(maxY, p.y);
			}
		}
		return new Rectangle2f(minX, minY, maxX - minX, maxY - minY);
	}
	
}
