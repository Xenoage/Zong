package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopDiscontinue;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML ending.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "print-object,print-style,end-length,text-x,text-y")
@AllArgsConstructor @Getter @Setter
public final class MxlEnding {

	public static final String elemName = "ending";

	@NonNull private String number;
	@NonNull private MxlStartStopDiscontinue type;


	@NonNull public static MxlEnding read(XmlReader reader) {
		String number = reader.getAttributeNotNull("number");
		MxlStartStopDiscontinue type = MxlStartStopDiscontinue.read(reader.getAttributeNotNull("type"));
		return new MxlEnding(number, type);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("number", number);
		writer.writeAttribute("type", type.write());
		writer.writeElementEnd();
	}

}
