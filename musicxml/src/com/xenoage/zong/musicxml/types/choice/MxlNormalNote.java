package com.xenoage.zong.musicxml.types.choice;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML normal note content.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "tie")
@Getter @Setter
public final class MxlNormalNote
	implements MxlNoteContent {

	private MxlFullNote fullNote = new MxlFullNote();
	private int duration = 0;


	@Override public MxlNoteContentType getNoteContentType() {
		return MxlNoteContentType.Normal;
	}

	public static MxlNormalNote read() {
		return new MxlNormalNote();
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
		fullNote.write(writer);
		writer.writeElementText("duration", duration);
	}

}
