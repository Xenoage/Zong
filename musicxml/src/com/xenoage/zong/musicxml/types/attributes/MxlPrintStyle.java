package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.zong.musicxml.types.attributes.MxlColor.noColor;
import static com.xenoage.zong.musicxml.types.attributes.MxlFont.noFont;
import static com.xenoage.zong.musicxml.types.attributes.MxlPosition.noPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.util.MxlPositionContent;

/**
 * MusicXML print-style.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public final class MxlPrintStyle
	implements MxlPositionContent {

	public static final MxlPrintStyle noPrintStyle = new MxlPrintStyle(noPosition, noFont, noColor);
	
	private final MxlPosition position;
	private final MxlFont font;
	private final MxlColor color;


	public static MxlPrintStyle read(XmlReader reader) {
		MxlPosition position = MxlPosition.read(reader);
		MxlFont font = MxlFont.read(reader);
		MxlColor color = MxlColor.read(reader);
		if (position != noPosition || font != noFont || color != noColor)
			return new MxlPrintStyle(position, font, color);
		else
			return noPrintStyle;
	}

	public void write(XmlWriter writer) {
		if (this != noPrintStyle) {
			position.write(writer);
			font.write(writer);
			color.write(writer);
		}
	}

}
