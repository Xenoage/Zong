package com.xenoage.zong.renderer.frames;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.frames.Frame;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.musiclayout.ScoreFrameLayout;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.renderer.RendererArgs;
import com.xenoage.zong.renderer.background.AWTBackgroundRenderer;
import com.xenoage.zong.renderer.canvas.AWTCanvas;
import com.xenoage.zong.renderer.canvas.CanvasDecoration;
import com.xenoage.zong.renderer.stampings.StampingRenderer;


/**
 * AWT renderer for a score frame.
 * 
 * @author Andreas Wenger
 */
public class AWTScoreFrameRenderer
  extends AWTFrameRendererBase
{
  
  
	/**
   * {@inheritDoc}
   */
  @Override protected void paintTransformed(Frame frame, Layout layout,
    AWTCanvas canvas, RendererArgs args)
  {
  	Graphics2D g2d = canvas.getGraphicsContext();
		float w = frame.data.size.width;
    float h = frame.data.size.height;
    
		//if there is a background, draw it
    if (frame.data.background != null) {
      Paint background = AWTBackgroundRenderer.getPaint(frame.data.background);
      g2d.setPaint(background);
      g2d.fill(new Rectangle2D.Float(-w/2, -h/2, w, h));
    }
    
    //draw musical elements
    ScoreFrame scoreFrame = (ScoreFrame) frame;
    ScoreFrameLayout scoreLayout = layout.getScoreFrameLayout(scoreFrame);
    if (scoreLayout != null)
    {
      //the coordinates of the layout elements are relative to the upper left
      //corner, so we have to translate them
    	AffineTransform oldTransform = g2d.getTransform();
      g2d.translate(-w / 2, -h / 2);
      
      //get musical stampings, and in interactive mode, also
      //stampings like for playback and selection
      Iterable<Stamping> stampings =
      	(canvas.getDecoration() == CanvasDecoration.Interactive ?
      		scoreLayout.getAllStampings() : scoreLayout.getMusicalStampings());
      //render them
      for (Stamping s : stampings) {
      	StampingRenderer.drawAny(s, canvas, args);
      }
      
      //restore old transformation
      g2d.setTransform(oldTransform);
    }
    
  }


}
