package com.xenoage.zong.musicxml.types.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML empty-trill-sound.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "trill-sound")
@AllArgsConstructor @Getter @Setter
public class MxlEmptyTrillSound {

	@MaybeNull private MxlPrintStyle printStyle;
	@MaybeNull private MxlPlacement placement;


	@MaybeNull public static MxlEmptyTrillSound read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		MxlPlacement placement = MxlPlacement.read(reader);
		if (printStyle != null || placement != null)
			return new MxlEmptyTrillSound(printStyle, placement);
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
