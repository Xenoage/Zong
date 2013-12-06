package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverEmpty;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.types.choice.MxlPartListContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML part-list.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children="part-group,score-part")
public final class MxlPartList
{
	
	public static final String ELEM_NAME = "part-list";
	
	@NeverEmpty public final PVector<MxlPartListContent> content;

	
	public MxlPartList(PVector<MxlPartListContent> content)
	{
		this.content = content;
	}
	
	
	@NeverNull public static MxlPartList read(Element e)
	{
		PVector<MxlPartListContent> content = pvec();
		boolean scorePartFound = false;
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			if (n.equals(MxlPartGroup.ELEM_NAME))
			{
				content = content.plus(MxlPartGroup.read(c));
			}
			else if (n.equals(MxlScorePart.ELEM_NAME))
			{
				content = content.plus(MxlScorePart.read(c));
				scorePartFound = true;
			}
		}
		if (!scorePartFound)
			throw invalid(e);
		return new MxlPartList(content);
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		for (MxlPartListContent item : content)
			item.write(e);
	}

}
