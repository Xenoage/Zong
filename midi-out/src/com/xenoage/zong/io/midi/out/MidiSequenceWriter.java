package com.xenoage.zong.io.midi.out;

import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.io.midi.out.time.TimeMap;
import lombok.val;

import static com.xenoage.utils.math.MathUtils.clamp;
import static com.xenoage.utils.math.MathUtils.log2;


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
	protected static final int typeClef             = 0x57; //87 TODO: really? no docs found!
	protected static final int typeTime             = 0x58; //88
	protected static final int typeKey              = 0x59; //89


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
	 * Writes a MIDI key signature. Only {@link TraditionalKey} is supported.
	 * @param track     the index of the track where to write the event
	 * @param tick      the time of the event
	 * @param key       The {@link Key} to write
	 */
	public void writeKey(int track, long tick, Key key) {
		if (key.getMusicElementType() == MusicElementType.TraditionalKey) {
			val tradKey = (TraditionalKey) key;
			byte fifths = (byte) tradKey.getFifths();
			byte mode = (byte) (tradKey.getMode() == TraditionalKey.Mode.Minor ? 1 : 0); //minor, or (everything else) major
			byte[] data = { fifths, mode };
			writeMetaMessage(track, tick, typeKey, data);
		}
	}

	/**
	 * Writes a MIDI time signature.
	 * @param track       the index of the track where to write the event
	 * @param tick        the time of the event
	 * @param time        the {@link TimeType} to write
	 * @param resolution  the resolution in ticks per quarter note
	 */
	public void writeTimeSignature(int track, long tick, TimeType time, int resolution) {
		if (time.getDenominator() > 0) {
			byte nom = (byte) time.getNumerator();
			byte den = getDenominatorExponent(time.getDenominator());
			byte res1 = (byte) (resolution * 4 / time.getDenominator()); //1 beat per quarter note
			byte res2 = (byte) 8; //32nd notes per beat: 8, since we defined 1 beat = 1 quarter note
			byte[] data = {nom, den, res1, res2};
			writeMetaMessage(track, tick, typeTime, data);
		}
	}

	/**
	 * Writes a MIDI clef, when possible.
	 * TODO: Found only documentation in the "Beyond MIDI" book, but without event type id.
	 * @param track       the index of the track where to write the event
	 * @param tick        the time of the event
	 * @param clef        the {@link ClefType} to write
	 */
	public void writeClef(int track, long tick, ClefType clef) {
		//clef type
		byte cl = -1;
		switch (clef.getSymbol()) {
			case C:
				cl = 0; break;
			case G:
				cl = 1; break;
			case F:
				cl = 2; break;
			case PercTwoRects: case PercEmptyRect:
				cl = 3; break;
		}
		if (cl != -1) {
			byte li = (byte) INSTANCE.clamp(clef.getLp() * 2 + 1, 1, 5); //line number from bottom
			byte oc = (byte) clef.getSymbol().octaveChange;
			byte[] data = {cl, li, oc};
			writeMetaMessage(track, tick, typeClef, data);
		}
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
	public abstract void writeMetaMessage(int track, long tick, int type, byte... data);
	
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
	 * @param metronomeTrack  the number of the metronome track, or null if no metronome track was created
	 * @param timeMap         the mapping between MIDI and score time.
	 */
	public MidiSequence<T> finish(Integer metronomeTrack, TimeMap timeMap) {
		return new MidiSequence<>(getSequence(), metronomeTrack, timeMap);
	}

	/**
	 * MIDI saves the denominator of a time signature as the exponent of the power of 2.
	 * For example, 8 = 2 ^ 3, thus, the denomiator for a x/8 time would be 3.
	 * 0 means x/1 time, 1 means x/2 time, 2 means x/4 time, and so on.
	 */
	private byte getDenominatorExponent(int denominator) {
		return (byte) INSTANCE.log2(denominator);
	}

}
