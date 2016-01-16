package com.xenoage.zong.core;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.header.ScoreHeader.scoreHeader;
import static com.xenoage.zong.core.music.util.BeatE.selectLatest;
import static com.xenoage.zong.core.music.util.Interval.At;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.core.music.util.MPE.mpE;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.MP.mpb0;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.xenoage.utils.annotations.Unneeded;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.info.ScoreInfo;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.key.TraditionalKey.Mode;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.music.util.Column;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.music.util.MPE;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.utils.exceptions.IllegalMPException;


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
	@Getter @Setter @NonNull private ScoreHeader header = scoreHeader(this);
	/** The list of staves, parts and groups. */
	@Getter @Setter @NonNull private StavesList stavesList = new StavesList();
	/** Additional meta information. This other application-dependend meta-information
	 * can be used for example to store page layout information, which is not part
	 * of the musical score in this project. */
	@Getter private Map<String, Object> metaData = new HashMap<String, Object>();
	
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
			if (mp.staff < 0 || mp.staff >= getStavesCount() || mp.measure < 0 || mp.measure >= getMeasuresCount() ||
				mp.voice < 0 || mp.voice >= getMeasure(mp).getVoices().size())
				return false;
			Voice voice = getVoice(mp);
			if (mp.element != MP.unknown && voice.getElements().size() <= mp.element)
				return false;
			if (mp.beat != MP.unknownBeat && (mp.beat.compareTo(_0) < 0 ||
				mp.beat.compareTo(voice.getParent().getFilledBeats()) > 0))
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
	 */
	public Fraction getMeasureFilledBeats(int measureIndex) {
		Fraction maxBeat = Fraction._0;
		for (Staff staff : stavesList.getStaves()) {
			Fraction beat = staff.getMeasures().get(measureIndex).getFilledBeats();
			if (beat.compareTo(maxBeat) > 0)
				maxBeat = beat;
		}
		return maxBeat;
	}
	
	
	/**
	 * Gets the used beats within the given measure column.
	 */
	public SortedList<Fraction> getMeasureUsedBeats(int measureIndex) {
		SortedList<Fraction> columnBeats = new SortedList<Fraction>(false);
		for (int iStaff : range(getStavesCount())) {
			columnBeats = columnBeats.merge(getMeasure(atMeasure(iStaff, measureIndex)).getUsedBeats(), false);
		}
		return columnBeats;
	}
	
	
	/**
	 * Clips a {@link MP} to a measure. If the {@link MP} is before or at the
	 * beginning of the measure with measureIndex, the first beat is returned.
	 * If it is after the measure, the last beat is returned.
	 * If the {@link BP} is in the measure, this position is returned.
	 * @param measureIndex	the index of the measure that to which the position is clipped
	 * @param mp	          the position which is clipped
	 */
	@Unneeded public MP clipToMeasure(int measureIndex, MP mp) {
		Fraction beat = null;
		if (mp.measure < measureIndex) {
			beat = Fraction._0;
		}
		else if (measureIndex == mp.measure) {
			Fraction endBeat = getMeasureBeats(measureIndex);
			beat = (endBeat.compareTo(mp.beat) < 0 ? endBeat : mp.beat);
		}
		else {
			beat = getMeasureBeats(measureIndex);
		}
		return mp.withMeasure(measureIndex).withBeat(beat);
	}
	
	
	/**
	 * Gets the last {@link Key} that is defined before (or at,
	 * dependent on the given {@link Interval}) the given
	 * {@link BMP}, also over measure boundaries. If there is
	 * none, a default C major time signature is returned.
	 * Private keys (in measure) override public keys (in measure column header). 
	 */
	@SuppressWarnings("unchecked")
	public MPE<? extends Key> getKey(MP mp, Interval interval) {
		if (!interval.isPrecedingOrAt()) {
			throw new IllegalArgumentException("Illegal interval for this method");
		}
		//begin with the given measure. if there is one, return it.
		BeatE<Key> columnKey = header.getColumnHeader(mp.measure).getKeys().getLastBefore(interval, mp.beat);
		BeatE<Key> measureKey = null;
		if (getMeasure(mp).getPrivateKeys() != null)
			measureKey = getMeasure(mp).getPrivateKeys().getLastBefore(interval, mp.beat);
		BeatE<Key> ret = selectLatest(columnKey, measureKey);
		if (ret != null)
			return mpE(ret.element, mp.withBeat(ret.beat));
		if (interval != At) {
			//search in the preceding measures
			for (int iMeasure = mp.measure - 1; iMeasure >= 0; iMeasure--) {
				columnKey = header.getColumnHeader(iMeasure).getKeys().getLast();
				BeatEList<Key> privateKeys = getMeasure(atMeasure(mp.staff, iMeasure)).getPrivateKeys();
				measureKey = (privateKeys != null ? privateKeys.getLast() : null);
				ret = selectLatest(columnKey, measureKey);
				if (ret != null)
					return mpE(ret.element, mp.withMeasure(iMeasure).withBeat(ret.beat));
			}
		}
		//no key found. return default key.
		return mpE(new TraditionalKey(0, Mode.Major), mpb0);
	}
	
	
	/**
	 * Gets the accidentals at the given {@link MP} that are
	 * valid before or at the given beat (depending on the given interval),
	 * looking at all voices. The beat in the {@link MP} is required.
	 */
	public Map<Pitch, Integer> getAccidentals(MP mp, Interval interval) {
		if (mp.beat == MP.unknownBeat)
			throw new IllegalArgumentException("beat is required");
		//start key of the measure always counts
		MPE<? extends Key> key = getKey(mp, BeforeOrAt);
		//if key change is in same measure, start at that beat. otherwise start at beat 0.
		Fraction keyBeat = (key.mp.measure == mp.measure ? key.mp.beat : _0);
		Measure measure = getMeasure(mp);
		return measure.getAccidentals(mp.beat, interval, keyBeat, key.element);
	}
	
	
	/**
	 * Gets the last {@link Clef} that is defined before (or at,
	 * dependent on the given {@link Interval}) the given
	 * {@link MP}, also over measure boundaries. If there is
	 * none, a default g clef is returned. The beat in the {@link MP} is required.
	 */
	public Clef getClef(MP mp, Interval interval) {
		if (!interval.isPrecedingOrAt())
			throw new IllegalArgumentException("Illegal interval for this method");
		if (mp.beat == MP.unknownBeat)
			throw new IllegalArgumentException("beat is required");
		//begin with the given measure. if there is one, return it.
		Measure measure = getMeasure(mp);
		BeatE<Clef> ret = null;
		if (measure.getClefs() != null) {
			ret = measure.getClefs().getLastBefore(interval, mp.beat);
			if (ret != null)
				return ret.element;
		}
		if (interval != At) {
			//search in the preceding measures
			for (int iMeasure : rangeReverse(mp.measure - 1, 0)) {
				measure = getMeasure(atMeasure(mp.staff, iMeasure));
				if (measure.getClefs() != null) {
					ret = measure.getClefs().getLast();
					if (ret != null)
						return ret.element;
				}
			}
		}
		//no clef found. return default clef.
		return new Clef(ClefType.clefTreble);
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
		Clef clef = getClef(mp, clefAndKeyInterval);
		Key key = getKey(mp, clefAndKeyInterval).element;
		Map<Pitch, Integer> accidentals = null;
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
						actualdivision = MathUtils.lcm(actualdivision, e.getDuration().getDenominator());
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
		int staffIndex = mp.staff;
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
	 * the start of the right element.
	 */
	public Fraction getGapBetween(VoiceElement left, VoiceElement right) {
		MP mpLeft = MP.getMP(left);
		mpLeft = mpLeft.withBeat(mpLeft.beat.add(left.getDuration()));
		MP mpRight = MP.getMP(right);
		if (mpRight.measure == mpLeft.measure) {
			//simple case: same measure. just subtract beats
			return mpRight.beat.sub(mpLeft.beat);
		}
		else if (mpRight.measure > mpLeft.measure) {
			//right element is in a following measure. add the following:
			//- beats between end of left element and its measure end
			//- beats in the following measures (until the measure which contains the right element)
			//- start beat of the right element in its measure
			Fraction gap = getMeasureFilledBeats(mpLeft.measure).sub(mpLeft.beat);
			for (int iMeasure : range(mpLeft.measure + 1, mpRight.measure - 1))
				gap = gap.add(getMeasureFilledBeats(iMeasure));
			gap = gap.add(mpRight.beat);
			return gap;
		}
		else {
			//right element is not really at the right, but actually before the left element
			//add the following and sign with minus:
			//- betweens between the start of the right element and the end of its measure
			//- beats in the following measures (until the measure which contains the left element)
			//- end beat of the left element in its measure
			Fraction gap = getMeasureFilledBeats(mpRight.measure).sub(mpRight.beat);
			for (int iMeasure : range(mpRight.measure + 1, mpLeft.measure - 1))
				gap = gap.add(getMeasureFilledBeats(iMeasure));
			gap = gap.add(mpLeft.beat);
			gap = gap.mult(fr(-1));
			return gap;
		}
	}

}
