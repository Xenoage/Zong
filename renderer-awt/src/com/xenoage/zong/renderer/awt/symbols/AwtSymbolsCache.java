package com.xenoage.zong.renderer.awt.symbols;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import static com.xenoage.utils.collections.CollectionUtils.map;

import java.awt.Shape;
import java.util.HashMap;

import com.xenoage.utils.annotations.Optimized;
import com.xenoage.zong.symbols.path.Path;

/**
 * Cache for AWT's {@link Shape} representations of {@link Path}
 * for better performance.
 *  
 * @author Andreas Wenger
 */
@Optimized(Performance)
public class AwtSymbolsCache {

	private static final int maxCapacity = 1000;
	private HashMap<Path, Shape> shapes = map();
	
	public Shape getShape(Path path) {
		//if cached, return it
		Shape shape = shapes.get(path);
		if (shape != null)
			return shape;
		//if not cached and the cache has already reached its capacity, clear it
		if (shapes.size() > maxCapacity)
			shapes.clear();
		//create the shape
		shape = AwtShape.createShape(path);
		shapes.put(path, shape);
		return shape;
	}
	
}
