package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle.noPrintStyle;
import static com.xenoage.zong.musicxml.types.enums.MxlPlacement.Unknown;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.types.util.MxlPlacementContent;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML empty-trill-sound.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "trill-sound")
@AllArgsConstructor @Getter
public class MxlEmptyTrillSound
	implements MxlPrintStyleContent, MxlPlacementContent {
	
	public static final MxlEmptyTrillSound noEmptyTrillSound = new MxlEmptyTrillSound(
		noPrintStyle, Unknown);

	private final MxlPrintStyle printStyle;
	private final MxlPlacement placement;


	public static MxlEmptyTrillSound read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		MxlPlacement placement = MxlPlacement.read(reader);
		if (printStyle != noPrintStyle || placement != Unknown)
			return new MxlEmptyTrillSound(printStyle, placement);
		else
			return noEmptyTrillSound;
	}

	public void write(XmlWriter writer) {
		if (this != noEmptyTrillSound) {
			printStyle.write(writer);
			placement.write(writer);
		}
	}
	
}
