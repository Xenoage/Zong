package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlFont;


/**
 * MusicXML lyric-font.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlLyricFont {

	public static final String elemName = "lyric-font";

	@MaybeNull private String number;
	@MaybeNull private String name;
	@MaybeNull private MxlFont font;


	@MaybeNull public static MxlLyricFont read(XmlReader reader) {
		String number = reader.getAttribute("number");
		String name = reader.getAttribute("name");
		MxlFont font = MxlFont.read(reader);
		if (number != null || name != null || font != null)
			return new MxlLyricFont(number, name, font);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (number != null)
			writer.writeAttribute("number", number);
		if (name != null)
			writer.writeAttribute("name", name);
		if (font != null)
			font.write(writer);
		writer.writeElementEnd();
	}

}
