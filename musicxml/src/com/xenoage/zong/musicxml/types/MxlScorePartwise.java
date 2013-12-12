package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.createEmptyDocument;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.base.annotations.NeverEmpty;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.utils.xml.XmlReader;
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
		List<MxlPart> parts = alist();
		while (reader.openNextChildElement()) {
			if (reader.getElementName().equals(MxlPart.elemName))
				parts.add(MxlPart.read(reader));
		}
		if (parts.size() < 1)
			throw reader.dataException("no parts found");
		return new MxlScorePartwise(MxlScoreHeader.read(e), parts, notNull(attribute(e, "version"),
			defaultVersion));
	}

	public Document write() {
		Document doc = createEmptyDocument();
		DOMImplementation domImpl = doc.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("score-partwise",
			"-//Recordare//DTD MusicXML 2.0 Partwise//EN", "http://www.musicxml.org/dtds/partwise.dtd");
		doc.appendChild(doctype);
		Element e = doc.createElement(elemName);
		addAttribute(e, "version", version);
		doc.appendChild(e);
		scoreHeader.write(e);
		for (MxlPart part : parts) {
			writeSeparatorComment(e);
			part.write(e);
		}
		writeSeparatorComment(e);
		return doc;
	}

	private void writeSeparatorComment(Element parent) {
		parent.appendChild(parent.getOwnerDocument().createComment(
			"========================================================="));
	}

}
