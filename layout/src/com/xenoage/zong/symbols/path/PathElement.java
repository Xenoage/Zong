package com.xenoage.zong.symbols.path;

import static com.xenoage.utils.annotations.Optimized.Reason.Performance;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Interface for all path elements.
 * 
 * @author Andreas Wenger
 */
public interface PathElement {

	/**
	 * Gets the type of the element.
	 * This allows using the switch statement instead of instanceof.
	 */
	@Optimized(Performance) PathElementType getType();
	
	/**
	 * The point where the segment ends, or null if not available.
	 */
	@MaybeNull Point2f getTarget();
	
}
