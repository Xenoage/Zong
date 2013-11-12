package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.base.collections.CollectionUtils.containsNull;

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.utils.pdlib.Vector;


/**
 * This class contains the arrangement
 * of systems on a single score frame.
 *
 * @author Andreas Wenger
 */
public final class FrameArrangement
{

  private final Vector<SystemArrangement> systems;
  private final Size2f usableSize;
  
  
  /**
   * Creates a new FrameArrangement, containing
   * the given systems.
   */
  public FrameArrangement(Vector<SystemArrangement> systems, Size2f usableSize)
  {
  	//null values are not allowed
  	if (containsNull(systems) || usableSize == null)
  		throw new IllegalArgumentException("Systems and size may not be null");
  	this.systems = systems;
  	this.usableSize = usableSize;
  }
  
  
  /**
   * Gets the systems.
   */
  public Vector<SystemArrangement> getSystems()
  {
    return systems;
  }


  /**
   * Gets the size in mm this frame arrangement may use.
   */
  public Size2f getUsableSize()
  {
    return usableSize;
  }

  
  /**
   * Gets the index of the first measure, or -1 if there are no measures.
   */
  public int getStartMeasureIndex()
  {
  	if (systems.size() == 0)
  		return -1;
  	else
  		return systems.getFirst().getStartMeasureIndex();
  }
  
  
  /**
   * Gets the index of the last measure, or -1 if there are no measures.
   */
  public int getEndMeasureIndex()
  {
  	if (systems.size() == 0)
  		return -1;
  	else
  		return systems.getLast().getEndMeasureIndex();
  }
  
  
}
