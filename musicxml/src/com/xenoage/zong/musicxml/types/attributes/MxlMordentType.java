package com.xenoage.zong.musicxml.types.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlYesNo;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML mordent (complexType).
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children = "empty-trill-sound")
@AllArgsConstructor @Getter @Setter
public class MxlMordentType {

	@MaybeNull private MxlEmptyTrillSound emptyTrillSound;
	@MaybeNull private Boolean longValue;


	@MaybeNull public static MxlMordentType read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		Boolean longValue = MxlYesNo.readNull(reader.getAttribute("long"));
		if (emptyTrillSound != null || longValue != null)
			return new MxlMordentType(emptyTrillSound, longValue);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		if (emptyTrillSound != null)
			emptyTrillSound.write(writer);
		if (longValue != null)
			writer.writeAttribute("long", MxlYesNo.write(longValue));
	}
	
}
