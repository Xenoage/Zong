package com.xenoage.zong.renderer.frames;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.xenoage.utils.io.IO;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ImageFrame;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;


/**
 * Android renderer for an image frame.
 * 
 * Notice that this renderer is not optimized
 * for performance in any way. It loads an image
 * each time it is painted!
 * 
 * @author Andreas Wenger
 */
public class AndroidImageFrameRenderer
	extends AndroidFrameRendererBase
{


	/**
	 * {@inheritDoc}
	 */
	@Override protected void paintTransformed(Frame frame, Layout layout, AndroidCanvas canvas,
		RendererArgs args)
	{
		Canvas c = canvas.getGraphicsContext();

		//try to load the image
		ImageFrame imageFrame = (ImageFrame) frame;
		Bitmap image = null;
		try {
			image = BitmapFactory.decodeStream(IO.openInputStream(imageFrame.getImagePath()));
		} catch (IOException ex) {
		}
		if (image == null) {
			log(warning("Could not load image: " + imageFrame.getImagePath()));
		}

		//if image could be loaded, paint it
		if (image != null) {
			//create transformation, that maps the image
			//to the frame borders
			Matrix matrix = new Matrix();
			float x = frame.data.size.width;
			float y = frame.data.size.height;
			matrix.preTranslate(-x / 2, -y / 2);
			matrix.setScale(x / image.getWidth(), y / image.getHeight());
			//draw image with this transform
			c.drawBitmap(image, matrix, null);
		}
	}


}
