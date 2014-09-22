package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.core.text.UnformattedText.ut;
import static com.xenoage.zong.io.musicxml.in.readers.MusicReader.readDuration;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readBezierPoint;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPlacement;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readPositioning;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readVSide;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.Parser;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.commands.core.music.direction.DirectionAdd;
import com.xenoage.zong.commands.core.music.lyric.LyricAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.annotation.Annotation;
import com.xenoage.zong.core.music.annotation.Articulation;
import com.xenoage.zong.core.music.annotation.ArticulationType;
import com.xenoage.zong.core.music.annotation.Fermata;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.music.format.BezierPoint;
import com.xenoage.zong.core.music.format.Placement;
import com.xenoage.zong.core.music.format.Positioning;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.lyric.SyllableType;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.musicxml.types.MxlArticulations;
import com.xenoage.zong.musicxml.types.MxlBeam;
import com.xenoage.zong.musicxml.types.MxlDynamics;
import com.xenoage.zong.musicxml.types.MxlFermata;
import com.xenoage.zong.musicxml.types.MxlLyric;
import com.xenoage.zong.musicxml.types.MxlNotations;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.MxlSlurOrTied;
import com.xenoage.zong.musicxml.types.MxlSlurOrTied.MxlElementType;
import com.xenoage.zong.musicxml.types.MxlStem;
import com.xenoage.zong.musicxml.types.MxlSyllabicText;
import com.xenoage.zong.musicxml.types.attributes.MxlEmptyPlacement;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
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
import com.xenoage.zong.musicxml.types.enums.MxlUprightInverted;
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
	 * including beams, notations and lyrics.
	 * All but the first given note must have a chord-element inside.
	 */
	public static void readChord(MusicReaderContext context, List<MxlNote> mxlNotes) {
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
		//then, see MusicReader class documentation.
		int staffVoice = 0;
		MxlEditorialVoice editorialVoice = mxlFirstNote.getEditorialVoice();
		if (editorialVoice != null) {
			String mxlVoice = editorialVoice.getVoice();
			if (mxlVoice != null) {
				staffVoice = context.getVoice(staff, mxlVoice);
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
				grace = new Grace(notNull(mxlFirstGraceNote.getSlash(), false), graceDuration);
				chord = new Chord(alist(new Note(pitch)), grace);
			}
			else {
				chord = new Chord(alist(new Note(pitch)), duration);
			}
			chord.setCue(cue);
			context.writeVoiceElement(chord, staff, staffVoice);
			//collect the following notes of this chord
			for (int i = 1; i < mxlNotes.size(); i++) {
				addChordNote(context, mxlNotes.get(i), chord, staff);
			}
		}
		else if (mxlFNCType == MxlFullNoteContentType.Rest) {
			//write a rest
			Rest rest = new Rest(duration);
			rest.setCue(cue);
			context.writeVoiceElement(rest, staff, staffVoice);
		}

		//more details for chord
		if (chord != null) {
			//check if chord could be written. if not, return
			if (getMP(chord) == null)
				return;

			//stem
			Stem stem = readStem(context, mxlFirstNote, chord, staff);
			if (stem != null) {
				chord.setStem(stem);
			}

			//add beams
			//TODO: also read beams for grace and cue chords
			if (mxlFirstGraceNote == null && !cue) {
				//currently we read only the beam elements with number 1
				for (MxlBeam mxlBeam : it(mxlFirstNote.getBeams())) {
					int number = mxlBeam.getNumber();
					//read only level 1 beams
					if (number != 1)
						continue;
					switch (mxlBeam.getValue()) {
						case Begin: {
							//open new beam
							context.openBeam(number);
							context.addBeamChord(chord, number);
							break;
						}
						case ForwardHook:
						case BackwardHook:
						case Continue: {
							//add chord to beam
							context.addBeamChord(chord, number);
							break;
						}
						case End: {
							//close the beam and create it
							context.addBeamChord(chord, number);
							context.closeBeam(number);
						}
					}
				}
			}

			//notations of first note
			for (MxlNotations mxlNotations : it(mxlFirstNote.getNotations())) {
				readNotations(context, mxlNotations, chord, 0, staff); //first note has index 0
			}

			//lyric
			//not supported yet: in MusicXML also rests can have lyrics. see measure 36 in Echigo-Jishi
			for (MxlLyric mxlLyric : it(mxlFirstNote.getLyrics())) {
				//not supported yet: number which are not integer and
				//name instead (?) of number attribute
				String number = mxlLyric.getNumber();
				int verse = 0;
				if (number != null) {
					Integer i = Parser.parseIntegerNull(number);
					if (i != null)
						verse = i - 1;
				}
				SyllableType syllableType = null;
				MxlLyricContentType mxlLCType = mxlLyric.getContent().getLyricContentType();
				if (mxlLCType == MxlLyricContentType.SyllabicText) {
					MxlSyllabicText mxlSyllabicText = (MxlSyllabicText) mxlLyric.getContent();
					//a syllable
					switch (mxlSyllabicText.getSyllabic()) {
						case Single:
							syllableType = SyllableType.Single;
							break;
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
					new LyricAdd(new Lyric(ut(mxlSyllabicText.getText().getValue()), syllableType, verse),
						chord).execute();
				}
				else if (mxlLCType == MxlLyricContentType.Extend) {
					//extend - TODO: extension to next chord!
					new LyricAdd(Lyric.lyricExtend(verse), chord).execute();
				}
			}

		}

		context.moveCursorForward(duration);
	}

	/**
	 * Reads the given note element, which is part of
	 * a chord (but not the first note element of the chord), and adds it to the given chord.
	 * Also the notations of this note are read.
	 */
	private static void addChordNote(MusicReaderContext context,
		MxlNote mxlNote, Chord chord, int staffIndexInPart) {
		//only pitch is interesting for us, since we do not allow
		//different durations for notes within a chord or other strange stuff
		MxlFullNoteContent mxlFNC = mxlNote.getContent().getFullNote().getContent();
		if (mxlFNC.getFullNoteContentType() == MxlFullNoteContentType.Pitch) {
			Pitch pitch = ((MxlPitch) mxlFNC).getPitch();
			Note note = new Note(pitch);
			chord.addNote(note);
			//notations. we are only interested in the first element.
			if (mxlNote.getNotations() != null) {
				for (MxlNotations notations : mxlNote.getNotations()) {
					readNotations(context, notations, chord,
						chord.getNotes().indexOf(note), staffIndexInPart);
				}
			}
		}
	}

	/**
	 * Reads the slurs, ties and dynamics belonging to the given chord and note.
	 * The beginnings and endings of the slurs are saved in the context.
	 */
	private static void readNotations(MusicReaderContext context,
		MxlNotations mxlNotations, Chord chord, int noteIndex, int staffIndexInPart) {
		
		ArrayList<Annotation> annotations = alist(0);
		for (MxlNotationsContent mxlNC : mxlNotations.getElements()) {
			MxlNotationsContentType mxlNCType = mxlNC.getNotationsContentType();

			switch (mxlNCType) {
				case SlurOrTied: {
					//slur or tied
					MxlSlurOrTied mxlSlur = (MxlSlurOrTied) mxlNC;
					Pitch pitch = chord.getNotes().get(noteIndex).getPitch();
					float noteLP = context.getMusicContext(staffIndexInPart).getLp(pitch);

					//type
					SlurType type;
					if (mxlSlur.getElementType() == MxlElementType.Slur)
						type = SlurType.Slur;
					else
						type = SlurType.Tie;

					//number (tied does usually not use number, but distinguishes by pitch)
					Integer number = mxlSlur.getNumber();
					BezierPoint bezierPoint = readBezierPoint(mxlSlur.getPosition(),
						mxlSlur.getBezier(), context.getTenthMm(),
						context.getStaffLinesCount(staffIndexInPart), noteLP, chord.getDuration());
					VSide side = readVSide(mxlSlur.getPlacement());

					//waypoint
					SlurWaypoint wp = new SlurWaypoint(chord, noteIndex, bezierPoint);
					if (type == SlurType.Tie && number == null) {
						//unnumbered tied
						if (mxlSlur.getType() == MxlStartStopContinue.Start)
							context.openUnnumberedTied(pitch, wp, side);
						else
							context.closeUnnumberedTied(pitch, wp, side);
					}
					else {
						//numbered curved line
						WaypointPosition wpPos;
						if (mxlSlur.getType() == MxlStartStopContinue.Start)
							wpPos = WaypointPosition.Start;
						else if (mxlSlur.getType() == MxlStartStopContinue.Stop)
							wpPos = WaypointPosition.Stop;
						else
							wpPos = WaypointPosition.Continue;
						context.registerSlur(type, wpPos, number, wp, side);
					}
					break;
				}

				case Dynamics: {
					//dynamics
					MxlDynamics mxlDynamics = (MxlDynamics) mxlNC;
					DynamicsType type = mxlDynamics.getElement();
					MxlPrintStyle printStyle = mxlDynamics.getPrintStyle();
					MxlPosition position = (printStyle != null ? printStyle.getPosition() : null);
					Positioning positioning = readPositioning(position,
						mxlDynamics.getPlacement(), null, context.getTenthMm(),
						context.getStaffLinesCount(staffIndexInPart));
					Dynamics dynamics = new Dynamics(type);
					dynamics.setPositioning(positioning);
					new DirectionAdd(dynamics, chord).execute();
					break;
				}

				case Articulations: {
					//articulations
					MxlArticulations mxlArticulations = (MxlArticulations) mxlNC;
					for (MxlArticulationsContent mxlAC : mxlArticulations.getContent()) {
						MxlArticulationsContentType mxlACType = mxlAC.getArticulationsContentType();
						//read type
						ArticulationType a = null;
						switch (mxlACType) {
							case Accent:
								a = ArticulationType.Accent;
								break;
							case Staccatissimo:
								a = ArticulationType.Staccatissimo;
								break;
							case Staccato:
								a = ArticulationType.Staccato;
								break;
							case StrongAccent:
								a = ArticulationType.Marcato;
								break;
							case Tenuto:
								a = ArticulationType.Tenuto;
								break;
						}
						//read placement
						Placement placement = null;
						MxlEmptyPlacement mxlPlacement = mxlAC.getEmptyPlacement();
						if (mxlPlacement != null) {
							placement = readPlacement(mxlPlacement.getPlacement());
						}
						if (a != null) {
							annotations.add(new Articulation(a, placement));
						}
					}
					break;
				}
				
				case Fermata: {
					//fermata
					MxlFermata mxlFermata = (MxlFermata) mxlNC;
					//determine position
					MxlPrintStyle printStyle = mxlFermata.getPrintStyle();
					MxlPosition position = (printStyle != null ? printStyle.getPosition() : null);
					Positioning positioning = readPositioning(position,
						null, null, context.getTenthMm(), context.getStaffLinesCount(staffIndexInPart));
					if (positioning == null) {
						if (mxlFermata.getType() == MxlUprightInverted.Upright)
							positioning = Placement.Above;
						else if (mxlFermata.getType() == MxlUprightInverted.Inverted)
							positioning = Placement.Below;
					}
					annotations.add(new Fermata(positioning));
					break;
				}
			}
		}
		
		if (annotations.size() > 0)
			chord.setAnnotations(annotations);
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
			MxlPosition yPos = mxlStem.getYPosition();
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
	private static float convertDefaultYToLinePosition(MusicReaderContext context, float defaultY,
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
	private static float getNoteLinePosition(MusicReaderContext context, Chord chord, float nearTo,
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
