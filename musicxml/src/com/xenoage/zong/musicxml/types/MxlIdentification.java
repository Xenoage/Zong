package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML identification.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="encoding,source,relation,miscellaneous")
public final class MxlIdentification
{
	
	public static final String ELEM_NAME = "identification";
	
	@MaybeEmpty private final PVector<MxlTypedText> creators;
	@MaybeEmpty private final PVector<MxlTypedText> rights;
	
	
	public MxlIdentification(PVector<MxlTypedText> creators, PVector<MxlTypedText> rights)
	{
		this.creators = creators;
		this.rights = rights;
	}

	
	@MaybeEmpty public PVector<MxlTypedText> getCreators()
	{
		return creators;
	}

	
	@MaybeEmpty public PVector<MxlTypedText> getRights()
	{
		return rights;
	}
	
	
	public static MxlIdentification read(Element e)
	{
		PVector<MxlTypedText> creators = pvec();
		PVector<MxlTypedText> rights = pvec();
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			if (n.equals("creator"))
				creators = creators.plus(MxlTypedText.read(c));
			else if (n.equals("rights"))
				rights = rights.plus(MxlTypedText.read(c));
		}
		return new MxlIdentification(creators, rights);
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		for (MxlTypedText t : creators)
			t.write("creator", e);
		for (MxlTypedText t : rights)
			t.write("rights", e);
	}
	

}
