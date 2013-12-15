package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.music.beam.Beam.beam;
import static com.xenoage.zong.core.position.MP.atBeat;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.group.StavesRange;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.util.InconsistentScoreException;
import com.xenoage.zong.io.musicxml.in.util.MusicReaderException;
import com.xenoage.zong.io.musicxml.in.util.OpenCurvedLine;
import com.xenoage.zong.io.musicxml.in.util.OpenElements;
import com.xenoage.zong.util.exceptions.MeasureFullException;

/**
 * This class stores the current context when
 * reading a MusicXML 2.0 document.
 * 
 * Example for context variables are the
 * current divisions value or open ties.
 *
 * @author Andreas Wenger
 */
@Getter @Setter
public final class MusicReaderContext {

	private Score score;
	private MP mp = MP.mp0;
	private int divisions = 1;

	//current system and page index. only valid if system breaks and page breaks
	//are used continuously
	private int systemIndex = 0;
	private int pageIndex = 0;

	//maps MusicXML staff-/voice-element to our voice:
	//musicXMLVoice == voiceMappings.get(musicXMLStaff).get(scoreVoice)
	private List<List<String>> voiceMappings = alist();

	private OpenElements openElements = new OpenElements();

	private String instrumentID = null;

	private MusicReaderSettings settings;


	public MusicReaderContext(Score score, MusicReaderSettings settings) {
		this.score = score;
		this.settings = settings;
	}

	/**
	 * Moves the current beat within the current part and measure.
	 */
	public MusicReaderContext moveCurrentBeat(Fraction beat) {
		Fraction newBeat = bmp.beat.add(beat);
		//never step back behind 0
		if (newBeat.getNumerator() < 0) {
			if (settings.isIgnoringErrors())
				newBeat = _0;
			else
				throw new MusicReaderException("Step back behind beat 0", this);
		}
		this.mp = this.mp.withBeat(newBeat);
	}

	/**
	 * Gets the current system index.
	 * The value is only valid if system breaks and page breaks
	 * are used continuously.
	 */
	public int getSystemIndex() {
		return systemIndex;
	}

	/**
	 * Increments the current system index.
	 */
	public void incSystemIndex() {
		this.systemIndex++;
	}

	/**
	 * Gets the current page index.
	 * The value is only valid if system breaks and page breaks
	 * are used continuously.
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * Increments the current page index.
	 */
	public void incPageIndex() {
		this.pageIndex++;
	}

	/**
	 * Gets the global value of a tenth in mm. 
	 */
	public float getTenthMm() {
		return getScore().getFormat().getInterlineSpace() / 10;
	}

	/**
	 * Gets the indices of the staves of the current part.
	 */
	public StavesRange getPartStaves() {
		StavesList stavesList = getScore().getStavesList();
		Part part = stavesList.getPartByStaffIndex(mp.staff);
		return stavesList.getPartStaves(part);
	}

	/**
	 * Gets the number of lines of the staff with the given index, relative
	 * to the current part.
	 */
	public int getStaffLinesCount(int staffIndexInPart) {
		MP mp = this.mp.withStaff(getPartStaves().getStart() + staffIndexInPart);
		return getScore().getStaff(mp).getLinesCount();
	}

	/**
	 * Call this method when a new part begins.
	 */
	public void beginNewPart(int partIndex) {
		Part part = score.getStavesList().getParts().get(partIndex);
		this.mp = atBeat(score.getStavesList().getPartStaves(part).getStart(), 0, 0, _0);
		List<List<String>> voiceMappings = alist();
		for (int i = 0; i < part.getStavesCount(); i++)
			voiceMappings.add(new ArrayList<String>());
		this.voiceMappings = voiceMappings;
		//reset instrument to null
		this.instrumentID = null;
	}

	/**
	 * Call this method when a new measure begins.
	 */
	public void beginNewMeasure(int measureIndex) {
		this.mp = atBeat(this.mp.staff, measureIndex, 0, _0);
	}

	/**
	 * Opens a new beam with the given number.
	 */
	public void openBeam(int number) {
		checkNumber1to6(number);
		List<List<Chord>> openBeams = this.openElements.getOpenBeams();
		if (openBeams.get(number - 1) != null) {
			//error: this beam is already open
			if (false == settings.isIgnoringErrors()) {
				throw new MusicReaderException("Beam " + number + " already open", this);
			}
		}
		//open beam
		openBeams.set(number - 1, new ArrayList<Chord>());
	}

	/**
	 * Adds the given chord to the open beam
	 * with the given number.
	 */
	public void addBeamChord(Chord chord, int number) {
		checkNumber1to6(number);
		List<List<Chord>> openBeams = this.openElements.getOpenBeams();
		if (openBeams.get(number - 1) == null) {
			//error: this beam is not open
			if (false == settings.isIgnoringErrors())
				throw new MusicReaderException("Beam " + number + " not open", this);
			return;
		}
		openBeams.get(number - 1).add(chord);
	}

	/**
	 * Closes the open beam with the given number
	 * and returns its chords.
	 */
	public List<Chord> closeBeam(int number) {
		checkNumber1to6(number);
		List<List<Chord>> openBeams = this.openElements.getOpenBeams();
		List<Chord> ret = openBeams.get(number - 1);
		openBeams.set(number - 1, null);
		return ret;
	}

	/**
	 * Sets a waypoint for a slur or tie with the given number.
	 * When all required waypoints of the slur are set, the slur is created.
	 * 
	 * For tied elements without a number, use openUnnumberedTied instead.
	 */
	public void registerCurvedLine(SlurType type, WaypointPosition wpPos,
		int number, SlurWaypoint wp, VSide side) {
		checkNumber1to6(number);
		//ignore continue waypoints at the moment
		if (wpPos == WaypointPosition.Continue)
			return;
		boolean start = (wpPos == WaypointPosition.Start);
		boolean stop = !start;
		List<OpenCurvedLine> openCLs = (type == SlurType.Slur ? openElements.getOpenSlurs()
			: openElements.getOpenTies());
		//get open slur, or create it
		OpenCurvedLine openCL = openCLs.get(number - 1);
		if (openCL == null) {
			openCL = new OpenCurvedLine();
			openCL.type = type;
		}
		//this point must not already be set
		if ((start && openCL.start != null) || (stop && openCL.stop != null)) {
			if (!settings.ignoreErrors)
				throw new MusicReaderException(wpPos + " waypoint already set for " + type + " " + number,
					this);
		}
		OpenCurvedLine.Waypoint wp = new OpenCurvedLine.Waypoint();
		wp.wp = wp;
		wp.side = side;
		if (start)
			openCL.start = wp;
		else
			openCL.stop = wp;
		//if complete, write it now, otherwise remember it
		MusicReaderContext ret = this;
		if (openCL.start != null && openCL.stop != null) {
			ret = ret.createCurvedLine(openCL);
			openCLs = openCLs.with(number - 1, null);
		}
		else {
			openCLs = openCLs.with(number - 1, openCL);
		}
		return new MusicReaderContext(ret.score, ret.bmp, ret.divisions, ret.systemIndex,
			ret.pageIndex, ret.voiceMappings,
			(type == Type.Slur ? ret.openElements.withOpenSlurs(openCLs) : ret.openElements
				.withOpenTies(openCLs)), ret.instrumentID, ret.settings);
	}

	/**
	 * Creates and writes a curved line from the given {@link OpenCurvedLine} object.
	 */
	private MusicReaderContext createCurvedLine(OpenCurvedLine openCurvedLine) {
		if (openCurvedLine.start != null && openCurvedLine.stop != null) {
			CurvedLine cl = new CurvedLine(openCurvedLine.type, pvec(openCurvedLine.start.wp,
				openCurvedLine.stop.wp), openCurvedLine.start.side);
			return writeCurvedLine(cl);
		}
		return this;
	}

	/**
	 * Sets a start waypoint for a tied element without a number.
	 */
	public MusicReaderContext openUnnumberedTied(Pitch pitch, CurvedLineWaypoint clwp, VSide side) {
		PMap<Pitch, OpenCurvedLine> openUnnumberedTies = openElements.getOpenUnnumberedTies();
		OpenCurvedLine openCL = new OpenCurvedLine();
		openCL.type = Type.Tie;
		openCL.start = new OpenCurvedLine.Waypoint();
		openCL.start.wp = clwp;
		openCL.start.side = side;
		openUnnumberedTies = openUnnumberedTies.plus(pitch, openCL);
		return new MusicReaderContext(score, bmp, divisions, systemIndex, pageIndex, voiceMappings,
			openElements.withOpenUnnumberedTies(openUnnumberedTies), instrumentID, settings);
	}

	/**
	 * Closes a tied element without a number.
	 */
	public MusicReaderContext closeUnnumberedTied(Pitch pitch, CurvedLineWaypoint stopWP, VSide side) {
		PMap<Pitch, OpenCurvedLine> openUnnumberedTies = openElements.getOpenUnnumberedTies();
		OpenCurvedLine openCL = openUnnumberedTies.get(pitch);
		if (openCL == null) {
			if (settings.ignoreErrors)
				return this;
			else
				throw new InconsistentScoreException("No tie open with pitch " + pitch);
		}
		openUnnumberedTies = openUnnumberedTies.minus(pitch);
		openCL.stop = new OpenCurvedLine.Waypoint();
		openCL.stop.wp = stopWP;
		openCL.stop.side = side;
		MusicReaderContext ret = this;
		ret = ret.createCurvedLine(openCL);
		return new MusicReaderContext(ret.score, ret.bmp, ret.divisions, ret.systemIndex,
			ret.pageIndex, ret.voiceMappings,
			ret.openElements.withOpenUnnumberedTies(openUnnumberedTies), ret.instrumentID, ret.settings);
	}

	/**
	 * Checks if the given beam/slur/tie number is valid,
	 * i.e. between 1 and 6. Otherwise an
	 * IllegalArgumentException is thrown.
	 */
	private void checkNumber1to6(int number) {
		if (number < 1 || number > 6) {
			throw new IllegalArgumentException("Number must be between 1 and 6.");
		}
	}

	/**
	 * Sets the beginning of a wedge with the given number.
	 */
	public MusicReaderContext openWedge(int number, Wedge wedge) {
		checkNumber1to6(number);
		PVector<Wedge> openWedges = openElements.getOpenWedges().with(number - 1, wedge);
		return new MusicReaderContext(score, bmp, divisions, systemIndex, pageIndex, voiceMappings,
			openElements.withOpenWedges(openWedges), instrumentID, settings);
	}

	/**
	 * Closes the wedge with the given number and returns it.
	 */
	public Tuple2<MusicReaderContext, Wedge> closeWedge(int number) {
		checkNumber1to6(number);
		Wedge ret = openElements.getOpenWedges().get(number - 1);
		PVector<Wedge> openWedges = openElements.getOpenWedges().with(number - 1, null);
		MusicReaderContext context = new MusicReaderContext(score, bmp, divisions, systemIndex,
			pageIndex, voiceMappings, openElements.withOpenWedges(openWedges), instrumentID, settings);
		return t(context, ret);
	}

	/**
	 * Gets the {@link MusicContext} at the current position at the
	 * staff with the given part-intern index.
	 */
	public MusicContext getMusicContext(int staffIndexInPart) {
		BMP bmp = this.bmp.withStaff(getPartStavesIndices().getStart() + staffIndexInPart);
		return ScoreController.getMusicContext(score, bmp, Interval.BeforeOrAt, Interval.Before);
	}

	/**
	 * Gets the voice index for the given MusicXML voice and MusicXML staff.
	 * This is needed, because voices are defined for staves in this program, while
	 * voices are defined for parts in MusicXML. If the voice does not exist yet, it
	 * is created.
	 * @param mxlStaff  0-based staff index, found in staff-element minus 1
	 * @param mxlVoice  voice id, found in voice-element
	 * @return the updated context and the voice index
	 */
	public Tuple2<MusicReaderContext, Integer> getVoice(int mxlStaff, String mxlVoice) {
		try {
			//gets the voices list for the given staff
			PVector<String> voices = voiceMappings.get(mxlStaff);
			//look for the given MusicXML voice
			for (int scoreVoice = 0; scoreVoice < voices.size(); scoreVoice++) {
				if (voices.get(scoreVoice).equals(mxlVoice))
					return t(this, scoreVoice);
			}
			//if not existent yet, we have to create it
			voices = voices.plus(mxlVoice);
			MusicReaderContext context = new MusicReaderContext(score, bmp, divisions, systemIndex,
				pageIndex, voiceMappings.with(mxlStaff, voices), openElements, instrumentID, settings);
			return t(context, voices.size() - 1);
		} catch (IndexOutOfBoundsException ex) {
			throw new RuntimeException("MusicXML staff " + mxlStaff + " and voice \"" + mxlVoice +
				"\" are invalid for the current position. Enough staves defined in attributes?");
		}
	}

	/**
	 * Writes the given {@link ColumnElement} at the
	 * current measure and current beat.
	 */
	public MusicReaderContext writeColumnElement(ColumnElement element) {
		return withScore(ScoreController.writeColumnElement(score, bmp, null, element));
	}

	/**
	 * Writes the given {@link MeasureElement} at the given staff (index relative to first
	 * staff in current part), current measure and current beat.
	 */
	public MusicReaderContext writeMeasureElement(MeasureElement element, int staffIndexInPart) {
		int staffIndex = getPartStavesIndices().getStart() + staffIndexInPart;
		BMP bmp = this.bmp.withStaff(staffIndex);
		return withScore(ScoreController.writeMeasureElement(score, bmp, element));
	}

	/**
	 * Writes the given {@link VoiceElement} to the current position
	 * without moving the cursor forward.
	 */
	public MusicReaderContext writeVoiceElement(VoiceElement element, int staffIndexInPart, int voice) {
		Score score = this.score;
		BMP bmp = this.bmp.withStaff(getPartStavesIndices().getStart() + staffIndexInPart).withVoice(
			voice);
		try {
			score = ScoreController.writeVoiceElement(score, bmp, element, true);
		} catch (MeasureFullException ex) {
			if (!settings.ignoreErrors)
				throw new MusicReaderException(ex, this);
		}
		return new MusicReaderContext(score, bmp, divisions, systemIndex, pageIndex, voiceMappings,
			openElements, instrumentID, settings);
	}

	/**
	 * Moves the cursor forward by one index.
	 */
	public MusicReaderContext moveCursorForward(Fraction duration) {
		BMP bmp = this.bmp.withBeat(this.bmp.beat.add(duration));
		return new MusicReaderContext(score, bmp, divisions, systemIndex, pageIndex, voiceMappings,
			openElements, instrumentID, settings);
	}

	/**
	 * Creates a beam for the given chords.
	 */
	public MusicReaderContext writeBeam(PVector<Chord> chords) {
		try {
			return withScore(ScoreController.plusBeam(getScore(), beam(chords)));
		} catch (InconsistentScoreException ex) {
			if (!settings.ignoreErrors)
				throw new MusicReaderException(ex, this);
			else
				return this; //when ignoring errors, ignore beam
		}
	}

	/**
	 * Adds a curved line.
	 */
	public MusicReaderContext writeCurvedLine(CurvedLine cl) {
		try {
			return withScore(ScoreController.plusCurvedLine(getScore(), cl));
		} catch (InconsistentScoreException ex) {
			if (!settings.ignoreErrors)
				throw new MusicReaderException(ex, this);
			else
				return this; //when ignoring errors, ignore curved line
		}
	}

	/**
	 * Writes an attachment to the given element.
	 */
	public MusicReaderContext writeAttachment(MusicElement anchor, Attachable attachment) {
		return withScore(getScore().withGlobals(getScore().globals.plusAttachment(anchor, attachment)));
	}

	/**
	 * Replaces the given old chord by the given new one.
	 * It must have the same duration like the chord which was already there.
	 * If the old chord had a beam, slur, directions or lyrics, they will be used
	 * again.
	 */
	public MusicReaderContext replaceChord(Chord oldChord, Chord newChord) {
		try {
			return withScore(ScoreController.replaceChord(getScore(), oldChord, newChord));
		} catch (InconsistentScoreException ex) {
			if (settings.ignoreErrors)
				return this;
			else
				throw ex;
		}
	}

	/**
	 * Returns the ID of the current instrument. May also return null
	 * for "default instrument" or "no change".
	 */
	public String getInstrumentID() {
		return instrumentID;
	}

	/**
	 * Changes the current instrument.
	 */
	public MusicReaderContext writeInstrumentChange(String instrumentID) {
		//find instrument
		Part part = getScore().stavesList.getPartByStaffIndex(bmp.staff);
		Instrument newInstrument = null;
		for (Instrument instr : part.getInstruments()) {
			if (instr.base.id.equals(instrumentID)) {
				newInstrument = instr;
				break;
			}
		}
		if (newInstrument == null) {
			//error: instrument is unknown to this part
			if (settings.ignoreErrors)
				return this; //don't change instrument
			else
				throw new InconsistentScoreException("Unknown instrument: \"" + instrumentID + "\"");
		}
		//apply instrument change
		MusicReaderContext ret = new MusicReaderContext(score, bmp, divisions, systemIndex, pageIndex,
			voiceMappings, openElements, instrumentID, settings);
		ret = ret.writeMeasureElement(new InstrumentChange(newInstrument), 0);
		return ret;
	}

	public MusicReaderSettings getSettings() {
		return settings;
	}

	@Override public String toString() {
		return "cursor at " + bmp + ", system " + systemIndex + ", page " + pageIndex;
	}

}
