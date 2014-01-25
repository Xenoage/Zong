package com.xenoage.zong.io.midi.out;

import java.util.List;

import com.xenoage.zong.core.position.MP;



/**
 * Factory for an {@link MidiSequence} instance.
 * It has to be subclassed to support platform-specific sequence data.
 * 
 * @param <T> the platform-specific sequence class
 * 
 * @author Andreas Wenger
 */
public abstract class MidiSequenceWriter<T> {

	//controller numbers
	protected static final int controllerVolume = 7;
	protected static final int controllerPan = 10;
	
	//short message commands - TIDY: not compatible with AndroidMidiSequenceWriter, since
	//the order of the bytes is reversed. Can we find a platform independent representation?
	protected static final int commandProgramChange = 0xC0; //192
	protected static final int commandControlChange = 0xB0; //176
	protected static final int commandNoteOn        = 0x90; //144
	protected static final int commandNoteOff       = 0x80; //128
	
	//meta message types
	protected static final int typeTempo            = 0x51; //81
	


	/**
	 * Initializes a MIDI sequence.
	 * @param tracksCount    the number of tracks in the sequence
	 * @param resolutionPpq  resolution in ticks ("pulses") per quarter note
	 */
	public abstract void init(int tracksCount, int resolutionPpq);

	/**
	 * Writes a MIDI program change.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the program change
	 * @param tick     the time of the event
	 * @param program  the MIDI program to change to
	 */
	public void writeProgramChange(int track, int channel, long tick, int program) {
		writeShortMessage(track, channel, tick, commandProgramChange, program, 0);
	}

	/**
	 * Writes a MIDI volume change.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the volume change
	 * @param tick     the time of the event
	 * @param volume   the volume between 0 (silent) and 1 (full)
	 */
	public void writeVolumeChange(int track, int channel, long tick, float volume) {
		writeShortMessage(track, channel, tick, commandControlChange, controllerVolume, (int) (127 * volume));
	}
	
	/**
	 * Writes a MIDI pan change.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the pan change
	 * @param tick     the time of the event
	 * @param pan      the panning between -1 (left) and 1 (right)
	 */
	public void writePanChange(int track, int channel, long tick, float pan) {
		writeShortMessage(track, channel, tick, commandControlChange, controllerPan, (int) (64 + (63 * pan)));
	}
	
	/**
	 * Writes a MIDI control change with the given data.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the pan change
	 * @param tick     the time of the event
	 * @param data1     the first data byte
	 * @param data2     the second data byte
	 */
	public void writeControlChange(int track, int channel, long tick, int data1, int data2) {
		writeShortMessage(track, channel, tick, commandControlChange, data1, data2);
	}
	
	/**
	 * Writes a MIDI note on or off.
	 * @param track     the index of the track where to write the event
	 * @param channel   the channel where to play the note
	 * @param tick      the time of the event
	 * @param note      the MIDI note
	 * @param on        true for note on, false for note off
	 * @param velocity  velocity of the note event
	 */
	public void writeNote(int track, int channel, long tick, int note, boolean on, int velocity) {
		writeShortMessage(track, channel, tick, on ? commandNoteOn : commandNoteOff, note, velocity);
	}
	
	/**
	 * Writes a MIDI tempo change.
	 * @param track     the index of the track where to write the event
	 * @param tick      the time of the event
	 * @param bpm       the new tempo in beats per minute
	 */
	public void writeTempoChange(int track, long tick, int bpm) {
		byte[] data = toByteArray(getMicrosecondsPerBeat(bpm));
		writeMetaMessage(track, tick, typeTempo, data);
	}
		
	/**
	 * Writes a MIDI short message with the given data.
	 * @param track     the index of the track where to write the event
	 * @param channel   the channel which is affected by the pan change
	 * @param tick      the time of the event
	 * @param command   the MIDI short message command
	 * @param data1     the first data byte
	 * @param data2     the second data byte
	 */
	public abstract void writeShortMessage(int track, int channel, long tick, int command, int data1, int data2);
	
	/**
	 * Writes a MIDI meta message with the given data.
	 * @param track  the index of the track where to write the event
	 * @param tick   the time of the event
	 * @param type   the type of the event
	 * @param data   the data bytes
	 */
	public abstract void writeMetaMessage(int track, long tick, int type, byte[] data);
	
	/**
	 * Gets the current length of the sequence in ticks.
	 */
	public abstract long getLength();
	
	/**
	 * Gets the position in microseconds of the given tick.
	 * This method is aware of all tempo changes written so far.
	 */
	public abstract long tickToMicrosecond(long tick);
	
	/**
	 * Converts the given value in beats per minutes into microseconds per beat.
	 */
	public static int getMicrosecondsPerBeat(int bpm) {
		final int msPerMinute = 60000000;
		return msPerMinute / bpm;
	}
	
	/**
	 * Returns the last three bytes of the given integer.
	 */
	private static byte[] toByteArray(int val) {
		byte[] res = new byte[3];
		res[0] = (byte) (val / 0x10000);
		res[1] = (byte) ((val - res[0] * 0x10000) / 0x100);
		res[2] = (byte) (val - res[0] * 0x10000 - res[1] * 0x100);
		return res;
	}
	
	/**
	 * Gets the platform-specific sequence with the current state.
	 */
	protected abstract T getSequence();
	
	/**
	 * Returns a {@link MidiSequence} with the written data.
	 * @param metronomeTrack     the number of the metronome track, or null if no metronome track was created
	 * @param timePool           the mappings from MIDI ticks to {@link MP}s and milliseconds
	 * @param measureStartTicks  the ticks of the beginnings of each measure (including repetitions)
	 */
	public MidiSequence<T> finish(Integer metronomeTrack, List<MidiTime> timePool,
		List<Long> measureStartTicks) {
		return new MidiSequence<T>(getSequence(), metronomeTrack, timePool, measureStartTicks);
	}

}
