package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.EnumUtils.getEnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML dynamics.
 * 
 * Only the first child element is read.
 * Combinations are not allowed here.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly = "")
@AllArgsConstructor @Getter @Setter
public final class MxlDynamics
	implements MxlNotationsContent, MxlDirectionTypeContent {

	public static final String elemName = "dynamics";

	@NonNull private DynamicsType element;
	@MaybeNull private MxlPrintStyle printStyle;
	@MaybeNull private MxlPlacement placement;


	@Override public MxlNotationsContentType getNotationsContentType() {
		return MxlNotationsContentType.Dynamics;
	}

	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Dynamics;
	}

	/**
	 * Reads the given element and returns it, or returns null if
	 * the element is not supported.
	 */
	@MaybeNull public static MxlDynamics read(XmlReader reader) {
		//get first element
		if (false == reader.openNextChildElement())
			throw reader.dataException("no child element found");
		String childText = reader.getElementName();
		reader.closeElement();
		DynamicsType element = getEnumValue(childText, DynamicsType.values());
		if (element != null)
			return new MxlDynamics(element, MxlPrintStyle.read(reader), MxlPlacement.read(reader));
		else
			return null;
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (printStyle != null)
			printStyle.write(writer);
		if (placement != null)
			placement.write(writer);
		writer.writeElementEmpty(element.name());
		writer.writeElementEnd();
	}

}
