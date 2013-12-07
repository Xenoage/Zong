package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
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
public final class MxlStaccato
	implements MxlArticulationsContent {

	public static final String elemName = "staccato";

	@MaybeNull private final MxlEmptyPlacement emptyPlacement;

	public static final MxlStaccato defaultInstance = new MxlStaccato(null);


	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.Staccato;
	}

	@NonNull public static MxlStaccato read(XmlReader reader) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(reader);
		if (emptyPlacement != null)
			return new MxlStaccato(emptyPlacement);
		else
			return defaultInstance;
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (emptyPlacement != null)
			emptyPlacement.write(writer);
		writer.writeElementEnd();
	}

}
