package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.xml.Parse;
import com.xenoage.utils.xml.XMLReader;
import com.xenoage.utils.xml.XMLWriter;
import com.xenoage.zong.musicxml.types.enums.MxlMode;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML key.
 * 
 * Currently only the fifths element is supported.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="non-traditional-key,key-octave,number,print-style,print-object",
	partly="traditional-key")
public final class MxlKey
{
	
	public static final String ELEM_NAME = "key";
	
	public final int fifths;
	@MaybeNull public final MxlMode mode;

	
	public MxlKey(int fifths, MxlMode mode)
	{
		this.fifths = fifths;
		this.mode = mode;
	}
	
	
	/**
	 * Returns null, if the key is unsupported.
	 */
	@MaybeNull public static MxlKey read(Element e)
	{
		Integer fifths = Parse.parseChildIntNull(e, "fifths");
		if (fifths != null)
		{
			MxlMode mode = null;
			Element eMode = XMLReader.element(e, MxlMode.ELEM_NAME);
			if (eMode != null)
				mode = MxlMode.read(eMode);
			return new MxlKey(fifths, mode);
		}
		return null;
	}
	
	
	public void write(Element parent)
	{
		Element e = XMLWriter.addElement(ELEM_NAME, parent);
		addElement("fifths", fifths, e);
		if (mode != null)
			mode.write(e);
	}
	

}
