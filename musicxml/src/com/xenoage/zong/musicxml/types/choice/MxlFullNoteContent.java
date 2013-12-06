package com.xenoage.zong.musicxml.types.choice;

import org.w3c.dom.Element;

import com.xenoage.zong.musicxml.types.MxlFullNote;


/**
 * Interface for all types of content that may appear within
 * a {@link MxlFullNote}.
 * 
 * @author Andreas Wenger
 */
public interface MxlFullNoteContent
{
	
	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlFullNoteContentType
	{
		Pitch,
		Unpitched,
		Rest;
	}
	
	
	public MxlFullNoteContentType getFullNoteContentType();
	
	
	public void write(Element e);

}
