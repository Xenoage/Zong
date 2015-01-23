package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.choice.MxlCreditContent;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;
import com.xenoage.zong.musicxml.types.enums.MxlVAlignImage;
import com.xenoage.zong.musicxml.types.util.MxlPositionContent;

/**
 * MusicXML image or credit-image.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlImage
	implements MxlCreditContent, MxlDirectionTypeContent, MxlPositionContent {

	private String elemName; //"image" or "credit-image"

	private String source;
	private String type;
	private MxlPosition position;
	private MxlLeftCenterRight hAlign;
	private MxlVAlignImage vAlign;


	@Override public MxlCreditContentType getCreditContentType() {
		return MxlCreditContentType.CreditImage;
	}

	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Image;
	}

	@NonNull public static MxlImage read(XmlReader reader) {
		String elemName = reader.getElementName();
		String source = reader.getAttributeNotNull("source");
		String type = reader.getAttributeNotNull("type");
		return new MxlImage(elemName, source, type, MxlPosition.read(reader),
			MxlLeftCenterRight.read(reader, "halign"), MxlVAlignImage.read(reader));
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("source", source);
		writer.writeAttribute("type", type);
		position.write(writer);
		hAlign.write(writer, "halign");
		vAlign.write(writer);
		writer.writeElementEnd();
	}

}
