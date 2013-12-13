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
			if (n.equals(MxlSystemMargins.elemName))
				systemMargins = MxlSystemMargins.read(reader);
			else if (n.equals("system-distance"))
				systemDistance = parseFloatNull(reader.getText());
			else if (n.equals("top-system-distance"))
				topSystemDistance = parseFloatNull(reader.getText());
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
