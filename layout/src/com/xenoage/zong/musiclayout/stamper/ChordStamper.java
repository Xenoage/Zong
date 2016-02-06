package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.LyricStamper.lyricStamper;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.SlurStamper.slurStamper;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.beamStamper;
import static com.xenoage.zong.musiclayout.stamper.DirectionStamper.directionStamper;
import static com.xenoage.zong.musiclayout.stamper.LegerLinesStamper.legerLinesStamper;

import java.util.List;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.VSide;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.layouter.cache.OpenLyricsCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenSlursCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenTupletsCache;
import com.xenoage.zong.musiclayout.layouter.cache.util.SlurCache;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.LastLyrics;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
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
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.stampings.AccidentalStamping;
import com.xenoage.zong.musiclayout.stampings.ArticulationStamping;
import com.xenoage.zong.musiclayout.stampings.FlagsStamping;
import com.xenoage.zong.musiclayout.stampings.LegerLineStamping;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;
import com.xenoage.zong.musiclayout.stampings.ProlongationDotStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StaffTextStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;

/**
 * Creates the {@link Stamping}s for a chord.
 * 
 * @author Andreas Wenger
 */
public class ChordStamper {
	
	public static final ChordStamper chordStamper = new ChordStamper();
	
	
	/**
	 * Returns all the stampings for the given {@link Chord}, including beams,
	 * tuplets, slurs and other attachments.
	 * 
	 * The given {@link OpenBeamsCache}, {@link OpenSlursCache},
	 * {@link OpenLyricsCache}, {@link LastLyrics} and {@link OpenTupletsCache} may be modified.
	 */
	public List<Stamping> stampAll(ChordNotation chord, float xMm,
		BeamSpacing beam, StaffStampings staffStampings,
		StamperContext context, FormattedTextStyle defaultLyricStyle,
		OpenSlursCache openCurvedLinesCache,
		OpenLyricsCache openLyricsCache, LastLyrics lastLyrics, OpenTupletsCache openTupletsCache) {
		
		List<Stamping> ret = alist();
		Chord element = chord.getElement();
		int staffIndex = context.getMp().getStaff();
		int systemIndex = context.systemIndex;

		//noteheads, leger lines, dots, accidentals, stem, flags, articulations
		ChordStampings chordSt = stampCore(chord, xMm, context);
		chordSt.addAllTo(ret);

		//beam  
		if (beam != null) {
			//stamp the whole beam (when we find the end of the beam)
			//TIDY: create/store beam stampings elsewhere?
			Beam beamElement = beam.notation.element;
			int chordIndex = beamElement.getWaypointIndex(element);
			if (chordIndex == beamElement.size() - 1) {
				StaffStamping leftStaff = staffStampings.get(context.systemIndex,
					beamElement.getStart().getChord().getMP().staff);
				StaffStamping rightStaff = staffStampings.get(context.systemIndex,
					beamElement.getStop().getChord().getMP().staff);
				ret.addAll(beamStamper.stamp(beam, leftStaff, rightStaff));
			}
			
		}

		//ties and slurs - GOON NOTATOR
		for (Slur slur : element.getSlurs()) {
			SlurWaypoint wp = slur.getWaypoint(element);
			WaypointPosition pos = slur.getWaypointPosition(element);
			int noteIndex = notNull(wp.getNoteIndex(), 0); //TODO: choose top/bottom
			NoteheadStamping notehead = chordSt.noteheads[noteIndex];
			//define the placement: above or below (TODO: better strategy)
			VSide side = VSide.Top;
			if (pos == WaypointPosition.Start) {
				if (slur.getSide() != null) {
					//use custom side
					side = slur.getSide();
				}
				else {
					//use default side:
					//for all notes over line position 3, use above, else below
					side = (notehead.position.lp > 3 ? VSide.Top : VSide.Bottom);
				}
			}
			//compute position
			if (pos == WaypointPosition.Start) {
				//create start information
				float distance = slurStamper.computeAdditionalDistance(chord, side);
				SlurCache tiedChords = SlurCache.createNew(slur, side, staffIndex, notehead, distance,
					systemIndex);
				openCurvedLinesCache.add(tiedChords);
			}
			else {
				//create stop information
				SlurCache tiedChords = openCurvedLinesCache.get(slur);
				if (tiedChords == null) {
					//start notehead of this tie is unknown (must be from some preceding frame), which was forgotten
					//ignore it. TODO: warning
				}
				else {
					float distance = slurStamper.computeAdditionalDistance(chord,
						tiedChords.getSide());
					tiedChords.setStop(notehead, distance, systemIndex);
				}
			}
		}

		//lyric
		List<Lyric> lyrics = element.getLyrics();
		if (lyrics.size() > 0) {
			float baseLine = -10;

			for (Lyric lyric : lyrics) {
				if (lyric != null) {
					SyllableType lyricType = lyric.getSyllableType();
					StaffTextStamping lastLyric = lastLyrics.get(staffIndex, lyric.getVerse());

					if (lyricType == SyllableType.Extend) {
						//extend
						if (lastLyric != null) //TODO: frame breaks...
						{
							//remember it
							openLyricsCache.setUnderscore((Lyric) lastLyric.getElement(), lastLyric,
								chordSt.noteheads[0]/* TODO*/, staffIndex);
						}
					}
					else {
						//normal lyric

						//create text stamping
						StaffTextStamping sts = lyricStamper.createSyllableStamping(lyric,
							defaultLyricStyle, context.staff, chordSt.noteheads[0]/* TODO*/.position.xMm,
							baseLine);
						ret.add(sts);

						//when middle or end syllable, add a hypen between the preceding syllable and this syllable
						if (lastLyric != null) //TODO: frame breaks...
						{
							if (lyricType == SyllableType.Middle || lyricType == SyllableType.End) {
								StaffTextStamping hyphenStamping = lyricStamper.createHyphenStamping(
									lastLyric, sts, defaultLyricStyle);
								ret.add(hyphenStamping);
							}
						}

						//remember this lyric as the currently last one in the current staff and verse
						lastLyrics.set(staffIndex, lyric.getVerse(), sts);

					}
				}

				baseLine += -5;
			}
		}

		//directions
		ret.addAll(directionStamper.stampForChord(chordSt, context.layouter.symbols));

		//tuplet
		Tuplet tuplet = element.getTuplet();
		if (tuplet != null) {
			openTupletsCache.addChord(element, tuplet, chordSt);
		}

		return ret;
	}
	
	/**
	 * Draws the given chord, including noteheads, stem, flags, accidentals, dots,
	 * articulations and leger lines.
	 */
	public ChordStampings stampCore(ChordNotation chord, float chordXMm, StamperContext context) {
		
		Chord element = chord.element;
		boolean grace = element.isGrace();
		LayoutSettings settings = context.getSettings();
		float scaling = (grace ? settings.scalingGrace : 1);
		ChordWidths chordWidths = (grace ? settings.graceChordWidths : settings.chordWidths);

		//GOON: move into ChordSpacing
		float leftNoteXMm = getLeftNoteXMm(chordXMm, chord.notes, context.staff.is);
		
		//stem
		StemStamping stem = stampStem(chord, leftNoteXMm, context.staff);
		
		//type of notehead
		CommonSymbol noteheadSymbol = CommonSymbol.NoteWhole;
		DurationInfo.Type symbolType = DurationInfo.getNoteheadSymbolType(element.getDisplayedDuration());
		if (symbolType == DurationInfo.Type.Half)
			noteheadSymbol = CommonSymbol.NoteHalf;
		else if (symbolType == DurationInfo.Type.Quarter)
			noteheadSymbol = CommonSymbol.NoteQuarter;

		//noteheads
		NotesNotation notes = chord.notes;
		NoteheadStamping[] noteheads = new NoteheadStamping[notes.getNotesCount()];
		for (int iNote : range(noteheads)) {
			NoteDisplacement note = notes.getNote(iNote);
			Symbol noteSymbol = context.getSymbol(noteheadSymbol);
			float noteXMm = getNoteheadXMm(leftNoteXMm + note.xIs * context.staff.is,
				scaling, context.staff, noteSymbol);
			NoteheadStamping noteSt = new NoteheadStamping(chord,	iNote, noteSymbol,
				Color.black, context.staff, sp(noteXMm, note.lp), scaling);
			noteheads[iNote] = noteSt;
		}

		//flags (only drawn if there is no beam)
		int flagsCount = DurationInfo.getFlagsCount(element.getDisplayedDuration());
		Beam beam = element.getBeam();
		StemDirection stemDir = chord.stemDirection;
		FlagsStamping flags = null;
		if (beam == null && flagsCount > 0 && chord.stem != null /* can happen when no stem is used */) {
			FlagsStamping.FlagsDirection flag = (stemDir == StemDirection.Up ?
				FlagsStamping.FlagsDirection.Down : FlagsStamping.FlagsDirection.Up);
			Symbol flagSymbol = context.getSymbol(CommonSymbol.NoteFlag);
			flags = new FlagsStamping(chord, context.staff, flag, flagsCount, flagSymbol, scaling,
				sp(leftNoteXMm + notes.stemOffsetIs * context.staff.is, chord.stem.endLp));
		}

		//accidentals
		AccidentalsNotation accs = chord.accidentals;
		AccidentalStamping[] accsSt = new AccidentalStamping[0];
		if (accs != null) {
			accsSt = new AccidentalStamping[accs.accidentals.length];
			for (int iAcc : range(accsSt)) {
				AccidentalDisplacement acc = accs.accidentals[iAcc];
				AccidentalStamping accSt = new AccidentalStamping(chord, iAcc,
					context.staff, sp(chordXMm +
						(acc.xIs - chord.width.frontGap + 0.5f /* 0.5f: half accidental width - TODO */) *
						context.staff.is, acc.yLp), 1, context.getSymbol(CommonSymbol.getAccidental(acc.accidental)));
				accsSt[iAcc] = accSt;
			}
		}

		//dots
		int[] dotPositions = notes.dotsLp;
		int dotsPerNote = notes.getDotsPerNoteCount();
		ProlongationDotStamping[] dots = new ProlongationDotStamping[dotPositions.length * dotsPerNote];
		Symbol dotSymbol = context.getSymbol(CommonSymbol.NoteDot);
		for (int iNote : range(dotPositions)) {
			for (int iDot : range(dotsPerNote)) {
				ProlongationDotStamping dotSt = new ProlongationDotStamping(chord, context.staff,
					dotSymbol, sp(leftNoteXMm + notes.getDotsOffsetIs(iDot) * context.staff.is, dotPositions[iNote]));
				dots[iNote * dotsPerNote + iDot] = dotSt;
			}
		}

		//articulations
		ArticulationsNotation arts = chord.articulations;
		ArticulationStamping[] artsSt = new ArticulationStamping[0];
		if (arts != null) {
			artsSt = new ArticulationStamping[arts.articulations.length];
			float noteheadWidth = chordWidths.get(element.getDuration());
			for (int iArt : range(artsSt)) {
				ArticulationDisplacement art = arts.articulations[iArt];
				ArticulationStamping artSt = new ArticulationStamping(chord, iArt,
					context.staff, sp(leftNoteXMm + (art.xIs + (noteheadWidth / 2)) * context.staff.is, art.yLp),
					1, context.getSymbol(CommonSymbol.getArticulation(art.articulation)));
				artsSt[iArt] = artSt;
			}
		}
		
		//leger lines
		LegerLineStamping[] legerLines = legerLinesStamper.stamp(chord, chordXMm, context.staff);

		return new ChordStampings(element, chordXMm, context.staff, noteheads,
			dots, accsSt, legerLines, artsSt, flags, stem);
	}
	
	float getLeftNoteXMm(float chordXMm, NotesNotation notesAlignment, float staffIs) {
		//left-suspended chord? then move chord to the left by the width of a notehead
		float leftNoteXMm = chordXMm;
		if (notesAlignment.leftSuspended)
			leftNoteXMm -= notesAlignment.noteheadWidthIs * staffIs;
		return leftNoteXMm;
	}
	
	StemStamping stampStem(ChordNotation chordNotation, float leftNoteXMm,
		StaffStamping staffStamping) {
		StemNotation stem = chordNotation.stem;
		if (stem == null)
			return null;
		float stemXMm = leftNoteXMm + chordNotation.notes.stemOffsetIs * staffStamping.is;
		return new StemStamping(chordNotation, stemXMm,
			stem.startLp, stem.endLp, chordNotation.stemDirection, staffStamping);
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
