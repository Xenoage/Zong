package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlBeamValue;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML beam.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "repeater,fan,color")
@AllArgsConstructor @Getter @Setter
public final class MxlBeam {

	public static final String elemName = "beam";

	@NonNull private MxlBeamValue value;
	private int number;

	private static final int defaultNumber = 1;


	@NonNull public static MxlBeam read(XmlReader reader) {
		int number = notNull(reader.getAttributeInt("number"), defaultNumber);
		MxlBeamValue value = MxlBeamValue.read(reader);
		return new MxlBeam(value, number);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("number", number);
		value.write(writer);
		writer.writeElementEnd();
	}

}
