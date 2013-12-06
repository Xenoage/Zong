package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.Parse.parseChildInt;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML backup.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="staff", children="editorial-voice")
public final class MxlForward
	implements MxlMusicDataContent
{
	
	public static final String ELEM_NAME = "forward";
	
	private final int duration;
	@NeverNull private final MxlEditorialVoice editorialVoice;


	public MxlForward(int duration, MxlEditorialVoice editorialVoice)
	{
		this.duration = duration;
		this.editorialVoice = editorialVoice;
	}

	
	public int getDuration()
	{
		return duration;
	}
	
	
	@NeverNull public MxlEditorialVoice getEditorialVoice()
	{
		return editorialVoice;
	}

	
	@Override public MxlMusicDataContentType getMusicDataContentType()
	{
		return MxlMusicDataContentType.Forward;
	}
	

	@NeverNull public static MxlForward read(Element e)
	{
		int duration = parseChildInt(e, "duration");
		MxlEditorialVoice editorialVoice = MxlEditorialVoice.read(e);
		return new MxlForward(duration, editorialVoice);
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement("duration", duration, e);
		editorialVoice.write(e);
	}

}
