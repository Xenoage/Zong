package com.xenoage.zong.renderer.pool;

import com.xenoage.zong.layout.frames.ImageFrame;


/**
 * A cache for images, used for example within {@link ImageFrame}s.
 * It is needed for better performance, since it would be too slow
 * to load each image each time before it is rendered.
 *
 * @author Andreas Wenger
 */
public interface ImagePool
{
 
  /**
   * Gets the ImageFrame image with the given ID, or null.
   * If loaded, it is returned.
   */
  public Object getImageFrameImage(String path);

}
