package com.xenoage.zong.io.midi.out;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.*;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.midi.out.channels.ChannelMap;
import com.xenoage.zong.io.midi.out.dynamics.Dynamics;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsFinder;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsInterpretation;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;
import com.xenoage.zong.io.midi.out.repetitions.Repetitions;
import com.xenoage.zong.io.midi.out.repetitions.RepetitionsFinder;
import com.xenoage.zong.io.midi.out.time.MidiTime;
import com.xenoage.zong.io.midi.out.time.TimeMap;
import com.xenoage.zong.io.midi.out.time.TimeMapBuilder;
import com.xenoage.zong.io.midi.out.time.TimeMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.*;
import static com.xenoage.zong.core.position.MP.atVoice;
import static com.xenoage.zong.core.position.Time.time;
import static com.xenoage.zong.io.midi.out.MidiSettings.defaultMidiSettings;
import static com.xenoage.zong.io.midi.out.MidiTools.getNoteNumber;
import static com.xenoage.zong.io.midi.out.channels.ChannelMap.unused;
import static com.xenoage.zong.io.midi.out.channels.ChannelMapper.createChannelMap;
import static java.lang.Math.round;


/**
 * This class creates a {@link MidiSequence} from a given {@link Score}.
 * 
 * @param <T> the platform-specific sequence class
 *
 * TODO: ZONG-101: Playback transposition changes
 * TODO: ZONG-102: Play tempo changes
 * TODO: ZONG-103: Play gradual tempo changes
 * TODO: ZONG-104: Play grace chords
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class MidiConverter<T> {

	/** Settings for the conversion. */
	@Const @Data @AllArgsConstructor
	public static class Options {

		public static final Options optionsForPlayback = new Options(
				true, true, defaultMidiSettings);

		public static final Options optionsForFileExport = new Options(
				false, false, defaultMidiSettings);

		/** True, iff controller events containing the current musical position
		 * should be inserted in the sequence. */
		@Wither public final boolean addTimeEvents;
		/** True to add metronome ticks, otherwise false. */
		@Wither public final boolean metronome;
		/** MIDI midiSettings for the conversion. */
		@Wither public final MidiSettings midiSettings;
	}

	/** Index for channel 10. It is 9, because the index is 0-based. */
	public static final int channel10 = 9;
	/** In MIDI "Format 1" files, the track for tempo changes and so on is track 0 by convention. */
	private static int systemTrackIndex = 0;
	/** The maximum value of a MIDI value, 127 ( = 7 bits). */
	private static int midiMaxValue = 127;
	
	//state
	private Score score;
	private Options options;
	private MidiSequenceWriter<T> writer;
	private int resolution;
	private ChannelMap channelMap;
	private Repetitions repetitions;
	private TimeMap timeMap;
	private Dynamics dynamics;


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

		//compute mapping of staff indices to channel numbers
		channelMap = createChannelMap(score);
		//compute repetitions (repeat barlines, segnos, ...)
		repetitions = RepetitionsFinder.findRepetitions(score);
		//compute the mappings from application time to MIDI time
		timeMap = new TimeMapper(score, repetitions, options.midiSettings.resolutionFactor).createTimeMap();
		//find all dynamics
		dynamics = DynamicsFinder.findDynamics(score, new DynamicsInterpretation(), repetitions);
		
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

		//fill tracks
		for (int iStaff : range(stavesCount)) {
			int channel = channelMap.getChannel(iStaff);
			if (channel == unused)
				continue; //no MIDI channel left for this staff

			Staff staff = score.getStaff(iStaff);
			int voicesCount = staff.getVoicesCount();
			int track = iStaff + 1; //first track is reserved; see declaration of tracksCount

			for (int iRepetition : range(repetitions)) {

				val rep = repetitions.get(iRepetition);
				int transpose = 0; //TODO
				for (int iMeasure : range(rep.start.measure, rep.end.measure)) {

					if (iMeasure == score.getMeasuresCount())
						continue;

					Measure measure = staff.getMeasure(iMeasure);

					/* transposition changes can happen everywhere in the measure - TODO: ZONG-101: Playback transposition changes
					Transpose t = measure.getInstrumentChanges()...
					if (t != null)
					{
						transposing = t.chromatic;
					} //*/

					for (int iVoice : range(measure.getVoices())) {
						writeVoice(atVoice(iStaff, iMeasure, iVoice), iRepetition);
					}
				}

			}

		}

		//Add Tempo Changes
		//TODO: ZONG-102: Play tempo changes
		//TODO: ZONG-103: Play gradual tempo changes
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

		//write events for time mapping between the MIDI sequence and the score
		if (options.addTimeEvents)
			writePlaybackControlEvents();

		//write metronome track
		if (options.metronome)
			writeMetronomeTrack(metronomeTrack);

		return writer.finish(metronomeTrack, timeMap);
	}

	/**
	 * Writes the given voice into the MIDI sequence.
	 * @param voiceMp         the staff, measure and voice index
	 * @param repetition      the index of the current {@link Repetition}
	 */
	private void writeVoice(MP voiceMp, int repetition) {

		val voice = score.getVoice(voiceMp);
		for (VoiceElement element : voice.getElements()) {

			//ignore rests. only chords are played
			if (false == MusicElementType.Chord.is(element))
				continue;
			val chord = (Chord) element;

			//grace chords are not supported yet - TODO: ZONG-104: Play grace chords
			if (chord.isGrace())
				continue;

			//start beat of the element
			Fraction duration = chord.getDuration();
			val startBeat = voice.getBeat(chord);
			val rep = repetitions.get(repetition);
			if (false == rep.contains(time(voiceMp.measure, startBeat)))
				continue; //start beat out of range: ignore element

			//MIDI ticks
			val startMidiTime = timeMap.getByRepTime(repetition, time(voiceMp.measure, startBeat));
			long startTick = startMidiTime.tick;
			long endTick = startTick + durationToTick(duration, resolution);
			long stopTick = endTick;
			if (false == options.midiSettings.durationFactor.equals(_1)) {
				//custom duration factor
				stopTick = startTick + round((endTick - startTick) *
						options.midiSettings.durationFactor.toFloat());
			}

			//play note
			if (startTick < stopTick) {
				float volume = dynamics.getVolumeAt(voiceMp.withBeat(startBeat), repetition);
				int midiVelocity = round(midiMaxValue * volume);
				for (Note note : chord.getNotes()) {
					addNoteToTrack(note.getPitch(), voiceMp.staff, startTick, stopTick, midiVelocity, 0);
				}
			}

			//TODO Timidity doesn't like the following midi events
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
			}*-/
			currenttickinvoice = endtick;
		}*/
		}
	}

	/**
	 * Adds the given note to the given track.
	 */
	private void addNoteToTrack(Pitch pitch, int staff, long startTick, long endTick, int velocity, int transpose) {
		int midiNote = getNoteNumber(pitch, transpose);
		int channel = channelMap.getChannel(staff);
		int track = staff + 1;
		writer.writeNote(track, channel, startTick, midiNote, true, velocity);
		writer.writeNote(track, channel, endTick, midiNote, false, 0);
	}

	/**
	 * Writes the control events for the playback cursor by using the
	 * control message {@link MidiEvents#PlaybackControl} and updates the
	 * internal {@link TimeMap} with the MIDI millisecond positions.
	 * At the end of the sequence, a {@link MidiEvents#PlaybackEnd} control
	 * event is added.
	 */
	private void writePlaybackControlEvents() {
		//write playback events and collect millisecond timing
		val newTimeMap = new TimeMapBuilder();
		for (val repTime : timeMap.getRepTimes()) {
			MidiTime time = timeMap.getByRepTime(repTime);
			writer.writeControlChange(systemTrackIndex, 0, time.tick,
					MidiEvents.PlaybackControl.code, 0);
			long ms = writer.tickToMicrosecond(time.tick) / 1000;
			newTimeMap.addTime(time.tick, repTime, ms);
		}
		//update time map
		timeMap = newTimeMap.build();
		//write playback end event
		writer.writeControlChange(systemTrackIndex, 0, writer.getLength(), MidiEvents.PlaybackEnd.code, 0);
	}

	/**
	 * Writes the metronome beats into the metronome track.
	 */
	private void writeMetronomeTrack(int track) {

		int strongBeatNote = options.midiSettings.metronomeStrongBeatNote;
		int weakBeatNote = options.midiSettings.metronomeWeakBeatNote;

		for (int iRep : range(repetitions)) {
			val rep = repetitions.get(iRep);
			for (int iMeasure : range(rep.start.measure, rep.end.measure)) {
				TimeSignature timeSig = score.getHeader().getTimeAtOrBefore(iMeasure);

				Fraction startBeat = (rep.start.measure == iMeasure ? rep.start.beat : _0);
				Fraction endBeat = (rep.end.measure == iMeasure ? rep.end.beat : score.getMeasureBeats(iMeasure));

				if (timeSig != null) {

					boolean[] accentuation = timeSig.getType().getBeatsAccentuation();
					int timeDenominator = timeSig.getType().getDenominator();
					long measureStartTick = timeMap.getByRepTime(iRep, time(iMeasure, _0)).tick;

					for (int beatNumerator : range(timeSig.getType().getNumerator())) {

						//compute start and stop tick
						val beat = fr(beatNumerator, timeDenominator);
						val time = time(iMeasure, beat);
						if (false == rep.contains(time))
							continue;

						long tickStart = measureStartTick + durationToTick(fr(beatNumerator, timeDenominator), resolution);
						long tickStop = tickStart + durationToTick(fr(1, timeDenominator), resolution);

						//write metronome note
						int note = (accentuation[beatNumerator] ? strongBeatNote : weakBeatNote);
						int velocity = midiMaxValue;
						writer.writeNote(track, channel10, tickStart, note, true, velocity);
						writer.writeNote(track, channel10, tickStop, note, false, 0);
					}
				}
			}
		}
	}

	/**
	 * Returns the number of ticks of the given {@link Fraction}.
	 */
	private int durationToTick(Fraction fraction, int resolution) {
		return fraction.getNumerator() * 4 * resolution / fraction.getDenominator();
	}

}
