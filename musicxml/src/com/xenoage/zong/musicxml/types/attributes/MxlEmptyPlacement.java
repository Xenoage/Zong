package com.xenoage.zong.musicxml.types.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.types.util.MxlPlacementContent;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;

/**
 * MusicXML empty-placement.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlEmptyPlacement
	implements MxlPrintStyleContent, MxlPlacementContent {

	@MaybeNull private MxlPrintStyle printStyle;
	@MaybeNull private MxlPlacement placement;


	@MaybeNull public static MxlEmptyPlacement read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		MxlPlacement placement = MxlPlacement.read(reader);
		if (printStyle != null || placement != null)
			return new MxlEmptyPlacement(printStyle, placement);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		if (printStyle != null)
			printStyle.write(writer);
		if (placement != null)
			placement.write(writer);
	}

}
