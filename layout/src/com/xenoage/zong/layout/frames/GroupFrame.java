package com.xenoage.zong.layout.frames;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.layout.Layout;


/**
 * A group frame is a frame that contains
 * any number of child frames.
 * 
 * @author Andreas Wenger
 */
public final class GroupFrame
	extends Frame
{

	/** The list of child frames. */
	public final PVector<Frame> children;


	/**
	 * Creates a new group frame.
	 */
	public GroupFrame(FrameData data)
	{
		super(data);
		this.children = new PVector<Frame>();
	}
	

	private GroupFrame(FrameData data, PVector<Frame> children)
	{
		super(data);
		this.children = children;
	}


	/**
	 * Adds a child frame to this group frame, if not already a child.
	 * Call the 
	 */
	public GroupFrame plusChildFrame(Frame child)
	{
		if (children.contains(child))
			return this;
		else
			return new GroupFrame(data, children.plus(child));
	}
	
	
	/**
   * Replaces the given child frame by the given new one,
   * or deletes it if the new frame is null.
   */
	public GroupFrame replaceChildFrame(Frame oldFrame, Frame newFrame)
  {
		if (!children.contains(oldFrame))
			return this;
		else
			return new GroupFrame(data, children.replaceOrMinus(oldFrame, newFrame));
  }


	/**
	 * Transforms the given coordinates in page space to a frame position.
	 * If the given coordinates are not within this frame, null is returned.
	 * If the given coordinates are within a child frame, the child frame
	 * and the coordinates relative to the child frame are returned.
	 */
	@Override public FramePosition computeFramePosition(Point2f p, Layout layout)
	{
		//check child frames in reverse direction (begin with
		//top frame).
		for (int i = children.size() - 1; i >= 0; i--)
		{
			FramePosition fp = children.get(i).computeFramePosition(p, layout);
			if (fp != null)
			{
				return fp;
			}
		}
		//check this frame
		FramePosition fp = super.computeFramePosition(p, layout);
		if (fp != null)
		{
			return fp;
		}
		else
		{
			return null;
		}
	}
	
	
	/**
   * Changes the basic data of this frame.
   */
  @Override public GroupFrame withData(FrameData data)
  {
  	return new GroupFrame(data, children);
  }
	
	
	/**
   * Gets the type of this frame.
   */
  @Override public FrameType getType()
  {
  	return FrameType.GroupFrame;
  }


}
