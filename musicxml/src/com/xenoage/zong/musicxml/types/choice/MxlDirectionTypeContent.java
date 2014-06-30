package com.xenoage.zong.musicxml.types.choice;

import com.xenoage.utils.xml.XmlWriter;

/**
 * Interface for all types of content that may appear within
 * a direction-type element.
 * 
 * @author Andreas Wenger
 */
public interface MxlDirectionTypeContent {

	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlDirectionTypeContentType {
		Segno,
		Words,
		Coda,
		Wedge,
		Dynamics,
		Pedal,
		Metronome,
		Image;
	}


	public MxlDirectionTypeContentType getDirectionTypeContentType();

	public void write(XmlWriter writer);

}
