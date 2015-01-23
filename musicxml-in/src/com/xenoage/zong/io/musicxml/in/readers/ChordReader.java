package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.io.musicxml.in.readers.OtherReader.readDuration;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musicxml.types.MxlBeam;
import com.xenoage.zong.musicxml.types.MxlNote;
import com.xenoage.zong.musicxml.types.MxlPitch;
import com.xenoage.zong.musicxml.types.choice.MxlCueNote;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent.MxlFullNoteContentType;
import com.xenoage.zong.musicxml.types.choice.MxlGraceNote;
import com.xenoage.zong.musicxml.types.choice.MxlNormalNote;
import com.xenoage.zong.musicxml.types.choice.MxlNoteContent.MxlNoteContentType;
import com.xenoage.zong.musicxml.types.enums.MxlYesNo;
import com.xenoage.zong.musicxml.types.groups.MxlEditorialVoice;
import com.xenoage.zong.musicxml.types.groups.MxlFullNote;

/**
 * This class reads a {@link Chord} from a given list
 * of note elements.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public final class ChordReader {

	private final List<MxlNote> mxlNotes;
	
	private Context context;
	private MxlNote mxlFirstNote;
	private VoiceElement chordOrRest;
	private Chord chord;
	private int staff;
	
	
	/**
	 * Reads the given chord, consisting of the given list of note elements,
	 * including beams, notations and lyrics.
	 * All but the first given note must have a chord-element inside.
	 */
	public void readToContext(Context context) {
		this.context = context;
		
		readFirstNote();
		
		//find staff
		//(not supported yet: multi-staff chords)
		staff = notNull(mxlFirstNote.getStaff(), 1) - 1;
			
		//find voice
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
		
		//write chord or rest
		if (chordOrRest != null)
			context.writeVoiceElement(chordOrRest, staff, staffVoice);
		
		//more details for chord
		if (chord != null) {
			//check if chord could be written. if not, return
			if (getMP(chord) == null)
				return;
			readFirstNoteNotations();
			readOtherChordNotes();
			readStem();
			readBeams();
			new LyricReader(mxlNotes).readToChord(chord);
		}
		
		if (chordOrRest != null)
			context.moveCursorForward(chordOrRest.getDuration());
	}
	
	private void readFirstNote() {
		mxlFirstNote = mxlNotes.get(0);
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
			duration = readDuration(mxlFirstNormalNote.getDuration(), context.getDivisions());
		}
		else if (mxlFirstCueNote != null) {
			duration = readDuration(mxlFirstCueNote.getDuration(), context.getDivisions());
		}

		//create new chord or rest
		if (mxlFNCType == MxlFullNoteContentType.Pitch) {
			//create a chord
			Pitch pitch = ((MxlPitch) mxlFirstFullNote.getContent()).getPitch();
			Grace grace = null;
			if (mxlFirstGraceNote != null) {
				//read grace duration from note-type ("eighth", "16th", ...)
				Fraction graceDuration = fr(1, 8);
				if (mxlFirstNote.getNoteType() != null)
					graceDuration = mxlFirstNote.getNoteType().getDuration();
				boolean slash = mxlFirstGraceNote.getSlash() == MxlYesNo.Yes;
				grace = new Grace(slash, graceDuration);
				chord = new Chord(alist(new Note(pitch)), grace);
			}
			else {
				chord = new Chord(alist(new Note(pitch)), duration);
			}
			chord.setCue(cue);
			chordOrRest = chord;
		}
		else if (mxlFNCType == MxlFullNoteContentType.Rest) {
			//create a rest
			Rest rest = new Rest(duration);
			rest.setCue(cue);
			chordOrRest = rest;
		}
	}
		
	private void readStem() {
		Stem stem = new StemReader(mxlFirstNote.getStem()).read(context, chord, staff);
		if (stem != null)
			chord.setStem(stem);
	}
		
	private void readBeams() {
		//add beams
		//TODO: also read beams for grace and cue chords
		if (false == (chord.isGrace() || chord.isCue())) {
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
						List<Chord> beamedChords = context.closeBeam(number);
						context.writeBeam(beamedChords);
					}
				}
			}
		}
	}
		
	private void readFirstNoteNotations() { //TIDY: merge with notations of other notes?
		if (mxlFirstNote.getNotations() != null) {
			new NotationsReader(mxlFirstNote.getNotations()).readToNote(
				chord, 0, staff, context); //first note has index 0
		}
	}
		
	private void readOtherChordNotes() {
		//collect the following notes of this chord
		for (int i = 1; i < mxlNotes.size(); i++) {
			addChordNote(context, mxlNotes.get(i), chord, staff);
		}
	}

	/**
	 * Reads the given note element, which is part of
	 * a chord (but not the first note element of the chord), and adds it to the given chord.
	 * Also the notations of this note are read.
	 */
	private static void addChordNote(Context context,
		MxlNote mxlNote, Chord chord, int staffIndexInPart) {
		//only pitch is interesting for us, since we do not allow
		//different durations for notes within a chord or other strange stuff
		MxlFullNoteContent mxlFNC = mxlNote.getContent().getFullNote().getContent();
		if (mxlFNC.getFullNoteContentType() == MxlFullNoteContentType.Pitch) {
			Pitch pitch = ((MxlPitch) mxlFNC).getPitch();
			Note note = new Note(pitch);
			chord.addNote(note);
			//notations
			if (mxlNote.getNotations() != null) {
				new NotationsReader(mxlNote.getNotations()).readToNote(chord,
					chord.getNotes().indexOf(note), staffIndexInPart, context);
			}
		}
	}

}
