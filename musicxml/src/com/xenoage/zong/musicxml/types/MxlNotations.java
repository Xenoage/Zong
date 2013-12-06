package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML notations.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "tuplet,glissando,slide,ornaments,technical,fermata,"
	+ "arpeggiate,non-arpeggiate,accidental-mark,other-notation", children = "slur,tied,articulations,dynamics")
@AllArgsConstructor @Getter @Setter public final class MxlNotations {

	public static final String elemName = "notations";

	private List<MxlNotationsContent> elements;
	

	public static MxlNotations read(XmlReader reader) {
		List<MxlNotationsContent> elements = alist();
		while (reader.moveToNextElement()) {
			String childName = reader.getElementName();
			MxlNotationsContent element = null;
			if (childName.equals(MxlArticulations.elemName)) {
				element = MxlArticulations.read(child);
			}
			else if (childName.equals(MxlDynamics.ELEM_NAME)) {
				element = MxlDynamics.read(child);
			}
			else if (childName.equals(MxlSlurOrTied.elemNameSlur) ||
				childName.equals(MxlSlurOrTied.elemNameTied)) {
				element = MxlSlurOrTied.read(child);
			}
			if (element != null) {
				elements = elements.plus(element);
			}
		}
		return new MxlNotations(elements);
	}

	public void write(Element parent) {
		Element e = addElement(elemName, parent);
		for (MxlNotationsContent element : elements) {
			element.write(e);
		}
	}

}
