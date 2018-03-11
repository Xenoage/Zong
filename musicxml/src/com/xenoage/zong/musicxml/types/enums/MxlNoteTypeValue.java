package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.xenoage.utils.math.Fraction.fr;

/**
 * MusicXML note-type-value.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlNoteTypeValue {

	_1024th(Companion.fr(1, 1024)),
	_512th(Companion.fr(1, 512)),
	_256th(Companion.fr(1, 256)),
	_128th(Companion.fr(1, 128)),
	_64th(Companion.fr(1, 64)),
	_32nd(Companion.fr(1, 32)),
	_16th(Companion.fr(1, 16)),
	Eighth(Companion.fr(1, 8)),
	Quarter(Companion.fr(1, 4)),
	Half(Companion.fr(1, 2)),
	Whole(Companion.fr(1, 1)),
	Breve(Companion.fr(1, 1)),
	Long(Companion.fr(1, 1));

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
