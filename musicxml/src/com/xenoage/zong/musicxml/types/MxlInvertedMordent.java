package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlMordentType;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;

/**
 * MusicXML inverted-mordent.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlInvertedMordent
	implements MxlOrnamentsContent {

	public static final String elemName = "inverted-mordent";

	private MxlMordentType mordentType;

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.InvertedMordent;
	}

	@NonNull public static MxlInvertedMordent read(XmlReader reader) {
		MxlMordentType mordentType = MxlMordentType.read(reader);
		return new MxlInvertedMordent(mordentType);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		mordentType.write(writer);
		writer.writeElementEnd();
	}

}
