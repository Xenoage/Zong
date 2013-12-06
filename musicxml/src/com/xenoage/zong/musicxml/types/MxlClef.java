package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.Parse.parseChildIntNull;
import static com.xenoage.utils.xml.XMLReader.element;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.xml.Parse;
import com.xenoage.zong.musicxml.types.enums.MxlClefSign;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML clef.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="line,additional,size,print-style,print-object")
public final class MxlClef
{
	
	public static final String ELEM_NAME = "clef";
	
	@NeverNull private final MxlClefSign sign;
	private final int clefOctaveChange;
	private final int number;
	
	private static final int defaultClefOctaveChange = 0;
	private static final int defaultNumber = 1;
	
	
	public MxlClef(MxlClefSign sign, int clefOctaveChange, int number)
	{
		this.sign = sign;
		this.clefOctaveChange = clefOctaveChange;
		this.number = number;
	}

	
	@NeverNull public MxlClefSign getSign()
	{
		return sign;
	}

	
	public int getClefOctaveChange()
	{
		return clefOctaveChange;
	}

	
	public int getNumber()
	{
		return number;
	}
	
	
	public static MxlClef read(Element e)
	{
		return new MxlClef(MxlClefSign.read(throwNull(element(e, MxlClefSign.ELEM_NAME), e)),
			notNull(parseChildIntNull(e, "clef-octave-change"), defaultClefOctaveChange),
			notNull(Parse.parseAttrIntNull(e, "number"), defaultNumber));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		sign.write(e);
		if (clefOctaveChange != defaultClefOctaveChange)
			addElement("clef-octave-change", clefOctaveChange, e);
		if (number != defaultNumber)
			addAttribute(e, "number", number);
	}
	

}
