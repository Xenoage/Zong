package com.xenoage.zong.musicxml.types.choice;

import org.w3c.dom.Element;

import com.xenoage.zong.musicxml.types.MxlMusicData;


/**
 * Interface for all types of content that may appear within
 * a {@link MxlMusicData}.
 * 
 * @author Andreas Wenger
 */
public interface MxlMusicDataContent
{
	
	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlMusicDataContentType
	{
		Note,
		Backup,
		Forward,
		Direction,
		Attributes,
		Print,
		Barline;
	}
	
	
	public MxlMusicDataContentType getMusicDataContentType();
	
	
	public void write(Element e);

}
