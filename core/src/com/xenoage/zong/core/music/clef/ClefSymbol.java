package com.xenoage.zong.core.music.clef;

import static com.xenoage.zong.core.music.Pitch.pi;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.zong.core.music.Pitch;

/**
 * Symbols for clefs.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor public enum ClefSymbol {
	/** G clef. */
	G(pi('G', 0, 4), 3, 1),
	/** G clef quindicesima bassa. */
	G15vb(pi('G', 0, 2), 3, 1),
	/** G clef ottava bassa. */
	G8vb(pi('G', 0, 3), 3, 1),
	/** G clef ottava alta. */
	G8va(pi('G', 0, 5), 3, 1),
	/** G clef quindicesima alta. */
	G15va(pi('G', 0, 6), 3, 1),
	/** F clef. */
	F(pi('F', 0, 3), 1, -1),
	/** F clef quindicesima bassa. */
	F15vb(pi('F', 0, 1), 1, -1),
	/** F clef ottava bassa. */
	F8vb(pi('F', 0, 2), 1, -1),
	/** F clef ottava alta. */
	F8va(pi('F', 0, 4), 1, -1),
	/** F clef quindicesima alta. */
	F15va(pi('F', 0, 5), 1, -1),
	/** C clef. */
	C(pi('C', 0, 4), 2, 0),
	/** Tab clef. */
	Tab(pi('B', 0, 4), 3, 1),
	/** Smaller tab clef. */
	TabSmall(pi('B', 0, 4), 3, 1),
	/** Percussion clef, two filled rects. */
	PercTwoRects(pi('B', 0, 4), 3, 1),
	/** Percussion clef, large empty rects. */
	PercEmptyRect(pi('B', 0, 4), 3, 1),
	/** No clef symbol. */
	None(pi('B', 0, 4), 3, 1);
	
	/** The pitch of the line position this type of clef sits on. */
	@Getter private final Pitch pitch;
	/** The minimum line position allowed for a sharp symbol of a key signature. */
	@Getter private final int minSharpLp;
	/** The minimum line position allowed for a flat symbol of a key signature. */
	@Getter private final int minFlatLp;
}
