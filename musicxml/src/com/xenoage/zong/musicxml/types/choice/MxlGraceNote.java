package com.xenoage.zong.musicxml.types.choice;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlYesNo;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML grace note content.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "tie,steal-time-previous,steal-time-following,make-time", children = "full-note")
@Getter @Setter
public final class MxlGraceNote
	implements MxlNoteContent {
	
	public static final String elemName = "grace";

	@NonNull private MxlFullNote fullNote;
	@MaybeNull private Boolean slash;


	@Override public MxlNoteContentType getNoteContentType() {
		return MxlNoteContentType.Grace;
	}
	
	@NonNull public static MxlGraceNote read(XmlReader reader) {
		MxlGraceNote ret = new MxlGraceNote();
		ret.fullNote = new MxlFullNote();
		ret.slash = MxlYesNo.readNull(reader.getAttribute("slash"));
		return ret;
	}

	@Override public void readElement(XmlReader reader) {
		fullNote.readElement(reader);
	}
	
	@Override public void check(XmlReader reader) {
		fullNote.check(reader);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementEmpty(elemName);
		if (slash != null)
			writer.writeAttribute("slash", MxlYesNo.write(slash));
		fullNote.write(writer);
	}

}