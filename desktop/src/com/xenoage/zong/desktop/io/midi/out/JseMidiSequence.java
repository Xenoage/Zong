package com.xenoage.zong.desktop.io.midi.out;

import java.util.List;

import javax.sound.midi.Sequence;

import com.xenoage.zong.io.midi.out.MidiSequence;
import com.xenoage.zong.io.midi.out.MidiTime;

/**
 * Java SE implementation of a MIDI sequence.
 * 
 * @author Andreas Wenger
 */
public class JseMidiSequence
	extends MidiSequence<Sequence> {

	public JseMidiSequence(Sequence sequence, Integer metronomeTrack, List<MidiTime> timePool) {
		super(sequence, metronomeTrack, timePool);
	}

}
