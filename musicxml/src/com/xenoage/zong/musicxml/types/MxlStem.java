package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.enums.MxlStemValue;

/**
 * MusicXML stem.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlStem {

	public static final String elemName = "stem";

	@NonNull private MxlStemValue value;
	@MaybeNull private MxlPosition yPosition;
	@MaybeNull private MxlColor color;


	@NonNull public static MxlStem read(XmlReader reader) {
		MxlPosition yPosition = MxlPosition.read(reader);
		MxlColor color = MxlColor.read(reader);
		MxlStemValue stem = MxlStemValue.read(reader);
		return new MxlStem(stem, yPosition, color);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (yPosition != null)
			yPosition.write(writer);
		if (color != null)
			color.write(writer);
		value.write(writer);
		writer.writeElementEnd();
	}

}
