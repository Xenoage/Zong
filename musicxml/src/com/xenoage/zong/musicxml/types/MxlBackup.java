package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.Parse.parseChildInt;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlMusicDataContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML backup.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="editorial")
public final class MxlBackup
	implements MxlMusicDataContent
{
	
	public static final String ELEM_NAME = "backup";
	
	private final int duration;

	
	public MxlBackup(int duration)
	{
		this.duration = duration;
	}

	
	public int getDuration()
	{
		return duration;
	}
	
	
	@Override public MxlMusicDataContentType getMusicDataContentType()
	{
		return MxlMusicDataContentType.Backup;
	}
	
	
	@NeverNull public static MxlBackup read(Element e)
	{
		return new MxlBackup(parseChildInt(e, "duration"));
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement("duration", duration, e);
	}

}
