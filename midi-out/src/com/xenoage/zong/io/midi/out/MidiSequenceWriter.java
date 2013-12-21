package com.xenoage.zong.io.midi.out;


/**
 * Factory for an {@link MidiSequence} instance.
 * It has to be subclassed to support platform-specific sequence data.
 * 
 * @author Andreas Wenger
 */
public abstract class MidiSequenceWriter {

	//controller numbers
	private static final int controllerVolume = 7;
	private static final int controllerPan = 10;
	
	//short message commands
	private static final int commandProgramChange = 0xC0; //192
	private static final int commandControlChange = 0xB0; //176
	private static final int commandNoteOn        = 0x90; //144
	private static final int commandNoteOff       = 0x80; //128
	


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
		writeEvent(track, channel, tick, commandProgramChange, program, 0);
	}

	/**
	 * Writes a MIDI volume change.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the volume change
	 * @param tick     the time of the event
	 * @param volume   the volume between 0 (silent) and 1 (full)
	 */
	public void writeVolumeChange(int track, int channel, long tick, float volume) {
		writeEvent(track, channel, tick, commandControlChange, controllerVolume, (int) (127 * volume));
	}
	
	/**
	 * Writes a MIDI pan change.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the pan change
	 * @param tick     the time of the event
	 * @param pan      the panning between -1 (left) and 1 (right)
	 */
	public void writePanChange(int track, int channel, long tick, float pan) {
		writeEvent(track, channel, tick, commandControlChange, controllerPan, (int) (64 + (63 * pan)));
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
		writeEvent(track, channel, tick, commandControlChange, data1, data2);
	}
	
	/**
	 * Writes a MIDI note on or off.
	 * @param track     the index of the track where to write the event
	 * @param channel   the channel which is affected by the pan change
	 * @param tick      the time of the event
	 * @param note      the MIDI note
	 * @param on        true for note on, false for note off
	 * @param velocity  velocity of the note event
	 */
	public void writeNote(int track, int channel, long tick, int note, boolean on, int velocity) {
		writeEvent(track, channel, tick, on ? commandNoteOn : commandNoteOff, note, velocity);
	}
	
	/**
	 * Writes a MIDI event with the given data.
	 * @param track     the index of the track where to write the event
	 * @param channel   the channel which is affected by the pan change
	 * @param tick      the time of the event
	 * @param command   the MIDI short message command
	 * @param data1     the first data byte
	 * @param data2     the second data byte
	 */
	public abstract void writeEvent(int track, int channel, long tick, int command, int data1, int data2);
	
	/**
	 * Gets the current length of the sequence in ticks.
	 */
	public abstract long getLength();

}
