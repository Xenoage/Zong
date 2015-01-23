package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;
import com.xenoage.zong.musicxml.types.enums.MxlVAlign;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML formatted-text, including text-formatting.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "text-decoration,text-rotation,"
	+ "letter-spacing,line-height,xml:lang,text-direction,enclosure")
@AllArgsConstructor @Getter @Setter
public final class MxlFormattedText
	implements MxlPrintStyleContent {

	@NonNull private String value;
	private MxlLeftCenterRight justify; //alignment within the text box
	private MxlLeftCenterRight hAlign; //alignment of the text box, see halign documentation
	private MxlVAlign vAlign;
	private MxlPrintStyle printStyle;


	@NonNull public static MxlFormattedText read(XmlReader reader) {
		MxlLeftCenterRight justify = MxlLeftCenterRight.read(reader, "justify");
		MxlLeftCenterRight hAlign = MxlLeftCenterRight.read(reader, "halign");
		MxlVAlign vAlign = MxlVAlign.read(reader);
		MxlPrintStyle printStyle = MxlPrintStyle.read(reader);
		String value = reader.getTextNotNull();
		return new MxlFormattedText(value, justify, hAlign, vAlign, printStyle);
	}

	public void write(XmlWriter writer) {
		justify.write(writer, "justify");
		hAlign.write(writer, "halign");
		vAlign.write(writer);
		printStyle.write(writer);
		writer.writeText(value);
	}

}
