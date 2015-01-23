package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.Parser.parseIntegerNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlNoteTypeValue;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML metronome.
 * 
 * Currently only the first type (e.g. "♩. = 160")
 * of metronome marks is supported. The right part must
 * be a number.
 * The "♩ = ♩" form is also not supported yet.
 * All other metronome elements are ignored.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "metronome-note,metronome-relation,parentheses")
@AllArgsConstructor @Getter @Setter
public final class MxlMetronome
	implements MxlDirectionTypeContent, MxlPrintStyleContent {

	public static final String elemName = "metronome";

	private MxlNoteTypeValue beatUnit;
	private int dotsCount;
	private int perMinute;
	private MxlPrintStyle printStyle;


	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Metronome;
	}

	/**
	 * Returns null, if the given element contains an unsupported metronome type.
	 */
	@MaybeNull public static MxlMetronome read(XmlReader reader) {
		//attributes
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		//elements
		String sBeatUnit = null;
		int dotsCount = 0;
		Integer perMinute = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("beat-unit"))
				sBeatUnit = reader.getText();
			else if (n.equals("beat-unit-dot"))
				dotsCount++;
			else if (n.equals("per-minute"))
				perMinute = parseIntegerNull(reader.getText());
			reader.closeElement();
		}
		if (sBeatUnit != null && perMinute != null) {
			MxlNoteTypeValue beatUnit = MxlNoteTypeValue.read(sBeatUnit);
			return new MxlMetronome(beatUnit, dotsCount, perMinute, printStyle);
		}
		else {
			return null;
		}
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		printStyle.write(writer);
		writer.writeElementText("beat-unit", beatUnit.write());
		for (int i = 0; i < dotsCount; i++)
			writer.writeElementEmpty("beat-unit-dot");
		writer.writeElementText("per-minute", perMinute);
		writer.writeElementEnd();
	}

}
