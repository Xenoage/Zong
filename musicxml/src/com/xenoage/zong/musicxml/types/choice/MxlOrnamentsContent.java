package com.xenoage.zong.musicxml.types.choice;

import com.xenoage.utils.xml.XmlWriter;

/**
 * Interface for all types of content that may appear within
 * an ornaments element.
 * 
 * @author Andreas Wenger
 */
public interface MxlOrnamentsContent {

	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlOrnamentsContentType {
		TrillMark,
		Turn,
		DelayedTurn,
		InvertedTurn,
		Mordent,
		InvertedMordent,
		//after each of the above elements, any number of accidental-mark elements may follow:
		AccidentalMark
	}

	public MxlOrnamentsContentType getOrnamentsContentType();

	public void write(XmlWriter writer);

}
