package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.VSide;
import com.xenoage.utils.math.geom.Rectangle2f;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.core.music.util.Duration;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.musiclayout.layouter.cache.OpenLyricsCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenSlursCache;
import com.xenoage.zong.musiclayout.layouter.cache.OpenTupletsCache;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.ChordStampings;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.LastLyrics;
import com.xenoage.zong.musiclayout.layouter.scoreframelayout.util.StaffStampings;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.chord.*;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.stampings.*;
import com.xenoage.zong.symbols.Symbol;
import com.xenoage.zong.symbols.common.CommonSymbol;
import lombok.val;

import java.util.List;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.LyricStamper.lyricStamper;
import static com.xenoage.zong.musiclayout.stamper.SlurStamper.slurStamper;
import static com.xenoage.zong.musiclayout.stamper.BeamStamper.beamStamper;
import static com.xenoage.zong.musiclayout.stamper.DirectionStamper.directionStamper;
import static com.xenoage.zong.musiclayout.stamper.LegerLinesStamper.legerLinesStamper;

/**
 * Creates the {@link Stamping}s for a chord.
 * 
 * TODO (ZONG-91): clean up by using a ChordSpacer/ChordSpacing
 * 
 * @author Andreas Wenger
 */
public class ChordStamper {
	
	public static final ChordStamper chordStamper = new ChordStamper();
	
	
	/**
	 * Returns all the stampings for the given {@link Chord}, including beams,
	 * tuplets, slurs and other attachments.
	 * 
	 * The given {@link OpenSlursCache},
	 * {@link OpenLyricsCache}, {@link LastLyrics} and {@link OpenTupletsCache} may be modified.
	 */
	public List<Stamping> stampAll(ChordNotation chord, float xMm,
		BeamSpacing beam, StaffStampings staffStampings,
		StamperContext context, FormattedTextStyle defaultLyricStyle,
		OpenSlursCache openSlursCache,
		OpenLyricsCache openLyricsCache, LastLyrics lastLyrics, OpenTupletsCache openTupletsCache) {
		
		List<Stamping> ret = alist();
		Chord element = chord.getElement();
		int staffIndex = context.staffIndex;
		int systemIndex = context.systemIndex;

		//noteheads, leger lines, dots, accidentals, stem, flags, articulations
		ChordStampings chordSt = stampCore(chord, xMm, context);
		chordSt.addAllTo(ret);

		//beam  
		if (beam != null) {
			//stamp the whole beam (when we find the beginning of the beam)
			//TIDY: create/store beam stampings elsewhere?
			Beam beamElement = beam.notation.element;
			int chordIndex = beamElement.getWaypointIndex(element);
			if (chordIndex == 0) {
				ret.addAll(beamStamper.stamp(beam, context.getCurrentStaffStamping()));
			}
			
		}

		//ties and slurs
		for (Slur slur : element.getSlurs()) {
			SlurWaypoint wp = slur.getWaypoint(element);
			WaypointPosition pos = slur.getWaypointPosition(element);
			int noteIndex = notNull(wp.getNoteIndex(), 0); //TODO: choose top/bottom
			NoteheadStamping notehead = chordSt.noteheads[noteIndex];
			//define the placement: above or below (TODO: better strategy)
			VSide side = slurStamper.getSide(slur);
			//compute position
			val staff = staffStampings.get(systemIndex, notehead.parentStaff.staffIndex);
			val slurCache = openSlursCache.getOrCreate(slur);
			float distanceIs = slurStamper.getAdditionalDistanceIs(chord, slur.getSide());
			SP defaultSp = sp(notehead.position.xMm, notehead.position.lp + side.getDir() * distanceIs * 2);
			if (pos == WaypointPosition.Start)
				slurCache.setStart(defaultSp, staff, systemIndex);
			else
				slurCache.setStop(defaultSp, staff, systemIndex);
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
							defaultLyricStyle, context.getCurrentStaffStamping(), chordSt.noteheads[0]/* TODO*/.position.xMm,
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
		val staff = context.getCurrentStaffStamping();
		
		Chord element = chord.element;
		boolean grace = element.isGrace();
		LayoutSettings settings = context.getSettings();
		float scaling = (grace ? settings.scalingGrace : 1);
		ChordWidths chordWidths = (grace ? settings.graceChordWidths : settings.chordWidths);

		float leftNoteXMm = getLeftNoteXMm(chordXMm, chord.notes, staff.is);
		
		//stem
		StemStamping stem = stampStem(chord, leftNoteXMm, context);
		
		//type of notehead
		CommonSymbol noteheadSymbol = CommonSymbol.NoteWhole;
		Duration.Type symbolType = Duration.INSTANCE.getNoteheadSymbolType(element.getDisplayedDuration());
		if (symbolType == Duration.INSTANCE.Type.Half)
			noteheadSymbol = CommonSymbol.NoteHalf;
		else if (symbolType == Duration.INSTANCE.Type.Quarter)
			noteheadSymbol = CommonSymbol.NoteQuarter;

		//noteheads
		NotesNotation notes = chord.notes;
		NoteheadStamping[] noteheads = new NoteheadStamping[notes.getNotesCount()];
		for (int iNote : range(noteheads)) {
			NoteDisplacement note = notes.getNote(iNote);
			Symbol noteSymbol = context.getSymbol(noteheadSymbol);
			float noteXMm = getNoteheadXMm(leftNoteXMm + note.xIs * staff.is,
				scaling, staff, noteSymbol);
			NoteheadStamping noteSt = new NoteheadStamping(chord,	iNote, noteSymbol,
				Color.black, staff, sp(noteXMm, note.lp), scaling);
			noteheads[iNote] = noteSt;
		}

		//flags (only drawn if there is no beam)
		int flagsCount = Duration.INSTANCE.getFlagsCount(element.getDisplayedDuration());
		Beam beam = element.getBeam();
		StemDirection stemDir = chord.stemDirection;
		FlagsStamping flags = null;
		if (beam == null && flagsCount > 0 && chord.stem != null /* can happen when no stem is used */) {
			FlagsStamping.FlagsDirection flag = (stemDir == StemDirection.Up ?
				FlagsStamping.FlagsDirection.Down : FlagsStamping.FlagsDirection.Up);
			Symbol flagSymbol = context.getSymbol(CommonSymbol.NoteFlag);
			flags = new FlagsStamping(chord, staff, flag, flagsCount, flagSymbol, scaling,
				sp(leftNoteXMm + notes.stemOffsetIs * staff.is, chord.stem.endSlp.lp));
		}

		//accidentals
		AccidentalsNotation accs = chord.accidentals;
		AccidentalStamping[] accsSt = new AccidentalStamping[0];
		if (accs != null) {
			accsSt = new AccidentalStamping[accs.accidentals.length];
			for (int iAcc : range(accsSt)) {
				AccidentalDisplacement acc = accs.accidentals[iAcc];
				AccidentalStamping accSt = new AccidentalStamping(chord, iAcc,
					staff, sp(chordXMm +
						(acc.xIs - chord.width.frontGap + 0.5f /* 0.5f: half accidental width - TODO */) *
						staff.is, acc.yLp), 1, context.getSymbol(CommonSymbol.getAccidental(acc.accidental)));
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
				ProlongationDotStamping dotSt = new ProlongationDotStamping(chord, staff,
					dotSymbol, sp(leftNoteXMm + notes.getDotsOffsetIs(iDot) * staff.is, dotPositions[iNote]));
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
					staff, sp(leftNoteXMm + (art.xIs + (noteheadWidth / 2)) * staff.is, art.yLp),
					1, context.getSymbol(CommonSymbol.getArticulation(art.articulation)));
				artsSt[iArt] = artSt;
			}
		}
		
		//leger lines
		LegerLineStamping[] legerLines = legerLinesStamper.stamp(chord, chordXMm, staff);

		return new ChordStampings(element, chordXMm, staff, noteheads,
			dots, accsSt, legerLines, artsSt, flags, stem);
	}
	
	float getLeftNoteXMm(float chordXMm, NotesNotation notesAlignment, float staffIs) {
		//left-suspended chord? then move chord to the left by the width of a notehead
		float leftNoteXMm = chordXMm;
		if (notesAlignment.leftSuspended)
			leftNoteXMm -= notesAlignment.noteheadWidthIs * staffIs;
		return leftNoteXMm;
	}
	
	StemStamping stampStem(ChordNotation chordNotation, float leftNoteXMm, StamperContext context) {
		StemNotation stem = chordNotation.stem;
		if (stem == null)
			return null;
		val noteStaff = context.getStaffStamping(stem.startSlp.staff);
		val endStaff = context.getStaffStamping(stem.endSlp.staff);
		float stemXMm = leftNoteXMm + chordNotation.notes.stemOffsetIs * noteStaff.is;
		return new StemStamping(chordNotation, stemXMm,
			stem.startSlp.lp, noteStaff, stem.endSlp.lp, endStaff, chordNotation.stemDirection);
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
