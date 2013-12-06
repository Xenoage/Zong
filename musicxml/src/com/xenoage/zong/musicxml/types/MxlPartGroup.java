package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.Parse.parseAttrIntNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.element;
import static com.xenoage.utils.xml.XMLReader.elementText;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlPartListContent;
import com.xenoage.zong.musicxml.types.enums.MxlStartStop;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML part-group.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="group-name-display,group-abbreviation-display," +
	"group-time,editorial")
public final class MxlPartGroup
	implements MxlPartListContent
{
	
	public static final String ELEM_NAME = "part-group";
	
	@MaybeNull private final String groupName;
	@MaybeNull private final String groupAbbreviation;
	@MaybeNull private final MxlGroupSymbol groupSymbol;
	@MaybeNull private final MxlGroupBarline groupBarline;
	@NeverNull private final MxlStartStop type;
	private final int number;
	
	private static final int defaultNumber = 1;

	
	public MxlPartGroup(String groupName, String groupAbbreviation, MxlGroupSymbol groupSymbol,
		MxlGroupBarline groupBarline, MxlStartStop type, int number)
	{
		this.groupName = groupName;
		this.groupAbbreviation = groupAbbreviation;
		this.groupSymbol = groupSymbol;
		this.groupBarline = groupBarline;
		this.type = type;
		this.number = number;
	}

	
	@MaybeNull public String getGroupName()
	{
		return groupName;
	}

	
	@MaybeNull public String getGroupAbbreviation()
	{
		return groupAbbreviation;
	}

	
	@MaybeNull public MxlGroupSymbol getGroupSymbol()
	{
		return groupSymbol;
	}

	
	@MaybeNull public MxlGroupBarline getGroupBarline()
	{
		return groupBarline;
	}

	
	@NeverNull public MxlStartStop getType()
	{
		return type;
	}


	public int getNumber()
	{
		return number;
	}
	
	
	@Override public PartListContentType getPartListContentType()
	{
		return PartListContentType.PartGroup;
	}
	
	
	@NeverNull public static MxlPartGroup read(Element e)
	{
		MxlGroupSymbol groupSymbol = null;
		MxlGroupBarline groupBarline = null;
		Element eGroupSymbol = element(e, "group-symbol");
		if (eGroupSymbol != null)
			groupSymbol = MxlGroupSymbol.read(eGroupSymbol);
		Element eGroupBarline = element(e, "group-barline");
		if (eGroupBarline != null)
			groupBarline = MxlGroupBarline.read(eGroupBarline);
		return new MxlPartGroup(
			elementText(e, "group-name"),
			elementText(e, "group-abbreviation"),
			groupSymbol, groupBarline,
			MxlStartStop.read(throwNull(attribute(e, "type"), e), e),
			notNull(parseAttrIntNull(e, "number"), defaultNumber));
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement("group-name", groupName, e);
		addElement("group-abbreviation", groupAbbreviation, e);
		if (groupSymbol != null)
			groupSymbol.write(e);
		if (groupBarline != null)
			groupBarline.write(e);
		addAttribute(e, "type", type.write());
		addAttribute(e, "number", number);
	}
	

}
