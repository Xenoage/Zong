package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.NonNull;

/**
 * MusicXML start-stop-discontinue.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStopDiscontinue {

	Start,
	Stop,
	Discontinue;
	

	@NonNull public static MxlStartStopDiscontinue read(String s) {
		return Utils.read("start-stop-discontinue", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
