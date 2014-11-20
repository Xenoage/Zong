package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.io.midi.out.MidiVelocityConverter.getVelocityAtPosition;
import static com.xenoage.zong.io.midi.out.MidiVelocityConverter.getVoiceforDynamicsInStaff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.midi.out.Playlist.PlayRange;

/**
 * This class creates a {@link MidiSequence} from a given {@link Score}.
 * 
 * @param <T> the platform-specific sequence class
 * 
 * TIDY
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class MidiConverter<T> {

	public static final int channel10 = 9; //channel 10 has index 9 (0-based)

	//in MIDI "Format 1" files, the track for tempo changes and so on is track 0 by convention
	private static int systemTrackIndex = 0;
	
	//state
	private Score score;
	private MidiSettings settings;
	private MidiSequenceWriter<T> writer;
	private boolean addMPEvents;
	private boolean metronome;
	private Fraction durationFactor;
	private int resolution;


	/**
	 * Converts a {@link Score} to a {@link MidiSequence}.
	 * @param score        the score to convert
	 * @param addMPEvents  true, if controller events containing the current musical position
	 *                     should be inserted in the sequence, otherwise false
	 * @param metronome    true to add metronome ticks, otherwise false
	 * @param writer          the writer for the midi data
	 */
	public static <T> MidiSequence<T> convertToSequence(Score score, boolean addMPEvents,
		boolean metronome, MidiSequenceWriter<T> writer) {
		return convertToSequence(score, addMPEvents, metronome, null, writer);
	}

	/**
	 * Converts a {@link Score} to a {@link MidiSequence}.
	 * @param score           the score to convert
	 * @param addMPEvents     true, if controller events containing the current musical position
	 *                        should be inserted in the sequence, otherwise false
	 * @param metronome       true to add metronome ticks, otherwise false
	 * @param durationFactor  factor to multiply all note durations with, or null
	 * @param writer          the writer for the midi data
	 */
	public static <T> MidiSequence<T> convertToSequence(Score score, boolean addMPEvents,
		boolean metronome, Fraction durationFactor, MidiSequenceWriter<T> writer) {
		return new MidiConverter<T>(score, writer, addMPEvents, metronome, durationFactor).convertToSequence();
	}
	
	private MidiConverter(Score score, MidiSequenceWriter<T> writer, boolean addMPEvents,
		boolean metronome, Fraction durationFactor) {
		this.score = score;
		this.settings = new MidiSettings(); //TODO: by parameter
		this.writer = writer;
		this.addMPEvents = addMPEvents;
		this.metronome = metronome;
		this.durationFactor = durationFactor;
	}
	
	private MidiSequence<T> convertToSequence() {
		ArrayList<Long> measureStartTicks = alist();
		ArrayList<MidiTime> timePool = alist();
		int stavesCount = score.getStavesCount();
		
		//compute mapping of staff indices to channel numbers
		int[] channelMap = createChannelMap(score);
		//compute playlist (which contains repetitions and so on)
		Playlist playlist = MidiRepetitionCalculator.createPlaylist(score);
		
		//one track for each staff and one system track for program changes, tempos and so on,
		//and maybe another track for the metronome
		int tracksCount = stavesCount + 1;
		Integer metronomeTrack = null;
		if (metronome) {
			metronomeTrack = tracksCount;
			tracksCount++;
		}
		//resolution in ticks per quarter
		resolution = score.getDivisions() * settings.getResolutionFactor();
		//init writer
		writer.init(tracksCount, resolution);
		
		//activate MIDI programs
		int partFirstStaff = 0;
		for (Part part : score.getStavesList().getParts()) {
			Instrument instrument = part.getFirstInstrument();
			if (instrument instanceof PitchedInstrument) {
				PitchedInstrument pitchedInstrument = (PitchedInstrument) instrument;
				int channel = channelMap[partFirstStaff];
				//program change
				if (channel > -1) {
					writer.writeProgramChange(systemTrackIndex, channel, 0, (pitchedInstrument).getMidiProgram());
				}
				//general volume for this instrument
				if (instrument.getVolume() != null) {
					writer.writeVolumeChange(systemTrackIndex, channel, 0, instrument.getVolume());
				}
				//general panning for this instrument
				if (instrument.getPan() != null) {
					writer.writePanChange(systemTrackIndex, channel, 0, instrument.getPan());
				}
			}
			partFirstStaff += part.getStavesCount();
		}

		//used beats in each measure column
		Fraction[] realMeasureColumnBeats = new Fraction[score.getMeasuresCount()];
		for (int i : range(score.getMeasuresCount())) {
			realMeasureColumnBeats[i] = score.getMeasureFilledBeats(i);
		}

		//fill tracks
		for (int iStaff : range(stavesCount)) {
			int channel = channelMap[iStaff];
			if (channel == -1) {
				continue; //no MIDI channel left for this staff
			}

			long currenttickinstaff = 0;
			Staff staff = score.getStaff(iStaff);

			int voicesCount = staff.getVoicesCount();
			int voicesVelocity[] = new int[voicesCount];
			Arrays.fill(voicesVelocity, 90);  

			int track = iStaff;

			int[] voiceforDynamicsInStaff = getVoiceforDynamicsInStaff(staff);
			for (PlayRange playRange : playlist.getRanges()) {
				int transposing = 0;
				for (int iMeasure : range(playRange.from.measure, playRange.to.measure)) {
					Measure measure = staff.getMeasure(iMeasure);
					measureStartTicks.add(currenttickinstaff);

					/*/TODO: transposition changes can happen everywhere in the measure
					Transpose t = measure.getInstrumentChanges()...
					if (t != null)
					{
						transposing = t.chromatic;
					} //*/

					Fraction start, end;
					start = score.clipToMeasure(iMeasure, playRange.from).beat;
					end = score.clipToMeasure(iMeasure, playRange.to).beat;

					if (realMeasureColumnBeats[iMeasure].compareTo(end) < 0)
						end = realMeasureColumnBeats[iMeasure];

					for (int iVoice : range(measure.getVoices())) {
						Voice voice = measure.getVoice(iVoice);
						int currentVelocity = voicesVelocity[voiceforDynamicsInStaff[iVoice]];
						voicesVelocity[iVoice] = generateMidiForVoice(resolution, channel, currenttickinstaff, staff,
							track, currentVelocity, iVoice, voice, start, end, transposing);
					}
					Fraction measureduration = end.sub(start);
					currenttickinstaff += calculateTickFromFraction(measureduration, resolution);
				}
			}
		}

		if (addMPEvents) {
			createControlEventChannel(measureStartTicks, timePool, 0, playlist); //score position events in channel 0
			addPlaybackAtEndControlEvent();
		}

		if (metronome) {
			createMetronomeTrack(metronomeTrack, playlist, measureStartTicks);
		}

		//Add Tempo Changes
		/*ArrayList<MidiElement> tempo = MidiTempoConverter.getTempo(score, playList);
		for (MidiElement midiElement : tempo)
		{
			long startTick = measureStartTick.get(midiElement.getMeasure());
			long beat = startTick + (long)midiElement.getPosition().toFloat() * resolution;
			MidiEvent event = new MidiEvent(midiElement.getMidiMessage(), beat);
			tempoTrack.add(event);
		}*/
		MidiTempoConverter.writeTempoTrack(score, playlist, resolution, writer, systemTrackIndex);

		return writer.finish(metronomeTrack, timePool, measureStartTicks);
	}

	/**
	 * Generates the MIDI data for the given voice.
	 * Returns the velocity at the end position.
	 */
	private int generateMidiForVoice(int resolution, int channel, long currenttickinstaff,
		Staff staff, int track, int currentVelocity, int iVoice, Voice voice, Fraction start,
		Fraction end, int transposing) {
		long currenttickinvoice = currenttickinstaff;
		for (VoiceElement element : voice.getElements()) {
			Fraction duration = element.getDuration();
			Fraction elementBeat = voice.getBeat(element);
			if (!isInRange(elementBeat, duration, start, end))
				continue;
			Fraction startBeat = elementBeat;
			Fraction endBeat = startBeat.add(duration);
			long starttick = currenttickinvoice;
			long endtick = calculateEndTick(startBeat, endBeat, start, end, currenttickinvoice,
				resolution);

			//custom duration factor
			long stoptick = endtick;
			if (durationFactor != null) {
				Fraction stopBeat = startBeat.add(duration.mult(durationFactor));
				stoptick = calculateEndTick(startBeat, stopBeat, start, end, currenttickinvoice, resolution);
			}

			if (starttick != stoptick && element instanceof Chord) {
				Chord chord = (Chord) element;
				for (Note note : chord.getNotes()) {
					currentVelocity = addNoteToTrack(channel, staff, track, currentVelocity, iVoice,
						starttick, stoptick, chord, note, transposing);
				}
			}
			//TODO Timidity doesn't like ithe following midi events
			/*MetaMessage m = null;
			if (musicelement instanceof Clef)
			{
				Clef c = (Clef) musicelement;
				m = createMidiEvent(c, tracknumber);
			}
			else if (musicelement instanceof NormalTime)
			{
				NormalTime t = (NormalTime) musicelement;
				m = createMidiEvent(t, resolution, tracknumber);
			}
			else if (musicelement instanceof Key)
			{
				Key k = (Key) musicelement;
				m = createMidiEvent(k, tracknumber);
			}
			else if (musicelement instanceof Tempo)
			{
				Tempo tempo = (Tempo)musicelement;
				m = MidiTempoConverter.createMetaMessage(tempo);
			}
			if (m != null)
			{
				MidiEvent event = new MidiEvent(m, starttick);
				track.add(event);
			}*/
			currenttickinvoice = endtick;
		}
		return currentVelocity;
	}

	/* UNUSED
	private static MetaMessage createMidiEvent(Key k, int tracknumber)
		throws InvalidMidiDataException
	{
		MetaMessage m = new MetaMessage();
		int type = 0x59;
		byte mm = (byte) tracknumber;
		byte sf = 0;
		for (int a : k.getAlterations())
		{
			if (a != 0)
			{
				sf++;
			}
		}
		byte mi = 0;
		byte[] data = { mm, sf, mi };
		m.setMessage(type, data, data.length);
		return null;
	}


	/**
	 * Creates the MetaMessage for a Time Signature
	 * @param t
	 * @return
	 * @throws InvalidMidiDataException
	 *-/
	private static MetaMessage createMidiEvent(NormalTime t, int resolution, int tracknumber)
		throws InvalidMidiDataException
	{
		MetaMessage m = new MetaMessage();
		int type = 0x58;
		byte mm = (byte) tracknumber;
		byte nn = (byte) t.getNumerator();
		byte dd = (byte) t.getDenominator();
		byte cc = (byte) (resolution * 4 / dd);
		byte bb = (byte) (32 / dd);
		byte[] data = { mm, nn, dd, cc, bb };
		m.setMessage(type, data, data.length);
		return m;
	}


	/**
	 * Creates the MetaMessage for a {@link Clef}
	 * @param clef
	 * @return
	 * @throws InvalidMidiDataException
	 *-/
	private static MetaMessage createMidiEvent(Clef clef, int tracknumber)
		throws InvalidMidiDataException
	{
		MetaMessage m = new MetaMessage();
		int type = 0x57; // TODO ????
		byte mm = (byte) tracknumber;
		byte cl = -1; // clef type
		byte li = -1; // clef position on staff (line position)
		byte oc = 0; // octave transposition
		if (clef.getType() == ClefType.G)
		{
			cl = 1;
			li = (byte) (clef.getType().getLine() + 1);
			oc = (byte) clef.getType().getOctaveChange();
		}
		else if (clef.getType() == ClefType.F)
		{
			cl = 2;
			li = (byte) (clef.getType().getLine() + 1);
			oc = (byte) clef.getType().getOctaveChange();
		}
		if (cl != -1)
		{
			byte[] data = { mm, cl, li, oc };
			m.setMessage(type, data, data.length);
		}
		return m;
	}
	*/

	private static boolean isInRange(Fraction startBeat, Fraction duration, Fraction start,
		Fraction end) {
		Fraction endBeat = startBeat.add(duration);
		if (endBeat.compareTo(start) == -1 || startBeat.compareTo(end) == 1) {
			return false;
		}
		return true;
	}

	private static long calculateEndTick(Fraction startBeat, Fraction endBeat, Fraction start,
		Fraction end, long currentTickInVoice, int resolution) {
		Fraction begin;
		Fraction finish;
		if (startBeat.compareTo(start) == -1) {
			begin = start;
		}
		else {
			begin = startBeat;
		}
		if (endBeat.compareTo(end) == 1) {
			finish = end;
		}
		else {
			finish = endBeat;
		}
		Fraction duration = finish.sub(begin);
		return currentTickInVoice + calculateTickFromFraction(duration, resolution);
	}

	/**
	 * Adds the given note to the given track.
	 */
	private int addNoteToTrack(int channel, Staff staff, int track, int currentVelocity,
		int iVoice, long starttick, long endtick, Chord chord, Note note, int transposing) {
		int[] velocityAtPosition = getVelocityAtPosition(staff, iVoice, getMP(chord),
			currentVelocity, score);
		int midiNote = MidiTools.getNoteNumberFromPitch(note.getPitch()) + transposing;
		writer.writeNote(track, channel, starttick, midiNote, true, velocityAtPosition[0]);
		writer.writeNote(track, channel, endtick, midiNote, false, 0);
		currentVelocity = velocityAtPosition[1];
		return currentVelocity;
	}

	private void addPlaybackAtEndControlEvent() {
		writer.writeControlChange(systemTrackIndex, 0, writer.getLength(), MidiEvents.eventPlaybackEnd, 0);
	}

	/**
	 * Creates the control events for the playback cursor.
	 * The control message {@link MidiEvents#eventPlaybackControl} is used because it has no other meaning.
	 */
	private void createControlEventChannel(
		List<Long> measureStartTicks, List<MidiTime> timePoolOpen, int channel, Playlist playlist) {
		List<SortedList<Fraction>> usedBeatsMeasures = CollectionUtils.alist();
		for (int i : range(score.getMeasuresCount()))
			usedBeatsMeasures.add(score.getMeasureUsedBeats(i));
		int imeasure = 0;
		for (PlayRange playRange : playlist.getRanges()) {

			for (int i : range(playRange.from.measure, playRange.to.measure)) {
				SortedList<Fraction> usedBeats = usedBeatsMeasures.get(i);

				Fraction start, end;
				start = score.clipToMeasure(playRange.from.measure, playRange.from).beat;
				end = score.clipToMeasure(playRange.to.measure, playRange.to).beat;

				for (Fraction fraction : usedBeats) {
					//only add, if beats are between start and end
					if (fraction.compareTo(start) > -1 && fraction.compareTo(end) < 1) {
						long tick = measureStartTicks.get(imeasure) +
							calculateTickFromFraction(fraction.sub(start), resolution);
						writer.writeControlChange(systemTrackIndex, 0, tick, MidiEvents.eventPlaybackControl, 0);

						MP bmp = atBeat(1, i, -1, fraction);
						long ms = writer.tickToMicrosecond(tick) / 1000;
						timePoolOpen.add(new MidiTime(tick, bmp, ms));
					}
				}
				imeasure++;
			}
		}
	}

	/**
	 * Creates the track in the sequence with the beats of the metronome.
	 */
	private void createMetronomeTrack(int track, Playlist playlist, List<Long> measureStartTicks) {
		// Load Settings
		int metronomeStrongBeatNote = settings.getMetronomeStrongBeatNote();
		int metronomeWeakBeatNote = settings.getMetronomeWeakBeatNote();

		int imeasure = 0;
		for (PlayRange playRange : playlist.getRanges()) {

			for (int i : range(playRange.from.measure, playRange.to.measure)) {
				Time time = score.getHeader().getTimeAtOrBefore(i);

				Fraction start, end;
				if (playRange.from.measure == i) {
					start = playRange.from.beat;
				}
				else {
					start = fr(0, 1);
				}
				if (playRange.to.measure == i) {
					end = playRange.to.beat;
				}
				else {
					end = score.getMeasureBeats(i);
				}

				if (time != null) {
					boolean[] accentuation = time.getType().getBeatsAccentuation();
					int timeDenominator = time.getType().getDenominator();
					for (int beatNumerator : range(time.getType().getNumerator())) {
						Fraction beat = fr(beatNumerator, timeDenominator);
						if (false == isInRange(beat, fr(0), start, end))
							continue;
						long tickStart = measureStartTicks.get(imeasure) +
							calculateTickFromFraction(beat.sub(start), resolution);
						long tickStop = tickStart + calculateTickFromFraction(fr(1, timeDenominator), resolution);
						
						int note = (accentuation[beatNumerator] ? metronomeStrongBeatNote : metronomeWeakBeatNote);

						int velocity = 127;
						writer.writeNote(track, channel10, tickStart, note, true, velocity);
						writer.writeNote(track, channel10, tickStop, note, false, 0);
					}
				}
				imeasure++;
			}
		}
	}

	/**
	 * This method calculates the tick of a {@link Fraction}. This is necessary
	 * to get the current beat in a measure or to get the duration of a
	 * {@link Fraction} in midi ticks.
	 */
	public static int calculateTickFromFraction(Fraction fraction, int resolution) {
		return fraction.getNumerator() * 4 * resolution / fraction.getDenominator();
	}

	/**
	 * Creates the mapping from staff indices to MIDI channel numbers.
	 * If possible, each part gets its own channel. If there are too many parts,
	 * the parts with the same MIDI program share the device. If there are still
	 * too many parts, the remaining parts are ignored (i.e. they are mapped to
	 * the device number -1).
	 * Channel 10 is always reserved for drums/percussion/metronome.
	 */
	private static int[] createChannelMap(Score score) {
		int[] ret = new int[score.getStavesCount()];
		//get number of parts which have not channel 10
		int melodicPartsCount = 0;
		for (Part part : score.getStavesList().getParts()) {
			if (part.getFirstInstrument() instanceof PitchedInstrument) {
				melodicPartsCount++;
			}
		}
		//find out if there are enough channels for all parts
		if (melodicPartsCount <= 15) {
			//ok, each pitched part can have its own channel.
			//all unpitched parts get channel 10.
			int partFirstStave = 0;
			int iChannel = 0;
			for (Part part : score.getStavesList().getParts()) {
				boolean pitched = (part.getFirstInstrument() instanceof PitchedInstrument);
				int channel = (pitched ? iChannel : channel10);
				for (int iStaff = partFirstStave; iStaff < partFirstStave + part.getStavesCount(); iStaff++) {
					ret[iStaff] = channel;
				}
				//after pitched part: increment channel number
				if (pitched) {
					iChannel = iChannel + 1 + (iChannel + 1 == channel10 ? 1 : 0); //don't use channel 10
				}
				partFirstStave += part.getStavesCount();
			}
		}
		else {
			//too many parts to have a device for each part.
			//share devices for parts with the same instrument. if none is left, ignore part.
			//all unpitched parts get device 10.
			int partFirstStave = 0;
			int iChannel = 0;
			HashMap<Integer, Integer> programToDeviceMap = new HashMap<Integer, Integer>();
			for (Part part : score.getStavesList().getParts()) {
				boolean pitched = (part.getFirstInstrument() instanceof PitchedInstrument);
				//find device
				int channel = -1;
				int program = 0;
				boolean isChannelReused = false;
				if (pitched && iChannel != -1) {
					PitchedInstrument pitchedInstr = (PitchedInstrument) part.getFirstInstrument();
					program = pitchedInstr.getMidiProgram();
					Integer channelReused = programToDeviceMap.get(program);
					if (channelReused != null) {
						isChannelReused = true;
						channel = channelReused;
					}
					else {
						channel = iChannel;
					}
				}
				else if (!pitched) {
					channel = channel10;
				}
				for (int iStaff = partFirstStave; iStaff < partFirstStave + part.getStavesCount(); iStaff++) {
					ret[iStaff] = channel;
				}
				//after pitched part with new device number: increment device number and remember program
				if (pitched && !isChannelReused && iChannel != -1) {
					iChannel = iChannel + 1 + (iChannel + 1 == channel10 ? 1 : 0); //don't use channel 10
					if (iChannel > 15) {
						iChannel = -1; //no channels left
					}
					programToDeviceMap.put(program, channel);
				}
				partFirstStave += part.getStavesCount();
			}
		}
		return ret;
	}

}
