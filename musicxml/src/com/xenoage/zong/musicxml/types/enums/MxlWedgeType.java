package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.NonNull;

/**
 * MusicXML wedge-type.
 * 
 * @author Andreas Wenger
 */
public enum MxlWedgeType {

	Crescendo,
	Diminuendo,
	Stop;

	@NonNull public static MxlWedgeType read(String s) {
		return Utils.read("wedge-type", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
