package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlLyricContent;

/**
 * MusicXML extend.
 * 
 * @author Andreas Wenger
 */
public final class MxlExtend
	implements MxlLyricContent {

	public static final String elemName = "extend";


	@Override public MxlLyricContentType getLyricContentType() {
		return MxlLyricContentType.Extend;
	}

	public static MxlExtend read() {
		return new MxlExtend();
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementEmpty(elemName);
	}

}
