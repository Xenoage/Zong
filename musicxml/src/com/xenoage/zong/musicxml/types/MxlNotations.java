package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML notations.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "tuplet,glissando,slide,technical,arpeggiate," +
 "non-arpeggiate,other-notation", children = "slur,tied,articulations,dynamics,ornaments")
@AllArgsConstructor @Getter @Setter
public final class MxlNotations {

	public static final String elemName = "notations";

	@NonNull private List<MxlNotationsContent> elements;
	

	public static MxlNotations read(XmlReader reader) {
		List<MxlNotationsContent> elements = alist();
		while (reader.openNextChildElement()) {
			String childName = reader.getElementName();
			MxlNotationsContent element = null;
			if (childName.equals(MxlAccidentalMark.elemName)) {
				element = MxlAccidentalMark.read(reader);
			}
			if (childName.equals(MxlArticulations.elemName)) {
				element = MxlArticulations.read(reader);
			}
			else if (childName.equals(MxlDynamics.elemName)) {
				element = MxlDynamics.read(reader);
			}
			else if (childName.equals(MxlFermata.elemName)) {
				element = MxlFermata.read(reader);
			}
			else if (childName.equals(MxlOrnaments.elemName)) {
				element = MxlOrnaments.read(reader);
			}
			else if (childName.equals(MxlSlurOrTied.elemNameSlur) ||
				childName.equals(MxlSlurOrTied.elemNameTied)) {
				element = MxlSlurOrTied.read(reader);
			}
			reader.closeElement();
			if (element != null)
				elements.add(element);
		}
		return new MxlNotations(elements);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		for (MxlNotationsContent element : elements)
			element.write(writer);
		writer.writeElementEnd();
	}

}
