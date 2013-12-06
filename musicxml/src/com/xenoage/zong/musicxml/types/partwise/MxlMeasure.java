package com.xenoage.zong.musicxml.types.partwise;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.MxlMusicData;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML measure in a partwise score.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly="measure-attributes", children="music-data")
public final class MxlMeasure
{
	
	public static final String ELEM_NAME = "measure";
	
	@NeverNull private final MxlMusicData musicData;
	@NeverNull private final String number;

	
	public MxlMeasure(MxlMusicData musicData, String number)
	{
		this.musicData = musicData;
		this.number = number;
	}

	
	@NeverNull public MxlMusicData getMusicData()
	{
		return musicData;
	}

	
	@NeverNull public String getNumber()
	{
		return number;
	}


	@NeverNull public static MxlMeasure read(Element e)
	{
		return new MxlMeasure(MxlMusicData.read(e), throwNull(attribute(e, "number"), e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addAttribute(e, "number", number);
		musicData.write(e);
	}
	

}
