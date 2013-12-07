package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.NonNull;

/**
 * MusicXML start-stop-continue.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStopContinue {

	Start,
	Stop,
	Continue;

	@NonNull public static MxlStartStopContinue read(String s) {
		return Utils.read("start-stop-continue", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
