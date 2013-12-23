package com.xenoage.zong.desktop.io.midi.out;

import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.instrument.PitchedInstrument;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.io.midi.out.MidiTools;

/**
 * This class provides the playback functions for simple chords. 
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiChordPlayer {

	private MidiChannel channel;
	private byte defaultvelocity = 60;
	private int duration = 300;


	public MidiChordPlayer() {
		//TODO: do we really have to search for a free channel? - i mean: do
		//we playback a score the same time when we play a chord?
		Synthesizer synth = SynthManager.getSynthesizer();
		for (int midichannel = 0; midichannel < 16; midichannel++) {
			channel = synth.getChannels()[midichannel];
			if (channel != null) {
				break;
			}
			if (midichannel == 15 && channel == null) {
				// No free midichannel => Now we have a Problem
				// TODO no free midichannel
			}
		}
	}

	/**
	 * Plays a single note with the default velocity.
	 * 
	 */
	public void playChord(Chord chord, Instrument instrument) {
		playChord(chord, instrument, defaultvelocity);
	}

	/**
	 * Plays a chord.
	 */
	public void playChord(Chord chord, Instrument instrument, byte velocity) {
		for (Object pitch : chord.getPitches()) {
			playNote((Pitch) pitch, instrument, velocity);
		}
	}

	/**
	 * Plays a single note with the default velocity.
	 */
	public void playNote(Pitch pitch, Instrument instrument) {
		playNote(pitch, instrument, defaultvelocity);
	}

	/**
	 * Plays a single note.
	 * 
	 */
	public void playNote(Pitch pitch, Instrument instrument, byte velocity) {
		if (instrument instanceof PitchedInstrument)
			setMidiprogram(((PitchedInstrument) instrument).getMidiProgram());
		int midipitch = MidiTools.getNoteNumberFromPitch(pitch);
		channel.noteOn(midipitch, velocity);
		final Pitch p = pitch;
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override public void run() {
				stopSingleNote(p);
				timer.cancel();
			}
		}, duration);
	}

	/**
	 * Stops the playback of the note with the given {@link Pitch}.
	 */
	public void stopSingleNote(Pitch pitch) {
		channel.noteOff(MidiTools.getNoteNumberFromPitch(pitch));
	}

	/**
	 * Stops all currently played notes.
	 */
	public void stopAll() {
		channel.allNotesOff();
	}

	/**
	 * Changes the midiprogram for the playback.
	 * @param midiprogram
	 */
	private void setMidiprogram(int midiprogram) {
		channel.programChange(midiprogram);
	}

	/**
	 * Gets the duration of the played notes.
	 * @return
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of the played notes. It nothing is set, the default value (300) is used.
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

}
