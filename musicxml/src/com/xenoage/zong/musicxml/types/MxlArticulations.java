package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
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

	@MaybeNull public static MxlArticulations read(XmlReader reader) {
		List<MxlArticulationsContent> content = alist();
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals(MxlAccent.elemName))
				content.add(MxlAccent.read(reader));
			else if (n.equals(MxlStrongAccent.elemName))
				content.add(MxlStrongAccent.read(reader));
			else if (n.equals(MxlStaccato.elemName))
				content.add(MxlStaccato.read(reader));
			else if (n.equals(MxlStaccatissimo.elemName))
				content.add(MxlStaccatissimo.read(reader));
			else if (n.equals(MxlTenuto.elemName))
				content.add(MxlTenuto.read(reader));
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
