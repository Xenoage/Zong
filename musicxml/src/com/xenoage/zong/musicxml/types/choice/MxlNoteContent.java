package com.xenoage.zong.musicxml.types.choice;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.MxlFullNote;

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

	@NonNull public MxlFullNote getFullNote();

	public void write(XmlWriter writer);

}
