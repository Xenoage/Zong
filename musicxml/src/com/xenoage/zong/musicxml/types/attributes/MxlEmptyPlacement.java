package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle.noPrintStyle;
import static com.xenoage.zong.musicxml.types.enums.MxlPlacement.noPlacement;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
@AllArgsConstructor @Getter
public final class MxlEmptyPlacement
	implements MxlPrintStyleContent, MxlPlacementContent {
	
	public static final MxlEmptyPlacement noEmptyPlacement = new MxlEmptyPlacement(
		noPrintStyle, noPlacement);

	private final MxlPrintStyle printStyle;
	private final MxlPlacement placement;


	@MaybeNull public static MxlEmptyPlacement read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		MxlPlacement placement = MxlPlacement.read(reader);
		if (printStyle != noPrintStyle || placement != noPlacement)
			return new MxlEmptyPlacement(printStyle, placement);
		else
			return noEmptyPlacement;
	}

	public void write(XmlWriter writer) {
		if (this != noEmptyPlacement) {
			printStyle.write(writer);
			placement.write(writer);
		}
	}

}
