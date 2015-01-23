package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlWedgeType;
import com.xenoage.zong.musicxml.types.util.MxlPositionContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML wedge.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "spread")
@AllArgsConstructor @Getter @Setter
public final class MxlWedge
	implements MxlDirectionTypeContent, MxlPositionContent {

	public static final String elemName = "wedge";

	@NonNull private MxlWedgeType type;
	private int number;
	private MxlPosition position;
	private MxlColor color;

	private static final int defaultNumber = 1;


	@Override public MxlDirectionTypeContentType getDirectionTypeContentType() {
		return MxlDirectionTypeContentType.Wedge;
	}

	@NonNull public static MxlWedge read(XmlReader reader) {
		return new MxlWedge(MxlWedgeType.read(reader.getAttribute("type")),
			notNull(reader.getAttributeInt("number"), defaultNumber),
			MxlPosition.read(reader), MxlColor.read(reader));
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("type", type.write());
		writer.writeAttribute("number", number);
		position.write(writer);
		color.write(writer);
		writer.writeElementEnd();
	}

}
