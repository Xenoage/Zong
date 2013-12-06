package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLReader.getTextContent;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.zong.musicxml.types.choice.MxlPartListContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML score-part.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="part-name-display,part-abbreviation-display," +
	"group,midi-device", children="score-instrument,midi-instrument")
public class MxlScorePart
	implements MxlPartListContent
{
	
	public static final String ELEM_NAME = "score-part";
	
	@MaybeNull private final MxlIdentification identification;
	@NeverNull private final String partName;
	@MaybeNull private final String partAbbreviation;
	@MaybeEmpty private final PVector<MxlScoreInstrument> scoreInstruments;
	@MaybeEmpty private final PVector<MxlMidiInstrument> midiInstruments;
	@NeverNull private final String id;
	

	public MxlScorePart(MxlIdentification identification, String partName,
		String partAbbreviation, PVector<MxlScoreInstrument> scoreInstruments,
		PVector<MxlMidiInstrument> midiInstruments, String id)
	{
		this.identification = identification;
		this.partName = partName;
		this.partAbbreviation = partAbbreviation;
		this.scoreInstruments = scoreInstruments;
		this.midiInstruments = midiInstruments;
		this.id = id;
	}

	
	@MaybeNull public String getID()
	{
		return id;
	}

	
	@MaybeNull public MxlIdentification getIdentification()
	{
		return identification;
	}

	
	@MaybeNull public String getName()
	{
		return partName;
	}

	
	@MaybeNull public String getAbbreviation()
	{
		return partAbbreviation;
	}

	
	@MaybeEmpty public PVector<MxlScoreInstrument> getScoreInstruments()
	{
		return scoreInstruments;
	}


	@MaybeEmpty public PVector<MxlMidiInstrument> getMidiInstruments()
	{
		return midiInstruments;
	}


	@Override public PartListContentType getPartListContentType()
	{
		return PartListContentType.ScorePart;
	}
	
	
	@NeverNull public static MxlScorePart read(Element e)
	{
		MxlIdentification identification = null;
		String partName = null;
		String partAbbreviation = null;
		PVector<MxlScoreInstrument> scoreInstruments = pvec();
		PVector<MxlMidiInstrument> midiInstruments = pvec();
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			switch (n.charAt(0))
			{
				case 'i':
					if (n.equals(MxlIdentification.ELEM_NAME))
						identification = MxlIdentification.read(c);
					break;
				case 'm':
					if (n.equals(MxlMidiInstrument.ELEM_NAME))
						midiInstruments = midiInstruments.plus(MxlMidiInstrument.read(c));
					break;
				case 'p':
					if (n.equals("part-abbreviation"))
						partAbbreviation = getTextContent(c);
					else if (n.equals("part-name"))
						partName = getTextContent(c);
					break;
				case 's':
					if (n.equals(MxlScoreInstrument.ELEM_NAME))
						scoreInstruments = scoreInstruments.plus(MxlScoreInstrument.read(c));
					break;
			}
		}
		return new MxlScorePart(identification, partName, partAbbreviation, scoreInstruments,
			midiInstruments, throwNull(attribute(e, "id"), e));
	}


	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		if (identification != null)
			identification.write(e);
		addElement("part-name", partName, e);
		addElement("part-abbreviation", partAbbreviation, e);
		for (MxlScoreInstrument scoreInstrument : scoreInstruments)
			scoreInstrument.write(e);
		for (MxlMidiInstrument midiInstrument : midiInstruments)
			midiInstrument.write(e);
		addAttribute(e, "id", id);
	}

}
