package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML midi-instrument.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "midi-name,midi-bank,midi-unpitched,elevation")
@AllArgsConstructor @Getter @Setter
public final class MxlMidiInstrument {

	public static final String elemName = "midi-instrument";

	@MaybeNull private Integer midiChannel;
	@MaybeNull private Integer midiProgram;
	@MaybeNull private Float volume;
	@MaybeNull private Float pan;
	@NonNull public final String id;


	@NonNull public static MxlMidiInstrument read(XmlReader reader) {
		//attributes
		String id = reader.getAttribute("id");
		//elements
		Integer midiChannel = null;
		Integer midiProgram = null;
		Float volume = null;
		Float pan = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("midi-channel"))
				midiChannel = reader.getTextIntNotNull();
			else if (n.equals("midi-program"))
				midiProgram = reader.getTextIntNotNull();
			else if (n.equals("volume"))
				volume = reader.getTextFloatNotNull();
			else if (n.equals("pan"))
				pan = reader.getTextFloatNotNull();
			reader.closeElement();
		}
		return new MxlMidiInstrument(midiChannel, midiProgram, volume, pan, id);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("id", id);
		writer.writeElementText("midi-channel", midiChannel);
		writer.writeElementText("midi-program", midiProgram);
		writer.writeElementText("volume", volume);
		writer.writeElementText("pan", pan);
		writer.writeElementEnd();
	}

}
