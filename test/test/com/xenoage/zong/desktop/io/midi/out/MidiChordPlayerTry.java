package com.xenoage.zong.desktop.io.midi.out;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;

/**
 * Tests for {@link MidiChordPlayer}.
 * 
 * @author Uli Teschemacher
 */
public class MidiChordPlayerTry {

	public static void main(String args[])
		throws Exception {
		SynthManager.init(false);

		MidiChordPlayer player = new MidiChordPlayer();
		Instrument instrument = Instrument.defaultInstrument;

		Pitch pitch = pi(2, 0, 4);

		player.playNote(pitch, instrument);
		sleep();

		Chord chord = chord(new Pitch[] { pi(2, 0, 4), pi(4, 0, 4) }, fr(1));
		player.playChord(chord, instrument);

		sleep();

		player.playChord(chord, instrument, (byte) 127);

		sleep();

		SynthManager.close();
	}

	private static void sleep() {
		try {
			Thread.sleep(2000);
		} catch (Throwable e) {
		}
	}

	public static Chord chord(Pitch[] pitches, Fraction duration) {
		return new Chord(Note.notes(pitches), duration);
	}

}
