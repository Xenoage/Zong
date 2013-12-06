package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.Parse.parseChildInt;
import static com.xenoage.utils.xml.Parse.parseChildIntNull;
import static com.xenoage.utils.xml.XMLReader.element;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML transpose.
 * 
 * @author Andreas Wenger
 */
public final class MxlTranspose
{
	
	public static final String ELEM_NAME = "transpose";
	
	@MaybeNull private final Integer diatonic;
	private final int chromatic;
	@MaybeNull private final Integer octaveChange;
	private final boolean doubleValue;

	
	public MxlTranspose(Integer diatonic, int chromatic, Integer octaveChange,
		boolean doubleValue)
	{
		this.diatonic = diatonic;
		this.chromatic = chromatic;
		this.octaveChange = octaveChange;
		this.doubleValue = doubleValue;
	}
	
	
	@MaybeNull public Integer getDiatonic()
	{
		return diatonic;
	}

	
	public int getChromatic()
	{
		return chromatic;
	}
	

	@MaybeNull public Integer getOctaveChange()
	{
		return octaveChange;
	}


	
	public boolean getDouble()
	{
		return doubleValue;
	}
	
	
	@NeverNull public static MxlTranspose read(Element e)
	{
		return new MxlTranspose(parseChildIntNull(e, "diatonic"),
			parseChildInt(e, "chromatic"), parseChildIntNull(e, "octave-change"),
			element(e, "double") != null);
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement("diatonic", diatonic, e);
		addElement("chromatic", chromatic, e);
		addElement("octave-change", octaveChange, e);
		if (doubleValue) addElement("double", e);
	}

}
