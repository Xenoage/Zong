package com.xenoage.zong.renderer.canvas;

/**
 * Completeness of the rendering.
 * 
 * @author Andreas Wenger
 */
public enum CanvasIntegrity {
	/** Complete rendering, e.g. needed for printing or PNG export. */
	Perfect,
	/** Some elements may be replaced by placeholders for performance reasons,
	 * e.g. on the screen. */
	PlaceholdersAllowed
}
