package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.util.MxlFormattedTextContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML words.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "formatted-text")
@AllArgsConstructor @Getter @Setter
public final class MxlWords
	implements MxlDirectionTypeContent, MxlFormattedTextContent {

	public static final String elemName = "words";

	@NonNull private final MxlFormattedText formattedText;


	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Words;
	}

	@NonNull public static MxlWords read(XmlReader reader) {
		return new MxlWords(MxlFormattedText.read(reader));
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		formattedText.write(writer);
		writer.writeElementEnd();
	}

}
