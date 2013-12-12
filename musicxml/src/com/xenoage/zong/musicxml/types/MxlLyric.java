package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlLyricContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML lyric.
 * 
 * Consists of a {@link MxlLyricContent}.
 * Other information is ignored.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "end-line,end-paragraph,editorial,name,justify,position,"
	+ "placement,color", children = "")
@AllArgsConstructor @Getter @Setter
public final class MxlLyric {

	public static final String elemName = "lyric";

	@NonNull private MxlLyricContent content;
	@MaybeNull private String number;
	

	/**
	 * Reads and returns the lyric content of the given element,
	 * or returns null if unsupported.
	 */
	@MaybeNull public static MxlLyric read(XmlReader reader) {
		String number = reader.getAttribute("number");
		MxlLyricContent content = null;
		if (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("syllabic") || n.equals("text")) {
				content = MxlSyllabicText.read(reader);
			}
			else if (n.equals("extend")) {
				content = MxlExtend.read();
			}
			reader.closeElement();
		}
		if (content != null) {
			return new MxlLyric(content, number);
		}
		else {
			return null;
		}
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		content.write(writer);
		writer.writeElementEnd();
	}

}
