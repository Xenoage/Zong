package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML unpitched.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "display-step-octave")
public final class MxlUnpitched
	implements MxlFullNoteContent {

	public static final String elemName = "unpitched";


	@Override public MxlFullNoteContentType getFullNoteContentType() {
		return MxlFullNoteContentType.Unpitched;
	}

	@NonNull public static MxlUnpitched read() {
		return new MxlUnpitched();
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementEmpty(elemName);
	}

}
