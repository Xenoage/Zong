package com.xenoage.zong.musiclayout.notator.chord;

import static com.xenoage.zong.core.music.annotation.ArticulationType.Staccato;
import static com.xenoage.zong.core.music.annotation.ArticulationType.Tenuto;

import java.util.List;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.chord.ArticulationDisplacement;
import com.xenoage.zong.musiclayout.notation.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notation.chord.NotesNotation;

/**
 * Computes the notations of the articulations of a given chord.
 * 
 * @author Andreas Wenger
 */
public class ArticulationsNotator {
	
	public static final ArticulationsNotator articulationsNotator = new ArticulationsNotator();

	
	/**
	 * Computes the notations of the articulations of the given chord.
	 * If there are no articulations, {@link ArticulationsNotation#empty} is returned.
	 */
	public ArticulationsNotation compute(Chord chord,
		StemDirection stemDirection, NotesNotation notesAlignment, int linesCount) {
		//depending on the stem direction, place the articulation on the other side.
		//if there is no stem direction, always place at the top
		VSide side = (stemDirection == StemDirection.Up ? VSide.Bottom : VSide.Top);
		//dependent on the side of the articulation, take the top or bottom note
		NoteDisplacement outerNote = (side == VSide.Top ? notesAlignment.getTopNote()
			: notesAlignment.getBottomNote());
		//compute alignment of articulations
		return compute(chord.getArticulations(), outerNote, side, linesCount);
	}

	ArticulationsNotation compute(List<Articulation> articulations,
		NoteDisplacement outerNote, VSide side, int staffLinesCount) {
		if (articulations.size() == 0)
			return ArticulationsNotation.empty;
		//special cases (which appear often): if there is only a single articulation
		//which is either a staccato or tenuto, we can place it between the staff lines
		if (articulations.size() == 1 &&
			(articulations.get(0).getType() == Staccato ||
			articulations.get(0).getType() == Tenuto)) {
			return computeSimpleArticulation(articulations.get(0).getType(), outerNote, side, staffLinesCount);
		}
		//otherwise, the articulations a placed above or below the staff
		else {
			return computeOtherArticulations(articulations, outerNote, side, staffLinesCount);
		}
	}

	/**
	 * Creates an {@link ArticulationsNotation} for the given {@link ArticulationType}
	 * at the given notehead at the given side. If possible, it is placed between
	 * the staff lines.
	 */
	ArticulationsNotation computeSimpleArticulation(ArticulationType articulation,
		NoteDisplacement outerNote, VSide side, int staffLinesCount) {
		//compute LP of the articulation: if within staff, it must be
		//between the staff lines (LP 1, 3, 5, ...)
		int lp = outerNote.lp + 2 * side.getDir();
		if (lp >= 0 && lp <= (staffLinesCount - 1) * 2 && //within staff
			lp % 2 == 0) { //on staff line
			lp += side.getDir(); //move one LP further
		}
		//compute ArticulationsAlignment
		float height = 1; //1 IS
		ArticulationDisplacement[] arts = { new ArticulationDisplacement(lp, outerNote.xIs, articulation) };
		return new ArticulationsNotation(arts, height);
	}

	/**
	 * Creates an {@link ArticulationsNotation} for the given {@link ArticulationType}s
	 * at the given notehead at the given side. The articulations are always placed
	 * outside the staff lines. The first one is placed as the innermost articulation,
	 * the last one as the outermost one.
	 */
	ArticulationsNotation computeOtherArticulations(List<Articulation> articulations,
		NoteDisplacement outerNote, VSide side, int staffLinesCount) {
		//compute LP of the first articulation:
		//if within staff, it must be moved outside
		int lp = outerNote.lp + 2 * side.getDir();
		if (lp >= 0 && lp <= (staffLinesCount - 1) * 2) //within staff
			lp = (side == VSide.Top ? (staffLinesCount - 1) * 2 + 1 : -1);
		//collect displacements
		ArticulationDisplacement[] arts = new ArticulationDisplacement[articulations.size()];
		for (int i = 0; i < articulations.size(); i++) {
			arts[i] = new ArticulationDisplacement(lp + 2 * i * side.getDir(), outerNote.xIs,
				articulations.get(i).getType());
		}
		//total height: 1 IS for each articulation
		float heightIS = Math.abs(lp - outerNote.lp) / 2;
		//create ArticulationsAlignment
		return new ArticulationsNotation(arts, heightIS);
	}

}
