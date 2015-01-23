package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyTrillSound;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;

/**
 * MusicXML delayed-turn.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public class MxlDelayedTurn
	implements MxlOrnamentsContent {

	public static final String elemName = "delayed-turn";

	private MxlEmptyTrillSound emptyTrillSound;

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.DelayedTurn;
	}

	public static MxlDelayedTurn read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		return new MxlDelayedTurn(emptyTrillSound);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		emptyTrillSound.write(writer);
		writer.writeElementEnd();
	}

}
