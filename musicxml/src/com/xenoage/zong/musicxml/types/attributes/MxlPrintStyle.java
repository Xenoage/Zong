package com.xenoage.zong.musicxml.types.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML print-style.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlPrintStyle {

	@MaybeNull private MxlPosition position;
	@MaybeNull private MxlFont font;
	@MaybeNull private MxlColor color;


	@MaybeNull public static MxlPrintStyle read(XmlReader reader) {
		MxlPosition position = MxlPosition.read(reader);
		MxlFont font = MxlFont.read(reader);
		MxlColor color = MxlColor.read(reader);
		if (position != null || font != null || color != null)
			return new MxlPrintStyle(position, font, color);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		if (position != null)
			position.write(writer);
		if (font != null)
			font.write(writer);
		if (color != null)
			color.write(writer);
	}

}
