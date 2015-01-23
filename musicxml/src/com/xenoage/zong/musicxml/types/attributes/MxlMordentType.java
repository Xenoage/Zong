package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.zong.musicxml.types.attributes.MxlEmptyTrillSound.noEmptyTrillSound;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
public final class MxlMordentType {
	
	public static final MxlMordentType noMordentType = new MxlMordentType(noEmptyTrillSound, MxlYesNo.Unknown);

	private final MxlEmptyTrillSound emptyTrillSound;
	private final MxlYesNo longValue;


	public static MxlMordentType read(XmlReader reader) {
		MxlEmptyTrillSound emptyTrillSound = MxlEmptyTrillSound.read(reader);
		MxlYesNo longValue = MxlYesNo.read(reader.getAttribute("long"));
		if (emptyTrillSound != noEmptyTrillSound || longValue != MxlYesNo.Unknown)
			return new MxlMordentType(emptyTrillSound, longValue);
		else
			return noMordentType;
	}

	public void write(XmlWriter writer) {
		emptyTrillSound.write(writer);
		longValue.write(writer, "long");
	}
	
}
