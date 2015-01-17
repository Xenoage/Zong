package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.io.musicxml.in.util.CommandPerformer.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.VoiceAdd;
import com.xenoage.zong.commands.core.music.VoiceElementWrite;
import com.xenoage.zong.commands.core.music.beam.BeamAdd;
import com.xenoage.zong.commands.core.music.slur.SlurAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.InstrumentChange;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MeasureSide;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.StavesList;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.group.StavesRange;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.core.music.slur.SlurType;
import com.xenoage.zong.core.music.slur.SlurWaypoint;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.util.InconsistentScoreException;
import com.xenoage.zong.io.musicxml.in.util.ClosedVolta;
import com.xenoage.zong.io.musicxml.in.util.MusicReaderException;
import com.xenoage.zong.io.musicxml.in.util.OpenElements;
import com.xenoage.zong.io.musicxml.in.util.OpenSlur;
import com.xenoage.zong.io.musicxml.in.util.OpenVolta;
import com.xenoage.zong.utils.exceptions.MeasureFullException;

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
public final class Context {

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

	private ReaderSettings settings;
	
	private VoiceElementWrite.Options writeVoicElementOptions;


	public Context(Score score, ReaderSettings settings) {
		this.score = score;
		this.settings = settings;
		
		//when writing voice elements, alwayse obey time signature and fill gaps
		//with hidden rests
		writeVoicElementOptions = new VoiceElementWrite.Options();
		writeVoicElementOptions.checkTimeSignature = true;
		writeVoicElementOptions.fillWithHiddenRests = true;
	}

	/**
	 * Moves the current beat within the current part and measure.
	 */
	public void moveCurrentBeat(Fraction beat) {
		Fraction newBeat = this.mp.beat.add(beat);
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
	public StavesRange getPartStaffIndices() {
		StavesList stavesList = getScore().getStavesList();
		Part part = stavesList.getPartByStaffIndex(mp.staff);
		return stavesList.getPartStaffIndices(part);
	}

	/**
	 * Gets the number of lines of the staff with the given index, relative
	 * to the current part.
	 */
	public int getStaffLinesCount(int staffIndexInPart) {
		MP mp = this.mp.withStaff(getPartStaffIndices().getStart() + staffIndexInPart);
		return getScore().getStaff(mp).getLinesCount();
	}

	/**
	 * Call this method when a new part begins.
	 */
	public Part beginNewPart(int partIndex) {
		Part part = score.getStavesList().getParts().get(partIndex);
		this.mp = atBeat(score.getStavesList().getPartStaffIndices(part).getStart(), 0, 0, _0);
		List<List<String>> voiceMappings = alist();
		for (int i = 0; i < part.getStavesCount(); i++)
			voiceMappings.add(new ArrayList<String>());
		this.voiceMappings = voiceMappings;
		//reset instrument to null
		this.instrumentID = null;
		return part;
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
	public void registerSlur(SlurType type, WaypointPosition wpPos,
		int number, SlurWaypoint wp, VSide side) {
		checkNumber1to6(number);
		//ignore continue waypoints at the moment
		if (wpPos == WaypointPosition.Continue)
			return;
		boolean start = (wpPos == WaypointPosition.Start);
		boolean stop = !start;
		List<OpenSlur> openSlurs = (type == SlurType.Slur ? openElements.getOpenSlurs()
			: openElements.getOpenTies());
		//get open slur, or create it
		OpenSlur openSlur = openSlurs.get(number - 1);
		if (openSlur == null) {
			openSlur = new OpenSlur();
			openSlur.type = type;
		}
		//this point must not already be set
		if ((start && openSlur.start != null) || (stop && openSlur.stop != null)) {
			if (false == settings.isIgnoringErrors())
				throw new MusicReaderException(wpPos + " waypoint already set for " + type + " " + number,
					this);
		}
		OpenSlur.Waypoint openSlurWP = new OpenSlur.Waypoint();
		openSlurWP.wp = wp;
		openSlurWP.side = side;
		if (start)
			openSlur.start = openSlurWP;
		else
			openSlur.stop = openSlurWP;
		//if complete, write it now, otherwise remember it
		if (openSlur.start != null && openSlur.stop != null) {
			createSlur(openSlur);
			openSlurs.set(number - 1, null);
		}
		else {
			openSlurs.set(number - 1, openSlur);
		}
	}

	/**
	 * Creates and writes a slur or tie from the given {@link OpenSlur} object.
	 */
	private void createSlur(OpenSlur openSlur) {
		if (checkSlur(openSlur)) {
			Slur slur = new Slur(openSlur.type, alist(openSlur.start.wp,
				openSlur.stop.wp), openSlur.start.side);
			writeSlur(slur);
		}
	}
	
	private boolean checkSlur(OpenSlur slur) {
		if (slur.start == null || slur.stop == null)
			return false;
		Fraction gap = score.getGapBetween(slur.start.wp.getChord(), slur.stop.wp.getChord());
		if (slur.type == SlurType.Slur) {
			//slur must end after the start chord
			return gap.compareTo(_0) >= 0;
		}
		else if (slur.type == SlurType.Tie) {
			//tie must end directly after the start chord (no gap)
			return gap.compareTo(_0) == 0;
		}
		return false;
	}

	/**
	 * Sets a start waypoint for a tied element without a number.
	 */
	public void openUnnumberedTied(Pitch pitch, SlurWaypoint wp, VSide side) {
		Map<Pitch, OpenSlur> openUnnumberedTies = openElements.getOpenUnnumberedTies();
		OpenSlur openTied = new OpenSlur();
		openTied.type = SlurType.Tie;
		openTied.start = new OpenSlur.Waypoint();
		openTied.start.wp = wp;
		openTied.start.side = side;
		openUnnumberedTies.put(pitch, openTied);
	}

	/**
	 * Closes a tied element without a number.
	 */
	public void closeUnnumberedTied(Pitch pitch, SlurWaypoint stopWP, VSide side) {
		Map<Pitch, OpenSlur> openUnnumberedTies = openElements.getOpenUnnumberedTies();
		OpenSlur openTied = openUnnumberedTies.get(pitch);
		if (openTied == null) {
			if (false == settings.isIgnoringErrors())
				return;
			else
				throw new InconsistentScoreException("No tie open with pitch " + pitch);
		}
		openUnnumberedTies.remove(pitch);
		openTied.stop = new OpenSlur.Waypoint();
		openTied.stop.wp = stopWP;
		openTied.stop.side = side;
		createSlur(openTied);
	}

	/**
	 * Checks if the given beam/slur/tie number is valid,
	 * i.e. between 1 and 6. Otherwise an
	 * {@link IllegalArgumentException} is thrown.
	 */
	private void checkNumber1to6(int number) {
		if (number < 1 || number > 6) {
			throw new IllegalArgumentException("Number must be between 1 and 6.");
		}
	}

	/**
	 * Sets the beginning of a wedge with the given number.
	 */
	public void openWedge(int number, Wedge wedge) {
		checkNumber1to6(number);
		List<Wedge> openWedges = openElements.getOpenWedges();
		openWedges.set(number - 1, wedge);
	}

	/**
	 * Closes the wedge with the given number and returns it.
	 */
	public Wedge closeWedge(int number) {
		checkNumber1to6(number);
		Wedge ret = openElements.getOpenWedges().get(number - 1);
		List<Wedge> openWedges = openElements.getOpenWedges();
		openWedges.set(number - 1, null);
		return ret;
	}

	/**
	 * Gets the {@link MusicContext} at the current position at the
	 * staff with the given part-intern index.
	 */
	public MusicContext getMusicContext(int staffIndexInPart) {
		MP mp = this.mp.withStaff(getPartStaffIndices().getStart() + staffIndexInPart);
		return score.getMusicContext(mp, Interval.BeforeOrAt, Interval.Before);
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
	public int getVoice(int mxlStaff, String mxlVoice) {
		try {
			//gets the voices list for the given staff
			List<String> voices = voiceMappings.get(mxlStaff);
			//look for the given MusicXML voice
			for (int scoreVoice = 0; scoreVoice < voices.size(); scoreVoice++) {
				if (voices.get(scoreVoice).equals(mxlVoice))
					return scoreVoice;
			}
			//if not existent yet, we have to create it
			voices.add(mxlVoice);
			return voices.size() - 1;
		} catch (IndexOutOfBoundsException ex) {
			throw new RuntimeException("MusicXML staff " + mxlStaff + " and voice \"" + mxlVoice +
				"\" are invalid for the current position. Enough staves defined in attributes?");
		}
	}

	/**
	 * Writes the given {@link ColumnElement} at the
	 * current measure and current beat.
	 */
	public void writeColumnElement(ColumnElement element) {
		new ColumnElementWrite(element, score.getColumnHeader(mp.getMeasure()), mp.getBeat(), null).execute();
	}
	
	/**
	 * Writes the given {@link ColumnElement} at the given measure.
	 */
	public void writeColumnElement(ColumnElement element, int measure) {
		new ColumnElementWrite(element, score.getColumnHeader(measure), MP.unknownBeat, null).execute();
	}
	
	/**
	 * Writes the given {@link ColumnElement} at the current measure.
	 * @param side     the side of the measure. If null, the current beat is used.
	 */
	public void writeColumnElement(ColumnElement element, MeasureSide side) {
		Fraction beat = (side == null ? mp.beat : MP.unknownBeat);
		new ColumnElementWrite(element, score.getColumnHeader(mp.measure), beat, side).execute();
	}

	/**
	 * Writes the given {@link MeasureElement} at the given staff (index relative to first
	 * staff in current part), current measure and current beat.
	 */
	public void writeMeasureElement(MeasureElement element, int staffIndexInPart) {
		int staffIndex = getPartStaffIndices().getStart() + staffIndexInPart;
		MP mp = this.mp.withStaff(staffIndex);
		new MeasureElementWrite(element, score.getMeasure(mp), mp.getBeat()).execute();
	}

	/**
	 * Writes the given {@link VoiceElement} to the current position
	 * without moving the cursor forward.
	 */
	public void writeVoiceElement(VoiceElement element, int staffIndexInPart, int voice) {
		MP mp = this.mp.withStaff(getPartStaffIndices().getStart() + staffIndexInPart).withVoice(voice);
		try {
			//create voice if needed
			Measure measure = score.getMeasure(mp);
			if (measure.getVoices().size() < voice + 1)
				execute(new VoiceAdd(measure, voice));
			execute(new VoiceElementWrite(score.getVoice(mp), mp, element, writeVoicElementOptions));
		} catch (MeasureFullException ex) {
			if (!settings.isIgnoringErrors())
				throw new MusicReaderException(ex, this);
		}
	}

	/**
	 * Moves the cursor forward by one index.
	 */
	public void moveCursorForward(Fraction duration) {
		this.mp = this.mp.withBeat(this.mp.beat.add(duration));
	}

	/**
	 * Creates a beam for the given chords.
	 */
	public void writeBeam(List<Chord> chords) {
		new BeamAdd(Beam.beamFromChords(chords)).execute();
	}
	
	/**
	 * Creates the given slur or tie.
	 */
	public void writeSlur(Slur slur) {
		new SlurAdd(slur).execute();
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
	public void writeInstrumentChange(String instrumentID) {
		//find instrument
		Part part = getScore().getStavesList().getPartByStaffIndex(mp.staff);
		Instrument newInstrument = null;
		for (Instrument instr : it(part.getInstruments())) {
			if (instr.getId().equals(instrumentID)) {
				newInstrument = instr;
				break;
			}
		}
		if (newInstrument == null) {
			//error: instrument is unknown to this part
			if (settings.isIgnoringErrors())
				return; //don't change instrument
			else
				throw new InconsistentScoreException("Unknown instrument: \"" + instrumentID + "\"");
		}
		//apply instrument change
		new MeasureElementWrite(new InstrumentChange(newInstrument), score.getMeasure(this.mp), mp.beat).execute();
	}

	public void openVolta(Range numbers, String caption) {
		if (openElements.getOpenVolta() == null)
			openElements.setOpenVolta(new OpenVolta(mp.measure, numbers, caption));
	}

	public ClosedVolta closeVolta(boolean rightHook) {
		OpenVolta openVolta = openElements.getOpenVolta();
		if (openVolta == null) {
			if (settings.isIgnoringErrors())
				return null;
			else 
				throw new InconsistentScoreException("No volta open at " + mp);
		}
		int length = mp.measure - openVolta.startMeasure + 1;
		Volta volta = new Volta(length, openVolta.numbers, openVolta.caption, rightHook);
		openElements.setOpenVolta(null);
		return new ClosedVolta(volta, openVolta.startMeasure);
	}
	
	public ReaderSettings getSettings() {
		return settings;
	}

	@Override public String toString() {
		return "cursor at " + mp + ", system " + systemIndex + ", page " + pageIndex;
	}

}
