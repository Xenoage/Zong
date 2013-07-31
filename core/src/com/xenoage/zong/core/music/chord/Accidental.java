package com.xenoage.zong.core.music.chord;


/**
 * Accidental.
 * 
 * Compare musicxml 2.0: note.mod, l. 216
 * harp-sharp, flat-flat, natural-sharp, natural-flat, quarter-flat, quarter-sharp,
 * three-quarters-flat, and three-quarters-sharp are not implemented yet.
 * 
 * @author Andreas Wenger
 */
public enum Accidental {
	
	DoubleSharp,
	Sharp,
	Natural,
	Flat,
	DoubleFlat;

	public static Accidental fromAlter(int alter) {
		switch (alter) {
			case -2:
				return DoubleFlat;
			case -1:
				return Flat;
			case +1:
				return Sharp;
			case +2:
				return DoubleSharp;
			default:
				return Natural;
		}
	}

}
