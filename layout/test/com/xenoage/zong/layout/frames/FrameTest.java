package com.xenoage.zong.layout.frames;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.graphics.color.ColorInfo;
import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.background.ColorBackground;


/**
 * Test cases for a Frame.
 *
 * @author Andreas Wenger
 */
public class FrameTest
{
  
  
  /**
   * Tests the computation of the absolute position.
   */
  @Test public void getAbsolutePosition()
  {
    double u = Math.PI / 180d;
    Layout layout = new Layout(null).plusPage(new Page(PageFormat.defaultValue));
    GroupFrame frame = createFrameWithChildren();
    layout = layout.plusFrame(frame, 0);
    //frame
    Point2f p = frame.getAbsolutePosition(layout);
    assertEquals(50, p.x, Delta.DELTA_FLOAT);
    assertEquals(75, p.y, Delta.DELTA_FLOAT);
    //child frame
    GroupFrame child = (GroupFrame) frame.children.get(0);
    p = child.getAbsolutePosition(layout);
    float r = 30;
    double childX = 50 + 20 * Math.cos(r * u) + 10 * Math.sin(r * u);
    double childY = 75 - 20 * Math.sin(r * u) + 10 * Math.cos(r * u);
    assertEquals(childX, p.x, Delta.DELTA_FLOAT_ROUGH);
    assertEquals(childY, p.y, Delta.DELTA_FLOAT_ROUGH);
    //child frame of child frame
    Frame child2 = child.children.get(0);
    p = child2.getAbsolutePosition(layout);
    r += 30;
    assertEquals(childX + -15 * Math.cos(r * u) + -10 * Math.sin(r * u), p.x, Delta.DELTA_FLOAT_ROUGH);
    assertEquals(childY - -15 * Math.sin(r * u) + -10 * Math.cos(r * u), p.y, Delta.DELTA_FLOAT_ROUGH);
  }
  
  
  public static GroupFrame createFrameWithChildren()
  {
    //frame
    GroupFrame frame = new GroupFrame(new FrameData(new Point2f(50, 75), new Size2f(80, 50),
    	30, new ColorBackground(ColorInfo.red)));
    //child frame
    GroupFrame child = new GroupFrame(new FrameData(new Point2f(20, 10), new Size2f(30, 20),
    	30, new ColorBackground(ColorInfo.green)));
    //child frame of child frame
    ImageFrame child2 = new ImageFrame(new FrameData(new Point2f(-15, -10), new Size2f(5, 5),
    	45, new ColorBackground(ColorInfo.blue)), "data/test/images/flag6.png");
    child = child.plusChildFrame(child2);
    frame = frame.plusChildFrame(child);
    return frame;
  }

}