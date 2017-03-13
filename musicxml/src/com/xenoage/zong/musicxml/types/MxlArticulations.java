package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;

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

	@MaybeNull public static MxlArticulations read(XmlReader reader) {
		List<MxlArticulationsContent> content = alist();
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			switch (n) {
				case MxlAccent.elemName:
					content.add(MxlAccent.read(reader));
					break;
				case MxlStrongAccent.elemName:
					content.add(MxlStrongAccent.read(reader));
					break;
				case MxlStaccato.elemName:
					content.add(MxlStaccato.read(reader));
					break;
				case MxlStaccatissimo.elemName:
					content.add(MxlStaccatissimo.read(reader));
					break;
				case MxlTenuto.elemName:
					content.add(MxlTenuto.read(reader));
					break;
			}
			reader.closeElement();
		}
		if (content.size() == 0)
			return null;
		return new MxlArticulations(content);
	}

	@Override public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		for (MxlArticulationsContent item : content)
			item.write(writer);
		writer.writeElementEnd();
	}

}
