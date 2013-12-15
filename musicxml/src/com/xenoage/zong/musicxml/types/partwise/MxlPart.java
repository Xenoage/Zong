package com.xenoage.zong.musicxml.types.partwise;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML part in a partwise score.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "measure")
@AllArgsConstructor @Getter @Setter
public final class MxlPart {

	public static final String elemName = "part";

	@NonEmpty private List<MxlMeasure> measures;
	@NonNull private String id;

	
	@NonNull public static MxlPart read(XmlReader reader) {
		String id = reader.getAttributeNotNull("id");
		List<MxlMeasure> measures = alist();
		while (reader.openNextChildElement()) {
			if (reader.getElementName().equals(MxlMeasure.elemName))
				measures.add(MxlMeasure.read(reader));
			reader.closeElement();
		}
		if (measures.size() < 1)
			throw reader.dataException("no measures found");
		return new MxlPart(measures, id);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("id", id);
		for (MxlMeasure measure : measures) {
			measure.write(writer);
			writeSeparatorComment(writer);
		}
		writer.writeElementEnd();
	}

	private void writeSeparatorComment(XmlWriter writer) {
		writer.writeLineBreak();
		writer.writeComment("=======================================================");
	}

}
