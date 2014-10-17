package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
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

	@MaybeNull private MxlMordentType mordentType;
	
	public static final MxlInvertedMordent defaultInstance = new MxlInvertedMordent(null);

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.InvertedMordent;
	}

	@NonNull public static MxlInvertedMordent read(XmlReader reader) {
		MxlMordentType mordentType = MxlMordentType.read(reader);
		if (mordentType != null)
			return new MxlInvertedMordent(mordentType);
		else
			return defaultInstance;
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (mordentType != null)
			mordentType.write(writer);
		writer.writeElementEnd();
	}

}
