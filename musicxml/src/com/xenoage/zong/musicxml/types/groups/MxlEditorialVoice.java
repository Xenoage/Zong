package com.xenoage.zong.musicxml.types.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML editorial-voice and editorial-voice-direction
 * (they have equal content).
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "footnote,level")
@AllArgsConstructor @Getter @Setter
public final class MxlEditorialVoice {

	@NonNull private final String voice;

	@MaybeNull public static MxlEditorialVoice read(XmlReader reader) {
		String voice = null;
		if (reader.openNextChildElement("voice")) {
			voice = reader.getText();
			reader.closeElement();
		}
		if (voice != null)
			return new MxlEditorialVoice(voice);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementText("voice", voice);
	}

}
