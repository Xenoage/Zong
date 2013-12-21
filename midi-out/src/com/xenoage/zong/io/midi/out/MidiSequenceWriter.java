package com.xenoage.zong.io.midi.out;

/**
 * Factory for an {@link MidiSequence} instance.
 * It has to be subclassed to support platform-specific sequence data.
 * 
 * @author Andreas Wenger
 */
public abstract class MidiSequenceWriter {

	//controller numbers
	protected static final int controllerVolume = 7;
	protected static final int controllerPan = 10;


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
	public abstract void writeProgramChange(int track, int channel, long tick, int program);

	/**
	 * Writes a MIDI volume change.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the volume change
	 * @param tick     the time of the event
	 * @param volume   the volume between 0 (silent) and 1 (full)
	 */
	public abstract void writeVolumeChange(int track, int channel, long tick, float volume);
	
	/**
	 * Writes a MIDI pan change.
	 * @param track    the index of the track where to write the event
	 * @param channel  the channel which is affected by the pan change
	 * @param tick     the time of the event
	 * @param pan      the panning between -1 (left) and 1 (right)
	 */
	public abstract void writePanChange(int track, int channel, long tick, float pan);

}
