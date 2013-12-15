package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.Parser.parseIntegerNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlTimeContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML beats/beats-type content for a time element.
 * 
 * Only one beats and beats-type is supported.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly = "beats,beat-type")
@AllArgsConstructor @Getter @Setter
public final class MxlNormalTime
	implements MxlTimeContent {

	private int beats;
	private int beatType;


	@Override public MxlTimeContentType getTimeContentType() {
		return MxlTimeContentType.NormalTime;
	}

	/**
	 * Returns null, when the time signature is not supported (e.g. 3+2/4).
	 * The "beats" element must already be open.
	 */
	@MaybeNull public static MxlNormalTime read(XmlReader reader) {
		Integer beats = null, beatType = null;
		do {
			String n = reader.getElementName();
			if (n.equals("beats"))
				beats = parseIntegerNull(reader.getText());
			else if (n.equals("beat-type"))
				beatType = parseIntegerNull(reader.getText());
			reader.closeElement();
		} while (reader.openNextChildElement());
		if (beats != null && beatType != null)
			return new MxlNormalTime(beats, beatType);
		else
			return null;
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementText("beats", beats);
		writer.writeElementText("beat-type", beatType);
	}

}
