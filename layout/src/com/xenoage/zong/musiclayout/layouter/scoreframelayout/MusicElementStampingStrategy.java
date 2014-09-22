package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.format.SP.sp;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.color.Color;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.util.OpenBeamMiddleStem;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.notations.TimeNotation;
import com.xenoage.zong.musiclayout.notations.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalAlignment;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationAlignment;
import com.xenoage.zong.musiclayout.notations.chord.ArticulationsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NoteAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.stampings.AccidentalStamping;
import com.xenoage.zong.musiclayout.stampings.ArticulationStamping;
import com.xenoage.zong.musiclayout.stampings.ClefStamping;
import com.xenoage.zong.musiclayout.stampings.CommonTimeStamping;
import com.xenoage.zong.musiclayout.stampings.FlagsStamping;
import com.xenoage.zong.musiclayout.stampings.KeySignatureStamping;
import com.xenoage.zong.musiclayout.stampings.LegerLineStamping;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.ProlongationDotStamping;
import com.xenoage.zong.musiclayout.stampings.RestStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;
import com.xenoage.zong.musiclayout.stampings.NormalTimeStamping;
import com.xenoage.zong.symbols.SymbolPool;

/**
 * Strategy to create stampings for elements like chords, rests,
 * clefs, keys and time signatures.
 * 
 * @author Andreas Wenger
 */
public class MusicElementStampingStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Creates stampings for the given chord.
	 * Beams, ties, slurs and lyrics are not handled here,
	 * those must be handled by other strategies.
	 */
	public ChordStampings createChordStampings(ChordNotation chordNot, float positionX,
		StaffStamping staffStamping, SymbolPool symbolPool, LayoutSettings layoutSettings) {
		float interlineSpace = staffStamping.is;
		Chord chord = chordNot.getMusicElement();
		boolean grace = chord.isGrace();
		float scaling = (grace ? layoutSettings.scalingGrace : 1);
		ChordWidths chordWidths = (grace ? layoutSettings.graceChordWidths : layoutSettings.chordWidths);

		CList<NoteheadStamping> noteheads = clist();
		CList<LegerLineStamping> legerLines = clist();
		CList<ProlongationDotStamping> dots = clist();
		CList<AccidentalStamping> accidentals = clist();
		CList<ArticulationStamping> articulations = clist();
		FlagsStamping flags = null;
		StemStamping stem = null;
		OpenBeamMiddleStem openStem = null;

		//left-suspended chord? then move chord to the left by the width of a notehead
		float leftNoteX = positionX;
		NotesAlignment notesAlign = chordNot.getNotesAlignment();
		float leftSuspendedWidth = notesAlign.getLeftSuspendedWidth() * staffStamping.is;
		leftNoteX -= leftSuspendedWidth;

		//leger lines
		NotesAlignment cna = chordNot.getNotesAlignment();
		int topNoteLinePosition = cna.getNoteAlignment(cna.getNotesCount() - 1).getLinePosition();
		float topNoteOffset = cna.getNoteAlignment(cna.getNotesCount() - 1).getOffset();
		int bottomNoteLinePosition = cna.getNoteAlignment(0).getLinePosition();
		float bottomNoteOffset = cna.getNoteAlignment(0).getOffset();
		int staffLines = staffStamping.linesCount;
		if (topNoteLinePosition >= staffLines * 2) {
			//create leger lines above the staff
			for (int line = staffLines * 2; line <= topNoteLinePosition; line += 2) {
				LegerLineStamping legerLineSt = new LegerLineStamping(staffStamping, chord,
				//TODO: 0.5f = half notehead width
					leftNoteX + (topNoteOffset + 0.5f) * interlineSpace, line);
				legerLines.add(legerLineSt);
			}
		}
		if (bottomNoteLinePosition <= -2) {
			//create leger lines below the staff
			for (int line = -2; line >= bottomNoteLinePosition; line -= 2) {
				LegerLineStamping legerLineSt = new LegerLineStamping(staffStamping, chord,
				//TODO: 0.5f = half notehead width
					leftNoteX + (bottomNoteOffset + 0.5f) * interlineSpace, line);
				legerLines.add(legerLineSt);
			}
		}

		//stem
		float stemEndPos = 0;
		int flagsCount = DurationInfo.getFlagsCount(chord.getDisplayedDuration());
		StemDirection csd = chordNot.getStemDirection();
		if (csd != StemDirection.None) {
			//collect known information
			float stemX = leftNoteX + cna.getStemOffset() * interlineSpace;

			//stamp all stems except middle-beam stems
			Beam beam = chord.getBeam();
			boolean stampNow = false;
			if (beam == null) {
				stampNow = true;
			}
			else {
				if (beam.getStart().getChord() == chord ||
					beam.getStop().getChord() == chord) {
					stampNow = true;
				}
			}

			//stamp or remember
			if (stampNow) {
				//stamp it now
				StemAlignment csa = chordNot.getStemAlignment();
				float stemStartPos = csa.getStartLinePosition();
				stemEndPos = csa.getEndLinePosition();
				StemStamping stemSt = new StemStamping(staffStamping, chord, stemX, stemStartPos,
					stemEndPos, csd);
				stem = stemSt;
			}
			else {
				//for beamed chords in the middle of a beam only: we do not know the end position yet,
				//but remember it as an open stem
				openStem = new OpenBeamMiddleStem(staffStamping, chord, chordNot.getStemDirection(), stemX,
					cna.getBottomNoteAlignment().getLinePosition(), cna.getTopNoteAlignment()
						.getLinePosition());
			}
		}

		//type of notehead
		int noteheadType = NoteheadStamping.NOTEHEAD_WHOLE;
		DurationInfo.Type symbolType = DurationInfo.getNoteheadSymbolType(chord.getDisplayedDuration());
		if (symbolType == DurationInfo.Type.Half)
			noteheadType = NoteheadStamping.NOTEHEAD_HALF;
		else if (symbolType == DurationInfo.Type.Quarter)
			noteheadType = NoteheadStamping.NOTEHEAD_QUARTER;

		//noteheads
		for (int iNote : range(cna.getNotesCount())) {
			NoteAlignment na = cna.getNoteAlignment(iNote);

			Color color = Color.black;
			//TEST: draw chords that are not part of voice 0 in blue
			//color = (chord.getMusicElement().getVoice().getMeasure().getVoices().get(0) ==
			//	chord.getMusicElement().getVoice() ? Color.black : Color.blue);

			NoteheadStamping noteSt = new NoteheadStamping(chord, noteheadType, color, staffStamping, sp(
				leftNoteX + na.getOffset() * interlineSpace, na.getLinePosition()), scaling, symbolPool);
			noteheads.add(noteSt);
		}

		//flags (only drawn if there is no beam)
		Beam beam = chord.getBeam();
		if (beam == null && flagsCount > 0) {
			FlagsStamping.FlagsDirection flag = (csd == StemDirection.Up ? FlagsStamping.FlagsDirection.Down
				: FlagsStamping.FlagsDirection.Up);
			flags = new FlagsStamping(flag, flagsCount, scaling, staffStamping, chord, sp(
				leftNoteX + cna.getStemOffset() * interlineSpace, stemEndPos), symbolPool);
		}

		//accidentals
		AccidentalsAlignment caa = chordNot.getAccidentalsAlignment();
		if (caa != null) {
			for (int iAcc = 0; iAcc < caa.accidentals.length; iAcc++) {
				AccidentalAlignment aa = caa.accidentals[iAcc];
				AccidentalStamping accSt = new AccidentalStamping(chord, aa.type, staffStamping,
					sp(positionX +
							(aa.offset - chordNot.getWidth().getFrontGap() + 0.5f /* 0.5f: half accidental width - TODO */) *
							interlineSpace, aa.linePosition), 1, symbolPool);
				accidentals.add(accSt);
			}
		}

		//dots
		int[] dotPositions = cna.getDotLPs();
		int dotsPerNote = cna.getDotsPerNoteCount();
		for (int iNote : range(dotPositions)) {
			for (int iDot : range(dotsPerNote)) {
				ProlongationDotStamping dotSt = new ProlongationDotStamping(staffStamping, chord, sp(
					leftNoteX + cna.getDotsOffset(iDot) * interlineSpace, dotPositions[iNote]), symbolPool);
				dots.add(dotSt);
			}
		}

		//articulations
		ArticulationsAlignment cara = chordNot.getArticulationsAlignment();
		if (cara != null) {
			float noteheadWidth = chordWidths.get(chord.getDuration());
			for (int iArt = 0; iArt < cara.getArticulations().length; iArt++) {
				ArticulationAlignment ara = cara.getArticulations()[iArt];
				ArticulationStamping araSt = new ArticulationStamping(chordNot.getMusicElement(),
					ara.articulationType, staffStamping, sp(leftNoteX + (ara.xOffsetIS + (noteheadWidth / 2)) *
						interlineSpace, ara.yLP), 1, symbolPool);
				articulations.add(araSt);
			}
		}

		return new ChordStampings(positionX, staffStamping, noteheads.close(), legerLines.close(),
			dots.close(), accidentals.close(), articulations.close(), flags, stem, openStem);
	}

	/**
	 * Creates a stamping for the given rest.
	 */
	public RestStamping createRestStamping(RestNotation rest, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		return new RestStamping(rest.getMusicElement(), DurationInfo.getRestType(rest.getMusicElement()
			.getDuration()), staff, positionX, 1, symbolPool);
	}

	/**
	 * Creates a stamping for the given clef.
	 */
	public ClefStamping createClefStamping(ClefNotation clef, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		return new ClefStamping(clef.getMusicElement(), staff, positionX, clef.scaling, symbolPool);
	}

	/**
	 * Creates a stamping for the given key signature.
	 */
	public KeySignatureStamping createKeyStamping(TraditionalKeyNotation key, float positionX,
		StaffStamping staff, SymbolPool symbolPool, LayoutSettings layoutSettings) {
		return new KeySignatureStamping(key.getMusicElement(), key.linePositionC4,
			key.linePositionMin, positionX, staff, symbolPool, layoutSettings);
	}

	/**
	 * Creates a stamping for the given time signature.
	 */
	public Stamping createTimeStamping(TimeNotation time, float positionX, StaffStamping staff,
		SymbolPool symbolPool) {
		if (time.getMusicElement().getType() == TimeType.timeCommon) {
			return new CommonTimeStamping(time.getMusicElement(), positionX, staff, symbolPool);
		}
		else {
			return new NormalTimeStamping(time.getMusicElement(), positionX, staff, time.numeratorOffset,
				time.denominatorOffset, time.digitGap);
		}
	}

}
