package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.choice.MxlPartListContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML score-part.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "part-name-display,part-abbreviation-display,group,midi-device",
	children = "score-instrument,midi-instrument")
@AllArgsConstructor @Getter @Setter
public class MxlScorePart
	implements MxlPartListContent {

	public static final String elemName = "score-part";

	@MaybeNull private MxlIdentification identification;
	@NonNull private String partName;
	@MaybeNull private String partAbbreviation;
	@MaybeEmpty private List<MxlScoreInstrument> scoreInstruments;
	@MaybeEmpty private List<MxlMidiInstrument> midiInstruments;
	@NonNull private String id;


	@Override public PartListContentType getPartListContentType() {
		return PartListContentType.ScorePart;
	}

	@NonNull public static MxlScorePart read(XmlReader reader) {
		//attributes
		String id = reader.getAttributeNotNull("id");
		//elements
		MxlIdentification identification = null;
		String partName = null;
		String partAbbreviation = null;
		List<MxlScoreInstrument> scoreInstruments = alist();
		List<MxlMidiInstrument> midiInstruments = alist();
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			switch (n.charAt(0)) { //switch for performance
				case 'i':
					if (n.equals(MxlIdentification.elemName))
						identification = MxlIdentification.read(reader);
					break;
				case 'm':
					if (n.equals(MxlMidiInstrument.elemName))
						midiInstruments.add(MxlMidiInstrument.read(reader));
					break;
				case 'p':
					if (n.equals("part-abbreviation"))
						partAbbreviation = reader.getTextNotNull();
					else if (n.equals("part-name"))
						partName = reader.getTextNotNull();
					break;
				case 's':
					if (n.equals(MxlScoreInstrument.ELEM_NAME))
						scoreInstruments = scoreInstruments.plus(MxlScoreInstrument.read(c));
					break;
			}
			reader.closeElement();
		}
		return new MxlScorePart(identification, partName, partAbbreviation, scoreInstruments,
			midiInstruments, throwNull(attribute(e, "id"), e));
	}

	@Override public void write(Element parent) {
		Element e = addElement(elemName, parent);
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
