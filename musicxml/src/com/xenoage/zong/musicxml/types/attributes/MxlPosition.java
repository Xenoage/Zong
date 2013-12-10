package com.xenoage.zong.musicxml.types.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML x-position and y-position.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlPosition {

	@MaybeNull private Float defaultX, defaultY, relativeX, relativeY;


	@MaybeNull public static MxlPosition read(XmlReader reader) {
		Float defaultX = reader.getAttributeFloat("default-x");
		Float defaultY = reader.getAttributeFloat("default-y");
		Float relativeX = reader.getAttributeFloat("relative-x");
		Float relativeY = reader.getAttributeFloat("relative-y");
		if (defaultX != null || defaultY != null || relativeX != null || relativeY != null)
			return new MxlPosition(defaultX, defaultY, relativeX, relativeY);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute("default-x", defaultX);
		writer.writeAttribute("default-y", defaultY);
		writer.writeAttribute("relative-x", relativeX);
		writer.writeAttribute("relative-y", relativeY);
	}

}
