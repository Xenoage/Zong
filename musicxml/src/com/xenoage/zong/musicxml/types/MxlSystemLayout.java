package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.xenoage.utils.Parser.parseFloatNull;


/**
 * MusicXML system-layout.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlSystemLayout {

	public static final String elemName = "system-layout";

	@MaybeNull private MxlSystemMargins systemMargins;
	@MaybeNull private final Float systemDistance;
	@MaybeNull private final Float topSystemDistance;
	

	@NonNull public static MxlSystemLayout read(XmlReader reader) {
		MxlSystemMargins systemMargins = null;
		Float systemDistance = null;
		Float topSystemDistance = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			switch (n) {
				case MxlSystemMargins.elemName:
					systemMargins = MxlSystemMargins.read(reader);
					break;
				case "system-distance":
					systemDistance = parseFloatNull(reader.getText());
					break;
				case "top-system-distance":
					topSystemDistance = parseFloatNull(reader.getText());
					break;
			}
			reader.closeElement();
		}
		return new MxlSystemLayout(systemMargins, systemDistance, topSystemDistance);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (systemMargins != null)
			systemMargins.write(writer);
		writer.writeElementText("system-distance", systemDistance);
		writer.writeElementText("top-system-distance", topSystemDistance);
		writer.writeElementEnd();
	}

}
