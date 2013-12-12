package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintAttributes;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.groups.MxlLayout;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML print.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "measure-layout,measure-numbering,part-name-display,"
	+ "part-abbreviation-display", children = "layout,print-attributes")
@AllArgsConstructor @Getter @Setter
public final class MxlPrint
	implements MxlMusicDataContent {

	public static final String elemName = "print";

	@MaybeNull private MxlLayout layout;
	@MaybeNull private MxlPrintAttributes printAttributes;


	@Override public MxlMusicDataContentType getMusicDataContentType() {
		return MxlMusicDataContentType.Print;
	}

	@MaybeNull public static MxlPrint read(XmlReader reader) {
		MxlLayout layout = new MxlLayout();
		MxlPrintAttributes printAttributes = MxlPrintAttributes.read(reader);
		while (reader.openNextChildElement()) {
			layout.readElement(reader);
			reader.closeElement();
		}
		if (false == layout.isUsed())
			layout = null;
		if (layout != null || printAttributes != null)
			return new MxlPrint(layout, printAttributes);
		else
			return null;
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (layout != null)
			layout.write(writer);
		if (printAttributes != null)
			printAttributes.write(writer);
		writer.writeElementEnd();
	}

}
