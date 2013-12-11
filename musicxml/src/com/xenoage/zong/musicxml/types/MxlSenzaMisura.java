package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlTimeContent;

/**
 * MusicXML senza-misura.
 * 
 * @author Andreas Wenger
 */
public final class MxlSenzaMisura
	implements MxlTimeContent {

	public static final String elemName = "senza-misura";


	@Override public MxlTimeContentType getTimeContentType() {
		return MxlTimeContentType.SenzaMisura;
	}

	@NonNull public static MxlSenzaMisura read() {
		return new MxlSenzaMisura();
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementEmpty(elemName);
	}

}
