package com.xenoage.zong.layout.frames;



/**
 * A image frame is a frame that contains
 * a bitmap image.
 * 
 * @author Andreas Wenger
 */
public class ImageFrame
  extends Frame
{
  
  private String imagePath;
  
  
  /**
   * Creates a new image frame.
   */
  public ImageFrame(FrameData data, String imagePath)
  {
    super(data);
    this.imagePath = imagePath;
  }


  /**
   * Gets the path of this image.
   */
  public String getImagePath()
  {
    return imagePath;
  }
  
  
  /**
   * Changes the basic data of this frame.
   */
  @Override public ImageFrame withData(FrameData data)
  {
  	return new ImageFrame(data, imagePath);
  }
  
  
  /**
   * Gets the type of this frame.
   */
  @Override public FrameType getType()
  {
  	return FrameType.ImageFrame;
  }
  

}
