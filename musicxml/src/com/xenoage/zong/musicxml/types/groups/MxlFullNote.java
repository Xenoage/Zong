package com.xenoage.zong.musicxml.types.groups;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlRest;
import com.xenoage.zong.musicxml.types.MxlUnpitched;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML full-note.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "unpitched,rest")
@Getter @Setter
public final class MxlFullNote {

	private boolean chord;
	@NonNull private MxlFullNoteContent content;


	public void readElement(XmlReader reader) {
		String n = reader.getElementName();
		if (n.equals("chord")) {
			chord = true;
		}
		else if (n.equals(MxlPitch.elemName)) {
			content = MxlPitch.read(reader);
		}
		else if (n.equals(MxlUnpitched.elemName)) {
			content = MxlUnpitched.read();
		}
		else if (n.equals(MxlRest.elemName)) {
			content = MxlRest.read();
		}
	}
	
	public void check(XmlReader reader) {
		if (content == null)
			throw reader.dataException("content missing");
	}

	public void write(XmlWriter writer) {
		if (chord)
			writer.writeElementEmpty("chord");
		content.write(writer);
	}

}
