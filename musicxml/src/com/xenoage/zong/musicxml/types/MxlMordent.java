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
 * MusicXML mordent (element).
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlMordent
	implements MxlOrnamentsContent {

	public static final String elemName = "mordent";

	@MaybeNull private MxlMordentType mordentType;
	
	public static final MxlMordent defaultInstance = new MxlMordent(null);

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.Mordent;
	}

	@NonNull public static MxlMordent read(XmlReader reader) {
		MxlMordentType mordentType = MxlMordentType.read(reader);
		if (mordentType != null)
			return new MxlMordent(mordentType);
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
