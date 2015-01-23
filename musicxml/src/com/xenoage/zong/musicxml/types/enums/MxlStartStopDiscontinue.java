package com.xenoage.zong.musicxml.types.enums;


/**
 * MusicXML start-stop-discontinue.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStopDiscontinue {

	Start,
	Stop,
	Discontinue;
	

	public static MxlStartStopDiscontinue read(String s) {
		return Utils.read("start-stop-discontinue", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
