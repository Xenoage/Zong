package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.Parser.parseFloat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML system-margins, including the left-right-margins group.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlSystemMargins {
	
	public static final String elemName = "system-margins";

	private float leftMargin;
	private float rightMargin;

	@NonNull public static MxlSystemMargins read(XmlReader reader) {
		float leftMargin = 0, rightMargin = 0;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("left-margin"))
				leftMargin = parseFloat(reader.getText());
			else if (n.equals("right-margin"))
				rightMargin = parseFloat(reader.getText());
			reader.closeElement();
		}
		return new MxlSystemMargins(leftMargin, rightMargin);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("left-margin", leftMargin);
		writer.writeElementText("right-margin", rightMargin);
		writer.writeElementEnd();
	}

}
