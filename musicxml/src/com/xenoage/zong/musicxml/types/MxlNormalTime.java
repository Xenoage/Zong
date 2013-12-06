package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.xml.Parse;
import com.xenoage.zong.musicxml.types.choice.MxlTimeContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML beats/beats-type content for a time element.
 * 
 * Only one beats and beats-type is supported.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly="beats,beat-type")
public final class MxlNormalTime
	implements MxlTimeContent
{
	
	private final int beats;
	private final int beatType;
	
	
	public MxlNormalTime(int beats, int beatType)
	{
		this.beats = beats;
		this.beatType = beatType;
	}

	
	public int getBeats()
	{
		return beats;
	}

	
	public int getBeatType()
	{
		return beatType;
	}
	
	
	@Override public MxlTimeContentType getTimeContentType()
	{
		return MxlTimeContentType.NormalTime;
	}
	
	
	/**
	 * Returns null, when the time signature is not supported (e.g. 3+2/4).
	 */
	@MaybeNull public static MxlNormalTime read(Element e)
	{
		Integer beats = Parse.parseChildIntNullTry(e, "beats");
		Integer beatType = Parse.parseChildIntNullTry(e, "beat-type");
		if (beats != null && beatType != null)
			return new MxlNormalTime(beats, beatType);
		else
			return null;
	}
	
	
	@Override public void write(Element e)
	{
		addElement("beats", beats, e);
		addElement("beat-type", beatType, e);
	}

}
