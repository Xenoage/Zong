package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;


/**
 * MusicXML tenuto.
 * 
 * @author Andreas Wenger
 */
public final class MxlTenuto
	implements MxlArticulationsContent
{
	
	public static final String ELEM_NAME = "tenuto";
	
	@NeverNull private final MxlEmptyPlacement emptyPlacement;
	
	public static final MxlTenuto defaultInstance = new MxlTenuto(MxlEmptyPlacement.empty);

	
	public MxlTenuto(MxlEmptyPlacement emptyPlacement)
	{
		this.emptyPlacement = emptyPlacement;
	}

	
	@NeverNull public MxlEmptyPlacement getEmptyPlacement()
	{
		return emptyPlacement;
	}
	
	
	@Override public MxlArticulationsContentType getArticulationsContentType()
	{
		return MxlArticulationsContentType.Tenuto;
	}
	
	
	@NeverNull public static MxlTenuto read(Element e)
	{
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(e);
		if (emptyPlacement != MxlEmptyPlacement.empty)
			return new MxlTenuto(emptyPlacement);
		else
			return defaultInstance;
	}

	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		emptyPlacement.write(e);
	}
	

}
