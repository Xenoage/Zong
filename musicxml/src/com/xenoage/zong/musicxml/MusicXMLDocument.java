package com.xenoage.zong.musicxml;

import static com.xenoage.utils.xml.XMLReader.root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;


/**
 * Root class of a MusicXML tree.
 * 
 * Only partwise scores are supported.
 * 
 * @author Andreas Wenger
 */
public final class MusicXMLDocument
{
	
	private MxlScorePartwise score; 
	
	
	private MusicXMLDocument(MxlScorePartwise score)
	{
		this.score = score;
	}
	
	
	public static MusicXMLDocument read(Document doc)
		throws XmlDataException
	{
		Element e = root(doc);
		String n = e.getNodeName();
		if (n.equals(MxlScorePartwise.ELEM_NAME))
			return new MusicXMLDocument(MxlScorePartwise.read(e));
		else if (n.equals("score-timewise"))
			throw new IllegalArgumentException("Timewise scores are not supported.");
		throw new IllegalArgumentException("Unknown root element: " + n);
	}
	
	
	public MxlScorePartwise getScore()
	{
		return score;
	}
	

}
