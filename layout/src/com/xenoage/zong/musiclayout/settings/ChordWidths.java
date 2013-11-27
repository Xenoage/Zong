package com.xenoage.zong.musiclayout.settings;

import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.utils.math.Fraction._1$2;
import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.chord.Accidental;

/**
 * Settings for symbol widths (in IS) within a chord.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public class ChordWidths {

	/** Whole notehead */
	public final float whole;
	/** Half notehead */
	public final float half;
	/** Quarter notehead */
	public final float quarter;
	/** The gap before the first dot */
	public final float dotGap;
	/** The width of a dot, i.e. the distance between two dots */
	public final float dot;
	public final float accToNoteGap;
	public final float accToAccGap;
	public final float doubleSharp;
	public final float sharp;
	public final float natural;
	public final float flat;
	public final float doubleFlat;

	/** Reasonable default values */
	public static final ChordWidths defaultValue = new ChordWidths(2, 1.2f, 1.2f, 0.7f, 0.6f, 0.5f,
		0.25f, 0.95f, 1f, 0.96f, 0.97f, 1.8f);


	/**
	 * Gets the width of the notehead of the given duration.
	 */
	public float get(Fraction duration) {
		if (duration.compareTo(_1$2) < 0)
			return quarter;
		else if (duration.compareTo(_1) < 0)
			return half;
		else
			return whole;
	}

	/**
	 * Gets the width of the accidental of the given type.
	 */
	public float get(Accidental type) {
		switch (type) {
			case DoubleSharp:
				return doubleSharp;
			case Sharp:
				return sharp;
			case Natural:
				return natural;
			case Flat:
				return flat;
			case DoubleFlat:
				return doubleFlat;
		}
		return 0;
	}

	/**
	 * Computes the width of the accidental having the greatest width.
	 */
	public float getMaxWidth(Accidental... types) {
		float maxWidth = 0;
		for (Accidental type : types) {
			maxWidth = Math.max(maxWidth, get(type));
		}
		return maxWidth;
	}

	/**
	 * Scales all values by the given factor.
	 */
	public ChordWidths scale(float scaling) {
		float s = scaling;
		return new ChordWidths(whole * s, half * s, quarter * s, dotGap * s, dot * s, accToNoteGap * s,
			accToAccGap * s, doubleSharp * s, sharp * s, natural * s, flat * s, doubleFlat * s);
	}

}
