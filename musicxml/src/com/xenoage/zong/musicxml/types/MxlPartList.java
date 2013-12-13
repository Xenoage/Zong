package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlPartListContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML part-list.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "part-group,score-part")
@AllArgsConstructor @Getter @Setter
public final class MxlPartList {

	public static final String elemName = "part-list";

	@NonEmpty private List<MxlPartListContent> content;


	@NonNull public static MxlPartList read(XmlReader reader) {
		List<MxlPartListContent> content = alist();
		boolean scorePartFound = false;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals(MxlPartGroup.elemName)) {
				content.add(MxlPartGroup.read(reader));
			}
			else if (n.equals(MxlScorePart.elemName)) {
				content.add(MxlScorePart.read(reader));
				scorePartFound = true;
			}
			reader.closeElement();
		}
		if (false == scorePartFound)
			throw reader.dataException("no " + MxlScorePart.elemName + " found");
		if (content.size() == 0)
			throw reader.dataException("no content");
		return new MxlPartList(content);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		for (MxlPartListContent item : content)
			item.write(writer);
		writer.writeElementEnd();
	}

}
