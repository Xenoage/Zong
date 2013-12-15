package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.text.UnformattedText.ut;
import static com.xenoage.zong.io.musicxml.in.readers.MusicReader.readDuration;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readBezierPoint;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPositioning;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readVSide;

import java.util.List;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Articulation;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musicxml.types.MxlArticulations;
import com.xenoage.zong.musicxml.types.MxlBeam;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlLyric;
import com.xenoage.zong.musicxml.types.MxlNotations;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlStem;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlArticulationsContent.MxlArticulationsContentType;
import com.xenoage.zong.musicxml.types.choice.MxlCueNote;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent.MxlFullNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlGraceNote;
import com.xenoage.zong.musicxml.types.choice.MxlLyricContent.MxlLyricContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent.MxlNotationsContentType;
import com.xenoage.zong.musicxml.types.choice.MxlNoteContent.MxlNoteContentType;
import com.xenoage.zong.musicxml.types.enums.MxlStartStopContinue;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;

/**
 * This class reads a {@link Chord} from a given list
 * of note elements.
 * 
 * @author Andreas Wenger
 */
public final class ChordReader {

	/**
	 * Reads the given chord, consisting of the given list of note elements,
	 * including slurs and ties.
	 * All but the first given note must have a chord-element inside.
	 */
	public static MusicReaderContext readChord(MusicReaderContext context, List<MxlNote> mxlNotes) {
		MxlNote mxlFirstNote = mxlNotes.get(0);
		MxlNoteContentType mxlFirstNoteType = mxlFirstNote.getContent().getNoteContentType();

		//type of chord/rest
		//(unpitched is still unsupported)
		MxlFullNote mxlFirstFullNote = mxlFirstNote.getContent().getFullNote();
		MxlNormalNote mxlFirstNormalNote = null;
		MxlCueNote mxlFirstCueNote = null;
		MxlGraceNote mxlFirstGraceNote = null;
		boolean cue = false;
		if (mxlFirstNoteType == MxlNoteContentType.Normal) {
			mxlFirstNormalNote = (MxlNormalNote) mxlNotes.get(0).getContent();
		}
		else if (mxlFirstNoteType == MxlNoteContentType.Cue) {
			mxlFirstCueNote = (MxlCueNote) mxlNotes.get(0).getContent();
			cue = true;
		}
		else if (mxlFirstNoteType == MxlNoteContentType.Grace) {
			mxlFirstGraceNote = (MxlGraceNote) mxlNotes.get(0).getContent();
			cue = false; //may also be true later, see (TODO) "Zong-Library/Discussions/MusicXML/Note - cue vs grace.txt"
		}
		MxlFullNoteContentType mxlFNCType = mxlFirstFullNote.getContent().getFullNoteContentType();

		//duration. here, the first duration is the duration of the whole chord.
		Fraction duration = _0;
		if (mxlFirstNormalNote != null) {
			duration = readDuration(context, mxlFirstNormalNote.getDuration());
		}
		else if (mxlFirstCueNote != null) {
			duration = readDuration(context, mxlFirstCueNote.getDuration());
		}

		//staff
		//(not supported yet: multi-staff chords)
		int staff = notNull(mxlFirstNote.getStaff(), 1) - 1;

		//voice
		//TODO: might not exist! we have to use a helper algorithm to determine the right voice
		//then, see MusicReader class documentation
		int staffVoice = 0;
		MxlEditorialVoice editorialVoice = mxlFirstNote.getEditorialVoice();
		if (editorialVoice != null) {
			String mxlVoice = editorialVoice.getVoice();
			if (mxlVoice != null) {
				Tuple2<MusicReaderContext, Integer> tVoice = context.getVoice(staff, mxlVoice);
				context = tVoice.get1();
				staffVoice = tVoice.get2();
			}
		}

		//create new chord or rest
		Chord chord = null;
		if (mxlFNCType == MxlFullNoteContentType.Pitch) {
			//create a chord
			Pitch pitch = ((MxlPitch) mxlFirstFullNote.getContent()).getPitch();
			Grace grace = null;
			if (mxlFirstGraceNote != null) {
				//read grace duration from note-type ("eighth", "16th", ...)
				Fraction graceDuration = fr(1, 8);
				if (mxlFirstNote.getNoteType() != null)
					graceDuration = mxlFirstNote.getNoteType().getDuration();
				grace = new Grace(notNull(mxlFirstGraceNote.getSlash(), true), graceDuration);
				chord = new Chord(alist(new Note(pitch)), grace);
			}
			else {
				chord = new Chord(alist(new Note(pitch)), duration);
			}
			chord.setCue(cue);
			context = context.writeVoiceElement(chord, staff, staffVoice);
			//collect the following notes of this chord
			for (int i = 1; i < mxlNotes.size(); i++) {
				Tuple2<MusicReaderContext, Chord> tChord = plusChordNote(context, mxlNotes.get(i), chord,
					staff);
				context = tChord.get1();
				chord = tChord.get2();
			}
		}
		else if (mxlFNCType == MxlFullNoteContentType.Rest) {
			//write a rest
			context = context.writeVoiceElement(new Rest(duration, cue), staff, staffVoice);
		}

		//more details for chord
		if (chord != null) {
			//check if chord could be written. if not, return
			if (context.getScore().globals.getIMP(chord) == null)
				return context;

			//stem
			Stem stem = readStem(context, mxlFirstNote, chord, staff);
			if (stem != null) {
				Chord oldChord = chord;
				chord = oldChord.withStem(stem);
				context = context.replaceChord(oldChord, chord);
			}

			//add beams
			//TODO: also read beams for grace and cue chords
			if (mxlFirstGraceNote == null && !cue) {
				//currently we read only the beam elements with number 1
				for (MxlBeam mxlBeam : mxlFirstNote.beams) {
					int number = mxlBeam.getNumber();
					//read only level 1 beams
					if (number != 1)
						continue;
					switch (mxlBeam.getValue()) {
						case Begin: {
							//open new beam
							context = context.openBeam(number);
							context = context.addBeamChord(chord, number);
							break;
						}
						case Continue: {
							//add chord to beam
							context = context.addBeamChord(chord, number);
							break;
						}
						case End: {
							//close the beam and create it
							context = context.addBeamChord(chord, number);
							Tuple2<MusicReaderContext, PVector<Chord>> t = context.closeBeam(number);
							if (t.get2() != null) {
								context = t.get1();
								context = context.writeBeam(t.get2());
							}
						}
					}
				}
			}

			//notations   
			for (MxlNotations mxlNotations : mxlFirstNote.notations) {
				Tuple2<MusicReaderContext, Chord> t = readNotations(context, mxlNotations, chord, 0, staff); //first note has index 0
				context = t.get1();
				chord = t.get2();
			}

			//lyric
			//not supported yet: in MusicXML also rests can have lyrics. see mesaure 36 in Echigo-Jishi
			for (MxlLyric mxlLyric : mxlFirstNote.lyrics) {
				//not supported yet: number which are not integer and
				//name instead (?) of number attribute
				String number = mxlLyric.number;
				int verse = 0;
				if (number != null) {
					Integer i = Parser.parseIntegerNull(number);
					if (i != null)
						verse = i - 1;
				}
				Lyric.SyllableType syllableType = SyllableType.Single;
				MxlLyricContentType mxlLCType = mxlLyric.getContent().getLyricContentType();
				if (mxlLCType == MxlLyricContentType.SyllabicText) {
					MxlSyllabicText mxlSyllabicText = (MxlSyllabicText) mxlLyric.getContent();
					//a syllable
					switch (mxlSyllabicText.getSyllabic()) {
						case Begin:
							syllableType = SyllableType.Begin;
							break;
						case Middle:
							syllableType = SyllableType.Middle;
							break;
						case End:
							syllableType = SyllableType.End;
							break;
					}
					//the next element must be the text element
					context = context.writeAttachment(chord, new Lyric(ut(mxlSyllabicText.getText()
						.getValue()), syllableType, verse));
				}
				else if (mxlLCType == MxlLyricContentType.Extend) {
					//extend - TODO: extension to next chord!
					context = context.writeAttachment(chord, Lyric.lyricExtend(verse));
				}
			}

		}

		context = context.moveCursorForward(duration);
		return context;
	}

	/**
	 * Reads the given note element, which is part of
	 * a chord (but not the first note element of the chord), and adds it to the given chord.
	 * Also the notations of this note are read.
	 */
	private static Tuple2<MusicReaderContext, Chord> plusChordNote(MusicReaderContext context,
		MxlNote mxlNote, Chord chord, int staffIndexInPart) {
		//only pitch is interesting for us, since we do not allow
		//different durations for notes within a chord or other strange stuff
		if (mxlNote.getContent().getNoteContentType() == MxlNoteContentType.Normal) {
			MxlFullNoteContent mxlFNC = ((MxlNormalNote) mxlNote.getContent()).getFullNote().getContent();
			if (mxlFNC.getFullNoteContentType() == MxlFullNoteContentType.Pitch) {
				Pitch pitch = ((MxlPitch) mxlFNC).getPitch();
				Note note = new Note(pitch);
				Chord oldChord = chord;
				chord = chord.plusNote(note);
				context = context.replaceChord(oldChord, chord);
				//notations. we are only interested in the first element.
				for (MxlNotations notations : mxlNote.notations) {
					Tuple2<MusicReaderContext, Chord> t = readNotations(context, notations, chord,
						chord.notes.indexOf(note), staffIndexInPart);
					context = t.get1();
					chord = t.get2();
				}
			}
		}
		return t(context, chord);
	}

	/**
	 * Reads the slurs, ties and dynamics belonging to the given chord and note.
	 * The beginnings and endings of the curved lines are saved in the context.
	 * The updated context and chord are returned.
	 */
	private static Tuple2<MusicReaderContext, Chord> readNotations(MusicReaderContext context,
		MxlNotations mxlNotations, Chord chord, int noteIndex, int staffIndexInPart) {
		for (MxlNotationsContent mxlNC : mxlNotations.getElements()) {
			MxlNotationsContentType mxlNCType = mxlNC.getNotationsContentType();

			switch (mxlNCType) {
				case CurvedLine: {
					//slur or tie
					MxlCurvedLine mxlCurvedLine = (MxlCurvedLine) mxlNC;
					Pitch pitch = chord.notes.get(noteIndex).getPitch();
					float noteLP = context.getMusicContext(staffIndexInPart).computeLinePosition(pitch);

					//type
					CurvedLine.Type type;
					if (mxlCurvedLine.getElementType() == MxlElementType.Slur)
						type = CurvedLine.Type.Slur;
					else
						type = CurvedLine.Type.Tie;

					//number (tied does usually not use number, but distinguishes by pitch)
					Integer number = mxlCurvedLine.getNumber();
					BezierPoint bezierPoint = readBezierPoint(mxlCurvedLine.getPosition(),
						mxlCurvedLine.getBezier(), context.getTenthMm(),
						context.getStaffLinesCount(staffIndexInPart), noteLP, chord.duration);
					VSide side = readVSide(mxlCurvedLine.getPlacement());

					//waypoint
					CurvedLineWaypoint wp = new CurvedLineWaypoint(chord, noteIndex, bezierPoint);
					if (type == CurvedLine.Type.Tie && number == null) {
						//unnumbered tied
						if (mxlCurvedLine.getType() == MxlStartStopContinue.Start)
							context = context.openUnnumberedTied(pitch, wp, side);
						else
							context = context.closeUnnumberedTied(pitch, wp, side);
					}
					else {
						//numbered curved line
						CurvedLineWaypoint.Type wpType;
						if (mxlCurvedLine.getType() == MxlStartStopContinue.Start)
							wpType = CurvedLineWaypoint.Type.Start;
						else if (mxlCurvedLine.getType() == MxlStartStopContinue.Stop)
							wpType = CurvedLineWaypoint.Type.Stop;
						else
							wpType = CurvedLineWaypoint.Type.Continue;
						context = context.registerCurvedLine(type, wpType, number, wp, side);
					}
					break;
				}

				case Dynamics: {
					//dynamics
					MxlDynamics mxlDynamics = (MxlDynamics) mxlNC;
					DynamicsType type = mxlDynamics.getElement();
					Positioning positioning = readPositioning(mxlDynamics.getPrintStyle().getPosition(),
						mxlDynamics.getPlacement(), null, context.getTenthMm(),
						context.getStaffLinesCount(staffIndexInPart));
					context = context.withScore(plusAttachment(context.getScore(), chord, new Dynamics(type,
						positioning)));
					break;
				}

				case Articulations: {
					//articulations
					MxlArticulations mxlArticulations = (MxlArticulations) mxlNC;
					PVector<Articulation> articulations = pvec();
					for (MxlArticulationsContent mxlAC : mxlArticulations.getContent()) {
						MxlArticulationsContentType mxlACType = mxlAC.getArticulationsContentType();
						Articulation.Type at = null;
						switch (mxlACType) {
							case Accent:
								at = Articulation.Type.Accent;
								break;
							case Staccatissimo:
								at = Articulation.Type.Staccatissimo;
								break;
							case Staccato:
								at = Articulation.Type.Staccato;
								break;
							case StrongAccent:
								at = Articulation.Type.StrongAccent;
								break;
							case Tenuto:
								at = Articulation.Type.Tenuto;
								break;
						}
						if (at != null) {
							articulations = articulations.plus(new Articulation(at));
						}
					}
					if (articulations.size() > 0) {
						Chord oldChord = chord;
						chord = chord.withArticulations(articulations);
						context = context.replaceChord(oldChord, chord);
					}
					break;
				}
			}
		}
		return t(context, chord);
	}

	/**
	 * Reads and returns the stem of the given chord.
	 * If not available, null is returned.
	 * @param context   the global context
	 * @param xmlNote   the note element, that contains the interesting
	 *                  stem element. if not, null is returned.
	 * @param chord     the chord, whose notes are already collected
	 * @param staff     the staff index of the current chord
	 */
	private static Stem readStem(MusicReaderContext context, MxlNote mxlNote, Chord chord, int staff) {
		Stem ret = null;
		MxlStem mxlStem = mxlNote.stem;
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
			if (mxlStem.getYPosition().getDefaultY() != null) {
				//convert position in tenths relative to topmost staff line into
				//a length in interline spaces measured from the outermost chord note on stem side
				float stemEndLinePosition = convertDefaultYToLinePosition(context, mxlStem.getYPosition()
					.getDefaultY(), staff);
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
	private static float convertDefaultYToLinePosition(MusicReaderContext context, float defaultY,
		int staff) {
		Score score = context.getScore();
		float defaultYInMm = defaultY * score.format.interlineSpace / 10;
		float interlineSpace = score.getInterlineSpace(context.getPartStavesIndices().getStart() +
			staff);
		int linesCount = context.getStaffLinesCount(staff);
		return 2 * (linesCount - 1) + 2 * defaultYInMm / interlineSpace;
	}

	/**
	 * Gets the line position of the note which is nearest to the given line position,
	 * using the musical context from the given staff.
	 */
	private static float getNoteLinePosition(MusicReaderContext context, Chord chord, float nearTo,
		int staff) {
		MusicContext mc = context.getMusicContext(staff);
		Vector<Pitch> pitches = chord.getPitches();
		//if there is just one note, it's easy
		if (pitches.size() == 1) {
			return mc.computeLinePosition(pitches.getFirst());
		}
		//otherwise, test for the topmost and bottommost note
		else {
			float top = mc.computeLinePosition(pitches.getLast());
			float bottom = mc.computeLinePosition(pitches.getFirst());
			return (Math.abs(top - nearTo) < Math.abs(bottom - nearTo) ? top : bottom);
		}
	}

}
