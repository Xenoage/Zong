package com.xenoage.zong.io.selection;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.music.beam.Beam.beam;
import static com.xenoage.zong.core.position.MP.mp;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.StaffAdd;
import com.xenoage.zong.commands.core.music.VoiceAdd;
import com.xenoage.zong.commands.core.music.VoiceElementWrite;
import com.xenoage.zong.commands.core.music.slur.SlurAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.util.MPE;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.score.ScoreInputOptions;
import com.xenoage.zong.io.score.ScoreInputOptions.WriteMode;
import com.xenoage.zong.utils.exceptions.IllegalMPException;
import com.xenoage.zong.utils.exceptions.MeasureFullException;


/**
 * Cursor for a score.
 * 
 * This is the most often used selection, since it is also useful for
 * sequential input like needed when reading from a file.
 * The actions can not be undone. If undo is needed, execute commands
 * on the {@link CommandPerformer} from the {@link Score} class.
 * 
 * It contains the current position within the score and provides many methods
 * to write or remove elements and move the cursor.
 * 
 * If the {@link #moving} flag is set, the cursor jumps to the end of written elements
 * instead of staying at its old position. Then it can be at a {@link MP} which
 * still does not exist (e.g. at the end of the score), which isn't a problem
 * since it is created as soon as needed.
 * 
 * There is also the possibility to open and close beams and curved lines.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public final class Cursor
	implements ScoreSelection {

	/** The score which will be modified. */
	@Getter private final Score score;
	/** The current position of the cursor. It contains both an element index and a beat. */
	private MP mp;
	/** True, when the cursor is moved when the write method is executed. */
	@Getter @Setter private boolean moving;
	/** True, when empty space should be filled with invisible rests. Defaults to false. */
	@Getter @Setter private boolean fillWithHiddenRests = false;


	private ArrayList<BeamWaypoint> openBeamWaypoints = null;
	private ArrayList<SlurWaypoint> openSlurWaypoints = null;
	private SlurType openSlurType = null;


	/**
	 * Creates a new {@link Cursor}.
	 * @param score   the score to work on
	 * @param pos     the musical position of the cursor, which may still not exist
	 *                in the score (the measure and voice will be created on demand)
	 * @param moving  move with input?
	 */
	public Cursor(Score score, MP mp, boolean moving) {
		this.score = score;
		this.mp = mp;
		if (this.mp.beat == MP.unknownBeat)
			this.mp = this.mp.getWithBeat(score);
		this.moving = moving;
	}
	
	
	/**
	 * The current position of the cursor. It contains both an element index and a beat.
	 */
	@Override public MP getMP() {
		return mp;
	}


	/**
	 * Sets the position of the cursor. The {@link MP#element} field must be set.
	 */
	public void setMP(MP mp) {
		if (mp.element == MP.unknown)
			throw new IllegalArgumentException("unknown element");
		if (mp.beat == MP.unknownBeat) {
			//beat is unknown. compute it or use 0 for nonexisting MPs.
			if (score.isMPExisting(mp))
				mp = mp.getWithBeat(score);
			else
				mp = mp.withBeat(_0);
		}
		this.mp = mp;
	}


	/**
	 * Writes the given {@link ColumnElement} at the current position.
	 * Dependent on its type, it may replace elements of the same type.
	 */
	@Override public void write(ColumnElement element) {
		ensureMeasureExists(mp);
		new ColumnElementWrite(element, score.getColumnHeader(mp.measure), mp.beat, null).execute();
	}


	/**
	 * See {@link #write(ColumnElement)}.
	 */
	@Override public void write(ColumnElement element, ScoreInputOptions options) {
		write(element);
	}


	/**
	 * Writes the given pitches as a chord. The position and overwrite mode
	 * depends on the given options.
	 */
	@Override public void write(Pitch[] pitches, ScoreInputOptions options)
		throws IllegalMPException, MeasureFullException {
		WriteMode wm = options.getWriteMode();
		if (wm == WriteMode.ChordBeforeCursor) {
			//add the given pitches to the chord before the cursor
			addPitchesToPrecedingChord(pitches);
		}
		else {
			//default behaviour: write chord after cursor
			Chord chord = new Chord(Note.notes(pitches), options.getDuration());
			write(chord, options);
		}
	}


	/**
	 * Adds the given pitches to the chord before the cursor. If there is no
	 * chord, nothing is done.
	 */
	private void addPitchesToPrecedingChord(Pitch[] pitches) {
		//find the last voice element starting before the current position
		MPE<VoiceElement> ive = score.getStaff(mp.staff).getVoiceElementBefore(mp, false);

		//if the target element is a chord, add the given pitches to it - TODO: use command
		if (ive != null && ive.element instanceof Chord) {
			Chord chord = (Chord) ive.element;
			for (Pitch pitch : pitches)
				chord.addNote(new Note(pitch));
		}
	}


	/**
	 * Writes the given {@link VoiceElement} at the current position and,
	 * if the moving flag is set, moves the cursor forward according to
	 * the duration of the element.
	 * 
	 * This method overwrites the elements overlapping the current cursor
	 * position (but not {@link NoVoiceElement}s like key or time signatures at
	 * the cursor position) and overlapping the current cursor position plus the
	 * duration of the given element.
	 * 
	 * Thus, if gaps appear before or after the written element, the corresponding
	 * elements are cut.
	 */
	@Override public void write(VoiceElement element)
		throws MeasureFullException {
		//create the voice, if needed
		ensureVoiceExists(mp);

		//create the options
		VoiceElementWrite.Options options = new VoiceElementWrite.Options();
		options.checkTimeSignature = true; //always obey to time signature
		options.fillWithHiddenRests = fillWithHiddenRests; //optionally, fill gaps with hidden rests
		
		//write the element
		new VoiceElementWrite(score.getVoice(mp), mp, element, options).execute();

		//if a beam is open and it is a chord, add it
		if (openBeamWaypoints != null && element instanceof Chord) {
			Chord chord = (Chord) element;
			openBeamWaypoints.add(new BeamWaypoint(chord, false));
		}

		//if move flag is set, move cursor forward, also over measure boundaries
		if (moving) {
			Fraction newBeat = mp.beat.add(element.getDuration());
			//if this beat is behind the end of the measure, jump into the next measure
			Fraction measureBeats = score.getHeader().getTimeAtOrBefore(mp.measure).getType().getMeasureBeats();
			if (measureBeats != null && newBeat.compareTo(measureBeats) >= 0) {
				//begin new measure
				mp = mp(mp.staff, mp.measure + 1, mp.voice, _0, 0);
			}
			else {
				//stay in the current measure
				mp = mp(mp.staff, mp.measure, mp.voice, newBeat, mp.element + 1);
			}
		}
	}


	/**
	 * See {@link #write(VoiceElement)}.
	 */
	@Override public void write(VoiceElement element, ScoreInputOptions options)
		throws MeasureFullException {
		write(element);
	}


	/**
	 * Writes the given {@link MeasureElement} at the current position.
	 * Dependent on its type, it may replace elements of the same type.
	 */
	@Override public void write(MeasureElement element) {
		//create the measure, if needed
		ensureMeasureExists(mp);
		//write the element
		new MeasureElementWrite(element, score.getMeasure(mp), mp.beat).execute();
	}


	/**
	 * See {@link #write(MeasureElement)}.
	 */
	@Override public void write(MeasureElement element, ScoreInputOptions options) {
		write(element);
	}


	/**
	 * Opens a beam. All following chords will be added to it.
	 */
	public void openBeam() {
		if (openBeamWaypoints != null)
			throw new IllegalStateException("Beam is already open");
		openBeamWaypoints = new ArrayList<BeamWaypoint>();
	}


	/**
	 * Closes a beam and adds it to the score.
	 */
	public void closeBeam() {
		if (openBeamWaypoints == null)
			throw new IllegalStateException("No beam is open");
		Beam beam = beam(openBeamWaypoints);
		for (BeamWaypoint wp : openBeamWaypoints)
			wp.getChord().setBeam(beam);
		openBeamWaypoints = null;
	}


	/**
	 * Opens a slur of the given type. All following chords will be added to it.
	 */
	public void openSlur(SlurType type) {
		if (openSlurWaypoints != null)
			throw new IllegalStateException("Slur is already open");
		checkArgsNotNull(type);
		openSlurWaypoints = new ArrayList<SlurWaypoint>();
		openSlurType = type;
	}


	/**
	 * Closes a slur and adds it to the score.
	 * @deprecated Does not work yet. Slur waypoints are not collected in this class.
	 */
	public void closeSlur() {
		if (openSlurWaypoints == null)
			throw new IllegalStateException("No curved line is open");
		Slur slur = new Slur(openSlurType, openSlurWaypoints, null);
		new SlurAdd(slur).execute();
		openSlurWaypoints = null;
	}
	
	
	/**
	 * Ensures, that the given given staff exists.
	 * If not, it is created.
	 */
	private void ensureStaffExists(MP mp) {
		//create staves if needed
		if (score.getStavesCount() <= mp.staff)
			new StaffAdd(score, mp.staff - score.getStavesCount() + 1).execute();
	}
	
	
	/**
	 * Ensures, that the given measure and staff exists.
	 * If not, it is created.
	 */
	private void ensureMeasureExists(MP mp) {
		//create staves if needed
		ensureStaffExists(mp);
		//create measures if needed
		if (score.getMeasuresCount() <= mp.measure)
			new MeasureAdd(score, mp.measure - score.getMeasuresCount() + 1).execute();
	}
	
	
	/**
	 * Ensures, that the given voice, measure and staff exists.
	 * If not, it is created.
	 */
	private void ensureVoiceExists(MP mp) {
		//create measure if needed
		ensureMeasureExists(mp);
		//create voice if needed
		Measure measure = score.getMeasure(mp);
		if (measure.getVoices().size() <= mp.voice)
			new VoiceAdd(measure, mp.voice).execute();
	}


}
