package com.xenoage.zong.musicxml.types.attributes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlYesNo;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML print-attributes.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "staff-spacing,blank-page,page-number")
@AllArgsConstructor @Getter
public class MxlPrintAttributes {

	@MaybeNull private Boolean newSystem;
	@MaybeNull private Boolean newPage;


	@MaybeNull public static MxlPrintAttributes read(XmlReader reader) {
		Boolean newSystem = MxlYesNo.readNull(reader.getAttributeString("new-system"));
		Boolean newPage = MxlYesNo.readNull(reader.getAttributeString("new-page"));
		if (newSystem != null || newPage != null)
			return new MxlPrintAttributes(newSystem, newPage);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		if (newSystem != null)
			writer.writeAttribute("new-system", MxlYesNo.write(newSystem));
		if (newPage != null)
			writer.writeAttribute("new-page", MxlYesNo.write(newPage));
	}

}
