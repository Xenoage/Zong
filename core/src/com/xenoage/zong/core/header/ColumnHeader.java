package com.xenoage.zong.core.header;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.CheckUtils.checkNotNull;
import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atColumnBeat;
import lombok.Data;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.annotations.Untested;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.exceptions.UnsupportedClassException;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.Break;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureSide;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineRepeat;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.DirectionContainer;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPContainer;
import com.xenoage.zong.core.position.MPElement;

/**
 * A {@link ColumnHeader} stores information that
 * is used for the whole measure column,
 * i.e. key signature and time signature.
 * 
 * The start and end barline as well as middle barlines
 * are saved here, and a volta if it begins in this
 * measure.
 * 
 * There is also a list of tempo directions for this
 * measure and layout break information.
 * 
 * @author Andreas Wenger
 */
@Data
public final class ColumnHeader
	implements DirectionContainer, MPContainer {

	/** The time signature at the beginning of this measure. */
	@MaybeNull private Time time;
	/** The barline at the beginning of this measure. */
	@MaybeNull private Barline startBarline;
	/** The barline at the end of this measure, or null if unset. */
	@MaybeNull private Barline endBarline;
	/** The barlines in the middle of the measure. */
	@NonNull @MaybeEmpty private BeatEList<Barline> middleBarlines;
	/** The volta that begins at this measure. */
	@MaybeNull private Volta volta;
	/** The key signature changes in this measure. */
	@NonNull @MaybeEmpty private BeatEList<Key> keys;
	/** The tempo changes in this measure */
	@NonNull @MaybeEmpty private BeatEList<Tempo> tempos;
	/** The {@link Break} after this measure, or null. */
	@MaybeNull private Break measureBreak;
	/** The other {@link Direction}s in this measure */
	@NonNull @MaybeEmpty private BeatEList<Direction> otherDirections;

	/** Back reference: parent score, or null if not part of a score. */
	private Score parentScore = null;
	/** Back reference: measure index, or null if not part of a score. */
	private Integer parentMeasureIndex = null;


	public ColumnHeader(Score parentScore, Integer parentMeasureIndex) {
		this.time = null;
		this.startBarline = null;
		this.endBarline = null;
		this.middleBarlines = new BeatEList<Barline>();
		this.volta = null;
		this.keys = new BeatEList<Key>();
		this.tempos = new BeatEList<Tempo>();
		this.measureBreak = null;
		this.otherDirections = new BeatEList<Direction>();
		this.parentScore = parentScore;
		this.parentMeasureIndex = parentMeasureIndex;
	}

	/**
	 * Sets the time signature, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	public Time setTime(Time time) {
		Time old = this.time;
		this.time = time;
		this.time.setParent(this);
		return old;
	}

	/**
	 * Sets the barline at the beginning of this measure, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	public Barline setStartBarline(Barline startBarline) {
		checkStartBarline(startBarline);
		Barline old = this.startBarline;
		this.startBarline = startBarline;
		this.startBarline.setParent(this);
		return old;
	}

	/**
	 * Sets the barline at the end of this measure, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	public Barline setEndBarline(Barline endBarline) {
		checkEndBarline(endBarline);
		Barline old = this.endBarline;
		this.endBarline = endBarline;
		this.endBarline.setParent(this);
		return old;
	}

	/**
	 * Sets a barline in the middle of the measure.
	 * If there is already one at the given beat, it is replaced and returned (otherwise null).
	 */
	public Barline setMiddleBarline(@NonNull Barline middleBarline, Fraction beat) {
		checkArgsNotNull(middleBarline);
		middleBarline.setParent(this);
		return middleBarlines.set(middleBarline, beat);
	}

	/**
	 * Removes a barline in the middle of the measure.
	 * If found, is returned (otherwise null).
	 */
	public Barline removeMiddleBarline(Fraction beat) {
		Barline ret = middleBarlines.remove(beat);
		if (ret != null)
			ret.setParent(null);
		return ret;
	}

	/**
	 * Sets the volta beginning at this measure, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	public Volta setVolta(Volta volta) {
		Volta old = this.volta;
		this.volta = volta;
		this.volta.setParent(this);
		return old;
	}

	/**
	 * Sets a key in this measure.
	 * If there is already one at the given beat, it is replaced and returned (otherwise null).
	 */
	public Key setKey(Key key, Fraction beat) {
		checkArgsNotNull(key, beat);
		key.setParent(this);
		return keys.set(key, beat);
	}

	/**
	 * Removes a key from this measure.
	 * If found, it is returned (otherwise null).
	 */
	public Key removeKey(Fraction beat) {
		Key ret = keys.remove(beat);
		if (ret != null)
			ret.setParent(null);
		return ret;
	}

	/**
	 * Sets a tempo in this measure.
	 * If there is already one at the given beat, it is replaced and returned (otherwise null).
	 */
	public Tempo setTempo(Tempo tempo, Fraction beat) {
		checkArgsNotNull(tempo, beat);
		tempo.setParent(this);
		return tempos.set(tempo, beat);
	}

	/**
	 * Removes a tempo from this measure.
	 * If found, it is returned (otherwise null).
	 */
	public Tempo removeTempo(Fraction beat) {
		Tempo ret = tempos.remove(beat);
		if (ret != null)
			ret.setParent(null);
		return ret;
	}

	/**
	 * Sets the {@link Break} after this measure, or null if there is none.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	public Break setBreak(Break measureBreak) {
		Break old = this.measureBreak;
		this.measureBreak = measureBreak;
		if (this.measureBreak != null)
			this.measureBreak.setParent(this);
		return old;
	}

	/**
	 * Adds the given {@link Direction} to this measure.
	 * For a {@link Tempo}, use {@link #setTempo(Tempo, Fraction)} with a null value instead.
	 */
	public void addOtherDirection(Direction direction, Fraction beat) {
		checkArgsNotNull(direction, beat);
		direction.setParent(this);
		otherDirections.add(direction, beat);
	}

	/**
	 * Removes the given {@link Direction} from this measure.
	 * If found, it is returned (otherwise null).
	 * For a {@link Tempo}, use {@link #removeTempo(Fraction)} instead.
	 */
	public Direction removeOtherDirection(Direction direction) {
		Direction ret = otherDirections.remove(direction);
		if (ret != null)
			ret.setParent(null);
		return ret;
	}

	/**
	 * Checks the given start barline.
	 */
	private void checkStartBarline(Barline startBarline) {
		//both side repeat is not allowed
		if (startBarline != null && startBarline.getRepeat() == BarlineRepeat.Both)
			throw new IllegalArgumentException(BarlineRepeat.Both +
				" is not supported for a start barline.");
	}

	/**
	 * Checks the given end barline.
	 */
	private void checkEndBarline(Barline endBarline) {
		//both side repeat is not allowed
		if (endBarline != null && endBarline.getRepeat() == BarlineRepeat.Both)
			throw new IllegalArgumentException(BarlineRepeat.Both +
				" is not supported for an end barline.");
	}

	/**
	 * Sets the given {@link ColumnElement} at the given beat.
	 * 
	 * If there is already another element of this type, it is replaced and returned (otherwise null),
	 * except for {@link Direction}s (except {@link Tempo}), where multiple elements on a single beat
	 * are allowed.
	 * 
	 * @param element  the element to add
	 * @param beat     the beat where to add the element. Only needed for
	 *                 key, tempo, middle barlines and other directions
	 * @param side     Only needed for barlines           
	 */
	@Untested public ColumnElement setColumnElement(ColumnElement element, @MaybeNull Fraction beat,
		@MaybeNull MeasureSide side) {
		if (element instanceof Barline) {
			Barline barline = (Barline) element;
			//left or right barline
			if (side == MeasureSide.Left)
				return setStartBarline(barline);
			else if (side == MeasureSide.Right)
				return setEndBarline(barline);
			//middle barline
			checkNotNull(beat);
			return setMiddleBarline(barline, beat);
		}
		else if (element instanceof Break) {
			return setBreak((Break) element);
		}
		else if (element instanceof Key) {
			checkNotNull(beat);
			return setKey((Key) element, beat);
		}
		else if (element instanceof Tempo) {
			checkNotNull(beat);
			return setTempo((Tempo) element, beat);
		}
		else if (element instanceof Time)
			return setTime((Time) element);
		else if (element instanceof Volta)
			return setVolta((Volta) element);
		else if (element instanceof Direction) {
			addOtherDirection((Direction) element, beat);
			return null;
		}
		else
			throw new UnsupportedClassException(element);
	}

	/**
	 * Removes the given {@link ColumnElement}.
	 */
	@Untested public void removeColumnElement(ColumnElement element) {
		if (element instanceof Barline) {
			//left or right barline
			if (element == startBarline)
				startBarline = null;
			else if (element == endBarline)
				endBarline = null;
			//middle barline
			else
				middleBarlines.remove((Barline) element);
		}
		else if (element instanceof Break) {
			measureBreak = null;
		}
		else if (element instanceof Key) {
			keys.remove((Key) element);
		}
		else if (element instanceof Tempo) {
			tempos.remove((Tempo) element);
		}
		else if (element instanceof Time) {
			time = null;
		}
		else if (element instanceof Volta) {
			volta = null;
		}
		else if (element instanceof Direction) {
			otherDirections.remove((Direction) element);
		}
		else {
			throw new UnsupportedClassException(element);
		}
	}

	/**
	 * Replaces the given {@link ColumnElement} with the other given one.
	 */
	@Untested public <T extends ColumnElement> void replaceColumnElement(T oldElement, T newElement) {
		if (newElement instanceof Barline) {
			Barline newBarline = (Barline) newElement;
			//left or right barline
			if (oldElement == startBarline)
				setStartBarline(newBarline);
			else if (oldElement == endBarline)
				setEndBarline(newBarline);
			else {
				//middle barline
				for (BeatE<Barline> middleBarline : middleBarlines) {
					if (middleBarline.getElement() == oldElement) {
						setMiddleBarline(newBarline, middleBarline.getBeat());
						return;
					}
				}
				throw new IllegalArgumentException("Old barline not part of this column");
			}
		}
		else if (newElement instanceof Break) {
			setBreak((Break) newElement);
		}
		else if (newElement instanceof Key) {
			for (BeatE<Key> key : keys) {
				if (key.getElement() == oldElement) {
					setKey((Key) newElement, key.getBeat());
					return;
				}
			}
			throw new IllegalArgumentException("Old key not part of this column");
		}
		else if (newElement instanceof Tempo) {
			for (BeatE<Tempo> tempo : tempos) {
				if (tempo.getElement() == oldElement) {
					setTempo((Tempo) newElement, tempo.getBeat());
					return;
				}
			}
			throw new IllegalArgumentException("Old tempo not part of this column");
		}
		else if (newElement instanceof Time) {
			setTime((Time) newElement);
		}
		else if (newElement instanceof Volta) {
			setVolta((Volta) newElement);
		}
		else {
			throw new UnsupportedClassException(newElement);
		}
	}

	/**
	 * Gets a list of all {@link ColumnElement}s in this column, which
	 * are assigned to a beat (middle barlines, keys and tempos).
	 */
	public BeatEList<ColumnElement> getColumnElementsWithBeats() {
		BeatEList<ColumnElement> ret = new BeatEList<ColumnElement>();
		ret.addAll(middleBarlines);
		ret.addAll(keys);
		ret.addAll(tempos);
		return ret;
	}

	/**
	 * Gets a list of all {@link ColumnElement}s in this column, which
	 * are not assigned to a beat (time, start and end barline, volta, measure break).
	 */
	public IList<ColumnElement> getColumnElementsWithoutBeats() {
		//elements with beats
		CList<ColumnElement> ret = clist();
		if (time != null)
			ret.add(time);
		if (startBarline != null)
			ret.add(startBarline);
		if (endBarline != null)
			ret.add(endBarline);
		if (volta != null)
			ret.add(volta);
		if (measureBreak != null)
			ret.add(measureBreak);
		return ret.close();
	}

	/**
	 * Gets a list of all {@link ColumnElement}s in this column.
	 */
	public IList<ColumnElement> getColumnElements() {
		CList<ColumnElement> ret = clist();
		if (time != null)
			ret.add(time);
		if (startBarline != null)
			ret.add(startBarline);
		if (endBarline != null)
			ret.add(endBarline);
		for (BeatE<Barline> e : middleBarlines)
			ret.add(e.getElement());
		if (volta != null)
			ret.add(volta);
		for (BeatE<Key> e : keys)
			ret.add(e.getElement());
		for (BeatE<Tempo> e : tempos)
			ret.add(e.getElement());
		if (measureBreak != null)
			ret.add(measureBreak);
		return ret.close();
	}

	/**
	 * Gets the {@link MP} of the given {@link ColumnElement}, or null if it is not part
	 * of this column or this column is not part of a score.
	 */
	@Override public MP getMP(MPElement element) {
		if (parentScore == null || parentMeasureIndex == null)
			return null;
		//elements at the beginning of the measure
		if (time == element || startBarline == element || volta == element)
			return atColumnBeat(parentMeasureIndex, _0);
		//elements at the end of the measure
		else if (endBarline == element || measureBreak == element)
			return atColumnBeat(parentMeasureIndex, parentScore.getMeasureBeats(parentMeasureIndex));
		//elements in the middle of the measure
		else if (element instanceof Barline)
			return getMPIn(element, middleBarlines);
		else if (element instanceof Key)
			return getMPIn(element, keys);
		else if (element instanceof Tempo)
			return getMPIn(element, tempos);
		return null;
	}

	/**
	 * Gets the {@link MP} of the given element within the given list of elements,
	 * or null if the list of elements is null or the element could not be found.
	 */
	private MP getMPIn(MPElement element, BeatEList<?> elements) {
		if (elements == null)
			return null;
		for (BeatE<?> e : elements)
			if (e.getElement() == element)
				return atColumnBeat(parentMeasureIndex, e.getBeat());
		return null;
	}

	/**
	 * Gets the {@link MeasureSide} of the given element in this column. This applies only to
	 * start and end barlines. For all other elements, null is returned.
	 */
	public MeasureSide getSide(ColumnElement element) {
		if (element == startBarline)
			return MeasureSide.Left;
		else if (element == endBarline)
			return MeasureSide.Right;
		else
			return null;
	}

}
