package com.xenoage.zong.core.music;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NonNull;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;


/**
 * A music context provides information about the musical state
 * at a specific position.
 * 
 * These are the current clef, the key signature,
 * the list of accidentals and the number of staff lines.
 *
 * @author Andreas Wenger
 */
@Const @Data public class MusicContext {

	public static final MusicContext simpleInstance = new MusicContext(new Clef(ClefType.clefTreble),
		new TraditionalKey(0, Mode.Major), new Pitch[0], 5);


	/** The current clef. */
	@NonNull private final Clef clef;
	/** The current key signature. */
	@NonNull private final Key key;
	/** The list of current accidentals (key: pitch without alter, value: alter). */
	@NonNull private final Map<Pitch, Integer> accidentals;
	/** The number of staff lines. */
	private final int linesCount;


	/**
	 * Creates a context with the given clef, key and map of accidentals.
	 */
	public MusicContext(Clef clef, Key key, Map<Pitch, Integer> accidentals, int linesCount) {
		this.clef = clef;
		this.key = key;
		this.accidentals = accidentals;
		this.linesCount = linesCount;
	}
	
	
	/**
	 * Creates a context with the given clef, key and list of accidentals.
	 */
	public MusicContext(Clef clef, Key key, Pitch[] accidentals, int linesCount) {
		this.clef = clef;
		this.key = key;
		Map<Pitch, Integer> accidentalsMap = new HashMap<Pitch, Integer>();
		for (Pitch acc : accidentals)
			accidentalsMap.put(acc.withoutAlter(), (int) acc.getAlter());
		this.accidentals = accidentalsMap;
		this.linesCount = linesCount;
	}


	/**
	 * Computes and returns the line position of the given pitch,
	 * that is the vertical offset of the note in half-spaces
	 * from the bottom line.
	 */
	public int getLp(Pitch pitch) {
		return clef.getType().getLp(pitch);
	}


	/**
	 * Gets the accidental for the given pitch.
	 * When no accidental is needed, null is returned.
	 */
	public Accidental getAccidental(Pitch pitch) {
		//look, if this pitch is already set as an accidental
		Integer alter = accidentals.get(pitch.withoutAlter());
		if (alter != null) {
			if (alter == pitch.getAlter()) {
				//we need no accidental
				return null;
			}
			else {
				//we need to show the accidental
				return Accidental.fromAlter(pitch.getAlter());
			}
		}
		//look, if this alteration is already set in the key signature
		if (key.getAlterations()[pitch.getStep()] == pitch.getAlter()) {
			//we need no accidental
			return null;
		}
		else {
			//we need to show the accidental
			return Accidental.fromAlter(pitch.getAlter());
		}
	}


}
