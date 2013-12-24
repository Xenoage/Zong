package com.xenoage.zong.core.music.chord;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.DirectionContainer;
import com.xenoage.zong.core.music.lyric.Lyric;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.tuplet.Tuplet;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.core.util.InconsistentScoreException;

/**
 * Class for a chord.
 * 
 * To make things easy, every note is in a chord.
 * Thus also single notes are chord elements by definition.
 * 
 * A chord can have a stem and articulations.
 * It may be a normal chord (default case), a cue chord (printed small,
 * but has a duration like normal chords) or a grace chord, which duration
 * is 0 (the grace duration is saved in the {@link Grace} class).
 * 
 * A chord can be part of a tuplet, a beam, one or more slurs and
 * and one or more lyrics and directions can be attached to it.
 *
 * @author Andreas Wenger
 */
public class Chord
	implements VoiceElement, DirectionContainer {

	/** The notes within this chord, sorted ascending (begin with lowest notated pitch). */
	@Getter @Setter @NonEmpty private List<Note> notes;
	/** The duration of this chord. */
	@Getter @Setter @NonNull private Fraction duration;
	/** The stem of this chord, or null if a default stem is used. */
	@Getter @Setter @MaybeNull private Stem stem = null;
	/** True, if this chord has cue size, otherwise false. */
	@Getter @Setter private boolean cue = false;
	/** The grace value of this chord, or null if it is a normal chord. */
	@Getter @Setter @MaybeNull private Grace grace = null;
	/** The articulations within this chord, sorted by ascending distance to the chord. */
	@Getter @Setter @MaybeNull private List<Articulation> articulations = null;
	/** The beam this chord is part of, or null. */
	@Getter @Setter @MaybeNull private Beam beam = null;
	/** The slurs which start or end at this chord, or null. */
	@Getter @Setter @MaybeNull private List<Slur> slurs = null;
	/** The tuplet this chord is part of, or null. */
	@Getter @Setter @MaybeNull private Tuplet tuplet = null;
	/** The lyrics attached to this chord, or null. */
	@Getter @Setter @MaybeNull private List<Lyric> lyrics = null;
	/** The directions attached to this chord, or null. */
	@Getter @Setter @MaybeNull private List<Direction> directions = null;

	/** Back reference: the parent voice, or null if not part of a score. */
	@Getter @Setter private Voice parent = null;


	/**
	 * Creates a chord with the given note and duration.
	 */
	public Chord(Note note, Fraction duration) {
		checkArgsNotNull(note, duration);
		this.notes = new ArrayList<Note>(1);
		this.notes.add(note);
		this.duration = duration;
	}

	/**
	 * Creates a chord with the given notes and duration.
	 * The pitches must be sorted ascending (begin with the lowest notated pitch,
	 * end with the highest notated pitch), otherwise an {@link IllegalArgumentException} is thrown.
	 */
	public Chord(ArrayList<Note> notes, Fraction duration) {
		checkArgsNotNull(notes, duration);
		checkNotesOrder(notes);
		if (false == duration.isGreater0())
			throw new InconsistentScoreException("Only grace chords may not have 0 duration");
		this.notes = notes;
		this.duration = duration;
	}
	
	/**
	 * Creates a grace chord with the given notes.
	 * The pitches must be sorted ascending (begin with the lowest notated pitch,
	 * end with the highest notated pitch), otherwise an {@link IllegalArgumentException} is thrown.
	 */
	public Chord(ArrayList<Note> notes, Grace grace) {
		checkArgsNotNull(notes, grace);
		checkNotesOrder(notes);
		if (false == grace.getGraceDuration().isGreater0())
			throw new InconsistentScoreException("Grace duration must be greater than 0");
		this.notes = notes;
		this.duration = _0;
		this.grace = grace;
	}

	/**
	 * Collects and returns all pitches of this chord.
	 * Pitches that are used more times are also used more
	 * times in the list.
	 */
	public List<Pitch> getPitches() {
		List<Pitch> ret = alist(notes.size());
		for (int i : range(notes)) {
			ret.add(notes.get(i).getPitch());
		}
		return ret;
	}

	/**
	 * Adds a pitch this chord.
	 */
	public void addPitch(Pitch pitch) {
		addNote(new Note(pitch));
	}

	/**
	 * Adds a note this chord.
	 */
	public void addNote(Note note) {
		//insert at right position
		int i = 0;
		for (; i < notes.size(); i++) {
			if (notes.get(i).getPitch().compareToNotation(note.getPitch()) > 0)
				break;
		}
		notes.add(i, note);
	}

	private void checkNotesOrder(ArrayList<Note> notes) {
		Pitch currentPitch = null;
		Pitch lastPitch = notes.get(0).getPitch();
		for (int i : range(1, notes.size() - 1)) {
			currentPitch = notes.get(i).getPitch();
			//pitches must be sorted ascending
			if (currentPitch.compareToNotation(lastPitch) < 0)
				throw new IllegalArgumentException("Pitches must be sorted ascending (notation order)!");
			lastPitch = currentPitch;
		}
	}

	/**
	 * Gets the displayed duration of this chord. For full chords, this method returns the
	 * same value as {@link #getDuration()}. For grace notes, the value of
	 * {@link Grace#getGraceDuration()} is returned.
	 */
	public Fraction getDisplayedDuration() {
		return (grace == null ? duration : grace.getGraceDuration());
	}

	/**
	 * Returns true, if this chord is a grace chord.
	 */
	public boolean isGrace() {
		return grace != null;
	}
	
	public void addDirection(Direction direction) {
		if (directions == null)
			directions = new ArrayList<Direction>();
		directions.add(direction);
	}

	@Override public String toString() {
		return "chord(" + notes.get(0).toString() + (notes.size() > 1 ? ",..." : "") +
			(duration.isGreater0() ? ";dur:" + duration : ";grace") + ")";
	}

	@Override public MP getMP(MPElement child) {
		//all children have the same musical position as this chord
		return MP.getMP(this);
	}

}
