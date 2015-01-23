package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyTrillSound;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;

/**
 * MusicXML turn.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public class MxlTurn
	implements MxlOrnamentsContent {

	public static final String elemName = "turn";

	private MxlEmptyTrillSound emptyTrillSound;

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.Turn;
	}

	@NonNull public static MxlTurn read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		return new MxlTurn(emptyTrillSound);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		emptyTrillSound.write(writer);
		writer.writeElementEnd();
	}

}
