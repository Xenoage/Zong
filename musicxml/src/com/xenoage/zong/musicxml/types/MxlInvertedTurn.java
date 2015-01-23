package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyTrillSound;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;

/**
 * MusicXML inverted-turn.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public class MxlInvertedTurn
	implements MxlOrnamentsContent {

	public static final String elemName = "inverted-turn";

	private MxlEmptyTrillSound emptyTrillSound;

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.InvertedTurn;
	}

	public static MxlInvertedTurn read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		return new MxlInvertedTurn(emptyTrillSound);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		emptyTrillSound.write(writer);
		writer.writeElementEnd();
	}

}
