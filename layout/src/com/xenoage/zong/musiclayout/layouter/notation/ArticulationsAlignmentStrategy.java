package com.xenoage.zong.musiclayout.layouter.notation;

import java.util.List;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationAlignment;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NoteAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;

/**
 * This strategy stores the alignment of
 * the articulations of the given chord.
 * 
 * @author Andreas Wenger
 */
public class ArticulationsAlignmentStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Computes the alignment of the articulations of the given chord,
	 * using the given note alignments and the given stem direction (optional),
	 * and returns it. If there are no articulations, null is returned.
	 */
	public ArticulationsAlignment computeArticulationsAlignment(Chord chord,
		StemDirection stemDirection, NotesAlignment notesAlignment, int linesCount) {
		//depending on the stem direction, place the articulation on the other side.
		//if there is no stem direction, always place at the top
		VSide side = (stemDirection == StemDirection.Up ? VSide.Bottom : VSide.Top);
		//dependent on the side of the articulation, take the top or bottom note
		NoteAlignment outerNote = (side == VSide.Top ? notesAlignment.getTopNoteAlignment()
			: notesAlignment.getBottomNoteAlignment());
		//compute alignment of articulations
		return computeArticulationsAlignment(chord.getArticulations(), outerNote, side, linesCount);
	}

	/**
	 * Creates a new {@link ArticulationsAlignment} for the outer chord note
	 * at the given side, or null if there are no articulations.
	 */
	ArticulationsAlignment computeArticulationsAlignment(List<Articulation> articulations,
		NoteAlignment outerNote, VSide side, int staffLinesCount) {
		//if there are no accidentals, return null
		if (articulations == null || articulations.size() == 0) {
			return null;
		}
		//special cases (which appear often): if there is only a single articulation
		//which is either a staccato or tenuto, we can place it between the staff lines
		if (articulations.size() == 1 &&
			(articulations.get(0).getType() == ArticulationType.Staccato ||
			articulations.get(0).getType() == ArticulationType.Tenuto)) {
			return computeSimpleArticulation(articulations.get(0).getType(), outerNote, side, staffLinesCount);
		}
		//otherwise, the articulations a placed above or below the staff
		else {
			return computeOtherArticulations(articulations, outerNote, side, staffLinesCount);
		}
	}

	/**
	 * Creates an {@link ArticulationsAlignment} for the given {@link ArticulationType}
	 * at the given notehead at the given side. If possible, it is placed between
	 * the staff lines.
	 */
	ArticulationsAlignment computeSimpleArticulation(ArticulationType articulation,
		NoteAlignment outerNote, VSide side, int staffLinesCount) {
		//compute LP of the articulation: if within staff, it must be
		//between the staff lines (LP 1, 3, 5, ...)
		float lp = outerNote.getLinePosition() + 2 * side.getDir();
		if (lp >= 0 && lp <= (staffLinesCount - 1) * 2 && //within staff
			lp % 2 == 0) //on staff line
		{
			lp += side.getDir(); //move one LP further
		}
		//compute ArticulationsAlignment
		float height = 1; //1 IS
		return new ArticulationsAlignment(new ArticulationAlignment[] { new ArticulationAlignment(lp,
			outerNote.getOffset(), articulation) }, height);
	}

	/**
	 * Creates an {@link ArticulationsAlignment} for the given {@link ArticulationType}s
	 * at the given notehead at the given side. The articulations are always placed
	 * outside the staff lines. The first one is placed as the innermost articulation,
	 * the last one as the outermost one.
	 */
	ArticulationsAlignment computeOtherArticulations(List<Articulation> articulations,
		NoteAlignment outerNote, VSide side, int staffLinesCount) {
		//compute LP of the first articulation:
		//if within staff, it must be moved outside
		float lp = outerNote.getLinePosition() + 2 * side.getDir();
		if (lp >= 0 && lp <= (staffLinesCount - 1) * 2) //within staff
		{
			lp = (side == VSide.Top ? (staffLinesCount - 1) * 2 + 1 : -1);
		}
		//collect ArticulationAlignments
		ArticulationAlignment[] aa = new ArticulationAlignment[articulations.size()];
		for (int i = 0; i < articulations.size(); i++) {
			aa[i] = new ArticulationAlignment(lp + 2 * i * side.getDir(), outerNote.getOffset(),
				articulations.get(i).getType());
		}
		//total height: 1 IS for each articulation
		float totalHeightIS = Math.abs(lp - outerNote.getLinePosition()) / 2;
		//create ArticulationsAlignment
		return new ArticulationsAlignment(aa, totalHeightIS);
	}

}
