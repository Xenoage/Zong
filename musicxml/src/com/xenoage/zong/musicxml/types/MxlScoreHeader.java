package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLReader.getTextContent;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML score-header.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children="work,identification,defaults,part-list")
public final class MxlScoreHeader
{
	
	@MaybeNull private final MxlWork work;
	@MaybeNull private final String movementNumber;
	@MaybeNull private final String movementTitle;
	@MaybeNull private final MxlIdentification identification;
	@MaybeNull private final MxlDefaults defaults;
	@MaybeEmpty private final PVector<MxlCredit> credits;
	@NeverNull private final MxlPartList partList;
	
	
	public MxlScoreHeader(MxlWork work, String movementNumber, String movementTitle,
		MxlIdentification identification, MxlDefaults defaults, PVector<MxlCredit> credits,
		MxlPartList partList)
	{
		this.work = work;
		this.movementNumber = movementNumber;
		this.movementTitle = movementTitle;
		this.identification = identification;
		this.defaults = defaults;
		this.credits = credits;
		this.partList = partList;
	}

	
	@MaybeNull public MxlWork getWork()
	{
		return work;
	}

	
	@MaybeNull public String getMovementNumber()
	{
		return movementNumber;
	}

	
	@MaybeNull public String getMovementTitle()
	{
		return movementTitle;
	}

	
	@MaybeNull public MxlIdentification getIdentification()
	{
		return identification;
	}

	
	@MaybeNull public MxlDefaults getDefaults()
	{
		return defaults;
	}

	
	@MaybeEmpty public PVector<MxlCredit> getCredits()
	{
		return credits;
	}

	
	@NeverNull public MxlPartList getPartList()
	{
		return partList;
	}
	
	
	@NeverNull public static MxlScoreHeader read(Element e)
	{
		MxlWork work = null;
		String movementNumber = null;
		String movementTitle = null;
		MxlIdentification identification = null;
		MxlDefaults defaults = null;
		PVector<MxlCredit> credits = PVector.pvec();
		MxlPartList partList = null;
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			switch (n.charAt(0))
			{
				case 'c':
					if (n.equals(MxlCredit.ELEM_NAME))
						credits = credits.plus(MxlCredit.read(c));
					break;
				case 'd':
					if (n.equals(MxlDefaults.ELEM_NAME))
						defaults = MxlDefaults.read(c);
					break;
				case 'i':
					if (n.equals(MxlIdentification.ELEM_NAME))
						identification = MxlIdentification.read(c);
					break;
				case 'm':
					if (n.equals("movement-number"))
						movementNumber = getTextContent(c);
					else if (n.equals("movement-title"))
						movementTitle = getTextContent(c);
					break;
				case 'p':
					if (n.equals(MxlPartList.ELEM_NAME))
						partList = MxlPartList.read(c);
					break;
				case 'w':
					if (n.equals(MxlWork.ELEM_NAME))
						work = MxlWork.read(c);
					break;
			}
		}
		return new MxlScoreHeader(work, movementNumber, movementTitle,
			identification, defaults, credits, throwNull(partList, e));
	}
	
	
	public void write(Element e)
	{
		if (work != null)
			work.write(e);
		addElement("movement-number", movementNumber, e);
		addElement("movement-title", movementTitle, e);
		if (identification != null)
			identification.write(e);
		if (defaults != null)
			defaults.write(e);
		for (MxlCredit credit : credits)
			credit.write(e);
		partList.write(e);
	}
	

}
