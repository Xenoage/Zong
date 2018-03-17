package com.xenoage.zong.core;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.core.music.*;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.util.*;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.utils.exceptions.IllegalMPException;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.header.ScoreHeader.scoreHeader;
import static com.xenoage.zong.core.music.MusicContext.noAccidentals;
import static com.xenoage.zong.core.music.util.BeatE.selectLatest;
import static com.xenoage.zong.core.music.util.Interval.At;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.music.util.MPE.mpE;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mpb0;


/**
 * This class contains a single piece of written music.
 * 
 * @author Andreas Wenger
 */
public final class Score
	implements Document {

	/** General information about the score. */
	@Getter @Setter @NonNull private ScoreInfo info = new ScoreInfo();
	/** The default formats of the score. */
	@Getter @Setter @NonNull private ScoreFormat format = new ScoreFormat();
	/** The list of elements that are normally used in all staves, like time signatures and key signatures. */
	@Getter @Setter @NonNull private ScoreHeader header = Companion.scoreHeader(this);
	/** The list of staves, parts and groups. */
	@Getter @Setter @NonNull private StavesList stavesList = new StavesList();
	/** Additional meta information. This other application-dependend meta-information
	 * can be used for example to store page layout information, which is not part
	 * of the musical score in this project. */
	@Getter private Map<String, Object> metaData = new HashMap<>();
	
	/** Performs commands on this score and supports undo. */
	@Getter private CommandPerformer commandPerformer = new CommandPerformer(this);
	/** Supported formats for reading scores from files and writing them to files. */
	@Getter private SupportedFormats<Score> supportedFormats = null;
	
	
	public Score() {
		stavesList.setScore(this);
	}


	/**
	 * Gets the number of staves.
	 */
	public int getStavesCount() {
		return stavesList.getStaves().size();
	}


	/**
	 * Sets the given meta-data information.
	 */
	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}
	
	
	/**
	 * Gets the measure column header at the given measure.
	 */
	public ColumnHeader getColumnHeader(int measureIndex) {
		return header.getColumnHeader(measureIndex);
	}


	/**
	 * Gets the measure at the given {@link BMP}.
	 */
	public Measure getMeasure(MP mp) {
		return stavesList.getMeasure(mp);
	}


	/**
	 * Gets the number of measures.
	 */
	public int getMeasuresCount() {
		return header.getColumnHeaders().size();
	}


	/**
	 * Gets the staff with the given index.
	 */
	public Staff getStaff(int staffIndex) {
		return stavesList.getStaff(staffIndex);
	}


	/**
	 * Gets the staff at the given {@link MP}.
	 */
	public Staff getStaff(MP mp) {
		return stavesList.getStaff(mp);
	}


	/**
	 * Gets the voice at the given {@link MP}.
	 */
	public Voice getVoice(MP mp) {
		return stavesList.getVoice(mp);
	}


	/**
	 * Gets the interline space for the staff with the given index.
	 * If unknown, the default value is returned.
	 */
	public float getInterlineSpace(int staffIndex) {
		Float is = getStaff(staffIndex).getInterlineSpace();
		if (is != null)
			return is;
		else
			return format.getInterlineSpace();
	}
	
	/**
	 * Gets the biggest interline space of the score.
	 */
	public float getMaxInterlineSpace() {
		float ret = 0;
		for (int iStaff : range(getStavesCount()))
			ret = Math.max(ret, getInterlineSpace(iStaff));
		return ret;
	}
	
	
	/**
	 * Returns true, if the given {@link MP} is in a valid range, otherwise false.
	 */
	public boolean isMPExisting(MP mp) {
		try {
			if (mp.getStaff() < 0 || mp.getStaff() >= getStavesCount() || mp.getMeasure() < 0 || mp.getMeasure() >= getMeasuresCount() ||
				mp.getVoice() < 0 || mp.getVoice() >= getMeasure(mp).getVoices().size())
				return false;
			Voice voice = getVoice(mp);
			if (mp.getElement() != MP.Companion.getUnknown() && voice.getElements().size() <= mp.getElement())
				return false;
			if (mp.getBeat() != MP.Companion.getUnknownBeat() && (mp.getBeat().compareTo(Companion.get_0()) < 0 ||
				mp.getBeat().compareTo(voice.getParent().getFilledBeats()) > 0))
				return false;
			return true;
		} catch (IllegalMPException ex) {
			return false;
		}
	}
	
	
	/**
	 * Gets the number of beats in the given measure column.
	 * If a time signature is defined, its beats are returned.
	 * If the time signature is unknown (senza-misura), the beats of the
	 * voice with the most beats are returned.
	 */
	public Fraction getMeasureBeats(int measureIndex)
	{
		Fraction ret = header.getTimeAtOrBefore(measureIndex).getType().getMeasureBeats();
		if (ret != null)
			return ret;
		else
			return getMeasureFilledBeats(measureIndex);
	}


	/**
	 * Gets the filled beats for the given measure column, that
	 * means, the first beat in each column where there is no music
	 * element following any more.
	 * The given measure may be one measure after the last measure. There, only beat 0
	 * exists to mark the ending of the score.
	 */
	public Fraction getMeasureFilledBeats(int measureIndex) {
		if (measureIndex == getMeasuresCount())
			return Companion.get_0();
		Fraction maxBeat = Fraction.Companion.get_0();
		for (Staff staff : stavesList.getStaves()) {
			Fraction beat = staff.getMeasures().get(measureIndex).getFilledBeats();
			if (beat.compareTo(maxBeat) > 0)
				maxBeat = beat;
		}
		return maxBeat;
	}
	
	
	/**
	 * Gets the used beats within the given measure column.
	 * The given measure may be one measure after the last measure. There, only beat 0
	 * exists to mark the ending of the score.
	 * @param measureIndex                  the index of the measure column
	 * @param withMeasureAndColumnElements  true, iff also the beats of column elements and
	 *                                      measure elements should be used
	 */
	public SortedList<Fraction> getMeasureUsedBeats(int measureIndex, boolean withMeasureAndColumnElements) {
		//last measure?
		if (measureIndex == getMeasuresCount())
			return new SortedList<>(new Fraction[]{Companion.get_0()}, false);
		//add measure beats
		SortedList<Fraction> columnBeats = new SortedList<>(false);
		for (int iStaff : range(getStavesCount())) {
			val measure = getMeasure(Companion.atMeasure(iStaff, measureIndex));
			val beats = measure.getUsedBeats(withMeasureAndColumnElements);
			columnBeats = columnBeats.merge(beats, false);
		}
		//add column beats
		if (withMeasureAndColumnElements) {
			for (val beatE : getColumnHeader(measureIndex).getColumnElementsWithBeats())
				columnBeats.add(beatE.getBeat());
		}
		return columnBeats;
	}
	
	
	/**
	 * Gets the last {@link Key} that is defined before (or at,
	 * dependent on the given {@link Interval}) the given
	 * {@link MP}, also over measure boundaries. If there is
	 * none, a default C major time signature is returned.
	 * Private keys (in measure) override public keys (in measure column header). 
	 */
	@SuppressWarnings("unchecked")
	public MPE<? extends Key> getKey(MP mp, Interval interval) {
		if (!interval.isPrecedingOrAt()) {
			throw new IllegalArgumentException("Illegal interval for this method");
		}
		//begin with the given measure. if there is one, return it.
		BeatE<Key> columnKey = header.getColumnHeader(mp.getMeasure()).getKeys().getLastBefore(interval, mp.getBeat());
		BeatE<Key> measureKey = null;
		if (getMeasure(mp).getPrivateKeys() != null)
			measureKey = getMeasure(mp).getPrivateKeys().getLastBefore(interval, mp.getBeat());
		BeatE<Key> ret = Companion.selectLatest(columnKey, measureKey);
		if (ret != null)
			return Companion.mpE(ret.getElement(), mp.withBeat(ret.getBeat()));
		if (interval != At) {
			//search in the preceding measures
			for (int iMeasure = mp.getMeasure() - 1; iMeasure >= 0; iMeasure--) {
				columnKey = header.getColumnHeader(iMeasure).getKeys().getLast();
				BeatEList<Key> privateKeys = getMeasure(Companion.atMeasure(mp.getStaff(), iMeasure)).getPrivateKeys();
				measureKey = (privateKeys != null ? privateKeys.getLast() : null);
				ret = Companion.selectLatest(columnKey, measureKey);
				if (ret != null)
					return Companion.mpE(ret.getElement(), mp.withMeasure(iMeasure).withBeat(ret.getBeat()));
			}
		}
		//no key found. return default key.
		return Companion.mpE(new TraditionalKey(0, Mode.Major), Companion.getMpb0());
	}
	
	
	/**
	 * Gets the accidentals at the given {@link MP} that are
	 * valid before or at the given beat (depending on the given interval),
	 * looking at all voices. The beat in the {@link MP} is required.
	 */
	public Map<Pitch, Integer> getAccidentals(MP mp, Interval interval) {
		if (mp.getBeat() == MP.Companion.getUnknownBeat())
			throw new IllegalArgumentException("beat is required");
		//start key of the measure always counts
		MPE<? extends Key> key = getKey(mp, BeforeOrAt);
		//if key change is in same measure, start at that beat. otherwise start at beat 0.
		Fraction keyBeat = (key.getMp().getMeasure() == mp.getMeasure() ? key.getMp().getBeat() : Companion.get_0());
		Measure measure = getMeasure(mp);
		return measure.getAccidentals(mp.getBeat(), interval, keyBeat, key.getElement());
	}
	
	
	/**
	 * Gets the last {@link Clef} that is defined before (or at,
	 * dependent on the given {@link Interval}) the given
	 * {@link MP}, also over measure boundaries. If there is
	 * none, a default g clef is returned. The beat in the {@link MP} is required.
	 */
	public ClefType getClef(MP mp, Interval interval) {
		if (!interval.isPrecedingOrAt())
			throw new IllegalArgumentException("Illegal interval for this method");
		if (mp.getBeat() == MP.Companion.getUnknownBeat())
			throw new IllegalArgumentException("beat is required");
		//begin with the given measure. if there is one, return it.
		Measure measure = getMeasure(mp);
		BeatE<Clef> ret = null;
		if (measure.getClefs() != null) {
			ret = measure.getClefs().getLastBefore(interval, mp.getBeat());
			if (ret != null)
				return ret.getElement().getType();
		}
		if (interval != At) {
			//search in the preceding measures
			for (int iMeasure : rangeReverse(mp.getMeasure() - 1, 0)) {
				measure = getMeasure(Companion.atMeasure(mp.getStaff(), iMeasure));
				if (measure.getClefs() != null) {
					ret = measure.getClefs().getLast();
					if (ret != null)
						return ret.getElement().getType();
				}
			}
		}
		//no clef found. return default clef.
		return ClefType.Companion.getClefTreble();
	}
	
	
	/**
	 * Gets the {@link MusicContext} that is defined before (or at,
	 * dependent on the given {@link Interval}s) the given
	 * {@link BMP}, also over measure boundaries.
	 * 
	 * Calling this method can be quite expensive, so call only when neccessary.
	 * 
	 * @param mp                   the musical position
	 * @param clefAndKeyInterval   where to look for the last clef and key:
	 *                             {@link Interval#Before} ignores a clef and a key
	 *                             at the given position, {@link Interval#BeforeOrAt}
	 *                             and {@link Interval#At} (meaning the same here)
	 *                             include it
	 * @param accidentalsInterval  the same as for clefAndKeyInterval, but for the accidentals.
	 *                             If null, no accidentals are collected.          
	 */
	public MusicContext getMusicContext(MP mp,
		Interval clefAndKeyInterval, Interval accidentalsInterval)
	{
		if (clefAndKeyInterval == At)
			clefAndKeyInterval = BeforeOrAt; //At and BeforeOrAt mean the same in this context
		ClefType clef = getClef(mp, clefAndKeyInterval);
		Key key = getKey(mp, clefAndKeyInterval).getElement();
		Map<Pitch, Integer> accidentals = noAccidentals;
		if (accidentalsInterval != null)
			accidentals = getAccidentals(mp, accidentalsInterval);
		return new MusicContext(clef, key, accidentals, getStaff(mp).getLinesCount());
	}
	
	
	/**
	 * Returns the number of divisions of a quarter note within the whole score.
	 * This is e.g. needed for Midi and MusicXML Export.
	 */
	public int getDivisions() {
		int actualdivision = 4;
		for (Staff staff : stavesList.getStaves()) {
			for (Measure measure : staff.getMeasures()) {
				for (Voice voice : measure.getVoices()) {
					for (VoiceElement e : voice.getElements()) {
						actualdivision = MathUtils.INSTANCE.lcm(actualdivision, e.getDuration().getDenominator());
					}
				}
			}
		}
		return actualdivision / 4;
	}
	
	
	/**
	 * Gets the measures of the column with the given index.
	 */
	public Column getColumn(int measureIndex) {
		Column ret = new Column(getStavesCount());
		for (Staff staff : stavesList.getStaves()) {
			ret.add(staff.getMeasure(measureIndex));
		}
		return ret;
	}
	
	
	/**
   * Gets the interline space of the staff with the given index.
   * If unspecified, the default value of the score is returned.
   */
	public float getInterlineSpace(MP mp) {
		int staffIndex = mp.getStaff();
		if (staffIndex >= 0 && staffIndex < getStavesCount()) {
			Float custom = getStaff(staffIndex).getInterlineSpace();
			if (custom != null) {
				return custom;
			}
		}
		return format.getInterlineSpace();
	}
	
	/**
	 * Gets the gap in beats between the end of the left element and
	 * the start of the right element. If it can not be determined, because
	 * the musical position of one of the elements is unknown, null is returned.
	 */
	public Fraction getGapBetween(VoiceElement left, VoiceElement right) {
		MP mpLeft = MP.Companion.getMP(left);
		if (mpLeft == null)
			return null;
		mpLeft = mpLeft.withBeat(mpLeft.getBeat().add(left.getDuration()));
		MP mpRight = MP.Companion.getMP(right);
		if (mpRight == null)
			return null;
		if (mpRight.getMeasure() == mpLeft.getMeasure()) {
			//simple case: same measure. just subtract beats
			return mpRight.getBeat().sub(mpLeft.getBeat());
		}
		else if (mpRight.getMeasure() > mpLeft.getMeasure()) {
			//right element is in a following measure. add the following:
			//- beats between end of left element and its measure end
			//- beats in the following measures (until the measure which contains the right element)
			//- start beat of the right element in its measure
			Fraction gap = getMeasureFilledBeats(mpLeft.getMeasure()).sub(mpLeft.getBeat());
			for (int iMeasure : range(mpLeft.getMeasure() + 1, mpRight.getMeasure() - 1))
				gap = gap.add(getMeasureFilledBeats(iMeasure));
			gap = gap.add(mpRight.getBeat());
			return gap;
		}
		else {
			//right element is not really at the right, but actually before the left element
			//add the following and sign with minus:
			//- betweens between the start of the right element and the end of its measure
			//- beats in the following measures (until the measure which contains the left element)
			//- end beat of the left element in its measure
			Fraction gap = getMeasureFilledBeats(mpRight.getMeasure()).sub(mpRight.getBeat());
			for (int iMeasure : range(mpRight.getMeasure() + 1, mpLeft.getMeasure() - 1))
				gap = gap.add(getMeasureFilledBeats(iMeasure));
			gap = gap.add(mpLeft.getBeat());
			gap = gap.mult(Companion.fr(-1));
			return gap;
		}
	}

}
