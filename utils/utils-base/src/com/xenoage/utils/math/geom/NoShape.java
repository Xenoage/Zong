package com.xenoage.utils.math.geom;

/**
 * Class for a non-existing {@link Shape}.
 * 
 * @author Andreas Wenger
 */
public class NoShape
	implements Shape {
	
	public static final NoShape noShape = new NoShape();

	
	/**
	 * Returns false.
	 */
	@Override public boolean contains(Point2f point) {
		return false;
	}

}
