package com.xenoage.zong.musicxml.types.choice;

import org.w3c.dom.Element;


/**
 * Interface for all types of content that may appear within
 * a credit element.
 * 
 * @author Andreas Wenger
 */
public interface MxlCreditContent
{
	
	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlCreditContentType
	{
		CreditImage,
		CreditWords;
	}
	
	
	public MxlCreditContentType getCreditContentType();
	
	
	public void write(Element e);

}
