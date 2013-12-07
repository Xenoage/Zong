package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.NonNull;

/**
 * MusicXML start-stop-change.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStopChange {

	Start,
	Stop,
	Change;

	@NonNull public static MxlStartStopChange read(String s) {
		return Utils.read("start-stop-change", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
