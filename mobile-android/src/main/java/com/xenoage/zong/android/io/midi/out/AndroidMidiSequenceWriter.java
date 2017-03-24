package com.xenoage.zong.android.io.midi.out;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.io.midi.out.MidiSequenceWriter.getMicrosecondsPerBeat;

import java.util.ArrayList;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.xenoage.zong.io.midi.out.MidiSequenceWriter;


/**
 * Android implementation of a {@link MidiSequenceWriter}.
 * 
 * @author Andreas Wenger
 */
public class AndroidMidiSequenceWriter
	extends MidiSequenceWriter<MidiFile> {

	private int resolutionPpq;
	private ArrayList<MidiTrack> tracks;


	@Override public void init(int tracksCount, int resolutionPpq) {
		this.resolutionPpq = resolutionPpq;
		//add tracks
		tracks = alist(tracksCount);
		for (int i = 0; i < tracksCount; i++) {
			tracks.add(new MidiTrack());
		}
	}
	
	@Override public void writeNote(int track, int channel, long tick, int note, boolean on, int velocity) {
		MidiEvent event;
		if (on)
			event = new NoteOn(tick, channel, note, velocity);
		else
			event = new NoteOff(tick, channel, note, velocity);
		tracks.get(track).insertEvent(event);
	}
	
	@Override public void writeProgramChange(int track, int channel, long tick, int program) {
		MidiEvent event = new ProgramChange(tick, channel, program);
		tracks.get(track).insertEvent(event);
	}

	@Override public void writeVolumeChange(int track, int channel, long tick, float volume) {
		//TODO
	}
	
	@Override public void writePanChange(int track, int channel, long tick, float pan) {
		//TODO
	}
	
	@Override public void writeControlChange(int track, int channel, long tick, int data1, int data2) {
		//TODO
	}
	
	@Override public void writeTempoChange(int track, long tick, int bpm) {
		MidiEvent event = new Tempo(tick, 0, getMicrosecondsPerBeat(bpm));
		tracks.get(track).insertEvent(event);
	}

	@Override public void writeShortMessage(int track, int channel, long tick, int command, int data1, int data2) {
		//TODO
		//ShortMessageEvent event = new ShortMessageEvent(tick, command, channel, data1, data2);
		//tracks.get(track).insertEvent(event);
	}
	
	@Override public void writeMetaMessage(int track, long tick, int type, byte[] data) {
		//TODO
		//MetaMessage event = new MetaMessage(tick, type, data);
		//tracks.get(track).insertEvent(event);
	}

	@Override public long getLength() {
		return getSequence().getLengthInTicks();
	}
	
	@Override public long tickToMicrosecond(long tick) {
		return tick * 1000; //unsupported
	}

	@Override protected MidiFile getSequence() {
		return new MidiFile(resolutionPpq, tracks);
	}

}
