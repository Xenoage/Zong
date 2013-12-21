package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.base.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.pdlib.IVector.ivec;
import static com.xenoage.zong.core.position.MP.atStaff;
import static com.xenoage.zong.io.midi.out.MidiVelocityConverter.getVelocityAtPosition;
import static com.xenoage.zong.io.midi.out.MidiVelocityConverter.getVoiceforDynamicsInStaff;
import static com.xenoage.zong.io.score.ScoreController.clipToMeasure;
import static com.xenoage.zong.io.score.ScoreController.getDivisions;
import static com.xenoage.zong.io.score.ScoreController.getFilledBeats;

import java.util.ArrayList;
import java.util.HashMap;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.Controller;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.pdlib.IVector;
import com.xenoage.utils.pdlib.Vector;
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
import com.xenoage.zong.io.midi.out.Playlist.PlayRange;


/**
 * Copy of the {@link MidiConverter}, but using the system-independent
 * <code>android-midi-lib</code>.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class MidiConverterAndroid
{
	
	public static final int CHANNEL_10 = 9; //channel 10 has index 9 (0-based)
	
	//even shortest note has 8 ticks. this allows staccato playback for example.
	private static final int RESOLUTION_FACTOR = 8; 
	
	//controller numbers
	private static final int CONTROLLER_VOLUME = 7;
	private static final int CONTROLLER_PAN = 10;
	
	
	/**
	 * Converts a {@link Score} to a {@link MidiFile}.
	 * @param score        the score to convert
	 * @param addMPEvents  true, if controller events containing the current musical position
	 *                     should be inserted in the sequence, otherwise false
	 * @param metronome    true to add metronome ticks, otherwise false
	 */
	public static MidiFile convertToMidiFile(Score score,
		boolean addMPEvents, boolean metronome)
	{
		return convertToMidiFile(score, addMPEvents, metronome, null);
	}


	/**
	 * Converts a {@link Score} to a {@link MidiFile}.
	 * @param score           the score to convert
	 * @param addMPEvents     true, if controller events containing the current musical position
	 *                        should be inserted in the sequence, otherwise false
	 * @param metronome       true to add metronome ticks, otherwise false
	 * @param durationFactor  factor to multiply all note durations with, or null
	 */
	public static MidiFile convertToMidiFile(Score score,
		boolean addMPEvents, boolean metronome, Fraction durationFactor)
	{

		IVector<Integer> staffTracks = ivec();
		int resolution = getDivisions(score) * RESOLUTION_FACTOR; 
		IVector<Long> measureStartTicks = ivec();
		Integer metronomeBeatTrackNumber = null;
		IVector<MidiTime> timePool = ivec();
		int stavesCount = score.stavesList.getStavesCount();
		ArrayList<MidiTrack> tracks = alist();

		//compute mapping of staff indices to channel numbers
		int[] channelMap = createChannelMap(score);

		//create sequence
		MidiTrack tempoTrack = new MidiTrack();

		Playlist playlist = MidiRepetitionCalculator.createPlaylist(score);

		//create tracks
		for (int iStaff = 0; iStaff < stavesCount; iStaff++)
		{
			tracks.add(new MidiTrack());
		}

		//activate MIDI programs
		int partFirstStaff = 0;
		for (Part part : score.stavesList.parts)
		{
			Instrument instrument = part.getInstruments().getFirst();
			if (instrument instanceof PitchedInstrument)
			{
				PitchedInstrument pitchedInstrument = (PitchedInstrument) instrument;
				int channel = channelMap[partFirstStaff];
				if (channel > -1)
				{
					ProgramChange prgMsg = new ProgramChange(0, channel, (pitchedInstrument).midiProgram);
					tempoTrack.insertEvent(prgMsg); //do all program changes in first track (tempoTrack)
				}
				
				//general volume for this instrument
				if (instrument.base.volume != null)
				{
					Controller ctrlVolume = new Controller(0, channel, CONTROLLER_VOLUME, (int) (127 * instrument.base.volume));
					tempoTrack.insertEvent(ctrlVolume); //in first track (tempoTrack)
				}
				
				//general panning for this instrument
				//TODO: does not work, no panning can be heared using gervill
				if (instrument.base.pan != null)
				{
					Controller ctrlPan = new Controller(0, channel, CONTROLLER_PAN, (int) (64 + (63 * instrument.base.pan)));
					tempoTrack.insertEvent(ctrlPan); //in first track (tempoTrack)
				}
				
			}
			partFirstStaff += part.getStavesCount();
		}
		
		//used beats in each measure column
		IVector<Fraction> realMeasureColumnBeats = IVector.ivec(score.getMeasuresCount());
		for (int iMeasure : range(score.getMeasuresCount()))
		{
			realMeasureColumnBeats.add(getFilledBeats(score, iMeasure));
		}
		realMeasureColumnBeats.close();

		//fill tracks
		for (int iStaff = 0; iStaff < stavesCount; iStaff++)
		{
			int channel = channelMap[iStaff];
			if (channel == -1)
			{
				continue; //no MIDI channel left for this staff
			}

			long currenttickinstaff = 0;
			Staff staff = score.getStaff(atStaff(iStaff));
			MidiTrack track = tracks.get(iStaff);

			Vector<Measure> measures = staff.measures;
			int numberOfVoices = MidiVelocityConverter.getNumberOfVoicesInStaff(staff);
			int velocity[] = new int[numberOfVoices];
			for (int i = 0; i < velocity.length; i++)
			{
				velocity[i] = 90;
			}

			int tracknumber = iStaff;

			int[] voiceforDynamicsInStaff = getVoiceforDynamicsInStaff(staff, score.globals);
			for (PlayRange playRange : playlist.ranges)
			{
				int transposing = 0;
				for (int iMeasure : range(playRange.from.measure, playRange.to.measure))
				{
					Measure measure = measures.get(iMeasure);
					measureStartTicks.add(currenttickinstaff);

					/*/TODO: transposition changes can happen everywhere in the measure
					Transpose t = measure.getInstrumentChanges()...
					if (t != null)
					{
						transposing = t.chromatic;
					} //*/
					
					Fraction start, end;
					start = clipToMeasure(score, iMeasure, playRange.from).beat;
					end = clipToMeasure(score, iMeasure, playRange.to).beat;

					if (realMeasureColumnBeats.get(iMeasure).compareTo(end) < 0)
						end = realMeasureColumnBeats.get(iMeasure);

					for (int iVoice = 0; iVoice < measure.voices.size(); iVoice++)
					{
						Voice voice = measure.voices.get(iVoice);
						int currentVelocity = velocity[voiceforDynamicsInStaff[iVoice]];
						velocity[iVoice] = generateMidi(resolution, channel, currenttickinstaff,
							staff, track, currentVelocity, iVoice, voice, start, end,
							transposing, score, durationFactor);
					}
					Fraction measureduration = end.sub(start);
					currenttickinstaff += calculateTickFromFraction(measureduration, resolution);

				}
				staffTracks.add(tracknumber);
			}
		}
		staffTracks.close();
		measureStartTicks.close();

		if (addMPEvents)
		{
			//GOON createControlEventChannel(score, resolution, measureStartTicks,
			//	timePool, seq, 0, playlist); //score position events in channel 0
			//addPlaybackAtEndControlEvent(seq);
		}
		timePool.close();

		if (metronome)
		{
			//metronomeBeatTrackNumber = createMetronomeTrack(score, resolution, seq, playlist,
			//	measureStartTicks);
		}

		//Add Tempo Changes
		MidiTempoConverterAndroid.writeTempoTrack(score, playlist, resolution, tempoTrack);

		tracks.add(0, tempoTrack);
		return new MidiFile(resolution, tracks);
	}


	/**
	 * Generates the midi sequence starting at the given position.
	 * Returns the velocity at the start position.
	 * TIDY
	 */
	private static int generateMidi(int resolution, int channel, long currenttickinstaff,
		Staff staff, MidiTrack track, int currentVelocity, int iVoice, Voice voice,
		Fraction start, Fraction end, int transposing, Score score,
		Fraction durationFactor)
	{
		long currenttickinvoice = currenttickinstaff;
		for (VoiceElement element : voice.elements)
		{
			Fraction duration = element.getDuration();
			Fraction elementBeat = score.getBMP(element).beat;
			if (!isInRange(elementBeat, duration, start, end))
				continue;
			Fraction startBeat = elementBeat;
			Fraction endBeat = startBeat.add(duration);
			long starttick = currenttickinvoice;
			long endtick = calculateEndTick(startBeat, endBeat, start, end, currenttickinvoice,
				resolution);
			
			//custom duration factor
			long stoptick = endtick;
			if (durationFactor != null)
			{
				Fraction stopBeat = startBeat.add(duration.mult(durationFactor));
				stoptick = calculateEndTick(startBeat, stopBeat, start, end, currenttickinvoice,
					resolution);
			}
			
			if (starttick != stoptick && element instanceof Chord)
			{
				Chord chord = (Chord) element;
				for (Note note : chord.notes)
				{
					currentVelocity = addNoteToTrack(channel, staff, track, currentVelocity,
						iVoice, starttick, stoptick, chord, note, transposing, score);
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
		Fraction end)
	{
		Fraction endBeat = startBeat.add(duration);
		if (endBeat.compareTo(start) == -1 || startBeat.compareTo(end) == 1)
		{
			return false;
		}
		return true;
	}


	private static long calculateEndTick(Fraction startBeat, Fraction endBeat,
		Fraction start, Fraction end, long currentTickInVoice, int resolution)
	{
		Fraction begin;
		Fraction finish;
		if (startBeat.compareTo(start) == -1)
		{
			begin = start;
		}
		else
		{
			begin = startBeat;
		}
		if (endBeat.compareTo(end) == 1)
		{
			finish = end;
		}
		else
		{
			finish = endBeat;
		}
		Fraction duration = finish.sub(begin);
		return currentTickInVoice + calculateTickFromFraction(duration, resolution);
	}


	private static int addNoteToTrack(int channel, Staff staff, MidiTrack track,
		int currentVelocity, int iVoice, long starttick, long endtick, Chord chord,
		Note note, int transposing, Score score)
	{
		int[] velocityAtPosition = getVelocityAtPosition(staff, iVoice,
			score.getBMP(chord), currentVelocity, score);
		int midiNote = MidiTools.getNoteNumberFromPitch(note.getPitch()) + transposing;
		track.insertNote(channel, midiNote, velocityAtPosition[0], starttick, endtick - starttick);
		currentVelocity = velocityAtPosition[1];
		return currentVelocity;
	}


	/* GOON
	private static void addPlaybackAtEndControlEvent(Sequence seq)
		throws InvalidMidiDataException
	{
		Track t = seq.createTrack();
		addEventToTrack(t, seq.getTickLength(), ShortMessage.CONTROL_CHANGE, 0, 117, 0);
	} */


	/**
	 * Creates the ControlEvents for the Playback cursor. Channel 119 is used
	 * because it has no other meaning.
	 * /
	private static void createControlEventChannel(Score score, int resolution,
		IVector<Long> measureStartTicks, IVector<MidiTime> timePoolOpen, int channel, Playlist playlist)
	{
		IVector<SortedList<Fraction>> usedBeatsMeasures = ivec();
		for (int i : range(score.getMeasuresCount()))
			usedBeatsMeasures.add(getUsedBeats(score, i));
		usedBeatsMeasures.close();
		
		// Add ControlEvents for Listener
		
		MidiTrack controltrack = new MidiTrack();
		
		//GOON TempoCache tempoCache = new TempoCache(sequence);

		int imeasure = 0;
		for (PlayRange playRange : playlist.ranges)
		{

			for (int i : range(playRange.from.measure, playRange.to.measure))
			{
				SortedList<Fraction> usedBeats = usedBeatsMeasures.get(i);

				Fraction start, end;
				start = clipToMeasure(score, playRange.from.measure, playRange.from).beat;
				end = clipToMeasure(score, playRange.to.measure, playRange.to).beat;

				for (Fraction fraction : usedBeats)
				{
					//only add, if beats are between start and end
					if (fraction.compareTo(start) > -1 && fraction.compareTo(end) < 1)
					{
						long tick = measureStartTicks.get(imeasure)
							+ calculateTickFromFraction(fraction.sub(start), resolution);
						addEventToTrack(controltrack, tick, ShortMessage.CONTROL_CHANGE, channel,
							119, 0);

						BMP bmp = bmp(1, i, -1, fraction);
						long ms = MidiUtils.tick2microsecond(sequence, tick, tempoCache) / 1000;
						timePoolOpen.add(new MidiTime(tick, bmp, ms));
					}
				}
				imeasure++;
			}
		} 
	} */


	/**
	 * Creates the track in the sequence with the beats of the metronome.
	 * Additional it adds a second track that is used for the controlevents to
	 * unmute the metronome in the correct moment. Message 118 is used. It
	 * returns the number of the track in the sequencer.
	 * 
	 * TIDY GOON
	 * 
	 * @param score
	 * @param resolution the midi resolution
	 * @param seq the midi sequence where the metronome should be added
	 * @param playList the Playlist
	 * @param measureStartTick
	 * @return the number of the metronome track in the sequence
	 * @throws InvalidMidiDataException
	 * /
	private static int createMetronomeTrack(Score score, int resolution, Sequence seq,
		Playlist playlist, IVector<Long> measureStartTicks)
		throws InvalidMidiDataException
	{
		int metronomeBeatTrackNumber;

		// Load Settings
		int metronomeStrongBeatNote = Settings.getInstance().getSetting(
			"MetronomeStrongBeat", "playback", 36);
		int metronomeWeakBeatNote = Settings.getInstance().getSetting("MetronomeWeakBeat",
			"playback", 50);

		// Create Tracks
		metronomeBeatTrackNumber = seq.getTracks().length;
		Track metronomeTrack = seq.createTrack();
		Track metronomeControlTrack = seq.createTrack();

		int imeasure = 0;
		for (PlayRange playRange : playlist.ranges)
		{

			for (int i : range(playRange.from.measure, playRange.to.measure))
			{
				Time time = getTimeAtOrBefore(score, i);

				Fraction start, end;
				if (playRange.from.measure == i)
				{
					start = playRange.from.beat;
				}
				else
				{
					start = fr(0, 1);
				}
				if (playRange.to.measure == i)
				{
					end = playRange.to.beat;
				}
				else
				{
					end = getMeasureBeats(score, i);
				}

				if (time != null)
				{
					Vector<Tuple2<Fraction, BeatWeight>> beatWeights = time.getBeatWeights();
					for (Tuple2<Fraction, BeatWeight> beatWeight : beatWeights)
					{
						if (!isInRange(beatWeight.get1(), fr(0), start, end))
							continue;
						long currenttick = measureStartTicks.get(imeasure)
							+ calculateTickFromFraction(beatWeight.get1().sub(start), resolution);
						int note;
						if (beatWeight.get2() == BeatWeight.StrongBeat)
						{
							note = metronomeStrongBeatNote;
						}
						else
						{
							note = metronomeWeakBeatNote;
						}

						int velocity = 127;
						addEventToTrack(metronomeTrack, currenttick, ShortMessage.NOTE_ON, 9, note,
							velocity);
						addEventToTrack(metronomeControlTrack, currenttick,
							ShortMessage.CONTROL_CHANGE, 9, 118, 0);
					}
				}
				imeasure++;
			}
		}
		return metronomeBeatTrackNumber;
	} */


	/**
	 * This method calculates the tick of a {@link Fraction}. This is necessary
	 * to get the current beat in a measure or to get the duration of a
	 * {@link Fraction} in midi ticks.
	 */
	public static int calculateTickFromFraction(Fraction fraction, int resolution)
	{
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
	private static int[] createChannelMap(Score score)
	{
		int[] ret = new int[score.getStavesCount()];
		//get number of parts which have not channel 10
		int melodicPartsCount = 0;
		for (Part part : score.stavesList.parts)
		{
			if (part.getInstruments().getFirst() instanceof PitchedInstrument)
			{
				melodicPartsCount++;
			}
		}
		//find out if there are enough channels for all parts
		if (melodicPartsCount <= 15)
		{
			//ok, each pitched part can have its own channel.
			//all unpitched parts get channel 10.
			int partFirstStave = 0;
			int iChannel = 0;
			for (Part part : score.stavesList.parts)
			{
				boolean pitched = (part.getInstruments().getFirst() instanceof PitchedInstrument);
				int channel = (pitched ? iChannel : CHANNEL_10);
				for (int iStaff = partFirstStave; iStaff < partFirstStave + part.getStavesCount(); iStaff++)
				{
					ret[iStaff] = channel;
				}
				//after pitched part: increment channel number
				if (pitched)
				{
					iChannel = iChannel + 1 + (iChannel + 1 == CHANNEL_10 ? 1 : 0); //don't use channel 10
				}
				partFirstStave += part.getStavesCount();
			}
		}
		else
		{
			//too many parts to have a device for each part.
			//share devices for parts with the same instrument. if none is left, ignore part.
			//all unpitched parts get device 10.
			int partFirstStave = 0;
			int iChannel = 0;
			HashMap<Integer, Integer> programToDeviceMap = new HashMap<Integer, Integer>();
			for (Part part : score.stavesList.parts)
			{
				boolean pitched = (part.getInstruments().getFirst() instanceof PitchedInstrument);
				//find device
				int channel = -1;
				int program = 0;
				boolean isChannelReused = false;
				if (pitched && iChannel != -1)
				{
					PitchedInstrument pitchedInstr = (PitchedInstrument) part.getInstruments().getFirst();
					program = pitchedInstr.midiProgram;
					Integer channelReused = programToDeviceMap.get(program);
					if (channelReused != null)
					{
						isChannelReused = true;
						channel = channelReused;
					}
					else
					{
						channel = iChannel;
					}
				}
				else if (!pitched)
				{
					channel = CHANNEL_10;
				}
				for (int iStaff = partFirstStave; iStaff < partFirstStave + part.getStavesCount(); iStaff++)
				{
					ret[iStaff] = channel;
				}
				//after pitched part with new device number: increment device number and remember program
				if (pitched && !isChannelReused && iChannel != -1)
				{
					iChannel = iChannel + 1 + (iChannel + 1 == CHANNEL_10 ? 1 : 0); //don't use channel 10
					if (iChannel > 15)
					{
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
