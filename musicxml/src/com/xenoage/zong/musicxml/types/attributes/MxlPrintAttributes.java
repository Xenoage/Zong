package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.zong.musicxml.types.enums.MxlYesNo.Unknown;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
	
	public static final MxlPrintAttributes noPrintAttributes = new MxlPrintAttributes(Unknown, Unknown);

	private MxlYesNo newSystem;
	private MxlYesNo newPage;


	public static MxlPrintAttributes read(XmlReader reader) {
		MxlYesNo newSystem = MxlYesNo.read(reader.getAttribute("new-system"));
		MxlYesNo newPage = MxlYesNo.read(reader.getAttribute("new-page"));
		if (newSystem != Unknown || newPage != Unknown)
			return new MxlPrintAttributes(newSystem, newPage);
		else
			return noPrintAttributes;
	}

	public void write(XmlWriter writer) {
		if (this != noPrintAttributes) {
			newSystem.write(writer, "new-system");
			newPage.write(writer, "new-page");
		}
	}

}
