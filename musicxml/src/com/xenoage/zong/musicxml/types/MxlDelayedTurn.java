package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
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
public final class MxlDelayedTurn
	implements MxlOrnamentsContent {

	public static final String elemName = "delayed-turn";

	@MaybeNull private MxlEmptyTrillSound emptyTrillSound;
	
	public static final MxlDelayedTurn defaultInstance = new MxlDelayedTurn(null);

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.DelayedTurn;
	}

	@NonNull public static MxlDelayedTurn read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		if (emptyTrillSound != null)
			return new MxlDelayedTurn(emptyTrillSound);
		else
			return defaultInstance;
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (emptyTrillSound != null)
			emptyTrillSound.write(writer);
		writer.writeElementEnd();
	}

}
