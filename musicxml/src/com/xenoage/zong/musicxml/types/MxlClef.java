package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.Parser.parseIntegerNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlClefSign;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML clef.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "line,additional,size,print-style,print-object")
@AllArgsConstructor @Getter @Setter
public final class MxlClef {

	public static final String elemName = "clef";

	@NonNull private MxlClefSign sign;
	private int clefOctaveChange;
	private int number;

	private static final int defaultClefOctaveChange = 0;
	private static final int defaultNumber = 1;

	
	public static MxlClef read(XmlReader reader) {
		Integer number = notNull(reader.getAttributeInt("number"), defaultNumber);
		MxlClefSign sign = null;
		int clefOctaveChange = defaultClefOctaveChange;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals(MxlClefSign.elemName))
				sign = MxlClefSign.read(reader);
			else if (n.equals("clef-octave-change"))
				clefOctaveChange = parseIntegerNull(reader.getText());
			reader.closeElement();
		}
		if (sign == null)
			throw reader.dataException(MxlClefSign.elemName + " missing");
		return new MxlClef(sign, clefOctaveChange, number);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (number != defaultNumber)
			writer.writeAttribute("number", number);
		sign.write(writer);
		if (clefOctaveChange != defaultClefOctaveChange)
			writer.writeElementText("clef-octave-change", clefOctaveChange);
		writer.writeElementEnd();
	}

}
