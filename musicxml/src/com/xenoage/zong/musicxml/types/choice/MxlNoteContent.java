package com.xenoage.zong.musicxml.types.choice;

import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;

/**
 * Interface for all types of content that may appear within
 * a MusicXML note.
 * 
 * @author Andreas Wenger
 */
public interface MxlNoteContent {

	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlNoteContentType {
		Grace,
		Cue,
		Normal;
	}


	public MxlNoteContentType getNoteContentType();
	
	public MxlFullNote getFullNote();
	
	/**
	 * Reads information from the given XML element, and returns true if relevant for this instance.
	 * Returns false if no information was read.
	 */
	public boolean readElement(XmlReader reader);
	
	/**
	 * Throws an {@link XmlDataException} if the data of the instance is not consistent.
	 */
	public void check(XmlReader reader)
		throws XmlDataException;

	public void write(XmlWriter writer);

}
