package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.enums.MxlUpDown;

/**
 * MusicXML strong-accent.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlStrongAccent
	implements MxlArticulationsContent {

	public static final String elemName = "strong-accent";
	public static final MxlUpDown defaultType = MxlUpDown.Up;

	private MxlEmptyPlacement emptyPlacement;
	private MxlUpDown type;


	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.StrongAccent;
	}

	public static MxlStrongAccent read(XmlReader reader) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(reader);
		MxlUpDown type = MxlUpDown.readOr(reader.getAttribute("type"), defaultType);
		return new MxlStrongAccent(emptyPlacement, type);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		emptyPlacement.write(writer);
		writer.writeAttribute("type", type.write());
		writer.writeElementEnd();
	}

}
