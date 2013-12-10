package com.xenoage.zong.musicxml.types.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlBackwardForward;

/**
 * MusicXML repeat.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public final class MxlRepeat {

	public static final String elemName = "repeat";

	@NonNull private MxlBackwardForward direction;
	@MaybeNull private Integer times;
	

	@NonNull public static MxlRepeat read(XmlReader reader) {
		return new MxlRepeat(MxlBackwardForward.read(reader), reader.getAttributeInt("times"));
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		direction.write(writer);
		writer.writeAttribute("times", times);
		writer.writeElementEnd();
	}

}
