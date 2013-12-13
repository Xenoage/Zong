package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.NullUtils.notNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlCreditContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML credit.
 * 
 * Several several credit-words elements are accumulated
 * within a single {@link MxlCreditWords} instance.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "link,bookmark", children = "credit-words")
@AllArgsConstructor @Getter @Setter
public final class MxlCredit {

	public static final String elemName = "credit";

	@NonNull private MxlCreditContent content;
	private int page;

	private static final int defaultPage = 1;

	
	@NonNull public static MxlCredit read(XmlReader reader) {
		int page = notNull(reader.getAttributeInt("page"), defaultPage);
		MxlCreditContent content = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("credit-image")) {
				content = MxlImage.read(reader);
				reader.closeElement();
				break;
			}
			else if (n.equals(MxlCreditWords.elemName)) {
				content = MxlCreditWords.read(reader);
				//element is already closed at this point
				break;
			}
			reader.closeElement();
		}
		if (content == null)
			throw reader.dataException("empty " + elemName);
		return new MxlCredit(content, page);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("page", page);
		content.write(writer);
		writer.writeElementEnd();
	}

}
