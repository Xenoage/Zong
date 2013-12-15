package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.groups.MxlScoreHeader;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML score-partwise.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "score-header,part")
@AllArgsConstructor @Getter @Setter
public final class MxlScorePartwise {

	public static final String elemName = "score-partwise";

	@NonNull private MxlScoreHeader scoreHeader;
	@NonEmpty private List<MxlPart> parts;
	@NonNull private String version;

	private static final String defaultVersion = "1.0";

	
	@NonNull public static MxlScorePartwise read(XmlReader reader) {
		//attributes
		String version = notNull(reader.getAttribute("version"), defaultVersion);
		//elements
		MxlScoreHeader scoreHeader = new MxlScoreHeader();
		List<MxlPart> parts = alist();
		while (reader.openNextChildElement()) {
			if (reader.getElementName().equals(MxlPart.elemName))
				parts.add(MxlPart.read(reader));
			else
				scoreHeader.readElement(reader);
			reader.closeElement();
		}
		scoreHeader.check(reader);
		if (parts.size() < 1)
			throw reader.dataException("no parts found");
		return new MxlScorePartwise(scoreHeader, parts, version);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("version", version);
		scoreHeader.write(writer);
		for (MxlPart part : parts) {
			writeSeparatorComment(writer);
			part.write(writer);
		}
		writeSeparatorComment(writer);
		writer.writeElementEnd();
	}

	private void writeSeparatorComment(XmlWriter writer) {
		writer.writeLineBreak();
		writer.writeComment("=======================================================");
	}

}
