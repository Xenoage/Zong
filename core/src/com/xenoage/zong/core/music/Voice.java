package com.xenoage.zong.core.music;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.FirstOrLast.First;
import static com.xenoage.zong.core.music.util.FirstOrLast.Last;
import static com.xenoage.zong.core.music.util.IndexE.indexE;
import static com.xenoage.zong.core.music.util.Interval.Result.FalseHigh;
import static com.xenoage.zong.core.music.util.Interval.Result.True;
import static com.xenoage.zong.core.music.util.StartOrStop.Start;
import static com.xenoage.zong.core.music.util.StartOrStop.Stop;
import static com.xenoage.zong.core.position.MP.atVoice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.util.FirstOrLast;
import com.xenoage.zong.core.music.util.IndexE;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.music.util.StartOrStop;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPContainer;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.utils.exceptions.IllegalMPException;


/**
 * Voice in a single measure within a single staff.
 * 
 * A voice contains musical elements like chords and rests.
 * 
 * All these elements have durations greater than 0.
 * Grace chords are attached to their main chords.
 *
 * @author Andreas Wenger
 */
public final class Voice
	implements MPContainer {

	/** The list of rests and chords, sorted by time */
	@MaybeEmpty @Getter private List<VoiceElement> elements;

	/** Back reference: the parent measure of this voice, or null if not part of a measure. */
	@Getter @Setter private Measure parent = null;


	/**
	 * Creates a new {@link Voice}.
	 * @param elements  the elements, sorted by time
	 */
	public Voice(List<VoiceElement> elements) {
		this.elements = elements;
	}


	/**
	 * Creates an empty voice.
	 */
	public static Voice voice() {
		return new Voice(new ArrayList<VoiceElement>(0));
	}
	
	
	/**
	 * Creates an empty voice with the given parent measure.
	 */
	public static Voice voice(Measure parent) {
		Voice voice = voice();
		voice.setParent(parent);
		return voice;
	}
	
	
	/**
	 * Gets the element with the given index, or throws an
	 * {@link IllegalMPException} if there is none.
	 */
	public VoiceElement getElement(int index) {
		if (index >= 0 && index <= elements.size())
			return elements.get(index);
		else
			throw new IllegalMPException(atVoice(index));
	}


	/**
	 * Gets the {@link FirstOrLast} element, which {@link StartOrStop}s within
	 * the given {@link Interval} relative to the given element index, or null if there is none.
	 */
	public IndexE<VoiceElement> getElement(FirstOrLast side, StartOrStop border, Interval interval, int elementIndex) {
		if (isEmpty())
			return null;
		int pos = 0;
		if (border == Start) {
			if (side == First) {
				for (VoiceElement e : elements) {
					Interval.Result r = interval.isInInterval(pos, elementIndex);
					if (r == True)
						return indexE(e, pos);
					else if (r == FalseHigh)
						return null; //gone to far
					pos++;
				}
				return null;
			}
			else if (side == Last) {
				IndexE<VoiceElement> ret = null;
				for (VoiceElement e : elements) {
					Interval.Result r = interval.isInInterval(pos, elementIndex);
					if (r == True)
						ret = indexE(e, pos);
					pos++;
				}
				return ret;
			}
		}
		else if (border == Stop) {
			if (side == First) {
				for (VoiceElement e : elements) {
					Interval.Result r = interval.isInInterval(pos + 1, elementIndex);
					if (r == True)
						return indexE(e, pos);
					else if (r == FalseHigh)
						return null; //gone to far
					pos++;
				}
				return null;
			}
			else if (side == Last) {
				IndexE<VoiceElement> ret = null;
				for (VoiceElement e : elements) {
					Interval.Result r = interval.isInInterval(pos + 1, elementIndex);
					if (r == True)
						ret = indexE(e, pos);
					pos++;
				}
				return ret;
			}
		}
		throw new IllegalArgumentException("Unknown parameters");
	}


	/**
	 * Adds the given element at the end of this voice.
	 */
	public void addElement(VoiceElement element) {
		element.setParent(this);
		elements.add(element);
	}


	/**
	 * Adds the given element at the given position within this voice.
	 */
	public void addElement(int index, VoiceElement element) {
		element.setParent(this);
		elements.add(index, element);
	}


	/**
	 * Replaces the element with the given index by the given one.
	 */
	public void replaceElement(int index, VoiceElement element) {
		element.setParent(this);
		elements.set(index, element);
	}


	/**
	 * Replaces the given element by the given ones.
	 */
	public void replaceElement(VoiceElement oldElement, VoiceElement... newElements) {
		int index = elements.indexOf(oldElement);
		if (index == -1)
			throw new IllegalArgumentException("Given element is not part of this voice.");
		for (VoiceElement newElement : newElements) {
			elements.add(index, newElement);
			index++;
		}
	}


	/**
	 * Removes the element with the given index.
	 */
	public void removeElement(int index) {
		elements.remove(index);
	}


	/**
	 * Removes the given element.
	 * If found, its index is returned, otherwise -1.
	 */
	public int removeElement(VoiceElement element) {
		for (int i : range(elements)) {
			if (elements.get(i) == element) {
				elements.remove(i);
				return i;
			}
		}
		return -1;
	}


	/**
	 * Gets the last used beat less than or equal the given one.
	 * If there are no elements, 0 is returned.
	 */
	public Fraction getLastUsedBeat(Fraction maxBeat) {
		Fraction beat = fr(0);
		for (VoiceElement e : elements) {
			Fraction pos = beat.add(e.getDuration());
			if (pos.compareTo(maxBeat) > 0)
				break;
			else
				beat = pos;
		}
		return beat;
	}


	/**
	 * Returns true, if the given beat is the starting
	 * beat of an element within this voice, beat 0,
	 * or the empty beat behind the last element.
	 */
	public boolean isBeatUsed(Fraction beat) {
		//all measures start with beat 0
		if (beat.getNumerator() == 0)
			return true;
		//is there an element at this beat?
		Fraction curBeat = fr(0);
		for (VoiceElement e : elements) {
			if (beat.equals(curBeat))
				return true;
			curBeat = curBeat.add(e.getDuration());
		}
		//first unused (empty) beat
		if (beat.equals(curBeat))
			return true;
		return false;
	}


	/**
	 * Returns true, if this voice contains no elements.
	 */
	public boolean isEmpty() {
		return elements.size() == 0;
	}


	/**
	 * Gets the filled beats in this voice, that means, the first beat in this
	 * voice where the is no music element following any more.
	 */
	public Fraction getFilledBeats() {
		Fraction ret = Fraction._0;
		for (VoiceElement e : elements)
			ret = ret.add(e.getDuration());
		return ret;
	}


	/**
	 * Gets the last element at the given beat.
	 * That means, that if the beat starts with grace elements followed
	 * by a full element, the full element is returned.
	 * If no element starts at exactly the given beat, null is returned.
	 */
	public VoiceElement getElementAt(Fraction beat) {
		Fraction currentBeat = Fraction._0;
		VoiceElement foundElement = null;
		for (VoiceElement e : elements) {
			int compare = beat.compareTo(currentBeat);
			if (compare == 0)
				foundElement = e;
			else if (compare < 0)
				break;
			currentBeat = currentBeat.add(e.getDuration());	
		}
		return foundElement;
	}


	/**
	 * Gets a list of all beats used in this voice, that means
	 * all beats where at least one element with a duration greater than 0 begins.
	 * Beat 0 is always used.
	 */
	public SortedList<Fraction> getUsedBeats() {
		SortedList<Fraction> ret = new SortedList<Fraction>(false);
		Fraction currentBeat = Fraction._0;
		ret.add(currentBeat);
		for (VoiceElement e : elements) {
			Fraction duration = e.getDuration();
			if (duration != null && duration.getNumerator() > 0) {
				if (!currentBeat.equals(ret.getLast()))
					ret.add(currentBeat);
				currentBeat = currentBeat.add(duration);
			}
		}
		return ret;
	}


	/**
	 * Gets a list of the elements in this voice, beginning at or after
	 * the given start beat and before the given end beat.
	 */
	public LinkedList<VoiceElement> getElementsInRange(Fraction startBeat, Fraction endBeat) {
		LinkedList<VoiceElement> ret = new LinkedList<VoiceElement>();
		//collect elements
		Fraction beat = Fraction._0;
		for (VoiceElement e : elements) {
			if (beat.compareTo(endBeat) >= 0)
				break;
			else if (beat.compareTo(startBeat) > 0 || (beat.compareTo(startBeat) == 0 && e.getDuration().isGreater0()))
				ret.add(e);
			beat = beat.add(e.getDuration());
		}
		return ret;
	}


	@Override public String toString() {
		return elements.toString();
	}


	/**
	 * Gets the start beat of the given element.
	 * If the element is not in this voice, null is returned.
	 */
	public Fraction getBeat(MusicElement element) {
		Fraction beat = Fraction._0;
		for (VoiceElement e : elements) {
			if (e == element)
				return beat;
			else
				beat = beat.add(e.getDuration());
		}
		return null;
	}


	/**
	 * Gets the start beat of the element with the given index.
	 * If the index is greater than the number of elements, the beat after
	 * the last element is returned (or 0 if the voice is empty).
	 */
	public Fraction getBeat(int elementIndex) {
		Fraction beat = Fraction._0;
		for (int i : range(elements)) {
			if (i >= elementIndex)
				break;
			beat = beat.add(elements.get(i).getDuration());
		}
		return beat;
	}


	/**
	 * Gets the index of the last element that starts before or at
	 * the given beat. If the given beat is after the last element,
	 * the index of the last element + 1 is returned (or 0 if the voice is empty).
	 */
	public int getElementIndex(Fraction beat) {
		int posI = 0;
		Fraction posB = Fraction._0;
		for (int i : range(elements)) {
			Fraction newPosB = posB.add(elements.get(i).getDuration());
			if (newPosB.compareTo(beat) > 0)
				break;
			posB = newPosB;
			posI++;
		}
		return posI;
	}


	@Override public MP getMP(MPElement element) {
		if (parent == null)
			return null;
		Fraction beat = getBeat(element);
		if (beat == null)
			return null;
		MP mp = parent.getMP(this);
		mp = mp.withBeat(beat);
		return mp;
	}
	
	
	/**
	 * Convenience method. Gets the parent score of this measure,
	 * or null, if this element is not part of a score.
	 */
	public Score getScore() {
		return (parent != null ? parent.getScore() : null);
	}

}
