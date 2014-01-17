package com.xenoage.zong.renderer.frames;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.background.AndroidBackgroundRenderer;
import com.xenoage.zong.renderer.canvas.AndroidCanvas;
import com.xenoage.zong.renderer.stampings.StampingRenderer;


/**
 * Android renderer for a score frame.
 * 
 * @author Andreas Wenger
 */
public class AndroidScoreFrameRenderer
  extends AndroidFrameRendererBase
{
  
  
	/**
	 * {@inheritDoc}
	 */
	@Override protected void paintTransformed(Frame frame, Layout layout, AndroidCanvas canvas,
		RendererArgs args)
	{
		Canvas c = canvas.getGraphicsContext();
    
    float w = frame.data.size.width;
    float h = frame.data.size.height;
    
    //if there is a background, draw it
    if (frame.data.background != null)
    {
      Paint background = AndroidBackgroundRenderer.getPaint(frame.data.background);
      c.drawRect(new RectF(-w/2, -h/2, w/2, h/2), background);
    }
    
    //draw musical elements
    ScoreFrame scoreFrame = (ScoreFrame) frame;
    ScoreFrameLayout scoreLayout = layout.getScoreFrameLayout(scoreFrame);
    
    //the coordinates of the layout elements are relative to the upper left
    //corner, so we have to translate them
  	int oldTransform = c.save();
  	c.translate(-w / 2, -h / 2);
    
    for (Stamping s : scoreLayout.getMusicalStampings())
    {
    	StampingRenderer.drawAny(s, canvas, args);
    }
    
    c.restoreToCount(oldTransform);
  }


}
