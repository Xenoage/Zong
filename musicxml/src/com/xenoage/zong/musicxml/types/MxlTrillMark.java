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
 * MusicXML trill-mark.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlTrillMark
	implements MxlOrnamentsContent {

	public static final String elemName = "trill-mark";

	private MxlEmptyTrillSound emptyTrillSound;

	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.TrillMark;
	}

	@NonNull public static MxlTrillMark read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		return new MxlTrillMark(emptyTrillSound);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		emptyTrillSound.write(writer);
		writer.writeElementEnd();
	}

}
