package com.xenoage.zong.renderer.frames;

import com.xenoage.utils.math.Units;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Abstract renderer class for one type of {@link Frame}.
 * 
 * Platform-specific implementations are provided as subclasses.
 * 
 * @author Andreas Wenger
 */
public abstract class FrameRenderer {

	/**
	 * Paints the given {@link Frame} on the
	 * given {@link Canvas} using the given {@link RendererArgs}.
	 */
	public void paint(Frame frame, Canvas canvas, RendererArgs args) {
		//backup old transformation
		canvas.transformSave();
		//apply translation: origin offset in screen space
		canvas.transformTranslate(args.offsetPx.x, args.offsetPx.y);
		//apply scaling
		float scaling = Units.mmToPx(args.scaling, 1);
		canvas.transformScale(scaling, scaling);
		//apply translation: absolute frame position in layout space
		Point2f pos = frame.getAbsolutePosition();
		canvas.transformTranslate(pos.x, pos.y);
		//apply rotation
		canvas.transformRotate(-frame.getAbsoluteRotation());

		//DEMO
		/*
		g2d.setColor(java.awt.Color.green);
		g2d.setStroke(new java.awt.BasicStroke(1));
		float w = frame.getSize().width;
		float h = frame.getSize().height;
		g2d.draw(new java.awt.geom.Rectangle2D.Float(-w/2, -h/2, w, h));
		//*/

		//paint the frame
		paintTransformed(frame, canvas, args);

		//restore old transformation
		canvas.transformRestore();
	}

	/**
	 * Paints the given {@link Frame} on the
	 * given {@link Canvas} using the given {@link RendererArgs}.
	 * This method is called by the {@link #paint(Frame, Canvas, RendererArgs)}
	 * method, the transformations are alread done. When painting, 1 unit corresponds
	 * to 1 mm, and the center point is in the middle of the frame.
	 */
	protected abstract void paintTransformed(Frame frame, Canvas canvas, RendererArgs args);

}
