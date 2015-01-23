package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopChange;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML pedal.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "line")
@AllArgsConstructor @Getter @Setter
public final class MxlPedal
	implements MxlDirectionTypeContent, MxlPrintStyleContent {

	public static final String elemName = "pedal";

	private MxlStartStopChange type;
	private MxlPrintStyle printStyle;
	

	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Pedal;
	}

	@NonNull public static MxlPedal read(XmlReader reader) {
		String type = reader.getAttributeNotNull("type");
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		return new MxlPedal(MxlStartStopChange.read(type), printStyle);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("type", type.write());
		printStyle.write(writer);
		writer.writeElementEnd();
	}

}
