package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.Parser.parseFloatNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML staff-layout.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlStaffLayout {

	public static final String elemName = "staff-layout";

	@MaybeNull private Float staffDistance;
	/** May be null. If within in the defaults element, this means
	 * "for all staves". Otherwise, use {@link #getNumberNotNull()}. */
	@MaybeNull private Integer number;

	private static final int defaultNumber = 1;


	@NonNull public int getNumberNotNull() {
		return (number != null ? number : defaultNumber);
	}

	@NonNull public static MxlStaffLayout read(XmlReader reader) {
		Float staffDistance = null;
		Integer number = reader.getAttributeInt("number");
		if (reader.openNextChildElement("staff-distance")) {
			staffDistance = parseFloatNull(reader.getText());
			reader.closeElement();
		}
		return new MxlStaffLayout(staffDistance, number);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("number", number);
		writer.writeElementText("staff-distance", staffDistance);
		writer.writeElementEnd();
	}

}
