package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;

/**
 * MusicXML staccato.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public class MxlStaccato
	implements MxlArticulationsContent {

	public static final String elemName = "staccato";

	private MxlEmptyPlacement emptyPlacement;


	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.Staccato;
	}

	@NonNull public static MxlStaccato read(XmlReader reader) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(reader);
		return new MxlStaccato(emptyPlacement);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		emptyPlacement.write(writer);
		writer.writeElementEnd();
	}

}
