package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;

/**
 * MusicXML segno.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlSegno
	implements MxlDirectionTypeContent {

	public static final String elemName = "segno";

	@MaybeNull private MxlPrintStyle printStyle;
	

	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Segno;
	}

	@NonNull public static MxlSegno read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		return new MxlSegno(printStyle);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (printStyle != null)
			printStyle.write(writer);
		writer.writeElementEnd();
	}

}