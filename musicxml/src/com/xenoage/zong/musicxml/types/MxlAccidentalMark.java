package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlOrnamentsContent;
import com.xenoage.zong.musicxml.types.enums.MxlAccidentalText;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.types.util.MxlPlacementContent;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;

/**
 * MusicXML accidental-mark.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlAccidentalMark
	implements MxlNotationsContent, MxlOrnamentsContent, MxlPrintStyleContent, MxlPlacementContent {

	public static final String elemName = "accidental-mark";

	private MxlAccidentalText accidentalText;
	private MxlPrintStyle printStyle;
	private MxlPlacement placement;

	
	@Override public MxlNotationsContentType getNotationsContentType() {
		return MxlNotationsContentType.AccidentalMark;
	}
	
	@Override public MxlOrnamentsContentType getOrnamentsContentType() {
		return MxlOrnamentsContentType.AccidentalMark;
	}
	
	@NonNull public static MxlAccidentalMark read(XmlReader reader) {
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		MxlPlacement placement = MxlPlacement.read(reader);
		MxlAccidentalText accidentalText = MxlAccidentalText.read(reader);
		return new MxlAccidentalMark(accidentalText, printStyle, placement);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		printStyle.write(writer);
		placement.write(writer);
		accidentalText.write(writer);
		writer.writeElementEnd();
	}

}
