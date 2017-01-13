package com.xenoage.zong.io.midi.out;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.SortedList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.io.midi.out.repetitions.PlayRange;
import com.xenoage.zong.io.midi.out.repetitions.Repetitions;
import com.xenoage.zong.io.midi.out.repetitions.RepetitionsFinder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.val;

import java.util.Arrays;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.core.position.Time.time;
import static com.xenoage.zong.io.midi.out.MidiSettings.defaultMidiSettings;
import static com.xenoage.zong.io.midi.out.MidiVelocityConverter.getVelocityAtPosition;
import static com.xenoage.zong.io.midi.out.channels.ChannelMap.unused;
import static com.xenoage.zong.io.midi.out.channels.ChannelMapper.createChannelMap;

/**
 * This class creates a {@link MidiSequence} from a given {@link Score}.
 * 
 * @param <T> the platform-specific sequence class
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class MidiConverter<T> {

	/** Settings for the conversion. */
	@Const @Data @AllArgsConstructor @Builder(builderMethodName="options")
	public static class Options {
		public static final Options defaultOptions = new Options(
				true, true, defaultMidiSettings);

		/** True, iff controller events containing the current musical position
		 * should be inserted in the sequence. */
		public final boolean addTimeEvents;
		/** True to add metronome ticks, otherwise false. */
		public final boolean metronome;
		/** MIDI midiSettings for the conversion. */
		public final MidiSettings midiSettings;
	}

	/** Index for channel 10. It is 9, because the index is 0-based. */
	public static final int channel10 = 9; //

	//in MIDI "Format 1" files, the track for tempo changes and so on is track 0 by convention
	private static int systemTrackIndex = 0;
	
	//state
	private Score score;
	private Options options;
	private MidiSequenceWriter<T> writer;
	private int resolution;


	/**
	 * Converts a {@link Score} to a {@link MidiSequence}.
	 * @param score    the score to convert
	 * @param options  midiSettings for the conversion
	 * @param writer   the writer for the midi data
	 */
	public static <T> MidiSequence<T> convertToSequence(Score score, Options options, MidiSequenceWriter<T> writer) {
		return new MidiConverter<T>(score, writer, options).convertToSequence();
	}
	
	private MidiConverter(Score score, MidiSequenceWriter<T> writer, Options options) {
		this.score = score;
		this.options = options;
		this.writer = writer;
	}
	
	private MidiSequence<T> convertToSequence() {

		//ArrayList<Long> measureStartTicks = alist();
		//ArrayList<MidiTime> timePool = alist();

		//compute mapping of staff indices to channel numbers
		val channelMap = createChannelMap(score);

		//compute repititions (repeats, segnos, ...)
		val repetitions = RepetitionsFinder.findRepetitions(score);
		
		//one track for each staff and one system track for program changes, tempos and so on,
		//and another track for the metronome
		int stavesCount = score.getStavesCount();
		int tracksCount = stavesCount + 1;
		Integer metronomeTrack = null;
		if (options.metronome) {
			metronomeTrack = tracksCount;
			tracksCount++;
		}

		//resolution in ticks per quarter
		resolution = score.getDivisions() * options.midiSettings.getResolutionFactor();

		//init writer
		writer.init(tracksCount, resolution);
		
		//set MIDI programs and init volume and pan
		for (val part : score.getStavesList().getParts()) {
			val instrument = part.getFirstInstrument();
			int partFirstStaff = score.getStavesList().getPartStaffIndices(part).getStart();
			int channel = channelMap.getChannel(partFirstStaff);
			if (channel != unused) {
				if (instrument instanceof PitchedInstrument) {
					val pitchedInstrument = (PitchedInstrument) instrument;
					writer.writeProgramChange(systemTrackIndex, channel, 0, (pitchedInstrument).getMidiProgram());
				}
				writer.writeVolumeChange(systemTrackIndex, channel, 0, instrument.getVolume());
				writer.writePanChange(systemTrackIndex, channel, 0, instrument.getPan());
			}
		}

		//find used beats in each measure column - GOON: use getMeasureUsedBeats ?
		Fraction[] realMeasureColumnBeats = new Fraction[score.getMeasuresCount()];
		for (int i : range(score.getMeasuresCount()))
			realMeasureColumnBeats[i] = score.getMeasureFilledBeats(i);

		//fill tracks
		for (int iStaff : range(stavesCount)) {
			int channel = channelMap.getChannel(iStaff);
			if (channel == unused)
				continue; //no MIDI channel left for this staff

			long currenttickinstaff = 0;
			Staff staff = score.getStaff(iStaff);
			int voicesCount = staff.getVoicesCount();
			int voicesVelocity[] = new int[voicesCount];
			Arrays.fill(voicesVelocity, 90);

			int track = iStaff;
/*
			int[] voiceforDynamicsInStaff = getVoiceforDynamicsInStaff(staff);

			for (PlayRange playRange : repetitions.getRanges()) {
				int transposing = 0;
				for (int iMeasure : range(playRange.start.measure, playRange.end.measure)) {
					Measure measure = staff.getMeasure(iMeasure);
					measureStartTicks.add(currenttickinstaff);

					//*TODO: transposition changes can happen everywhere in the measure
					Transpose t = measure.getInstrumentChanges()...
					if (t != null)
					{
						transposing = t.chromatic;
					} //* /

					Fraction start, end;
					start = null; //GOON score.clipToMeasure(iMeasure, playRange.start).beat;
					end = null; //GOON score.clipToMeasure(iMeasure, playRange.end).beat;

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
			} */
		}

		/*
		if (addTimeEvents) {
			createControlEventChannel(measureStartTicks, timePool, 0, repetitions); //score position events in channel 0
			addPlaybackAtEndControlEvent();
		}

		if (metronome) {
			createMetronomeTrack(metronomeTrack, repetitions, measureStartTicks);
		}

		//Add Tempo Changes
		/*ArrayList<MidiElement> tempo = MidiTempoConverter.getTempo(score, playList);
		for (MidiElement midiElement : tempo)
		{
			long startTick = measureStartTick.get(midiElement.getMeasure());
			long beat = startTick + (long)midiElement.getPosition().toFloat() * resolution;
			MidiEvent event = new MidiEvent(midiElement.getMidiMessage(), beat);
			tempoTrack.add(event);
		}* /
		MidiTempoConverter.writeTempoTrack(score, repetitions, resolution, writer, systemTrackIndex);

		return writer.finish(metronomeTrack, timePool, measureStartTicks);
		*/
		return null;
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
			if (options.midiSettings.durationFactor != null) {
				Fraction stopBeat = startBeat.add(duration.mult(options.midiSettings.durationFactor));
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
		writer.writeControlChange(systemTrackIndex, 0, writer.getLength(), MidiEvents.eventPlaybackEnd.code, 0);
	}

	/**
	 * Creates the control events for the playback cursor.
	 * The control message {@link MidiEvents#eventPlaybackControl} is used because it has no other meaning.
	 */
	private void createControlEventChannel(
			List<Long> measureStartTicks, List<MidiTime> timePoolOpen, int channel, Repetitions repetitions) {
		List<SortedList<Fraction>> usedBeatsMeasures = alist();
		for (int i : range(score.getMeasuresCount()))
			usedBeatsMeasures.add(score.getMeasureUsedBeats(i));
		int imeasure = 0;
		for (PlayRange playRange : repetitions.getRanges()) {

			for (int iMeasure : range(playRange.start.measure, playRange.end.measure)) {
				SortedList<Fraction> usedBeats = usedBeatsMeasures.get(iMeasure);

				Fraction start, end;
				start = null; //GOON: score.clipToMeasure(playRange.start.measure, playRange.start).beat;
				end = null; //GOON score.clipToMeasure(playRange.end.measure, playRange.end).beat;

				for (Fraction beat : usedBeats) {
					//only add, if beats are between start and end
					if (beat.compareTo(start) > -1 && beat.compareTo(end) < 1) {
						long tick = measureStartTicks.get(imeasure) +
							calculateTickFromFraction(beat.sub(start), resolution);
						writer.writeControlChange(systemTrackIndex, 0, tick, MidiEvents.eventPlaybackControl.code, 0);

						long ms = writer.tickToMicrosecond(tick) / 1000;
						timePoolOpen.add(new MidiTime(tick, time(iMeasure, beat), ms));
					}
				}
				imeasure++;
			}
		}
	}

	/**
	 * Creates the track in the sequence with the beats of the metronome.
	 */
	private void createMetronomeTrack(int track, Repetitions repetitions, List<Long> measureStartTicks) {
		// Load Settings
		int metronomeStrongBeatNote = options.midiSettings.getMetronomeStrongBeatNote();
		int metronomeWeakBeatNote = options.midiSettings.getMetronomeWeakBeatNote();

		int imeasure = 0;
		for (PlayRange playRange : repetitions.getRanges()) {

			for (int i : range(playRange.start.measure, playRange.end.measure)) {
				TimeSignature time = score.getHeader().getTimeAtOrBefore(i);

				Fraction start, end;
				if (playRange.start.measure == i) {
					start = playRange.start.beat;
				}
				else {
					start = fr(0, 1);
				}
				if (playRange.end.measure == i) {
					end = playRange.end.beat;
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



}
