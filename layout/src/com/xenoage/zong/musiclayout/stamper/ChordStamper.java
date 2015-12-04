package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.musiclayout.stamper.LegerLinesStamper.legerLinesStamper;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalDisplacement;
import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.ArticulationDisplacement;
import com.xenoage.zong.musiclayout.notation.chord.ArticulationsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notation.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.AccidentalStamping;
import com.xenoage.zong.musiclayout.stampings.ArticulationStamping;
import com.xenoage.zong.musiclayout.stampings.FlagsStamping;
import com.xenoage.zong.musiclayout.stampings.LegerLineStamping;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.ProlongationDotStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Creates stampings for chords.
 * 
 * Beams, ties, slurs and lyrics are not handled here,
 * those must be handled by other classes.
 * 
 * @author Andreas Wenger
 */
public class ChordStamper {
	
	public static final ChordStamper chordStamper = new ChordStamper();
	
	
	public ChordStampings create(ChordNotation chordNotation, float chordXMm, StaffStamping staffStamping,
		SymbolPool symbolPool, LayoutSettings layoutSettings) {
		Chord chord = chordNotation.element;
		boolean grace = chord.isGrace();
		float scaling = (grace ? layoutSettings.scalingGrace : 1);
		ChordWidths chordWidths = (grace ? layoutSettings.graceChordWidths : layoutSettings.chordWidths);

		FlagsStamping flags = null;

		float leftNoteXMm = getLeftNoteXMm(chordXMm,
			chordNotation.notes, staffStamping.is);
		
		//stem
		StemStamping stem = stampStem(chordNotation, leftNoteXMm, staffStamping);
		
		//type of notehead
		int noteheadType = NoteheadStamping.NOTEHEAD_WHOLE;
		DurationInfo.Type symbolType = DurationInfo.getNoteheadSymbolType(chord.getDisplayedDuration());
		if (symbolType == DurationInfo.Type.Half)
			noteheadType = NoteheadStamping.NOTEHEAD_HALF;
		else if (symbolType == DurationInfo.Type.Quarter)
			noteheadType = NoteheadStamping.NOTEHEAD_QUARTER;

		//noteheads
		NotesNotation nas = chordNotation.notes;
		NoteheadStamping[] noteheads = new NoteheadStamping[nas.getNotesCount()];
		for (int iNote : range(noteheads)) {
			NoteDisplacement note = nas.getNote(iNote);
			Color color = Color.black;
			Symbol noteSymbol = getNoteheadSymbol(noteheadType, symbolPool);
			float noteXMm = getNoteheadXMm(leftNoteXMm + note.xIs * staffStamping.is,
				scaling, staffStamping, noteSymbol);
			NoteheadStamping noteSt = new NoteheadStamping(chordNotation,	iNote, noteSymbol,
				color, staffStamping, sp(noteXMm, note.lp), scaling);
			noteheads[iNote] = noteSt;
		}

		//flags (only drawn if there is no beam)
		int flagsCount = DurationInfo.getFlagsCount(chord.getDisplayedDuration());
		Beam beam = chord.getBeam();
		StemDirection stemDir = chordNotation.stemDirection;
		if (beam == null && flagsCount > 0) {
			FlagsStamping.FlagsDirection flag = (stemDir == StemDirection.Up ? FlagsStamping.FlagsDirection.Down
				: FlagsStamping.FlagsDirection.Up);
			Symbol flagSymbol = symbolPool.getSymbol(CommonSymbol.NoteFlag);
			flags = new FlagsStamping(chordNotation, staffStamping, flag, flagsCount, flagSymbol, scaling,
				sp(leftNoteXMm + nas.stemOffsetIs * staffStamping.is, chordNotation.stem.endLp));
		}

		//accidentals
		AccidentalsNotation accs = chordNotation.accidentals;
		AccidentalStamping[] accsSt = new AccidentalStamping[0];
		if (accs != null) {
			accsSt = new AccidentalStamping[accs.accidentals.length];
			for (int iAcc : range(accsSt)) {
				AccidentalDisplacement acc = accs.accidentals[iAcc];
				AccidentalStamping accSt = new AccidentalStamping(chordNotation, iAcc,
					staffStamping, sp(chordXMm +
						(acc.xIs - chordNotation.width.frontGap + 0.5f /* 0.5f: half accidental width - TODO */) *
						staffStamping.is, acc.yLp), 1, symbolPool.getSymbol(CommonSymbol.getAccidental(acc.accidental)));
				accsSt[iAcc] = accSt;
			}
		}

		//dots
		int[] dotPositions = nas.dotsLp;
		int dotsPerNote = nas.getDotsPerNoteCount();
		ProlongationDotStamping[] dots = new ProlongationDotStamping[dotPositions.length * dotsPerNote];
		Symbol dotSymbol = symbolPool.getSymbol(CommonSymbol.NoteDot);
		for (int iNote : range(dotPositions)) {
			for (int iDot : range(dotsPerNote)) {
				ProlongationDotStamping dotSt = new ProlongationDotStamping(chordNotation, staffStamping,
					dotSymbol, sp(leftNoteXMm + nas.getDotsOffsetIs(iDot) * staffStamping.is, dotPositions[iNote]));
				dots[iNote * dotsPerNote + iDot] = dotSt;
			}
		}

		//articulations
		ArticulationsNotation arts = chordNotation.articulations;
		ArticulationStamping[] artsSt = new ArticulationStamping[0];
		if (arts != null) {
			artsSt = new ArticulationStamping[arts.articulations.length];
			float noteheadWidth = chordWidths.get(chord.getDuration());
			for (int iArt : range(artsSt)) {
				ArticulationDisplacement art = arts.articulations[iArt];
				ArticulationStamping artSt = new ArticulationStamping(chordNotation, iArt,
					staffStamping, sp(leftNoteXMm + (art.xIs + (noteheadWidth / 2)) * staffStamping.is, art.yLp),
					1, symbolPool.getSymbol(CommonSymbol.getArticulation(art.articulation)));
				artsSt[iArt] = artSt;
			}
		}
		
		//leger lines
		LegerLineStamping[] legerLines = legerLinesStamper.stamp(chordNotation, chordXMm, staffStamping);

		return new ChordStampings(chord, chordXMm, staffStamping, noteheads,
			dots, accsSt, legerLines, artsSt, flags, stem);
	}
	
	float getLeftNoteXMm(float chordXMm, NotesNotation notesAlignment, float staffIs) {
		//left-suspended chord? then move chord to the left by the width of a notehead
		float leftNoteXMm = chordXMm;
		if (notesAlignment.leftSuspended)
			leftNoteXMm -= notesAlignment.noteheadWidthIs * staffIs;
		return leftNoteXMm;
	}
	
	StemStamping stampStem(ChordNotation chordNotation, float leftNoteXMm, StaffStamping staffStamping) {
		float stemXMm = leftNoteXMm + chordNotation.notes.stemOffsetIs * staffStamping.is;
		StemNotation sa = chordNotation.stem;
		return new StemStamping(chordNotation, stemXMm,
			sa.startLp, sa.endLp, chordNotation.stemDirection, staffStamping);
	}
	
	Symbol getNoteheadSymbol(int notehead, SymbolPool symbolPool) {
		if (notehead == NoteheadStamping.NOTEHEAD_WHOLE)
			return symbolPool.getSymbol(CommonSymbol.NoteWhole);
		else if (notehead == NoteheadStamping.NOTEHEAD_HALF)
			return symbolPool.getSymbol(CommonSymbol.NoteHalf);
		else
			return symbolPool.getSymbol(CommonSymbol.NoteQuarter);
	}
	
	//TIDY
	private float getNoteheadXMm(float xMm, float scaling, StaffStamping staff, Symbol symbol) {
		float ret = xMm;
		Rectangle2f bounds = symbol.getBoundingRect().scale(scaling);
		float interlineSpace = staff.is;
		float lineWidth = staff.getLineWidthMm();
		ret += (bounds.size.width / 2) * interlineSpace - lineWidth / 2;
		return ret;
	}

}