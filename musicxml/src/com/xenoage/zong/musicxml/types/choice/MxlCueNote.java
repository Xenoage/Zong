package com.xenoage.zong.musicxml.types.choice;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML cue note content.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "full-note")
@Getter @Setter
public final class MxlCueNote
	implements MxlNoteContent {
	
	public static final String elemName = "cue";

	MxlFullNote fullNote = new MxlFullNote();
	private int duration = 0;


	@Override public MxlNoteContentType getNoteContentType() {
		return MxlNoteContentType.Cue;
	}
	
	public static MxlCueNote read() {
		return new MxlCueNote();
	}

	@Override public boolean readElement(XmlReader reader) {
		String n = reader.getElementName();
		if (n.equals("duration")) {
			duration = reader.getTextIntNotNull();
			return true;
		}
		else {
			return fullNote.readElement(reader);
		}
	}
	
	@Override public void check(XmlReader reader) {
		fullNote.check(reader);
		if (duration <= 0)
			throw reader.dataException("duration <= 0");
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementEmpty(elemName);
		fullNote.write(writer);
		writer.writeElementText("duration", duration);
	}

}
