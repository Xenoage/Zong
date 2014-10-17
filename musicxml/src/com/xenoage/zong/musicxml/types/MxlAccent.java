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
 * MusicXML accent.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlAccent
	implements MxlArticulationsContent {

	public static final String elemName = "accent";

	@MaybeNull private MxlEmptyPlacement emptyPlacement;
	
	public static final MxlAccent defaultInstance = new MxlAccent(null);

	
	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.Accent;
	}

	@NonNull public static MxlAccent read(XmlReader reader) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(reader);
		if (emptyPlacement != null)
			return new MxlAccent(emptyPlacement);
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
