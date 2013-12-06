package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.Parse.parseChildFloatNull;
import static com.xenoage.utils.xml.Parse.parseChildIntNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML midi-instrument.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="midi-name,midi-bank,midi-unpitched,elevation")
public final class MxlMidiInstrument
{
	
	public static final String ELEM_NAME = "midi-instrument";
	
	@MaybeNull public final Integer midiChannel;
	@MaybeNull public final Integer midiProgram;
	@MaybeNull public final Float volume;
	@MaybeNull public final Float pan;
	@NeverNull public final String id;
	
	
	public MxlMidiInstrument(Integer midiChannel, Integer midiProgram,
		Float volume, Float pan, String id)
	{
		this.midiChannel = midiChannel;
		this.midiProgram = midiProgram;
		this.volume = volume;
		this.pan = pan;
		this.id = id;
	}
	
	
	@NeverNull public static MxlMidiInstrument read(Element e)
	{
		return new MxlMidiInstrument(
			parseChildIntNull(e, "midi-channel"),
			parseChildIntNull(e, "midi-program"),
			parseChildFloatNull(e, "volume"),
			parseChildFloatNull(e, "pan"),
			throwNull(attribute(e, "id"), e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement("midi-channel", midiChannel, e);
		addElement("midi-program", midiProgram, e);
		addElement("volume", volume, e);
		addElement("pan", pan, e);
		addAttribute(e, "id", id);
	}
	

}
