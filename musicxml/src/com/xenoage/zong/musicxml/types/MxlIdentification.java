package com.xenoage.zong.musicxml.types;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML identification.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "encoding,source,relation,miscellaneous")
@AllArgsConstructor @Getter @Setter
public final class MxlIdentification {

	public static final String elemName = "identification";

	@MaybeNull private List<MxlTypedText> creators;
	@MaybeNull private List<MxlTypedText> rights;


	@MaybeNull public static MxlIdentification read(XmlReader reader) {
		List<MxlTypedText> creators = null;
		List<MxlTypedText> rights = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("creator")) {
				if (creators == null)
					creators = new ArrayList<MxlTypedText>();
				creators.add(MxlTypedText.read(reader));
			}
			else if (n.equals("rights")) {
				if (rights == null)
					rights = new ArrayList<MxlTypedText>();
				rights.add(MxlTypedText.read(reader));
			}
			reader.closeElement();
		}
		if (creators != null || rights != null)
			return new MxlIdentification(creators, rights);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		if (creators != null) {
			for (MxlTypedText t : creators)
				t.write("creator", writer);
		}
		if (rights != null) {
			for (MxlTypedText t : rights)
				t.write("rights", writer);
		}
		writer.writeElementEnd();
	}

}
