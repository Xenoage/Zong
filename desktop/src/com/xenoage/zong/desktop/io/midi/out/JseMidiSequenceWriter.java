package com.xenoage.zong.desktop.io.midi.out;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import com.sun.media.sound.MidiUtils;
import com.sun.media.sound.MidiUtils.TempoCache;
import com.xenoage.zong.io.midi.out.MidiSequenceWriter;


/**
 * Java SE implementation of a {@link MidiSequenceWriter}.
 * 
 * @author Andreas Wenger
 */
public class JseMidiSequenceWriter
	extends MidiSequenceWriter<Sequence> {

	private Sequence sequence = null;
	private Track[] tracks;
	private TempoCache tempoCache = null;


	@Override public void init(int tracksCount, int resolutionPpq) {
		//create sequence
		try {
			sequence = new Sequence(Sequence.PPQ, resolutionPpq);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(); //TODO
		}
		//add tracks
		tracks = new Track[tracksCount];
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
		//when it is a tempo change, the tempo cache is obsolete
		if (type == typeTempo)
			tempoCache = null;
	}

	@Override public long getLength() {
		return sequence.getTickLength();
	}
	
	@Override public long tickToMicrosecond(long tick) {
		//recompute tempo cache if required
		if (tempoCache == null)
			tempoCache = new TempoCache(sequence);
		//compute position
	  return MidiUtils.tick2microsecond(sequence, tick, tempoCache);
	}

	@Override protected Sequence getSequence() {
		return sequence;
	}

}
