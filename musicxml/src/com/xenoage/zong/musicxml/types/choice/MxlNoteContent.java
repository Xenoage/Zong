package com.xenoage.zong.musicxml.types.choice;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.MxlFullNote;


/**
 * Interface for all types of content that may appear within
 * a MusicXML note.
 * 
 * @author Andreas Wenger
 */
public interface MxlNoteContent
{
	
	
	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlNoteContentType
	{
		Grace,
		Cue,
		Normal;
	}
	
	
	public MxlNoteContentType getNoteContentType();
	
	@NeverNull public MxlFullNote getFullNote();
	
	public void write(Element e);

}
