package com.xenoage.zong.musicxml.types.enums;


/**
 * MusicXML start-stop-continue.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStopContinue {

	Start,
	Stop,
	Continue;

	
	public static MxlStartStopContinue read(String s) {
		return Utils.read("start-stop-continue", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
