package com.xenoage.zong.musicxml.types.choice;

import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.MxlNotations;

/**
 * Interface for all elements that can be children of
 * {@link MxlNotations}.
 * 
 * @author Andreas Wenger
 */
public interface MxlNotationsContent {

	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the element.
	 */
	public enum MxlNotationsContentType {
		AccidentalMark,
		Articulations,
		Dynamics,
		Fermata,
		Ornaments,
		SlurOrTied;
	}


	/**
	 * Gets the type of this {@link MxlNotationsContent}.
	 */
	public MxlNotationsContentType getNotationsContentType();

	public void write(XmlWriter writer);

}
