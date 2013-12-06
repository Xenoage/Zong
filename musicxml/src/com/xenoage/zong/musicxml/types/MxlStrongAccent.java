package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.enums.MxlUpDown;


/**
 * MusicXML strong-accent.
 * 
 * @author Andreas Wenger
 */
public final class MxlStrongAccent
	implements MxlArticulationsContent
{
	
	public static final String ELEM_NAME = "strong-accent";
	
	@NeverNull private final MxlEmptyPlacement emptyPlacement;
	@NeverNull private final MxlUpDown type;
	
	private static final MxlUpDown defaultType = MxlUpDown.Up;
	public static final MxlStrongAccent defaultInstance = new MxlStrongAccent(
		MxlEmptyPlacement.empty, defaultType);

	
	public MxlStrongAccent(MxlEmptyPlacement emptyPlacement, MxlUpDown type)
	{
		this.emptyPlacement = emptyPlacement;
		this.type = type;
	}

	
	@NeverNull public MxlEmptyPlacement getEmptyPlacement()
	{
		return emptyPlacement;
	}

	
	@NeverNull  public MxlUpDown getType()
	{
		return type;
	}
	
	
	@Override public MxlArticulationsContentType getArticulationsContentType()
	{
		return MxlArticulationsContentType.StrongAccent;
	}
	
	
	@NeverNull public static MxlStrongAccent read(Element e)
	{
		MxlEmptyPlacement emptyPlacement = MxlEmptyPlacement.read(e);
		MxlUpDown type = notNull(MxlUpDown.read(attribute(e, "type")), defaultType);
		if (emptyPlacement != MxlEmptyPlacement.empty || type != defaultType)
			return new MxlStrongAccent(emptyPlacement, type);
		else
			return defaultInstance;
	}

	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		emptyPlacement.write(e);
		addAttribute(e, "type", type.write());
	}
	

}
