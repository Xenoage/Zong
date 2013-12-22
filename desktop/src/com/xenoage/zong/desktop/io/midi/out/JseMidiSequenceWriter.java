package com.xenoage.zong.desktop.io.midi.out;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.xenoage.zong.io.midi.out.MidiSequenceWriter;
import com.xenoage.zong.io.midi.out.MidiTime;

/**
 * Java SE implementation of a {@link MidiSequenceWriter}.
 * 
 * @author Andreas Wenger
 */
public class JseMidiSequenceWriter
	extends MidiSequenceWriter<Sequence> {

	private Sequence sequence = null;
	private Track[] tracks;


	@Override public void init(int tracksCount, int resolutionPpq) {
		//create sequence
		try {
			sequence = new Sequence(Sequence.PPQ, resolutionPpq);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(); //TODO
		}
		//add tracks
		for (int i = 0; i < tracksCount; i++) {
			tracks[i] = sequence.createTrack();
		}
	}
	
	@Override public void writeShortMessage(int track, int channel, long tick, int command, int data1, int data2) {
		try {
			ShortMessage message = new ShortMessage();
			message.setMessage(command, channel, data1, data2);
			tracks[track].add(new MidiEvent(message, tick));
		} catch (InvalidMidiDataException ex) {
			//ignore - TODO
		}
	}
	
	@Override public void writeMetaMessage(int track, long tick, int type, byte[] data) {
		try {
			MetaMessage message = new MetaMessage();
			message.setMessage(type, data, data.length);
			tracks[track].add(new MidiEvent(message, tick));
		} catch (InvalidMidiDataException ex) {
			//ignore - TODO
		}
	}

	@Override public long getLength() {
		return sequence.getTickLength();
	}

	@Override public JseMidiSequence finish(Integer metronomeTrack, List<MidiTime> timePool) {
		return new JseMidiSequence(sequence, metronomeTrack, timePool);
	}
	
}
