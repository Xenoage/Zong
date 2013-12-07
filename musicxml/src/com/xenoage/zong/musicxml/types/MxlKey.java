package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.Parser;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlMode;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML key.
 * 
 * Currently only the fifths element is supported.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "non-traditional-key,key-octave,number,print-style,print-object",
	partly = "traditional-key")
@AllArgsConstructor @Getter @Setter
public final class MxlKey {

	public static final String elemName = "key";

	public int fifths;
	@MaybeNull public MxlMode mode;


	/**
	 * Returns null, if the key is unsupported.
	 */
	@MaybeNull public static MxlKey read(XmlReader reader) {
		Integer fifths = null;
		MxlMode mode = null;
		while (reader.openNextChildElement()) {
			String eName = reader.getElementName();
			if (eName.equals("fifths")) {
				fifths = Parser.parseIntegerNull(reader.getText());
			}
			else if (eName.equals(MxlMode.elemName)) {
				mode = MxlMode.read(reader);
			}
			reader.closeElement();
		}
		if (fifths != null)
			return new MxlKey(fifths, mode);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("fifths", fifths);
		if (mode != null)
			mode.write(writer);
		writer.writeElementEnd();
	}

}
