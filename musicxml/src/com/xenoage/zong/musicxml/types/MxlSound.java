package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML sound.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "midi-instrument,offset,dynamics,dacapo,segno,dalsegno,"
	+ "coda,tocoda,divisions,forward-repeat,fine,time-only,pizzicato,pan,elevation,damper-pedal,"
	+ "soft-pedal,sostenuto-pedal")
@AllArgsConstructor @Getter @Setter
public final class MxlSound {

	public static final String elemName = "sound";

	@MaybeNull private Float tempo;


	/**
	 * Returns null, if no supported data was found.
	 */
	@MaybeNull public static MxlSound read(XmlReader reader) {
		Float tempo = reader.getAttributeFloat("tempo");
		if (tempo != null)
			return new MxlSound(tempo);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		if (tempo != null) {
			writer.writeElementStart(elemName);
			writer.writeAttribute("tempo", tempo);
			writer.writeElementEnd();
		}
	}

}
