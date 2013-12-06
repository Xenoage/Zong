package com.xenoage.zong.musicxml.types.partwise;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverEmpty;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML part in a partwise score.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children="measure")
public final class MxlPart
{
	
	public static final String ELEM_NAME = "part";
	
	@NeverEmpty private final PVector<MxlMeasure> measures;
	@NeverNull private final String id;
	
	
	public MxlPart(PVector<MxlMeasure> measures, String id)
	{
		this.measures = measures;
		this.id = id;
	}
	
	
	@NeverEmpty public PVector<MxlMeasure> getMeasures()
	{
		return measures;
	}

	
	@NeverNull public String getID()
	{
		return id;
	}


	public static MxlPart read(Element e)
	{
		PVector<MxlMeasure> measures = pvec();
		for (Element c : elements(e))
		{
			if (c.getNodeName().equals(MxlMeasure.ELEM_NAME))
				measures = measures.plus(MxlMeasure.read(c));
		}
		if (measures.size() < 1)
			throw invalid(e);
		return new MxlPart(measures, throwNull(attribute(e, "id"), e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		for (MxlMeasure measure : measures)
		{
			measure.write(e);
			writeSeparatorComment(e);
		}
		addAttribute(e, "id", id);
	}
	
	
	private void writeSeparatorComment(Element parent)
	{
		parent.appendChild(parent.getOwnerDocument().createComment(
			"======================================================="));
	}
	

}
