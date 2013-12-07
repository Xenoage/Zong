package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.NonNull;

/**
 * MusicXML start-stop.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStop {

	Start,
	Stop;

	@NonNull public static MxlStartStop read(String s) {
		return Utils.read("start-stop", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
