package com.xenoage.zong.layout;

import com.xenoage.utils.math.geom.Point2f;

/**
 * Interface for layout containers which contain frames.
 * 
 * @author Andreas Wenger
 */
public interface LayoutContainer {
	
	/**
	 * Gets the parent layout of this container, or null if not part of a layout.
	 */
	public Layout getParentLayout();
	
	/**
	 * Gets the parent page of this container, or null if not part of a page.
	 * If this object is the page itself, it is returned.
	 */
	public Page getParentPage();
	
	/**
	 * Gets the counter clockwise rotation of this element in degrees.
	 * Returns 0 for the parent page.
	 */
	public float getAbsoluteRotation();
	
	/**
	 * Gets the center position of this element in mm, relative to the page.
	 * Returns 0 for the parent page.
	 */
	public Point2f getAbsolutePosition();
	
	/**
	 * Transforms the given coordinates in frame space to
	 * a position in page space.
	 */
	public Point2f getPagePosition(Point2f p);

}
