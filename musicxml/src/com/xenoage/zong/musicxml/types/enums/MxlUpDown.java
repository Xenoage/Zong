package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;

/**
 * MusicXML up-down.
 * 
 * @author Andreas Wenger
 */
public enum MxlUpDown {

	Up,
	Down;

	@MaybeNull public static MxlUpDown readOr(String s, MxlUpDown replacement) {
		return Utils.readOr("up-down", s, values(), replacement);
	}

	public String write() {
		return toString().toLowerCase();
	}

}
