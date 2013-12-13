package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlPartListContent;
import com.xenoage.zong.musicxml.types.enums.MxlStartStop;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML part-group.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "group-name-display,group-abbreviation-display,"
	+ "group-time,editorial")
@AllArgsConstructor @Getter @Setter
public final class MxlPartGroup
	implements MxlPartListContent {

	public static final String elemName = "part-group";

	@MaybeNull private final String groupName;
	@MaybeNull private final String groupAbbreviation;
	@MaybeNull private final MxlGroupSymbol groupSymbol;
	@MaybeNull private final MxlGroupBarline groupBarline;
	@NonNull private final MxlStartStop type;
	private final int number;

	private static final int defaultNumber = 1;


	@Override public PartListContentType getPartListContentType() {
		return PartListContentType.PartGroup;
	}

	@NonNull public static MxlPartGroup read(XmlReader reader) {
		//attributes
		MxlStartStop type = MxlStartStop.read(reader.getAttributeNotNull("type"));
		int number = notNull(reader.getAttributeInt("number"), defaultNumber);
		//elements
		String groupName = null;
		String groupAbbreviation = null;
		MxlGroupSymbol groupSymbol = null;
		MxlGroupBarline groupBarline = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("group-name"))
				groupName = reader.getTextNotNull();
			else if (n.equals("group-abbreviation"))
				groupAbbreviation = reader.getTextNotNull();
			else if (n.equals("group-symbol"))
				groupSymbol = MxlGroupSymbol.read(reader);
			else if (n.equals("group-barline"))
				groupBarline = MxlGroupBarline.read(reader);
			reader.closeElement();
		}
		return new MxlPartGroup(groupName, groupAbbreviation, groupSymbol, groupBarline, type, number);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("type", type.write());
		writer.writeAttribute("number", number);
		writer.writeElementText("group-name", groupName);
		writer.writeElementText("group-abbreviation", groupAbbreviation);
		if (groupSymbol != null)
			groupSymbol.write(writer);
		if (groupBarline != null)
			groupBarline.write(writer);
		writer.writeElementEnd();
	}

}
