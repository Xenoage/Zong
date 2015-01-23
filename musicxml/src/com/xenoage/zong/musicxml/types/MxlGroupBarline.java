package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.enums.MxlGroupBarlineValue;

/**
 * MusicXML group-barline.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlGroupBarline {

	public static final String elemName = "group-barline";

	@NonNull private MxlGroupBarlineValue value;
	@NonNull private MxlColor color;


	@NonNull public static MxlGroupBarline read(XmlReader reader) {
		MxlColor color = MxlColor.read(reader);
		MxlGroupBarlineValue value = MxlGroupBarlineValue.read(reader);
		return new MxlGroupBarline(value, color);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		value.write(writer);
		color.write(writer);
		writer.writeElementEnd();
	}

}
