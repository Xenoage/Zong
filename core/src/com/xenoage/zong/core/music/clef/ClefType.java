package com.xenoage.zong.core.music.clef;

import static com.xenoage.zong.core.music.Pitch.pi;
import lombok.Getter;

import com.xenoage.zong.core.music.Pitch;


/**
 * This is the type of a clef.
 * 
 * It contains the octave change, the pitch, the line position,
 * and the minimum line position allowed for flats and sharps
 * of a key signature.
 *
 * @author Andreas Wenger
 */
public enum ClefType {

	G(0, pi('G', 0, 4), 2, 3, 1),
	F(0, pi('F', 0, 3), 6, 1, -1);

	/** The octave change in octaves of this type of clef, e.g. -1 means one octave deeper. */
	@Getter private final int octaveChange;
	/** The pitch of the line position this type of clef sits on. */
	@Getter private final Pitch pitch;
	/** The line position of this type of clef. */
	@Getter private final int line;
	/** The minimum line position allowed for a sharp symbol of a key signature. */
	@Getter private final int minSharpLine;
	/** The minimum line position allowed for a flat symbol of a key signature. */
	@Getter private final int minFlatLine;


	/**
	 * Creates a new type of clef with the given values.
	 */
	private ClefType(int octaveChange, Pitch pitch, int line, int minSharpLine, int minFlatLine) {
		this.octaveChange = octaveChange;
		this.pitch = pitch;
		this.line = line;
		this.minSharpLine = minSharpLine;
		this.minFlatLine = minFlatLine;
	}


	/**
	 * Computes and returns the line position of the given pitch, that is the
	 * vertical offset of the note in half-spaces from the bottom line at a
	 * 5-lines-staff. Some examples:
	 * <ul>
	 * <li>0: note is on the bottom line</li>
	 * <li>-2: note is on the first lower leger line</li>
	 * <li>5: note is between the 3rd and 4th line from below</li>
	 * </ul>
	 */
	public int computeLinePosition(Pitch pitch) {
		Pitch clefPitch = this.pitch;
		int ret = pitch.getStep() + 7 * pitch.getOctave();
		ret -= clefPitch.getStep() + 7 * clefPitch.getOctave();
		ret += line;
		ret -= octaveChange * 7;
		return ret;
	}


	/**
	 * Gets the lowest line that may be used for a key signature.
	 */
	public int getKeySignatureLowestLine(int fifth) {
		if (fifth < 0)
			return minFlatLine;
		else
			return minSharpLine;
	}


}
