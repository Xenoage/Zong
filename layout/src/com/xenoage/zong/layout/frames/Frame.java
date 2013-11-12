package com.xenoage.zong.layout.frames;

import static com.xenoage.zong.layout.LP.lp;

import com.xenoage.utils.math.MathUtils;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LP;


/**
 * A frame is the abstract base class for
 * object in a layout of a score document.
 * 
 * It is basically a rectangle with a position and size
 * and rotation and an optional background.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public abstract class Frame
{
  
	/** Basic information about the frame */
  public FrameData data;
  
  
  public Frame(FrameData data)
  {
  	this.data = data;
  }
  
  
  /**
   * Gets the center point of the frame in mm, relative to its parent.
   */
  public final Point2f getPosition()
  {
  	return data.position;
  }
  
  
  /**
   * Gets the size of the frame in mm.
   */
  public final Size2f getSize()
  {
  	return data.size;
  }
  
  
  /**
   * Gets the center position of the frame in mm, relative to the page.
   */
  public final Point2f getAbsolutePosition(Layout layout)
  {
    Point2f ret = data.position;
    GroupFrame parent = layout.getParentGroupFrame(this);
    if (parent != null)
    {
      ret = MathUtils.rotate(ret, parent.getAbsoluteRotation(layout));
      ret = ret.add(parent.getAbsolutePosition(layout));
    }
    return ret;
  }

  
  /**
   * Gets the {@link LP} of the center of the frame.
   */
  public final LP getCenterLayoutPosition(Layout layout)
  {
	  Point2f pos = getAbsolutePosition(layout);
	  int pageIndex = layout.pages.indexOf(layout.getPage(this));
	  return lp(layout, pageIndex, pos);
  }
  
  
  /**
   * Gets the counter clockwise rotation of the frame in degrees.
   */
  public final float getAbsoluteRotation(Layout layout)
  {
    float ret = data.rotation;
    GroupFrame parent = layout.getParentGroupFrame(this);
    if (parent != null)
    {
      ret += parent.getAbsoluteRotation(layout);
    }
    return ret;
  }
  
  
  /**
   * Changes the basic data of this frame.
   */
  public abstract Frame withData(FrameData data);

  
  /**
   * Transforms the given coordinates in page space to a frame position.
   * If the given coordinates are not within this frame, null is returned.
   */
  public FramePosition computeFramePosition(Point2f p, Layout layout)
  {
    return computeFramePosition(p, false, layout);
  }
  
  
  /**
   * Transforms the given coordinates in page space to a frame position.
   * If the given coordinates are not within this frame and force is false,
   * null is returned. Otherwise the computed coordinates are returned, even if
   * they are outside the frame.
   */
  public FramePosition computeFramePosition(Point2f p, boolean force, Layout layout)
  {
    Point2f pos = getAbsolutePosition(layout);
    float rot = getAbsoluteRotation(layout);
    float hw = data.size.width / 2;
    float hh = data.size.height / 2;
    //two cases: no rotation or rotation
    if (rot == 0f)
    {
      //no rotation. this is easy to compute.
      if (force || (p.x >= pos.x - hw && p.x <= pos.x + hw &&
        p.y >= pos.y - hh && p.y <= pos.y + hh))
      {
        return new FramePosition(this, new Point2f(p.x - pos.x, p.y - pos.y));
      }
      else
      {
        return null;
      }
    }
    else
    {
      //rotated frame. this is more complicated.
      //first fast check: point within circle around center point?
      //radius is half width + half height (easy to compute)
      float radius = (data.size.width + data.size.height) / 2f;
      Point2f pRel = new Point2f(p.x - pos.x, p.y - pos.y);
      float distanceSq = pRel.x * pRel.x + pRel.y * pRel.y;
      if (force || (distanceSq <= radius * radius)) //TODO: does this really work?
      {
        //the given point could be within the frame. rotate the
        //point and check again.
        pRel = MathUtils.rotate(pRel, -rot);
        if (force || (pRel.x >= -hw && pRel.x <= +hw &&
          pRel.y >= -hh && pRel.y <= +hh))
        {
          return new FramePosition(this, new Point2f(pRel.x, pRel.y));
        }
        else
        {
          return null;
        }
      }
      else
      {
        return null;
      }
    }
  }
  
  
  /**
   * Transforms the given coordinates in frame space to
   * a position in page space.
   * 
   * TODO: untested for higher levels
   */
  public final Point2f computePagePosition(Point2f p, Layout layout)
  {
  	Point2f ret = p;
  	Frame frame = this;
  	//convert level after level, until page level is reached
  	while (frame != null)
  	{
  		if (frame.data.rotation != 0f)
  			ret = MathUtils.rotate(ret, frame.data.rotation);
  		ret = ret.add(frame.data.position);
  		frame = layout.getParentGroupFrame(frame);
  	}
    return ret;
  }
  
  
  /**
   * Gets the type of this frame.
   */
  public abstract FrameType getType();
  

}
