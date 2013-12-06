package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML articulations.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "detached-legato,spiccato,scoop,plop,doit,falloff,breath-mark,"
	+ "caesura,stress,unstress,other-articulation")
@AllArgsConstructor @Getter @Setter
public final class MxlArticulations
	implements MxlNotationsContent {

	public static final String elemName = "articulations";

	@MaybeEmpty private List<MxlArticulationsContent> content;


	@Override public MxlNotationsContentType getNotationsContentType() {
		return MxlNotationsContentType.Articulations;
	}

	public static MxlArticulations read(XmlReader reader) {
		List<MxlArticulationsContent> content = alist();
		while (reader.moveToNextElement()) {
			String n = reader.getElementName();
			switch (n.charAt(0)) { //switch start letter for performance
				case 'a':
					if (n.equals(MxlAccent.elemName))
						content = content.plus(MxlAccent.read(c));
					break;
				case 's':
					if (n.equals(MxlStrongAccent.ELEM_NAME))
						content = content.plus(MxlStrongAccent.read(c));
					else if (n.equals(MxlStaccato.ELEM_NAME))
						content = content.plus(MxlStaccato.read(c));
					else if (n.equals(MxlStaccatissimo.ELEM_NAME))
						content = content.plus(MxlStaccatissimo.read(c));
					break;
				case 't':
					if (n.equals(MxlTenuto.ELEM_NAME))
						content = content.plus(MxlTenuto.read(c));
					break;
			}
		}
		return new MxlArticulations(content);
	}

	@Override public void write(Element parent) {
		Element e = addElement(elemName, parent);
		for (MxlArticulationsContent item : content)
			item.write(e);
	}

}
