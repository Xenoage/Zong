package com.xenoage.zong.musicxml.types.groups;

import static com.xenoage.utils.Parser.parseFloat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML all-margins, including the left-right-margins group.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlAllMargins {

	private float leftMargin;
	private float rightMargin;
	private float topMargin;
	private float bottomMargin;

	
	@MaybeNull public static MxlAllMargins read(XmlReader reader) {
		float leftMargin = 0;
		float rightMargin = 0;
		float topMargin = 0;
		float bottomMargin = 0;
		while (reader.openNextChildElement()) {
			String eName = reader.getElementName();
			if (eName.equals("left-margin"))
				leftMargin = parseFloat(reader.getText());
			else if (eName.equals("right-margin"))
				rightMargin = parseFloat(reader.getText());
			else if (eName.equals("top-margin"))
				topMargin = parseFloat(reader.getText());
			else if (eName.equals("bottom-margin"))
				bottomMargin = parseFloat(reader.getText());
			reader.closeElement();
		}
		if (leftMargin != 0 || rightMargin != 0 || topMargin != 0 || bottomMargin != 0)
			return new MxlAllMargins(leftMargin, rightMargin, topMargin, bottomMargin);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementText("left-margin", leftMargin);
		writer.writeElementText("right-margin", rightMargin);
		writer.writeElementText("top-margin", topMargin);
		writer.writeElementText("bottom-margin", bottomMargin);
	}

}
