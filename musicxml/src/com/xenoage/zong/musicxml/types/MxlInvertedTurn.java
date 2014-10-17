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
 * MusicXML inverted-turn.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlInvertedTurn
	implements MxlOrnamentsContent {

	public static final String elemName = "inverted-turn";

	@MaybeNull private MxlEmptyTrillSound emptyTrillSound;
	
	public static final MxlInvertedTurn defaultInstance = new MxlInvertedTurn(null);

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.InvertedTurn;
	}

	@NonNull public static MxlInvertedTurn read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		if (emptyTrillSound != null)
			return new MxlInvertedTurn(emptyTrillSound);
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
