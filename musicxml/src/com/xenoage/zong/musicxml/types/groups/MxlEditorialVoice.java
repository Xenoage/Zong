package com.xenoage.zong.musicxml.types.groups;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
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
@Getter @Setter
public final class MxlEditorialVoice {

	@MaybeNull private String voice;
	
	/**
	 * Returns true, if this instance contains information.
	 */
	public boolean isUsed() {
		return voice != null;
	}

	/**
	 * Reads information from the current element, if relevant for this instance.
	 */
	public void readElement(XmlReader reader) {
		if (reader.getElementName().equals("voice"))
			voice = reader.getText();
	}

	public void write(XmlWriter writer) {
		writer.writeElementText("voice", voice);
	}

}
