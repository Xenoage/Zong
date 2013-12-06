package com.xenoage.zong.musicxml.types.choice;

import org.w3c.dom.Element;


/**
 * Interface for all types of content that may appear within
 * a MusicXML time.
 * 
 * @author Andreas Wenger
 */
public interface MxlTimeContent
{
	
	
	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlTimeContentType
	{
		NormalTime,
		SenzaMisura;
	}
	
	
	public MxlTimeContentType getTimeContentType();
	
	
	public void write(Element e);

}
