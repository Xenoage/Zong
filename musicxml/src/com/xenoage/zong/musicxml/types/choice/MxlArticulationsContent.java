package com.xenoage.zong.musicxml.types.choice;

import org.w3c.dom.Element;


/**
 * Interface for all types of content that may appear within
 * an articulations element.
 * 
 * @author Andreas Wenger
 */
public interface MxlArticulationsContent
{
	
	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlArticulationsContentType
	{
		Accent,
		StrongAccent,
		Staccato,
		Tenuto,
		Staccatissimo;
	}
	
	
	public MxlArticulationsContentType getArticulationsContentType();
	
	
	public void write(Element e);

}
