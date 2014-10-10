package com.xenoage.zong.core.music;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.Voice.voice;
import static com.xenoage.zong.core.music.util.BeatEList.beatEList;
import static com.xenoage.zong.core.music.util.Interval.At;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.position.MP.atVoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.DirectionContainer;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPContainer;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.utils.exceptions.IllegalMPException;


/**
 * Measure within a single staff.
 * 
 * A measure consists of one or more voices and a
 * list of clefs, private keys (keys that only apply to
 * this staff), directions and instrument changes.
 *
 * @author Andreas Wenger
 */
public class Measure
	implements MPContainer, DirectionContainer {

	/** The list of voices (at least one). */
	@Getter @NonEmpty private List<Voice> voices;
	/** The list of clefs, or null. */
	@Getter private BeatEList<Clef> clefs;
	/** The list of staff-intern key signature changes, or null. */
	@Getter private BeatEList<Key> privateKeys;
	/** The list of directions, or null. */
	@Getter private BeatEList<Direction> directions;
	/** The list of instrument changes, or null. */
	@Getter private BeatEList<InstrumentChange> instrumentChanges;

	/** Back reference: the parent staff, or null if not part of a staff. */
	@Getter @Setter private Staff parent = null;


	public Measure(List<Voice> voices, BeatEList<Clef> clefs, BeatEList<Key> privateKeys, BeatEList<Direction> directions,
		BeatEList<InstrumentChange> instrumentChanges) {
		checkArgsNotNull(voices);
		if (voices.size() == 0)
			throw new IllegalArgumentException("A measure must have at least one voice");
		for (Voice voice : voices)
			voice.setParent(this);
		this.voices = voices;
		this.clefs = clefs;
		this.privateKeys = privateKeys;
		this.directions = directions;
		this.instrumentChanges = instrumentChanges;
	}
	
	
	/**
	 * Creates an empty measure.
	 */
	public static Measure measure() {
		return new Measure(alist(Voice.voice()), null, null, null, null);
	}


	/**
	 * Adds a clef at the given beat or removes it, when null is given.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	public Clef setClef(Clef clef, Fraction beat) {
		if (clef != null) {
			//add clef to list. create list if needed
			clef.setParent(this);
			if (clefs == null)
				clefs = new BeatEList<Clef>();
			return clefs.set(clef, beat);
		}
		else if (clefs != null) {
			//remove clef from list. delete list if not needed any more.
			Clef ret = clefs.remove(beat);
			if (clefs.size() == 0)
				clefs = null;
			return ret;
		}
		return null;
	}


	/**
	 * Adds a key at the given beat. If there is already one, it is replaced
	 * and returned (otherwise null).
	 */
	@Untested public Key setKey(Key key, Fraction beat) {
		if (key != null) {
			//add key to list. create list if needed
			key.setParent(this);
			if (privateKeys == null)
				privateKeys = new BeatEList<Key>();
			return privateKeys.set(key, beat);
		}
		else if (privateKeys != null) {
			//remove key from list. delete list if not needed any more.
			Key ret = privateKeys.remove(beat);
			if (privateKeys.size() == 0)
				privateKeys = null;
			return ret;
		}
		return null;
	}


	/**
	 * Adds a direction at the given beat. If there is already one, it is not
	 * replaced, since there may be many directions belonging to a single beat.
	 */
	@Untested public void addDirection(Direction direction, Fraction beat) {
		direction.setParent(this);
		if (directions == null)
			directions = new BeatEList<Direction>();
		directions.add(direction, beat);
	}


	/**
	 * Adds an instrument change at the given beat.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	@Untested public InstrumentChange setInstrumentChange(InstrumentChange instrumentChange, Fraction beat) {
		if (instrumentChange != null) {
			//add instrumentChange to list. create list if needed
			instrumentChange.setParent(this);
			if (instrumentChanges == null)
				instrumentChanges = new BeatEList<InstrumentChange>();
			return instrumentChanges.set(instrumentChange, beat);
		}
		else if (instrumentChanges != null) {
			//remove instrumentChange from list. delete list if not needed any more.
			InstrumentChange ret = instrumentChanges.remove(beat);
			if (instrumentChanges.size() == 0)
				instrumentChanges = null;
			return ret;
		}
		return null;
	}


	/**
	 * Adds the given {@link MeasureElement} at the given beat. Dependent on its type,
	 * it may replace elements of the same type, which is then returned (otherwise null).
	 * See the documentation for the methods working with specific {@link MeasureElement}s.
	 */
	@Untested public MeasureElement addMeasureElement(MeasureElement element, Fraction beat) {
		if (element instanceof Clef)
			return setClef((Clef) element, beat);
		else if (element instanceof Key)
			return setKey((Key) element, beat);
		else if (element instanceof Direction) {
			addDirection((Direction) element, beat);
			return null;
		}
		else if (element instanceof InstrumentChange)
			return setInstrumentChange((InstrumentChange) element, beat);
		else
			throw new IllegalArgumentException("Unknown MeasureElement subclass: " + element.getClass().getName());
	}


	/**
	 * Removes the given {@link MeasureElement}.
	 */
	@Untested public void removeMeasureElement(MeasureElement element) {
		if (element instanceof Clef)
			clefs.remove((Clef) element);
		else if (element instanceof Key)
			privateKeys.remove((Key) element);
		else if (element instanceof Direction)
			directions.remove((Direction) element);
		else if (element instanceof InstrumentChange)
			instrumentChanges.remove((InstrumentChange) element);
		else
			throw new IllegalArgumentException("Unknown MeasureElement subclass: " + element.getClass().getName());
	}


	/**
	 * Replaces the given {@link MeasureElement} at the given beat with the other given one.
	 */
	@Untested public <T extends MeasureElement> void replaceMeasureElement(T oldElement, T newElement, Fraction beat) {
		if (oldElement instanceof Direction) {
			directions.remove((Direction) oldElement);
			directions.add((Direction) newElement, beat);
		}
		else {
			//all other cases are like addMeasureElement
			addMeasureElement(newElement, beat);
		}
	}


	/**
	 * Collect the accidentals within this measure (backwards),
	 * beginning at the given start beat where the given key is valid, ending before or at
	 * the given beat (depending on the given interval), looking at all voices.
	 * The private keys of this measure are ignored. They must be queried before and
	 * used for the last two parameters.
	 *
	 * @param beat       the maximum beat (inclusive if exclusive, depending on the interval)
	 * @param interval   where to stop looking ({@link Interval#Before} or
	 *                     {@link Interval#BeforeOrAt}). {@link Interval#At} is
	 *                     handled like {@link Interval#BeforeOrAt}.
	 * @param startBeat  the beat where to start collecting accidentals (if there are
	 *                     no private keys in this measure before the given beat, this
	 *                     is always 0).
	 * @param startBeatKey  the key that is valid at the given start beat
	 * @return a map with the pitches that have accidentals (without alter)
	 *   as keys and their corresponding alter values as values.
	 */
	@Untested public Map<Pitch, Integer> getAccidentals(Fraction beat, Interval interval, Fraction startBeat, Key startBeatKey) {
		if (!(interval == Before || interval == BeforeOrAt || interval == At)) {
			throw new IllegalArgumentException("Illegal interval for this method: " + interval);
		}
		if (interval == At) {
			interval = BeforeOrAt;
		}
		Map<Pitch, Integer> ret = new HashMap<Pitch, Integer>();
		Map<Pitch, Fraction> retBeats = new HashMap<Pitch, Fraction>();
		for (Voice voice : voices) {
			Fraction pos = startBeat;
			for (VoiceElement e : voice.getElements()) {
				if (pos.compareTo(startBeat) < 0) {
					pos = pos.add(e.getDuration());
					continue;
				}
				if (interval.isInInterval(pos, beat) != Interval.Result.True) {
					break;
				}
				if (e instanceof Chord) {
					Chord chord = (Chord) e;
					for (Note note : chord.getNotes()) {
						Pitch pitch = note.getPitch();
						Pitch pitchUnaltered = pitch.withoutAlter();
						//accidental already set?
						Integer oldAccAlter = ret.get(pitchUnaltered);
						if (oldAccAlter != null) {
							//there is already an accidental. only replace it if alter changed
							//and if it is at a later position than the already found one
							Fraction existingBeat = retBeats.get(pitch);
							if (pitch.getAlter() != oldAccAlter && pos.compareTo(existingBeat) > 0) {
								ret.put(pitchUnaltered, (int) pitch.getAlter());
								retBeats.put(pitchUnaltered, pos);
							}
						}
						else {
							//accidental not neccessary because of key?
							if (startBeatKey.getAlterations()[pitch.getStep()] == pitch.getAlter()) {
								//ok, we need no accidental here.
							}
							else {
								//add accidental
								ret.put(pitchUnaltered, (int) pitch.getAlter());
								retBeats.put(pitchUnaltered, pos);
							}
						}
					}
				}
				pos = pos.add(e.getDuration());
			}
		}
		return ret;
	}


	/**
	 * Gets a list of all beats used in this measure, that means
	 * all beats where at least one element with a duration greater than 0 begins.
	 * Beat 0 is always used.
	 */
	public SortedList<Fraction> getUsedBeats() {
		SortedList<Fraction> ret = new SortedList<Fraction>(false);
		for (Voice voice : voices) {
			SortedList<Fraction> voiceBeats = voice.getUsedBeats();
			ret = ret.merge(voiceBeats, false);
		}
		return ret;
	}


	/**
	 * Gets the filled beats in this measure, that
	 * means, the first beat in this measure where there is no music
	 * element following any more.
	 */
	public Fraction getFilledBeats() {
		Fraction maxBeat = Fraction._0;
		for (Voice voice : voices) {
			Fraction beat = voice.getFilledBeats();
			if (beat.compareTo(maxBeat) > 0)
				maxBeat = beat;
		}
		return maxBeat;
	}


	/**
	 * Gets the voice with the given index, or throws an
	 * {@link IllegalMPException} if there is none.
	 */
	public Voice getVoice(int index) {
		if (index >= 0 && index <= voices.size())
			return voices.get(index);
		else
			throw new IllegalMPException(atVoice(index));
	}


	/**
	 * Gets the voice with the given index, or throws an
	 * {@link IllegalMPException} if there is none.
	 * Only the voice index of the given position is relevant.
	 */
	public Voice getVoice(MP mp) {
		int index = mp.voice;
		if (index >= 0 && index < voices.size())
			return voices.get(index);
		else
			throw new IllegalMPException(mp);
	}


	/**
	 * Sets the voice with the given index.
	 * If the voice does not exist yet, it is created.
	 */
	public void setVoice(int index, Voice voice) {
		while (index >= voices.size()) {
			Voice voiceFill = voice();
			voiceFill.setParent(this);
			voices.add(voiceFill);
		}
		voice.setParent(this);
		voices.set(index, voice);
	}


	/**
	 * Gets a list of all {@link MeasureElement},s sorted by beat,
	 * and within beat sorted by clef, key, directions, instrument change.
	 */
	public BeatEList<MeasureElement> getMeasureElements() {
		BeatEList<MeasureElement> ret = beatEList();
		ret.addAll(clefs);
		ret.addAll(privateKeys);
		ret.addAll(directions);
		ret.addAll(instrumentChanges);
		return ret;
	}
	
	
	/**
	 * Gets the MP of the given {@link Voice}, or null if it is not part
	 * of this measure or this measure is not part of a score.
	 */
	public MP getMP(Voice voice) {
		int voiceIndex = voices.indexOf(voice);
		if (parent == null || voiceIndex == -1)
			return null;
		MP mp = parent.getMP(this);
		mp = mp.withVoice(voiceIndex);
		return mp;
	}

	
	/**
	 * Gets the MP of the given {@link ColumnElement}, or null if it is not part
	 * of this measure or this measure is not part of a score.
	 */
	@Override public MP getMP(MPElement element) {
		if (parent == null)
			return null;
		MP mp = parent.getMP(this);
		if (element instanceof Clef)
			return getMPIn(element, clefs, mp);
		else if (element instanceof Key)
			return getMPIn(element, privateKeys, mp);
		else if (element instanceof Direction)
			return getMPIn(element, directions, mp);
		else if (element instanceof InstrumentChange)
			return getMPIn(element, instrumentChanges, mp);
		return null;
	}
	
	
	/**
	 * Gets the {@link MP} of the given element within the given list of elements,
	 * based on the given {@link MP} (staff, measure), or null if the list of elements
	 * is null or the element could not be found.
	 */
	private MP getMPIn(MPElement element, BeatEList<?> elements, MP baseMP) {
		if (elements == null)
			return null;
		for (BeatE<?> e : elements)
			if (e.getElement() == element)
				return MP.atBeat(baseMP.staff, baseMP.measure, baseMP.voice, e.getBeat());
		return null;
	}
	
	
	/**
	 * Convenience method. Gets the parent score of this voice,
	 * or null, if this element is not part of a score.
	 */
	public Score getScore() {
		return (parent != null ? parent.getScore() : null);
	}
	

}
