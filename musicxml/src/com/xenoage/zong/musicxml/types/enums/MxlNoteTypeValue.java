package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.math.Fraction.fr;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;

/**
 * MusicXML note-type-value.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlNoteTypeValue {

	_256th(fr(1, 256)),
	_128th(fr(1, 128)),
	_64th(fr(1, 64)),
	_32nd(fr(1, 32)),
	_16th(fr(1, 16)),
	Eighth(fr(1, 8)),
	Quarter(fr(1, 4)),
	Half(fr(1, 2)),
	Whole(fr(1, 1)),
	Breve(fr(1, 1)),
	Long(fr(1, 1));

	private final Fraction duration;


	@NonNull public static MxlNoteTypeValue read(String s) {
		return Utils.read("note-type-value", s, values());
	}

	public String write() {
		String ret = toString().toLowerCase();
		if (ret.charAt(0) == '_')
			ret = ret.substring(1);
		return ret;
	}

}
