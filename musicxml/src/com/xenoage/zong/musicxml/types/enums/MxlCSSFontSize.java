package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.EnumUtils.getEnumValue;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.EnumWithXmlNames;

/**
 * MusicXML css-font-size.
 * 
 * @author Andreas Wenger
 */
public enum MxlCSSFontSize
	implements EnumWithXmlNames {

	XXSmall("xx-small"),
	XSmall("x-small"),
	Small("small"),
	Medium("medium"),
	Large("large"),
	XLarge("x-large"),
	XXLarge("xx-large");

	private final String xmlName;


	private MxlCSSFontSize(String xmlName) {
		this.xmlName = xmlName;
	}

	@Override public String getXmlName() {
		return xmlName;
	}

	@MaybeNull public static MxlCSSFontSize read(String s) {
		return getEnumValue(s, values());
	}

}
