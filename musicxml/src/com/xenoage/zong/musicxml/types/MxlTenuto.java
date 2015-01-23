package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;

/**
 * MusicXML tenuto.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public class MxlTenuto
	implements MxlArticulationsContent {

	public static final String elemName = "tenuto";

	private MxlEmptyPlacement emptyPlacement;


	@Override public MxlArticulationsContentType getArticulationsContentType() {
		return MxlArticulationsContentType.Tenuto;
	}

	public static MxlTenuto read(XmlReader reader) {
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(reader);
		return new MxlTenuto(emptyPlacement);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (emptyPlacement != null)
			emptyPlacement.write(writer);
		writer.writeElementEnd();
	}

}
