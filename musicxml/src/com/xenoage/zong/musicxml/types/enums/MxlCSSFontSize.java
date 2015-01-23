package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.EnumWithXmlNames;

/**
 * MusicXML css-font-size.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
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

	@MaybeNull public static MxlCSSFontSize read(String s) {
		return Utils.readOr("css-font-size", s, values(), null);
	}
	
	public String write() {
		return xmlName;
	}

}
