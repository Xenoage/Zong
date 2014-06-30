package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML direction-type.
 * 
 * Only one content element is used.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "rehearsal,dashes,bracket,"
	+ "octave-shift,harp-pedals,damp,damp-all,eyeglasses,scordatura,accordion-registration,"
	+ "other-direction")
@AllArgsConstructor @Getter @Setter
public final class MxlDirectionType {

	public static final String elemName = "direction-type";

	@NonNull private MxlDirectionTypeContent content;


	/**
	 * Returns null, if content is unsupported.
	 */
	@MaybeNull public static MxlDirectionType read(XmlReader reader) {
		MxlDirectionTypeContent content = null;
		if (reader.openNextChildElement()) {
			String n = reader.getElementName();
			switch (n.charAt(0)) { //switch for performance
				case 'c':
					if (n.equals(MxlCoda.elemName))
						content = MxlCoda.read(reader);
					break;
				case 'd':
					if (n.equals(MxlDynamics.elemName))
						content = MxlDynamics.read(reader);
					break;
				case 'i':
					if (n.equals("image"))
						content = MxlImage.read(reader);
					break;
				case 'p':
					if (n.equals(MxlPedal.elemName))
						content = MxlPedal.read(reader);
					break;
				case 'm':
					if (n.equals(MxlMetronome.elemName))
						content = MxlMetronome.read(reader);
					break;
				case 's':
					if (n.equals(MxlSegno.elemName))
						content = MxlSegno.read(reader);
					break;
				case 'w':
					if (n.equals(MxlWedge.elemName))
						content = MxlWedge.read(reader);
					else if (n.equals(MxlWords.elemName))
						content = MxlWords.read(reader);
					break;
			}
			reader.closeElement();
		}
		if (content != null)
			return new MxlDirectionType(content);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		content.write(writer);
		writer.writeElementEnd();
	}

}
