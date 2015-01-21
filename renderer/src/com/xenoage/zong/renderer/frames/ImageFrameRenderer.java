package com.xenoage.zong.renderer.frames;

import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ImageFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.Canvas;

/**
 * Renderer for an {@link ImageFrame}.
 * 
 * @author Andreas Wenger
 */
public class ImageFrameRenderer
	extends FrameRenderer {

	@Override protected void paintTransformed(Frame frame, Canvas canvas,	RendererArgs args) {
		ImageFrame imageFrame = (ImageFrame) frame;
		String imagePath = imageFrame.getImagePath();
		Rectangle2f rect = getLocalRect(frame);
		canvas.drawImage(rect, imagePath);
	}

}
