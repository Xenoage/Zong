package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML text-element-data.
 * 
 * Only the actual text content is used, and format
 * information is ignored. 
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="font,color,text-decoration,text-rotation,letter-spacing,xml:lang,text-direction")
@AllArgsConstructor @Getter @Setter
public final class MxlTextElementData {

	@NonNull private String value;
	

	public static MxlTextElementData read(XmlReader reader) {
		String value = notNull(reader.getText(), "");
		return new MxlTextElementData(value);
	}

	public void write(XmlWriter writer) {
		writer.writeText(value);
	}

}
