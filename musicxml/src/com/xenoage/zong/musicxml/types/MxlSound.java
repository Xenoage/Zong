package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.Parse.parseAttrFloatNull;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML sound.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="midi-instrument,offset,dynamics,dacapo,segno,dalsegno," +
	"coda,tocoda,divisions,forward-repeat,fine,time-only,pizzicato,pan,elevation,damper-pedal," +
	"soft-pedal,sostenuto-pedal")
public final class MxlSound
{
	
	public static final String ELEM_NAME = "sound";
	
	private final Float tempo;

	
	public MxlSound(Float tempo)
	{
		this.tempo = tempo;
	}

	
	@MaybeNull public Float getTempo()
	{
		return tempo;
	}
	
	
	@NeverNull public static MxlSound read(Element e)
	{
		return new MxlSound(parseAttrFloatNull(e, "tempo"));
	}
	
	
	public void write(Element parent)
	{
		if (tempo != null)
		{
			Element e = addElement(ELEM_NAME, parent);
			addAttribute(e, "tempo", tempo);
		}
	}

}
