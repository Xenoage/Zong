package com.xenoage.zong.layout.frames;

import com.xenoage.zong.text.FormattedText;


/**
 * A text frame is a frame that contains a
 * formatted text.
 * 
 * It may use multiple fonts, colors and
 * styles and may contain several paragraphs.
 * 
 * @author Andreas Wenger
 */
public class TextFrame
	extends Frame
{
	
	private final FormattedText text;
	
	
	/**
	 * Line breaks: Manual means, that if there is no line break in the formatted text,
	 * the text will continue beyond the borders of the frame. Automatic means, that
	 * pragraphs are automatically broken into lines fitting into the borders of the frame.
	 */
	public enum LineBreakStyle { Manual, Automatic };
	private final LineBreakStyle lineBreakStyle;
	
	private FormattedText cacheTextLineBreak = null;


	/**
	 * Creates a new {@link TextFrame}.
	 */
	public TextFrame(FrameData data, FormattedText text)
	{
		this(data, text, LineBreakStyle.Manual);
	}
	

	private TextFrame(FrameData data, FormattedText text, LineBreakStyle lineBreakStyle)
	{
		super(data);
		this.text = text;
		this.lineBreakStyle = lineBreakStyle;
		updateCache();
	}


	/**
   * Gets the text content of this frame.
   */
  public FormattedText getText()
  {
  	return text;
  }
  
  
  /**
   * Sets the text content of this frame.
   */
  public TextFrame withText(FormattedText text)
  {
  	return new TextFrame(data, text, lineBreakStyle);
  }
  
  
  /**
   * Gets the text content of this frame with automatic line breaks,
   * if enabled, otherwise the original text.
   */
  public FormattedText getTextWithLineBreaks()
  {
  	if (lineBreakStyle == LineBreakStyle.Automatic)
  	{
  		return cacheTextLineBreak;
  	}
  	else
  	{
  		return text;
  	}
  }
  
  
  /**
   * Gets the type of line breaks.
   */
  public LineBreakStyle getLineBreakStyle()
	{
		return lineBreakStyle;
	}


  /**
   * Sets the type of line breaks.
   */
	public TextFrame withLineBreakStyle(LineBreakStyle lineBreakStyle)
	{
		return new TextFrame(data, text, lineBreakStyle);
	}
  
  
  /**
   * Gets the type of this frame.
   */
  @Override public FrameType getType()
  {
  	return FrameType.TextFrame;
  }
  
  
  /**
   * Updates the cache:
   * Text with line breaks for text frame with automatic line break.
   */
  private void updateCache()
  {
  	if (lineBreakStyle == LineBreakStyle.Automatic)
  	{
  		cacheTextLineBreak = text.lineBreak(data.size.width);
  	}
  	else
  	{
  		cacheTextLineBreak = null;
  	}
  }
  
  
  /**
   * Changes the basic data of this frame.
   */
  @Override public TextFrame withData(FrameData data)
  {
  	return new TextFrame(data, text, lineBreakStyle);
  }
	
	
}
