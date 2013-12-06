package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.createEmptyDocument;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverEmpty;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.types.partwise.MxlPart;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML score-partwise.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children="score-header,part")
public final class MxlScorePartwise
{
	
	public static final String ELEM_NAME = "score-partwise";
	
	@NeverNull public final MxlScoreHeader scoreHeader;
	@NeverEmpty public final PVector<MxlPart> parts;
	@NeverNull public final String version;
	
	private static final String defaultVersion = "1.0";

	
	public MxlScorePartwise(MxlScoreHeader scoreHeader, PVector<MxlPart> parts, String version)
	{
		this.scoreHeader = scoreHeader;
		this.parts = parts;
		this.version = version;
	}
	
	
	@NeverNull public static MxlScorePartwise read(Element e)
	{
		PVector<MxlPart> parts = PVector.pvec();
		for (Element c : elements(e))
		{
			if (c.getNodeName().equals(MxlPart.ELEM_NAME))
				parts = parts.plus(MxlPart.read(c));
		}
		if (parts.size() < 1)
			throw invalid(e);
		return new MxlScorePartwise(MxlScoreHeader.read(e), parts,
			notNull(attribute(e, "version"), defaultVersion));
	}
	
	
	public Document write()
	{
		Document doc = createEmptyDocument();
		DOMImplementation domImpl = doc.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("score-partwise",
			"-//Recordare//DTD MusicXML 2.0 Partwise//EN",
			"http://www.musicxml.org/dtds/partwise.dtd");
		doc.appendChild(doctype);
		Element e = doc.createElement(ELEM_NAME);
		addAttribute(e, "version", version);
		doc.appendChild(e);
		scoreHeader.write(e);
		for (MxlPart part : parts)
		{
			writeSeparatorComment(e);
			part.write(e);
		}
		writeSeparatorComment(e);
		return doc;
	}
	
	
	private void writeSeparatorComment(Element parent)
	{
		parent.appendChild(parent.getOwnerDocument().createComment(
			"========================================================="));
	}
	

}
