package com.xenoage.zong.musicxml.types.choice;

import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.util.MxlEmptyPlacementContent;

/**
 * Interface for all types of content that may appear within
 * an articulations element.
 * 
 * @author Andreas Wenger
 */
public interface MxlArticulationsContent
	extends MxlEmptyPlacementContent {

	/**
	 * This enum allows using quick switch-case statements
	 * for finding out the type of the content.
	 */
	public enum MxlArticulationsContentType {
		Accent,
		StrongAccent,
		Staccato,
		Tenuto,
		Staccatissimo;
	}
	
	/**
	 * Not in MusicXML spec, but all types of articulations have a placement.
	 */
	@Override public MxlEmptyPlacement getEmptyPlacement();

	public MxlArticulationsContentType getArticulationsContentType();

	public void write(XmlWriter writer);

}
