package com.xenoage.zong.musicxml.types.enums;


/**
 * MusicXML start-stop.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStop {

	Start,
	Stop;

	
	public static MxlStartStop read(String s) {
		return Utils.read("start-stop", s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
