package com.xenoage.zong.renderer;

import com.xenoage.utils.math.geom.Point2i;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Parameters for rendering, like the scaling and pool of symbols.
 *
 * @author Andreas Wenger
 */
public class RendererArgs {

	/** The scaling, i.e. (zoom * other factors). 1: 72 dpi, 2 = 144 dpi, ... */
	public final float scaling;

	/** The target scaling. Normally this is equal to scaling, but during a
	 * smooth zooming operation, it contains the scaling where the zooming will stop */
	public final float targetScaling;

	/** The painting offset in px on the screen (use 0,0 for printing) */
	public final Point2i offsetPx;

	/** The pool of musical symbols */
	public final SymbolPool symbolPool;


	public RendererArgs(float scaling, float targetScaling, Point2i offsetPx, SymbolPool symbolPool) {
		this.scaling = scaling;
		this.targetScaling = targetScaling;
		this.offsetPx = offsetPx;
		this.symbolPool = symbolPool;
	}

}
