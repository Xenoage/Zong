package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;

/**
 * MusicXML coda.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlCoda
	implements MxlDirectionTypeContent, MxlPrintStyleContent {

	public static final String elemName = "coda";

	private MxlPrintStyle printStyle;
	

	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Coda;
	}

	@NonNull public static MxlCoda read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		return new MxlCoda(printStyle);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		printStyle.write(writer);
		writer.writeElementEnd();
	}

}
