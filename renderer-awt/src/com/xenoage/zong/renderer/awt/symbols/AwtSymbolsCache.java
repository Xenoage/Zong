package com.xenoage.zong.renderer.awt.symbols;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;
import static com.xenoage.utils.collections.CollectionUtils.map;

import java.awt.Shape;
import java.util.HashMap;

import com.xenoage.utils.annotations.Optimized;
import com.xenoage.zong.symbols.PathSymbol;

/**
 * Cache for AWT's {@link Shape} representations of {@link PathSymbol}s
 * for better performance.
 *  
 * @author Andreas Wenger
 */
@Optimized(Performance)
public class AwtSymbolsCache {

	private static final int maxCapacity = 1000;
	private HashMap<String, Shape> shapes = map(); //key: symbol ID
	
	public Shape getShape(PathSymbol symbol) {
		//if cached, return it
		Shape shape = shapes.get(symbol.id);
		if (shape != null)
			return shape;
		//if not cached and the cache has already reached its capacity, clear it
		if (shapes.size() > maxCapacity)
			shapes.clear();
		//create the shape
		shape = AwtShape.createShape(symbol.path);
		shapes.put(symbol.id, shape);
		return shape;
	}
	
}
