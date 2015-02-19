package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.None;
import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.Right;

import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteSuspension;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.stampings.LegerLineStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;

/**
 * Creates {@link LegerLineStamping}s from a {@link ChordNotation}.
 *  
 * @author Andreas Wenger
 */
public class LegerLinesStamper {
	
	public static final LegerLinesStamper legerLinesStamper = new LegerLinesStamper();
	
	public static final LegerLineStamping[] empty = new LegerLineStamping[0];
	
	
	public LegerLineStamping[] stamp(ChordNotation chordNotation, float chordXMm, StaffStamping staffStamping) {
		NotesNotation notes = chordNotation.notes;
		int bottomCount = getBottomCount(notes.getBottomNote().lp);
		int topCount = getTopCount(notes.getTopNote().lp, staffStamping.linesCount);
		if (bottomCount > 0 || topCount > 0) {
			//horizontal position and width (may differ above and below staff, dependent on suspended notes)
			NoteSuspension bottomSuspension = getBottomSuspension(notes.notes);
			float xTopMm = getXMm(chordXMm, notes.noteheadWidthIs, bottomSuspension, staffStamping.is);
			float widthBottomIs = getWidthIs(bottomSuspension != None);
			NoteSuspension topSuspension = getTopSuspension(notes.notes, staffStamping.linesCount);
			float xBottomMm = getXMm(chordXMm, notes.noteheadWidthIs, topSuspension, staffStamping.is);
			float widthTopIs = getWidthIs(bottomSuspension != None);
			//vertical positions
			int[] bottomLps = getBottomLps(bottomCount);
			int[] topLps = getTopLps(topCount, staffStamping.linesCount);
			//create stampings
			LegerLineStamping[] ret = new LegerLineStamping[bottomCount + topCount];
			for (int i : range(bottomCount))
				ret[i] = new LegerLineStamping(staffStamping, xBottomMm, bottomLps[i], widthBottomIs);
			for (int i : range(topCount))
				ret[bottomCount + i] = new LegerLineStamping(staffStamping, xTopMm, topLps[i], widthTopIs);
			return ret;
		}
		else {
			return empty;
		}
	}
	
	float getXMm(float chordXMm, float noteheadWidthIs, NoteSuspension suspension, float staffIs) {
		float leftNoteXMm = chordXMm;
		//center x on middle of chord notes
		if (suspension == None)
			leftNoteXMm += noteheadWidthIs * staffIs / 2;
		else if (suspension == Right)
			leftNoteXMm += noteheadWidthIs * staffIs;
		return leftNoteXMm;
	}
	
	NoteSuspension getBottomSuspension(NoteDisplacement[] notes) {
		//find a suspended note which needs a leger line on the bottom side
		for (NoteDisplacement note : notes)
			if (note.suspension != None && note.lp <= -2)
				return note.suspension;
		return None;
	}
	
	NoteSuspension getTopSuspension(NoteDisplacement[] notes, int staffLinesCount) {
		//find a suspended note which needs a leger line on the top side
		for (NoteDisplacement note : notes)
			if (note.suspension != None && note.lp >= staffLinesCount * 2)
				return note.suspension;
		return None;
	}
	
	float getWidthIs(boolean suspended) {
		return suspended ? LegerLineStamping.lengthSuspendedIs : LegerLineStamping.lengthNormalIs;
	}
	
	int getTopCount(int topNoteLp, int staffLinesCount) {
		int staffTopLp = staffLinesCount * 2 - 2;
		if (topNoteLp > staffTopLp + 1)
			return (topNoteLp - staffTopLp) / 2;
		else
			return 0;
	}
	
	int getBottomCount(int bottomNoteLp) {
		if (bottomNoteLp < -1)
			return bottomNoteLp / -2;
		else
			return 0;
	}
	
	int[] getBottomLps(int legerLinesBottomCount) {
		int[] ret = new int[legerLinesBottomCount];
		//in ascending order
		for (int i : range(legerLinesBottomCount)) {
			int lp = -2 - (legerLinesBottomCount - i - 1) * 2;
			ret[i] = lp;
		}
		return ret;
	}
	
	int[] getTopLps(int legerLinesTopCount, int staffLinesCount) {
		int[] ret = new int[legerLinesTopCount];
		//in ascending order
		for (int i : range(legerLinesTopCount)) {
			int lp = (staffLinesCount + i) * 2;
			ret[i] = lp;
		}
		return ret;
	}

}
