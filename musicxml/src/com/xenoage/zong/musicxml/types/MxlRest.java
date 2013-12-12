package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML rest.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "display-step-octave")
public final class MxlRest
	implements MxlFullNoteContent {

	public static final String elemName = "rest";


	@Override public MxlFullNoteContentType getFullNoteContentType() {
		return MxlFullNoteContentType.Rest;
	}

	@NonNull public static MxlRest read() {
		return new MxlRest();
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementEmpty(elemName);
	}

}
