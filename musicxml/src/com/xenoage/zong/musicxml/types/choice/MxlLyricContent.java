package com.xenoage.zong.musicxml.types.choice;

import org.w3c.dom.Element;

import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * Interface for all types of content that may appear within
 * a MusicXML lyric.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="laughing,humming")
public interface MxlLyricContent
{
	
	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlLyricContentType
	{
		SyllabicText,
		Extend;
	}
	
	
	public MxlLyricContentType getLyricContentType();
	
	
	public void write(Element parent);

}
