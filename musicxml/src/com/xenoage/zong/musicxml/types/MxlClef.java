package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.enums.MxlClefSign;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML clef.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "line,additional,size,print-style,print-object")
@AllArgsConstructor @Getter @Setter
public final class MxlClef {

	public static final String elemName = "clef";

	@NonNull private MxlClefSign sign;
	private int clefOctaveChange;
	private int number;

	private static final int defaultClefOctaveChange = 0;
	private static final int defaultNumber = 1;

	public static MxlClef read(XmlReader reader) {
		return new MxlClef(MxlClefSign.read(throwNull(element(e, MxlClefSign.elemName), e)), notNull(
			parseChildIntNull(e, "clef-octave-change"), defaultClefOctaveChange), notNull(
			Parse.parseAttrIntNull(e, "number"), defaultNumber));
	}

	public void write(Element parent) {
		Element e = addElement(elemName, parent);
		sign.write(e);
		if (clefOctaveChange != defaultClefOctaveChange)
			addElement("clef-octave-change", clefOctaveChange, e);
		if (number != defaultNumber)
			addAttribute(e, "number", number);
	}

}
