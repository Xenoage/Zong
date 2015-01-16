package com.xenoage.zong.io.musicxml.in.readers;

import java.util.List;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlStem;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;

/**
 * Reads a {@link Stem} from the given {@link MxlNote} and the context.
 * 
 * @author Andreas Wenger
 */
public class StemReader {
	
	/**
	 * Reads and returns the stem of the given chord.
	 * If not available, null is returned.
	 * @param context   the global context
	 * @param xmlNote   the note element, that contains the interesting
	 *                  stem element. if not, null is returned.
	 * @param chord     the chord, whose notes are already collected
	 * @param staff     the staff index of the current chord
	 */
	public static Stem readStem(Context context, MxlNote mxlNote, Chord chord, int staff) {
		Stem ret = null;
		MxlStem mxlStem = mxlNote.getStem();
		if (mxlStem != null) {
			//direction
			StemDirection direction = null;
			switch (mxlStem.getValue()) {
				case None:
					direction = StemDirection.None;
					break;
				case Up:
					direction = StemDirection.Up;
					break;
				case Down:
					direction = StemDirection.Down;
					break;
				case Double:
					direction = StemDirection.Up; //currently double is not supported
					break;
			}
			//length
			Float length = null;
			MxlPosition yPos = mxlStem.getPosition();
			if (yPos != null && yPos.getDefaultY() != null) {
				//convert position in tenths relative to topmost staff line into
				//a length in interline spaces measured from the outermost chord note on stem side
				float stemEndLinePosition = convertDefaultYToLinePosition(context, yPos.getDefaultY(), staff);
				length = Math.abs(stemEndLinePosition -
					getNoteLinePosition(context, chord, stemEndLinePosition, staff)) / 2;
			}
			//create stem
			ret = new Stem(direction, length);
		}
		return ret;
	}

	/**
	 * Converts the given default-y position in global tenths (that is always
	 * relative to the topmost staff line) to a line position, using the
	 * musical context from the given staff.
	 */
	private static float convertDefaultYToLinePosition(Context context, float defaultY,
		int staff) {
		Score score = context.getScore();
		float defaultYInMm = defaultY * score.getFormat().getInterlineSpace() / 10;
		float interlineSpace = score.getInterlineSpace(context.getPartStaffIndices().getStart() +
			staff);
		int linesCount = context.getStaffLinesCount(staff);
		return 2 * (linesCount - 1) + 2 * defaultYInMm / interlineSpace;
	}

	/**
	 * Gets the line position of the note which is nearest to the given line position,
	 * using the musical context from the given staff.
	 */
	private static float getNoteLinePosition(Context context, Chord chord, float nearTo,
		int staff) {
		MusicContext mc = context.getMusicContext(staff);
		List<Pitch> pitches = chord.getPitches();
		//if there is just one note, it's easy
		if (pitches.size() == 1) {
			return mc.getLp(pitches.get(0));
		}
		//otherwise, test for the topmost and bottommost note
		else {
			float top = mc.getLp(pitches.get(pitches.size() - 1));
			float bottom = mc.getLp(pitches.get(0));
			return (Math.abs(top - nearTo) < Math.abs(bottom - nearTo) ? top : bottom);
		}
	}

}
